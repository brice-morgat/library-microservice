package com.example.library.userservice.service;

import com.example.library.userservice.dto.CreateUserRequest;
import com.example.library.userservice.dto.RegisterRequest;
import com.example.library.userservice.dto.UpdateUserRequest;
import com.example.library.userservice.dto.UserDto;
import com.example.library.userservice.exception.BadRequestException;
import com.example.library.userservice.exception.ForbiddenException;
import com.example.library.userservice.exception.NotFoundException;
import com.example.library.userservice.mapper.UserMapper;
import com.example.library.userservice.model.MembershipType;
import com.example.library.userservice.model.Role;
import com.example.library.userservice.model.RoleName;
import com.example.library.userservice.model.User;
import com.example.library.userservice.repository.RoleRepository;
import com.example.library.userservice.repository.UserRepository;
import com.example.library.userservice.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des utilisateurs.
 *
 * @since 1.0
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Retourne tous les utilisateurs.
     *
     * @return liste d'utilisateurs.
     */
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Retourne un utilisateur par id (contrôle des droits).
     *
     * @param id identifiant utilisateur.
     * @param authentication contexte de sécurité.
     * @return utilisateur.
     */
    public UserDto findById(Long id, Authentication authentication) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        enforceSelfOrAdmin(user, authentication);
        return UserMapper.toDto(user);
    }

    /**
     * Crée un utilisateur (ADMIN).
     *
     * @param request données de création.
     * @return utilisateur créé.
     */
    public UserDto create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        if (userRepository.existsByMembershipNumber(request.getMembershipNumber())) {
            throw new BadRequestException("Membership number already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .membershipNumber(request.getMembershipNumber())
                .membershipDate(LocalDate.now())
                .membershipType(request.getMembershipType() != null ? request.getMembershipType() : MembershipType.STANDARD)
                .active(request.isActive())
                .build();

        Set<Role> roles = resolveRoles(request.getRoles());
        user.setRoles(roles);

        return UserMapper.toDto(userRepository.save(user));
    }

    /**
     * Inscrit un utilisateur et retourne l'entité persistée.
     *
     * @param request données d'inscription.
     * @return utilisateur persisté.
     */
    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }
        if (userRepository.existsByMembershipNumber(request.getMembershipNumber())) {
            throw new BadRequestException("Membership number already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .membershipNumber(request.getMembershipNumber())
                .membershipDate(LocalDate.now())
                .membershipType(request.getMembershipType() != null ? request.getMembershipType() : MembershipType.STANDARD)
                .active(true)
                .build();

        Set<Role> roles = resolveRoles(request.getRoles());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    /**
     * Met à jour un utilisateur (ADMIN ou soi-même).
     *
     * @param id identifiant utilisateur.
     * @param request données de mise à jour.
     * @param authentication contexte de sécurité.
     * @return utilisateur mis à jour.
     */
    public UserDto update(Long id, UpdateUserRequest request, Authentication authentication) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        enforceSelfOrAdmin(user, authentication);

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use");
        }

        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        if (request.getMembershipType() != null) {
            user.setMembershipType(request.getMembershipType());
        }
        user.setActive(request.isActive());

        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            if (!isAdmin(authentication)) {
                throw new ForbiddenException("Only admins can update roles");
            }
            user.setRoles(resolveRoles(request.getRoles()));
        }

        return UserMapper.toDto(userRepository.save(user));
    }

    /**
     * Supprime un utilisateur.
     *
     * @param id identifiant utilisateur.
     */
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * Charge un utilisateur par email.
     *
     * @param email email utilisateur.
     * @return utilisateur.
     */
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        Set<String> names = roleNames == null || roleNames.isEmpty()
                ? Set.of(RoleName.USER.name())
                : roleNames;
        return names.stream()
                .map(String::trim)
                .map(String::toUpperCase)
                .map(RoleName::valueOf)
                .map(this::getOrCreateRole)
                .collect(Collectors.toSet());
    }

    private Role getOrCreateRole(RoleName name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(Role.builder().name(name).build()));
    }

    private void enforceSelfOrAdmin(User user, Authentication authentication) {
        if (authentication == null) {
            throw new ForbiddenException("Not authenticated");
        }
        if (isAdmin(authentication)) {
            return;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            if (userPrincipal.getId() != null && userPrincipal.getId().equals(user.getId())) {
                return;
            }
        }
        throw new ForbiddenException("Access denied");
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
