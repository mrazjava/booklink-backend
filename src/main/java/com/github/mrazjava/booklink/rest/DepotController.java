package com.github.mrazjava.booklink.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.rest.depot.DepotStats;
import com.github.mrazjava.booklink.rest.model.depot.FeaturedWorkResponse;
import com.github.mrazjava.booklink.service.DepotService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

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
    @RolesAllowed("ROLE_DETECTIVE")
    public ResponseEntity<DepotAuthor> findAuthorById(@ApiIgnore Authentication auth, @RequestParam String id) {
        return ResponseEntity.ok(depotService.findAuthorById(id).orElse(null));
    }

    @ApiOperation(
            value = "Desired number of randomly selected authors which contain an image"
    )
    @GetMapping("/author/featured")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @SwaggerIgnoreAuthToken
    @PermitAll
    public ResponseEntity<List<DepotAuthor>> featuredAuthors(@RequestParam Integer count) {
        return ResponseEntity.ok(depotService.randomAuthorWithImage(count));
    }

    @ApiOperation(
            value = "Find author by id"
    )
    @GetMapping("/author/search")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @SwaggerIgnoreAuthToken
    @PermitAll
    public ResponseEntity<List<DepotAuthor>> searchAuthors(@ApiIgnore Authentication auth, @RequestParam String search) {
        return ResponseEntity.ok(depotService.searchAuthors(search));
    }

    @ApiOperation(
            value = "Compute counts"
    )
    @GetMapping("/counts")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @RolesAllowed("ROLE_ADMIN")
    public ResponseEntity<DepotStats> counts() {
        return ResponseEntity.ok(depotService.getCounts());
    }

    @ApiOperation(
            value = "Desired number of randomly selected works which contain an image"
    )
    @GetMapping("/work/featured")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @SwaggerIgnoreAuthToken
    @PermitAll
    public ResponseEntity<List<FeaturedWorkResponse>> featuredWorks(@RequestParam Integer count) {

    	List<FeaturedWorkResponse> results = depotService.randomWorkWithImage(1).stream()
    		.map(work -> {
    			DepotAuthor author = work.getAuthors().stream()
    					// FIXME: pull author but exclude an image (not needed here)
    					.map(id -> depotService.findAuthorById(id).orElse(null))
    					.filter(au -> Optional.ofNullable(au).map(a -> StringUtils.isNotBlank(a.getId())).orElse(false))
    					.findFirst()
    					.orElse(null);
    			return new FeaturedWorkResponse(work, author);    			
    		})
    		.collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }
}
