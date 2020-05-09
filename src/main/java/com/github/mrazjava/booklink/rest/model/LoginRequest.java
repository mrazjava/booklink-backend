package com.github.mrazjava.booklink.rest.model;

import com.github.mrazjava.booklink.persistence.model.UserOrigin;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author AZ
 */
@ApiModel
public class LoginRequest {

    @ApiModelProperty(required = true)
    private String email;

    @ApiModelProperty(required = true)
    private String password;

    @ApiModelProperty(required = true)
    private UserOrigin origin;

    // sm = (s)ocial (m)edia

    @ApiModelProperty
    private String smId;

    @ApiModelProperty
    private String smFirstName;

    @ApiModelProperty
    private String smLastName;


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

    public UserOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(UserOrigin origin) {
        this.origin = origin;
    }

    public String getSmId() {
        return smId;
    }

    public void setSmId(String smId) {
        this.smId = smId;
    }

    public String getSmFirstName() {
        return smFirstName;
    }

    public void setSmFirstName(String smFirstName) {
        this.smFirstName = smFirstName;
    }

    public String getSmLastName() {
        return smLastName;
    }

    public void setSmLastName(String smLastName) {
        this.smLastName = smLastName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
