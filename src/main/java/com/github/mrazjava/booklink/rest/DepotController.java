package com.github.mrazjava.booklink.rest;

import com.github.mrazjava.booklink.config.SwaggerConfiguration;
import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.rest.depot.DepotStats;
import com.github.mrazjava.booklink.security.AccessTokenSecurityFilter;
import com.github.mrazjava.booklink.service.DepotService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.ws.rs.Produces;
import java.util.List;

/**
 * @author AZ (mrazjava)
 */
@Api(
        tags = {"Book Depot"}
)
@RestController
@RequestMapping("rest/v1/depot")
public class DepotController {

    @Autowired
    private DepotService depotService;


    @ApiOperation(
            value = "Find author by id"
    )
    @GetMapping("/author/find")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DepotAuthor> findAuthorById(@ApiIgnore Authentication auth, @RequestParam String id) {
        return ResponseEntity.ok(depotService.findAuthorById(id).orElse(null));
    }

    @ApiOperation(
            value = "Desired number of randomly selected authors which contain an image"
    )
    @GetMapping("/author/featured")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DepotAuthor>> featuredAuthors(@ApiIgnore Authentication auth, @RequestParam Integer count) {
        return ResponseEntity.ok(depotService.randomAuthorWithImage(count));
    }

    @ApiOperation(
            value = "Find author by id"
    )
    @GetMapping("/author/search")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DepotAuthor>> searchAuthors(@ApiIgnore Authentication auth, @RequestParam String search) {
        return ResponseEntity.ok(depotService.searchAuthors(search));
    }

    @ApiOperation(
            value = "Compute counts"
    )
    @GetMapping("/counts")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams(@ApiImplicitParam(
            name = AccessTokenSecurityFilter.AUTHORIZATION_HEADER_NAME,
            paramType = "header",
            value = SwaggerConfiguration.HEADER_NOT_USED_MSG,
            allowEmptyValue = true
    ))
    public ResponseEntity<DepotStats> counts() {
        return ResponseEntity.ok(depotService.getCounts());
    }
}
