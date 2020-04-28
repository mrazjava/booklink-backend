package com.github.mrazjava.booklink.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * @author AZ (mrazjava)
 * @since 0.2.2
 */
@ApiModel
public class AuthResponse {

    @ApiModelProperty
    private String token;

    @ApiModelProperty
    private String firstName;

    @ApiModelProperty
    private String lastName;

    @ApiModelProperty
    private OffsetDateTime lastLoginOn;

    @ApiModelProperty
    private Long origin;

    @ApiModelProperty
    private List<String> roles;


    public AuthResponse(String token, String firstName, String lastName) {
        this.token = token;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getToken() {
        return token;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public AuthResponse withRoles(List<String> roles) {
        setRoles(roles);
        return this;
    }

    public OffsetDateTime getLastLoginOn() {
        return lastLoginOn;
    }

    public void setLastLoginOn(OffsetDateTime lastLoginOn) {
        this.lastLoginOn = lastLoginOn;
    }

    public AuthResponse withLastLoginOn(OffsetDateTime lastLoginOn) {
        setLastLoginOn(lastLoginOn);
        return this;
    }

    public Long getOrigin() {
        return origin;
    }

    public void setOrigin(Long origin) {
        this.origin = origin;
    }

    public AuthResponse withOrigin(Long origin) {
        setOrigin(origin);
        return this;
    }
}
