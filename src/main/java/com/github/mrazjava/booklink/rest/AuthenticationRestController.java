package com.github.mrazjava.booklink.rest;

import com.github.mrazjava.booklink.config.SwaggerDocumentationConfiguration;
import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.rest.model.ErrorResponse;
import com.github.mrazjava.booklink.rest.model.LoginRequest;
import com.github.mrazjava.booklink.rest.model.LoginResponse;
import com.github.mrazjava.booklink.security.AccessTokenSecurityFilter;
import com.github.mrazjava.booklink.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;

/**
 * @author AZ (mrazjava)
 * @since 0.2.0
 */
@Api(
        tags = {"Authentication"}
)
@RestController
@RequestMapping("rest/v1/auth")
public class AuthenticationRestController {

    @Inject
    private Logger log;

    @Inject
    private UserService userService;

    @Value("${spring.profiles.active}")
    private String environment;


    @ApiOperation(
            value = "Verify credentials and obtain authentication token",
            consumes = "application/json"
    )
    @PostMapping("login")
    @Produces("application/json")
    @ApiResponses(
            {
                    @ApiResponse(
                            message = "successful login",
                            code = 200,
                            response = LoginResponse.class
                    ),
                    @ApiResponse(
                            message = "invalid login",
                            code = 401,
                            response = ErrorResponse.class
                    )
            }
    )
    @ApiImplicitParams(@ApiImplicitParam(
            name = AccessTokenSecurityFilter.AUTHORIZATION_HEADER_NAME,
            paramType = "header",
            value = SwaggerDocumentationConfiguration.HEADER_NOT_USED_MSG,
            allowEmptyValue = true
    ))
    public ResponseEntity<LoginResponse> login(
            @RequestBody(required = true) LoginRequest loginRequest, HttpServletRequest request) {

        log.debug("login request: {}", loginRequest.getEmail());

        UserEntity user = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        user = userService.ensureValidToken(user);

        return new ResponseEntity(new LoginResponse(user), HttpStatus.OK);
    }
}
