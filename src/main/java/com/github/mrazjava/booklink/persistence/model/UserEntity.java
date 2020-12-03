package com.github.mrazjava.booklink.persistence.model;

import com.github.mrazjava.booklink.BooklinkException;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Defines user and core account settings such as authentication credentials. The main
 * premise is that single account record represents any login method based on the same
 * email address.
 *
 * @author AZ
 */
@Entity(name = "bl_user")
public class UserEntity implements UserDetails {

	private static final long serialVersionUID = -4085482946301042468L;

	private static final Logger log = LoggerFactory.getLogger(UserEntity.class);

    static final int STATUS_ACTIVE = 1;

    static final int STATUS_LOCKED = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_sequence")
    private Long id;

    @Column(unique = true)
    private String email;

    /**
     * native booklink password
     */
    @Column(name = "pwd_bk")
    private String passwordBk;

    /**
     * password used by facebook auth into booklink - this is NOT user's facebook password!
     */
    @Column(name = "pwd_fb")
    private String passwordFb;

    /**
     * password used by google auth into booklink - this is NOT user's google password!
     */
    @Column(name = "pwd_gl")
    private String passwordGl;

    /**
     * relevant only to native booklink password
     */
    @Column(name = "last_pwd_change")
    private OffsetDateTime lastPwdChange;

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

    @Column(name = "nick_name")
    private String nickName;

    @ManyToOne
    @JoinColumn(name = "reg_origin_id", referencedColumnName = "id", nullable = false)
    private UserOriginEntity registrationOrigin;

    @ManyToOne
    @JoinColumn(name = "last_login_origin_id", referencedColumnName = "id")
    private UserOriginEntity lastLoginOrigin;

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

    @Transient
    @Override
    public boolean isEnabled() {
        return getActive() == STATUS_ACTIVE;
    }

    public String getPasswordBk() {
        return passwordBk;
    }

    @Transient
    @Override
    public String getPassword() {
        String password;
        long lastLoginId;
        if(lastLoginOrigin == null) {
            log.warn("computed password relies on lastLoginOrigin which is not set; assuming {}", UserOrigin.BOOKLINK);
            lastLoginId = UserOrigin.BOOKLINK.getId();
        }
        else {
            lastLoginId = lastLoginOrigin.getId();
        }
        UserOrigin loginOrigin = UserOrigin.fromId(lastLoginId);
        switch(loginOrigin) {
            case BOOKLINK:
                password = passwordBk;
                break;
            case FACEBOOK:
                password = passwordFb;
                break;
            case GOOGLE:
                password = passwordGl;
                break;
            default:
                throw new BooklinkException(String.format("unsupported login origin: %s", loginOrigin));
        }
        return password;
    }

    public void setPasswordBk(String passwordBk) {
        this.passwordBk = passwordBk;
    }

    public OffsetDateTime getLastPwdChange() {
        return lastPwdChange;
    }

    public void setLastPwdChange(OffsetDateTime lastPwdChange) {
        this.lastPwdChange = lastPwdChange;
    }

    public String getPasswordFb() {
        return passwordFb;
    }

    public void setPasswordFb(String passwordFb) {
        this.passwordFb = passwordFb;
    }

    public String getPasswordGl() {
        return passwordGl;
    }

    public void setPasswordGl(String passwordGl) {
        this.passwordGl = passwordGl;
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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Set<RoleEntity> getRoles() {
        return roles == null ? new HashSet<>() : roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    public UserOriginEntity getRegistrationOrigin() {
        return registrationOrigin;
    }

    public void setRegistrationOrigin(UserOriginEntity registrationOrigin) {
        this.registrationOrigin = registrationOrigin;
    }

    public UserOriginEntity getLastLoginOrigin() {
        return lastLoginOrigin;
    }

    public void setLastLoginOrigin(UserOriginEntity lastLoginOrigin) {
        this.lastLoginOrigin = lastLoginOrigin;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
