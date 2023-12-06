package jpabook.jpashop.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class Order {
	
	@Id @GeneratedValue
	@Column(name="order_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY) // 모든 연관관계는 지연로딩해야함!!!
	@JoinColumn(name ="member_id") // 연관관계의 주인!
	private Member member;
	// 즉시로딩(EAGER)은 예측이 어렵고 SQL 추적이 어렵다, n+1 문제가 자주 발생
	// JPQL select o from order o; -> SQL SELECT * FROM ORDER 100+1(order)
	// @ToOne(OneToOne, ManyToOne) 관계는 기본이 즉시로딩이므로 직접 지연로딩으로 설정
	
	
	// Cascade : 한 엔터티의 특정 작업이 연관된 엔터티에 어떻게 영향을 미칠지 정의하는 것
	// 1. PERSIST - 부모 저장시 자식도 저장
	// 2. REMOVE - 부모 삭제시 자식도 삭제
	// 3. MERGE - 부모 병합시 자식도 삭제
	// 4. REFRESH - 부모 새로고침시 자식도 새로고침
	// 5. DETACH - 부모 영속성 컨텍스트 분리시 자식도 분리(= 데이터베이스와의 동기화 중단)
	// => ALL : 위 다 포함 적용
	
	@OneToMany(mappedBy = "order", cascade =  CascadeType.ALL) 
	private List<OrderItem> orderItems = new ArrayList<>();
	
	@OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
	@JoinColumn(name = "delivery_id")
	private Delivery delivery;
	
	private LocalDateTime orderDate;
	
	@Enumerated(EnumType.STRING) 
	private OrderStatus status; // 주문 상태 [ORDER, CANCEL]
	
	// enum 클래스 : @Enumerated 어노테이션을 넣어야함
	
	// @Enumerated : enum타입일 때 값을 DB에 어떻게 저장할지 정의하는데 사용!
	// 				 - EnumType.ORDINAL(기본값) : enum 값의 순서를 데이터베이스에 저장 (쓰지말자!)
	//				 		순서에 민감O, 순서가 변경되면 데이터베이스에 저장된 값과 실제값이 일치X
	//				 - EnumType.STRING : enum 값의 이름을 문자열로 데이터베이스에 저장
	// 						순서에 민감X, 필드 크기가 더 클 수 있고 쿼리 성능이 느려질 수 있다는 단점이 있음
	
	//==연관관계 편의 메서드==//
	// 양방향 연관관계를 가진 경우, 핵심적인 코드 작성하는 곳에서 작성
	public void setMember(Member member) {
		this.member = member;
		member.getOrders().add(this);
	}
	
	public void addOrderItem(OrderItem orderItem) {
		orderItems.add(orderItem);
		orderItem.setOrder(this);
	}
	
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
		delivery.setOrder(this);
	}
	

	//==생성 메서드==//
	public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { 
		// OrderItem... : Varargs 메서드에 여러 개의 OrderItem을 넣을 수 있게 한다. 해당 타입의 배열로 변환되어 메서드 내에서 사용
		Order order = new Order();
		order.setMember(member);
		order.setDelivery(delivery);
		for (OrderItem orderItem : orderItems) {
			order.addOrderItem(orderItem);
		}
		order.setStatus(OrderStatus.ORDER);
		order.setOrderDate(LocalDateTime.now());
		return order;
	}
	
	//==비지니스 로직==//
	/*
	 * 주문 취소
	 */
	public void cancel() {
		if(delivery.getStatus() == DeliveryStatus.COMP) {
			throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
		}
		
		this.setStatus(OrderStatus.CANCEL);
		for (OrderItem orderItem : orderItems) {
			orderItem.cancel();
		}
	}
	
	//==조회 로직==//
	/*
	 * 전체 주문 가격 조회
	 */
	public int getTotalPrice() {
//		int totalPrice = 0;
//		for (OrderItem orderItem : orderItems) {
//			totalPrice += orderItem.getTotalPrice();
//		}
//		return totalPrice; // 아래 코드와 동일한 로직
		return orderItems.stream()
				.mapToInt(OrderItem::getTotalPrice)
				.sum();
		// 자바8의 Stream API
		// stream() 메서드는 주로 컬렉션(예: 리스트, 세트)에서 사용, 호출시 컬렉션 항목들을 표현하는 스트림 객체를 얻음
		// mapToInt() : 스트림에서 제공하는 맵핑 연산, 
		//				OrderItem 객체의 스트림에서 각 항목의 특정 int 값을 얻고 싶을 때 사용
		//	OrderItem::getTotalPrice -> 각 OrderItem에서 총 가격을 int로 가져와 스트림으로 만듬
		// :: => 특정 클래스의 메서드나 생성자를 참조할 떄 사용
		//		 람다 표현식 (orderItem) -> orderItem.getTotalPrice()를 더 간결하게 표현한 것
	}
			
}
