package jpabook.jpashop.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;

@Repository // @Repository : 스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
@RequiredArgsConstructor 	// @Autowired 로 바꾸면 생성자주입으로 바꿀 수 있음
public class MemberRepository {
	
	// @PersistenceContext //Spring EntityManager 주입, JPA 지원 Autowired로 바꿀 수 있음
	private final EntityManager em;
	

	public void save(Member member) {
		em.persist(member); 
		// persist() 메서드를 통해 객체를 영속성 컨텍스트에 추가
		// 나중에 트랜잭션 커밋 시 DB에 반영 => insert
	}
	
	public Member findOne(Long id) {
		return em.find(Member.class, id); 
		// find(엔터티의 Type, 식별자(PK))
	}
	
	public List<Member> findAll(){
		return em.createQuery("select m from Member m",Member.class)
				.getResultList(); //SelectAll
		// jpql : 엔터티 객체에 대한 alias를 m 으로 주고 entity 멤버 조회함
	}
	
	public List<Member> findByName(String name){
		return em.createQuery("select m from Member m where m.name = :name",Member.class)
				.setParameter("name", name)
				.getResultList(); //SelectName
	}
}
