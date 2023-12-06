package jpabook.jpashop;


import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class MemberRepository2 {
	
	@PersistenceContext // 엔티티 메니저
	private EntityManager em;
	
	public Long save(Member2 member2) {
		em.persist(member2);
		return member2.getId();
	}
	
	public Member2 find(Long id) {
		return em.find(Member2.class, id);
	}
	
	// - 스테레오타입 애노테이션 : Spring에서 빈을 자동으로 스캔하고 등록하기 위한 주요 애노테이션
	//		 @Repository : 해당 클래스가 DAO 또는 Repository 역할을 하는 빈이라는 걸 알려주는 어노테이션
	//				 	   @Component의 일종, 컴포넌트 스캔의 대상이 되어 자동으로 Spring 빈 등록
	// 		 @Component : 특별한 역할을 가지지 않은 빈을 정의할 때 사용
	// 		 @Service : 서비스 계층의 비즈니스의 로직을 포함하는 컴포넌트를 나타냄
	// 		 @Controller : Spring MVC 컨트롤러를 정의, 주로 뷰 이름을 반환
	//				    - @ResponseBody : 메소드 반환값이 문자열 그 자체일 때 사용함!
	//       @RestController : RESTful 웹 서비스의 컨트롤러를 정의
	//					- 메서드의 반환값이 ResponseBody로 자동변환, @ResponseBody를 별도로 사용할 필요X
	//					- 데이터(예: JSON)를 직접 반환
	
	// @PersistenceContext : JPA 애노테이션, 주로 EntityManager를 주입받기 위해 사용
	// EntityManager는 JPA의 핵심 컴포넌트로, 데이터베이스의 CRUD(Create, Read, Update, Delete) 작업을 수행하는 데 사용
}
