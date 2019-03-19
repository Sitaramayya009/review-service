package com.epam.microservices.reviewservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="review")
public class Review {
	
	@Id
	private Long id;

	@Column(name = "PRODUCTID")
	private Long productId;

	private String name;

	@Column(name="notes")
	private String desc;

	private int rating;

}
