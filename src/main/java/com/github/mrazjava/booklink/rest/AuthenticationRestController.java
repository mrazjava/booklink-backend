package com.github.mrazjava.booklink.rest;

import com.github.mrazjava.booklink.config.SwaggerConfiguration;
import com.github.mrazjava.booklink.persistence.model.UserEntity;
import com.github.mrazjava.booklink.rest.model.ErrorResponse;
import com.github.mrazjava.booklink.rest.model.LoginRequest;
import com.github.mrazjava.booklink.security.AccessTokenSecurityFilter;
import com.github.mrazjava.booklink.security.InvalidAccessTokenException;
import com.github.mrazjava.booklink.service.UserService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Produces;
import java.util.Optional;

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
    private AuthenticationManager authenticationManager;

    @Inject
    private UserService userService;


    @ApiOperation(
            value = "Verify credentials and obtain authentication token",
            consumes = "application/json"
    )
    @PatchMapping("login")
    @Produces("application/json")
    @ApiResponses(
            {
                    @ApiResponse(
                            message = "successful login",
                            code = 200
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
            value = SwaggerConfiguration.HEADER_NOT_USED_MSG,
            allowEmptyValue = true
    ))
    public ResponseEntity<String> login(@RequestBody(required = true) LoginRequest loginRequest) {

        log.debug("login request: {}", loginRequest.getEmail());

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        UserEntity userEntity = userService.ensureValidToken(authentication.getPrincipal());

        return ResponseEntity.ok(userEntity.getToken());
    }

    @ApiOperation(
            value = "Removes a valid authentication token of a user. New token can be generated via login.",
            consumes = "application/json"
    )
    @PatchMapping("logout")
    @Produces("application/json")
    @ApiResponses(
            {
                    @ApiResponse(
                            message = "confirmed login id of a user whose auth token was removed",
                            code = 200
                    ),
                    @ApiResponse(
                            message = "invalid login",
                            code = 401,
                            response = ErrorResponse.class
                    )
            }
    )
    public ResponseEntity<String> logout(@ApiIgnore Authentication auth) {
        UserDetails credentials = UserService.getCredentials(auth);
        return ResponseEntity.ok(userService.deleteAuthToken(credentials));
    }
}
