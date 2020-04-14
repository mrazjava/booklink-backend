package com.github.mrazjava.booklink.rest.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
}
