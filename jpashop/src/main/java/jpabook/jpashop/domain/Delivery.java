package jpabook.jpashop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Delivery {
	
	@Id @GeneratedValue
	@Column(name = "delivery_id")
	private Long id;
	
	@OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
	private Order order;
	
	
	@Embedded //다른 엔터티에 포함될 수 있는 타입임을 나타냄
	private Address address; 
	
	@Enumerated(EnumType.STRING) 
	private DeliveryStatus status; // 배송 상태  [READY, COMP]
}
