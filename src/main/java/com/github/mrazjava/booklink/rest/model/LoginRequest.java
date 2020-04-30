package com.github.mrazjava.booklink.rest.model;

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

    @ApiModelProperty
    private String fbId;

    @ApiModelProperty
    private String fbFirstName;

    @ApiModelProperty
    private String fbLastName;

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

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getFbFirstName() {
        return fbFirstName;
    }

    public void setFbFirstName(String fbFirstName) {
        this.fbFirstName = fbFirstName;
    }

    public String getFbLastName() {
        return fbLastName;
    }

    public void setFbLastName(String fbLastName) {
        this.fbLastName = fbLastName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
