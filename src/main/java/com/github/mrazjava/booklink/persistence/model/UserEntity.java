package com.github.mrazjava.booklink.persistence.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @since 0.2.0
 */
@Entity(name = "bl_user")
public class UserEntity implements UserDetails {

    static final int STATUS_ACTIVE = 1;

    static final int STATUS_LOCKED = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_sequence")
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(name = "pwd")
    private String password;

    @Column(name = "last_login_on")
    private OffsetDateTime lastLoginOn;

    @Column(name = "auth_token", unique = true)
    private String token;

    @Column(name = "auth_token_expiry")
    private OffsetDateTime tokenExpiry;

    @Column(name = "f_name")
    private String firstName;

    @Column(name = "l_name")
    private String lastName;

    @Column
    private int active;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "bl_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;


    public UserEntity() {
    }

    public UserEntity(String token, OffsetDateTime tokenExpiry) {
        setToken(token);
        setTokenExpiry(tokenExpiry);
    }

    public UserEntity(UserEntity source) {
        id = source.getId();
        email = source.getEmail();
        password = source.getPassword();
        token = source.getToken();
        tokenExpiry = source.getTokenExpiry(); // immutable, ref copy ok
        firstName = source.getFirstName();
        lastName = source.getLastName();
        active = source.getActive();
        roles = new HashSet<>(source.getRoles());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(String.format("ROLE_%s", role.getName())))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Transient
    @Override
    public String getUsername() {
        return getEmail();
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return getActive() != STATUS_LOCKED;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getActive() == STATUS_ACTIVE;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Transient
    public boolean isTokenExpired() {
        OffsetDateTime tokenExpiry = getTokenExpiry();
        return tokenExpiry == null || OffsetDateTime.now().isAfter(tokenExpiry);
    }

    public OffsetDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(OffsetDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
    }

    public OffsetDateTime getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(OffsetDateTime lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
