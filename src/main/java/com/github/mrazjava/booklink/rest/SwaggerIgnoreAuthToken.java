package com.github.mrazjava.booklink.rest;

import com.github.mrazjava.booklink.config.SwaggerConfiguration;
import com.github.mrazjava.booklink.security.AccessTokenSecurityFilter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

import java.lang.annotation.*;

@ApiImplicitParams(@ApiImplicitParam(
        name = AccessTokenSecurityFilter.AUTHORIZATION_HEADER_NAME,
        paramType = "header",
        value = SwaggerConfiguration.HEADER_NOT_USED_MSG,
        allowEmptyValue = true
))
@Documented
@Target(ElementType.METHOD)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface SwaggerIgnoreAuthToken {
}
