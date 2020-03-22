package com.github.mrazjava.booklink.rest.model;

import com.github.mrazjava.booklink.persistence.model.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@ApiModel
public class LoginResponse {

    @ApiModelProperty
    private Long id;

    @ApiModelProperty
    private String email;

    @ApiModelProperty
    private String token;

    @ApiModelProperty
    private List<String> roles;


    public LoginResponse() {
    }

    public LoginResponse(UserEntity entity) {
        id = entity.getId();
        email = entity.getEmail();
        token = entity.getToken();
        roles = entity.getRoles().stream().map(r -> r.getName()).collect(Collectors.toList());
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
