package com.github.mrazjava.booklink.persistence.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @since 0.2.0
 */
@Entity(name = "bl_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column(name = "pwd")
    private String password;

    @Column(name = "auth_token", unique = true)
    private String token;

    @Column(name = "auth_token_expiry")
    private OffsetDateTime tokenExpiry;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private int active;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "bl_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;


    public UserEntity() {
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

    public OffsetDateTime getTokenExpiry() {
        return tokenExpiry;
    }

    public void setTokenExpiry(OffsetDateTime tokenExpiry) {
        this.tokenExpiry = tokenExpiry;
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
