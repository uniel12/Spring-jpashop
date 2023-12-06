package jpabook.jpashop.domain.item;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToMany;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="dtype") // 싱클테이블 저장할 때 구분자
@Getter @Setter
public abstract class Item {
	
	// @Inheritance : JPA 상속관계 매핑시 사용
	// strategy(상속관계 매핑전략) 종류 :  
	//				  1. SingleTable - 모든 엔티티를 단일 테이블에 저장하는 전략
	//				  2. TablePerClass - 각 엔티티 타입마다 별도의 테이블을 사용하는 전략 (특정 상황 비효율적)
	//				  3. Joined - 가장 정교화된 스타일(부-자 엔티티마다 테이블을 사용하며 조인을 통해 연관된 데이터 조회)
	// @DiscriminatorColumn : SingleTable 전략을 사용할 때, 엔티티 타입을 구별하기 위한 컬럼 지정하는데 사용함
	// @DiscriminatorValue : SingleTable 전략을 사용할 때, 해당 엔티티 타입의 구분자를 정해주는데 사용함
	@Id
	@GeneratedValue
	@Column(name ="item_id")
	private Long id;
	
	private String name;
	private int price;
	private int stockQuantity;
	
	@ManyToMany(mappedBy = "items")
	private List<Category> categories = new ArrayList<>();
	
	//==비즈니스 로직==//
	/*
	 * stock 증가 
	 */
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	/*
	 * stock 감소
	 */
	public void removeStock(int orderQuantity) {
		int restStock = this.stockQuantity - orderQuantity;
		if(restStock <0) {
			throw new NotEnoughStockException("need more stock");
		}
		this.stockQuantity =restStock;
	}
	
}
