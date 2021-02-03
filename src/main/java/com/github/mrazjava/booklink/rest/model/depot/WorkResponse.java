package com.github.mrazjava.booklink.rest.model.depot;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.rest.depot.DepotWork;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class WorkResponse {

	@JsonProperty
	@ApiModelProperty
	private DepotWork work;

	@JsonProperty
	@ApiModelProperty
	private List<DepotAuthor> authors;


	public WorkResponse(DepotWork work, List<DepotAuthor> authors) {
		this.work = work;
		this.authors = authors;
	}
}
