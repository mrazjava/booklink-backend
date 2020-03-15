package com.github.mrazjava.booklink.rest;

import com.github.mrazjava.booklink.model.DbInfoResponse;
import com.github.mrazjava.booklink.service.ProofOfConceptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.ws.rs.Produces;
import java.sql.SQLException;

/**
 * @since 0.1.0
 */
@Api(
        description = "Experiments for initial deployment setup and other new concepts.",
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
    @GetMapping("random-count")
    @Produces("application/json")
    @ApiResponses(
            {
                    @ApiResponse(
                            message = "ok",
                            code = 200
                    )
            }
    )
    public ResponseEntity<Integer> countAll() {
        return new ResponseEntity<>(pocService.randomCount(), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Database info",
            consumes = "application/json"
    )
    @GetMapping("db-info")
    @Produces("application/json")
    @ApiResponses(
            {
                    @ApiResponse(
                            message = "ok",
                            code = 200
                    )
            }
    )
    public ResponseEntity<DbInfoResponse> dbInfo() throws SQLException {
        return new ResponseEntity<>(pocService.dbInfo(), HttpStatus.OK);
    }
}
