package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Category {
	
	@Id @GeneratedValue
	@Column(name ="category_id")
	private Long id;
	
	private String name;
	
	@ManyToMany // 실무에서 쓰지마라!
	@JoinTable(name = "category_item", // 중간(조인)테이블 이름
			   joinColumns = @JoinColumn(name="category_id"), // 현재의 Category 엔터티와 매핑되는 컬럼, Category 기본키 참조
			   inverseJoinColumns = @JoinColumn(name = "item_id"))// item 엔터티와 매핑되는 컬럼, Item 기본키 찹조
	private List<Item> items = new ArrayList<>(); // 연관관계의 주인!
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="parent_id")
	private Category parent;
	
	@OneToMany(mappedBy = "parent")
	private List<Category> child = new ArrayList<>();	
	
	//==연관관계 편의 메서드==//
	// 양방향 연관관계를 가진 경우, 핵심적인 코드 작성하는 곳에서 작성
	public void setMember(Category child) {
		this.child.add(child);
		child.setParent(this);
	}
	
}
