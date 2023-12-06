package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
	
	private final ItemRepository itemRepository;
	
	@Transactional
	public void saveItem(Item item) {
		itemRepository.save(item);
	}
	/* 준영속 엔티티를 수정하는 2가지 방법
	 * 1. 변경 감지 기능 사용해서 데이터를 변경하는 방법(권장방법)
	 * 	  영속성 컨텍스트가 자동 변경
	 */
	// transaction이 커밋되면 JPA가 플러쉬 해줘 이제 영속성 컨텍스트에 있는 걸 찾아서 바뀐걸 찾아 update query를 날려줘
	// 이게 변경 감지야. 이게 더 나은 방법이지!
	@Transactional
	public void updateItem(Long itemId, UpdateItemDto dto) {
		Book findBook = (Book)itemRepository.findOne(itemId); 
		
		findBook.change(dto.getName(), dto.getPrice(), dto.getStockQuantity(),dto.getAuthor(),dto.getIsbn()); 
//		Item findItem = itemRepository.findOne(itemId);
//		findItem.setName(name);
//		findItem.setPrice(price); // 이렇게 쓰지말고! 위의 방식처럼 setter 대신에 의미있는 메소드를 만들어서 쓰자
		
	}
//	- 영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법
//	트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 트랜잭션 커밋 시점에
//	변경 감지(Dirty Checking)이 동작해서 데이터베이스에 UPDATE SQL 실행
	
	
	public List<Item> findItems(){
		return itemRepository.findAll();
	}
	
	public Item findOne(Long itemId) {
		return itemRepository.findOne(itemId);
	}
	

}
