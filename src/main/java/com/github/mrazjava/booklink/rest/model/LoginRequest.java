package com.github.mrazjava.booklink.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@ApiModel
public class LoginRequest {

    @ApiModelProperty(required = true, position = 1)
    private String email;

    @ApiModelProperty(required = true, position = 2)
    private String password;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
