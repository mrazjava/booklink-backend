package com.github.mrazjava.booklink.rest;

import com.github.mrazjava.booklink.config.SwaggerConfiguration;
import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.security.AccessTokenSecurityFilter;
import com.github.mrazjava.booklink.service.ProofOfConceptService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Produces;

/**
 * @author AZ
 */
@Api(
        tags = {"Proof of Concept"}
)
@RestController
@RequestMapping("rest/v1/poc")
public class ProofOfConceptRestController {

    @Inject
    private Logger log;

    @Inject
    private ProofOfConceptService pocService;


    @ApiOperation(
            value = "Randomized value over the entire domain of java.lang.Integer",
            consumes = "application/json"
    )
    @GetMapping("/random-count")
    @Produces("application/json")
    @ApiResponses(
            {
                    @ApiResponse(
                            message = "ok",
                            code = 200
                    )
            }
    )
    @ApiImplicitParams(@ApiImplicitParam(
            name = AccessTokenSecurityFilter.AUTHORIZATION_HEADER_NAME,
            paramType = "header",
            value = SwaggerConfiguration.HEADER_NOT_USED_MSG,
            allowEmptyValue = true
    ))
    public ResponseEntity<Integer> countAll() {
        return new ResponseEntity<>(pocService.randomCount(), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Dummy secured endpoint; requires no roles"
    )
    @GetMapping("/secured/hello")
    @Produces("application/text")
    public String securedHello(@ApiIgnore Authentication auth) {
        return pocService.sayHello(auth);
    }

    @ApiOperation(
            value = "Dummy secured endpoint; allowed roles: FOO|ADMIN|DETECTIVE"
    )
    @GetMapping("/secured/foo")
    @Produces("application/text")
    @RolesAllowed({"ROLE_FOO", "ROLE_ADMIN", "ROLE_DETECTIVE"})
    public String securedFoo(@ApiIgnore Authentication auth) {
        return pocService.randomAlphanumeric();
    }

    @ApiOperation(
            value = "Dummy secured endpoint; allowed roles: BAR|ADMIN|DETECTIVE"
    )
    @GetMapping("/secured/bar")
    @Produces("application/text")
    @RolesAllowed({"ROLE_BAR", "ROLE_ADMIN", "ROLE_DETECTIVE"})
    public String securedBar() {
        return pocService.randomAlpha();
    }

    @ApiOperation(
            value = "Dummy secured endpoint; allowed roles: ADMIN|DETECTIVE"
    )
    @GetMapping("/secured/investigation")
    @Produces("application/text")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_DETECTIVE"})
    public String securedInvestigation(@ApiIgnore Authentication auth) {
        return pocService.randomWords().toString();
    }

    @ApiOperation(
            value = "Encode password"
    )
    @PostMapping("/password/encode")
    @Produces("application/text")
    @ApiImplicitParams(@ApiImplicitParam(
            name = AccessTokenSecurityFilter.AUTHORIZATION_HEADER_NAME,
            paramType = "header",
            value = SwaggerConfiguration.HEADER_NOT_USED_MSG,
            allowEmptyValue = true
    ))
    public String encodePassword(@RequestParam String plainText, @ApiIgnore Authentication auth) {
        return pocService.getEncodedPassword(plainText);
    }

    @ApiOperation(
            value = "Search depot for author by id"
    )
    @GetMapping("/depot/author/find")
    @Produces("application/text")
    @ApiImplicitParams(@ApiImplicitParam(
            name = AccessTokenSecurityFilter.AUTHORIZATION_HEADER_NAME,
            paramType = "header",
            value = SwaggerConfiguration.HEADER_NOT_USED_MSG,
            allowEmptyValue = true
    ))
    public DepotAuthor findAuthorById(@RequestParam String id) {
        return pocService.depotFindAuthor(id);
    }
}
