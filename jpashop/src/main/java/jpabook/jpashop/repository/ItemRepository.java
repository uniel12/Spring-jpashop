package jpabook.jpashop.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
	
	private final EntityManager em;
	
	public void save(Item item) {
		if(item.getId() == null) {
			// insert
			em.persist(item);
		}else {
			// update
			/*
			 * 	준영속 엔티티를 수정하는 2가지 방법
			 * 	2. 병합(merge) 사용해서 데이터를 변경하는 방법(가급적 쓰지말자)
			 */
			em.merge(item); 

		}
	}
	
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll(){
		return em.createQuery("select i from Item i",Item.class)
				.getResultList();
	}
	
	/*
	 * 변경 감지와 병합(merge) 중요!!!
	 */
	// ** 준영속 엔티티 : 영속성 컨텍스트가 더는 관리하지 않는 엔티티를 말함
	// (여기서는 itemService.saveItem(book) 에서 수정을 시도하는 Book 객체 . 
	// Book 객체는 이미 DB에 한번 저장되어서 식별자가 존재.
	// 임의로 만들어낸 엔티티도 기존 식별자를 가지고 있으면 준영속 엔티티로 볼 수 있음
	
	// => JPA가 관리를 안하기 떄문에 영속성 컨텍스트가 아니다.
	// 따라서 JPA가 트랜잭션 후 커밋 전에 자동으로 해주는 Dirty Checking 이 일어나지 않는다!!!
	

}
