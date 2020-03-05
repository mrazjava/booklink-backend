package com.github.mrazjava.booklink.ui;

import com.github.mrazjava.booklink.service.BookService;
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

/**
 * @since 0.1.0
 */
@Api(
        tags = {"Books"}
)
@RestController
@RequestMapping("rest/v1/book")
public class BookRestController {

    @Inject
    private Logger log;

    @Inject
    private BookService bookService;


    @ApiOperation(
            value = "Book count",
            consumes = "application/json"
    )
    @GetMapping("count-all")
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
        return new ResponseEntity<>(bookService.totalCount(), HttpStatus.OK);
    }
}
