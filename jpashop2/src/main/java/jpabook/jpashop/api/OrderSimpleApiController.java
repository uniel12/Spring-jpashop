package jpabook.jpashop.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/*
*
* xToOne(ManyToOne, OneToOne) 관계 성능 최적화
* Order 조회
* Order -> Member 연관
* Order -> Delivery 연관
*
*/
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
	
	private final OrderRepository orderRepository;
	
	/*
	 * V1. 엔티티 직접 노출
	 * - Hibernate5Module 모듈 등록, LAZY=null 처리
	 * - 양방향 관계 문제 발생 -> @JsonIgnore
	 *  주의: 엔티티를 직접 노출할 때는 양방향 연관관계가 걸린 곳은 꼭! 한곳을 @JsonIgnore 처리 해야 한다. 
	 *		  안그러면 양쪽을 서로 호출하면서 무한 루프가 걸린다
	 *- Hibernate5Module 를 사용하기 보다는 DTO로 변환해서 반환하는 것이 더 좋은방법
	 */
	@GetMapping("/api/v1/simple-orders")
	public List<Order> odersV1(){
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		for(Order order : all) {
			order.getMember().getName(); // Lazy 강제 초기화
			order.getDelivery().getAddress(); // Lazy 강제 초기화
		}
		return all;
	}
	/*
	 * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
	 * - 단점: 지연로딩으로 쿼리 N번 호출
	 */
	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2(){
		// 결과 주문수 2개-> 루프 2번
		// N + 1 -> 1 + 회원 N + 배송 N
//		order 조회 1번(order 조회 결과 수가 N이 된다.)
//		order -> member 지연 로딩 조회 N 번
//		order -> delivery 지연 로딩 조회 N 번
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		
		List<SimpleOrderDto> result = orders.stream()
				.map(o ->new SimpleOrderDto(o))
				.collect(Collectors.toList());
		return result;
//		return orderRepository.findAllByString(new OrderSearch()).stream()
//				.map(SimpleOrderDto::new)
//				.collect(toList()); //요약본
	}	
	
	@Data
	static class SimpleOrderDto {
		
		private Long orderId;
		private String name;
		private LocalDateTime orderDate; // 주문시간
		private OrderStatus orderStatus;
		private Address address;
		
		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName(); //Lazy 초기화
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress(); // Lazy 초기화
		}
	}
	
}
