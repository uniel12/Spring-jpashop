package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.websocket.server.PathParam;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.UpdateItemDto;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;
	
	// 상품 등록 폼이동
	@GetMapping("/items/new")
	public String createForm(Model model) {
		model.addAttribute("form", new BookForm());
		return "items/createItemForm";
	}
	// 상품 등록
	@PostMapping("/items/new")
	public String create(BookForm form) {
		// 예제라 setter 열어놨지만 Order처럼 createBook 따로 만들어서 하는게 좋음
		// 실무에서는 setter 다 날려야 함
		Book book = new Book();
		book.setName(form.getName());
		book.setPrice(form.getPrice());
		book.setStockQuantity(form.getStockQuantity());
		book.setAuthor(form.getAuthor());
		book.setIsbn(form.getIsbn());
		
		itemService.saveItem(book);	
		return "redirect:/items";	
	}
	
	// 상품 목록
	@GetMapping("/items")
	public String list(Model model) {
		
		List<Item> items = itemService.findItems();
		model.addAttribute("items",items);
		return "items/itemList";
 	}
	
	// 상뭎 수정 폼
	@GetMapping("/items/{itemId}/edit")
	public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
		Book item = (Book)itemService.findOne(itemId); // 다운캐스팅이 좋지는 않음 예제를 단순화하기 위함
		
		BookForm form = new BookForm();
		
		form.setId(item.getId());
		form.setName(item.getName());
		form.setPrice(item.getPrice());
		form.setStockQuantity(item.getStockQuantity());
		form.setAuthor(item.getAuthor());
		form.setIsbn(item.getIsbn());
		
		model.addAttribute("form",form);
		return "items/updateItemForm";
		
	}
	// 상품 수정
	@PostMapping("/items/{itemId}/edit")
	public String updateItem(@PathVariable Long itemId ,@ModelAttribute("form") BookForm form) { 
		// @PathVariable String itemId 여기서는 생략 가능
		// 주의 ) 원래는 유저가 아이템에 대해서 권한 여부를 확인하는 로직이 있어야함!
		
		// 컨트롤러에서 어설프게 이렇게 엔티티 생성하지말자.
//		Book book = new Book(); //준영속 엔티티
//		book.setId(form.getId());
//		book.setName(form.getName());
//		book.setPrice(form.getPrice());
//		book.setStockQuantity(form.getStockQuantity());
//		book.setAuthor(form.getAuthor());
//		book.setIsbn(form.getIsbn());
//		
//		itemService.saveItem(book);
		UpdateItemDto dto = new UpdateItemDto(form.getName(),form.getPrice(),form.getStockQuantity(),form.getAuthor(),form.getIsbn());
	
//		itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity(),form.getAuthor(),form.getIsbn());
		itemService.updateItem(itemId, dto);
		
		
		return "redirect:/items";
		
		// 주의!!! 변경 감지 기능을 사용하면 원하는 속성만 선택해서 변경할 수 있지만, 병합을 사용하면 모든 속성이
		// 변경된다. 병합시 값이 없으면 null 로 업데이트 할 위험도 있다. (병합은 모든 필드를 교체한다.) 
		// => 따라서 merge 방식이 아닌 변경 감지 기능을 사용해야함!
	}
	
}
