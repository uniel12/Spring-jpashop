package jpabook.jpashop.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Member {
	
	@Id @GeneratedValue
	@Column(name = "member_id") 
	private Long id;
	
	//@NotEmpty //엔티티를 받지 말자!  (api/v1/members)
	private String name;
	
	@Embedded
	private Address address;
	
	// Order 테이블의 member의 맵핑된 거울, 읽기 전용 FK값 변경X
	@JsonIgnore
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>(); 
	// 컬렉션은 필드에서 바로 초기화하자! null 문제에서 안전함
	
	
	// 엔터티 관계를 표시하는 JPA 어노테이션
	// 1. @ManyToOne : N:1
	// 				  @JoinColumn(name = "team_id") : FK를 명시적으로 지정 가능(연관관계의 주인이다!)
	// 2.  @OneToMany : 1:N
	//				  mappedBy 속성을 사용하여 연관관계의 주인 여부 지정 가능
	//		*연관관계의 주인?
	//		 => 외래 키의 등록, 수정, 삭제와 같은 관계를 관리하는 측에서 지정, 반대쪽은 단순히 참조만 
	//       ORM(Object-Relational Mapping)에서 객체와 테이블 사이의 관계를 효율적으로 관리하기 위함
	// 3. @OneToOne : 1:1
	// 4. @ManyToMany : N:N -> 중간 테이블 사용, @JoinTable을 사용하여 중간 테이블과의 관계를 설정 
	//						   => 실전에서는 쓰지 말 것!! 실무에서 거의 못 씀
}
