package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
	// item 공통 솟성
	private Long id;
	
	private String name;
	private int price;
	private int stockQuantity;
	
	// Book 속성
	private String author;
	private String isbn;
	
}
