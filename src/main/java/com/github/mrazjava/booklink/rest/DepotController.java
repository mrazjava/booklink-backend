package com.github.mrazjava.booklink.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Produces;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.rest.depot.DepotEdition;
import com.github.mrazjava.booklink.rest.depot.DepotStats;
import com.github.mrazjava.booklink.rest.depot.DepotWork;
import com.github.mrazjava.booklink.rest.model.depot.FeaturedWorkResponse;
import com.github.mrazjava.booklink.service.DepotService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
    @GetMapping("/author/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<DepotAuthor> findAuthorById(
    		@ApiIgnore Authentication auth, 
    		@PathVariable String id,
    		@ApiParam(value = "include small img?") @RequestParam(value = "imgS", required = false) Boolean imgS, 
    		@ApiParam(value = "include medium img?") @RequestParam(value = "imgM", required = false) Boolean imgM, 
    		@ApiParam(value = "include large img?") @RequestParam(value = "imgL", required = false) Boolean imgL    		
    		) {
    	DepotAuthor result = depotService.findAuthorById(
    			id, 
    			BooleanUtils.toBooleanDefaultIfNull(imgS, true), 
    			BooleanUtils.toBooleanDefaultIfNull(imgM, true), 
    			BooleanUtils.toBooleanDefaultIfNull(imgL, true)
    			).orElse(null);
    	return ResponseEntity.ok(result);
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
            value = "Free style author search by keyword(s) (bio, title, name, etc)"
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
    @PermitAll
    public ResponseEntity<DepotStats> counts() {
        return ResponseEntity.ok(depotService.getCounts());
    }
    
    @ApiOperation(value = "Fetch AUTHORS, paginated")
    @GetMapping(path = "paged/authors")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<List<DepotAuthor>> pagedAuthors(
    		@ApiParam(value = "page number starting with 1", required = true) @RequestParam("pageNo") Integer pageNo, 
    		@ApiParam(value = "number of rows per page") @RequestParam(value = "pageSize", required = false) Integer pageSize, 
    		@ApiParam(value = "include small img?") @RequestParam(value = "imgS", required = false) Boolean imgS, 
    		@ApiParam(value = "include medium img?") @RequestParam(value = "imgM", required = false) Boolean imgM, 
    		@ApiParam(value = "include large img?") @RequestParam(value = "imgL", required = false) Boolean imgL
    		) {
    	List<DepotAuthor> results = depotService.getAuthorsPaged(pageNo, pageSize, imgS, imgM, imgL);
    	return ResponseEntity.ok(results);
    }

    @ApiOperation(
            value = "Find author by id"
    )
    @GetMapping("/work/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<DepotWork> findWorkById(
    		@ApiIgnore Authentication auth, 
    		@PathVariable String id,
    		@ApiParam(value = "include small img?") @RequestParam(value = "imgS", required = false) Boolean imgS, 
    		@ApiParam(value = "include medium img?") @RequestParam(value = "imgM", required = false) Boolean imgM, 
    		@ApiParam(value = "include large img?") @RequestParam(value = "imgL", required = false) Boolean imgL    		
    		) {
    	DepotWork result = depotService.findWorkById(
    			id, 
    			BooleanUtils.toBooleanDefaultIfNull(imgS, true), 
    			BooleanUtils.toBooleanDefaultIfNull(imgM, true), 
    			BooleanUtils.toBooleanDefaultIfNull(imgL, true)
    			).orElse(null);
    	return ResponseEntity.ok(result);
    }

    @ApiOperation(
            value = "Desired number of randomly selected works which contain an image"
    )
    @GetMapping("/work/featured")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @SwaggerIgnoreAuthToken
    @PermitAll
    public ResponseEntity<List<FeaturedWorkResponse>> featuredWorks(@ApiParam(value = "desired number of works", defaultValue = "1") @RequestParam(required = false, defaultValue = "1") Integer count) {

    	List<FeaturedWorkResponse> results = depotService.randomWorkWithImage(Optional.ofNullable(count).orElse(1)).stream()
    		.map(work -> {
    			DepotAuthor author = work.getAuthors().stream()
    					.map(id -> depotService.findAuthorById(id, false, false, false).orElse(null))
    					.filter(au -> Optional.ofNullable(au).map(a -> StringUtils.isNotBlank(a.getId())).orElse(false))
    					.findFirst()
    					.orElse(null);
    			return new FeaturedWorkResponse(work, author);    			
    		})
    		.collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    @ApiOperation(value = "Fetch WORKS, paginated")
    @GetMapping(path = "paged/works")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<List<DepotWork>> pagedWorks(
    		@ApiParam(value = "page number starting with 1", required = true) @RequestParam("pageNo") Integer pageNo, 
    		@ApiParam(value = "number of rows per page") @RequestParam(value = "pageSize", required = false) Integer pageSize, 
    		@ApiParam(value = "include small img?") @RequestParam(value = "imgS", required = false) Boolean imgS, 
    		@ApiParam(value = "include medium img?") @RequestParam(value = "imgM", required = false) Boolean imgM, 
    		@ApiParam(value = "include large img?") @RequestParam(value = "imgL", required = false) Boolean imgL
    		) {
    	List<DepotWork> results = depotService.getWorksPaged(pageNo, pageSize, imgS, imgM, imgL);
    	return ResponseEntity.ok(results);
    }

    @ApiOperation(
            value = "Find edition by id"
    )
    @GetMapping("/edition/{id}")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<DepotEdition> findEditionById(
    		@ApiIgnore Authentication auth, 
    		@PathVariable String id,
    		@ApiParam(value = "include small img?") @RequestParam(value = "imgS", required = false) Boolean imgS, 
    		@ApiParam(value = "include medium img?") @RequestParam(value = "imgM", required = false) Boolean imgM, 
    		@ApiParam(value = "include large img?") @RequestParam(value = "imgL", required = false) Boolean imgL    		
    		) {
    	DepotEdition result = depotService.findEditionById(
    			id, 
    			BooleanUtils.toBooleanDefaultIfNull(imgS, true), 
    			BooleanUtils.toBooleanDefaultIfNull(imgM, true), 
    			BooleanUtils.toBooleanDefaultIfNull(imgL, true)
    			).orElse(null);
    	return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "Fetch EDITIONS, paginated")
    @GetMapping(path = "paged/editions")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<List<DepotEdition>> pagedEditions(
    		@ApiParam(value = "page number starting with 1", required = true) @RequestParam("pageNo") Integer pageNo, 
    		@ApiParam(value = "number of rows per page") @RequestParam(value = "pageSize", required = false) Integer pageSize, 
    		@ApiParam(value = "include small img?") @RequestParam(value = "imgS", required = false) Boolean imgS, 
    		@ApiParam(value = "include medium img?") @RequestParam(value = "imgM", required = false) Boolean imgM, 
    		@ApiParam(value = "include large img?") @RequestParam(value = "imgL", required = false) Boolean imgL
    		) {
    	List<DepotEdition> results = depotService.getEditionsPaged(pageNo, pageSize, imgS, imgM, imgL);
    	return ResponseEntity.ok(results);
    }
}
