package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;
	private final MemberService memberService;
	private final ItemService itemService;
	
	// 주문 폼 이동
	@GetMapping("/order")
	public String createForm(Model model) {
		
		List<Member> members= memberService.findMembers();
		List<Item> items = itemService.findItems();
		
		model.addAttribute("members", members);
		model.addAttribute("items", items);
		
		return "order/orderForm";
	}
	
	// 주문
	@PostMapping("/order")
	public String order (@RequestParam("memberId") Long memberId, // @RequestParam : form 의 데이터를 바인딩해서 받는 것
						 @RequestParam("itemId") Long itemId,	  //                (form 안 태그 name 의 value 값)
						 @RequestParam("count") int count) {
		orderService.order(memberId, itemId, count); 
		// 이런 핵심 비지니스 로직은 이렇게 식별자만 남겨주고 엔티티의 @Transaction 안에서(영속 컨텍스트가 존재하는 상태에서)
		// 처리해줘야 예를 들면 주문하면서 멤버나 아이템 정보가 바뀌더라도 DirtyChecking 이 적용된다
		return "redirect:/orders";
		// 나중에 주문된 자신의 주문 정보 페이지로 간다면
//		Long orderId = orderService.order(memberId, itemId, count);
//		return "redirect:/orders/"+orderId;
	}
	
	// 주문 목록
	@GetMapping("/orders")
	public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
		// @ModelAttribute에 세팅 시 모델 박스에 자동으로 담기고 뿌릴 수도 있음
		// model.addAttribute("orderSearch", orderSearch); 이 코드가 생략됐다고 보면 됨
		List<Order> orders = orderService.findOrders(orderSearch);
		model.addAttribute("orders", orders);
		return "order/orderList";
	}
	
	// 주문 취소
	@PostMapping("/orders/{orderId}/cancel")
	public String cancelOrder(@PathVariable("orderId") Long orderId) {
		orderService.cancelOrder(orderId);
		
		return "redirect:/orders";
	}
	

}
