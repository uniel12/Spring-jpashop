package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") // 엔티티 구별자
@Getter @Setter
public class Book extends Item {
	
	private String author;
	private String isbn;
	
	//== 수정 메서드==//
	public void change(String name, int price, int stockQuantity, String author, String isbn) {
		this.setName(name);
		this.setPrice(price);
		this.setStockQuantity(stockQuantity);
		this.setAuthor(author);
		this.setIsbn(isbn);
	}
}
