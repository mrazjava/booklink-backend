package com.github.mrazjava.booklink.rest.model.depot;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mrazjava.booklink.rest.depot.DepotAuthor;
import com.github.mrazjava.booklink.rest.depot.DepotWork;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class FeaturedWorkResponse {

	@JsonProperty
	@ApiModelProperty
	private DepotWork work;

	@JsonProperty
	@ApiModelProperty
	private DepotAuthor author;


	public FeaturedWorkResponse(DepotWork work, DepotAuthor author) {
		this.work = work;
		this.author = author;
	}
}
