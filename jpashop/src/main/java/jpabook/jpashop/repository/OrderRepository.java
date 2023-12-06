package jpabook.jpashop.repository;



import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

	private final EntityManager em;

	public void save(Order order) {
		em.persist(order);
	}

	public Order findOne(Long id) {
		return em.find(Order.class, id);
	}

	public List<Order> findAllByString(OrderSearch orderSearch) {
//		return em.createQuery("select o from Order o join o.member"+ 
//						"where o.status = :status "+
//						"and m.name like :name", Order.class)
//				.setParameter("status", orderSearch.getOrderStatus())
//				.setParameter("name", orderSearch.getMemberName())
//				.setMaxResults(1000) // 최대 1000건
//				.getResultList();
//		.setFirstResult(100) : 페이징시 100부터
		// 동적쿼리 : 검색 조건에 동적으로 쿼리를 생성해서 주문 엔티티를 조회
		// language=JPAQL
		String jpql = "select o From Order o join o.member m";
		boolean isFirstCondition = true;
		// 주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " o.status = :status";
		}
		// 회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			if (isFirstCondition) {
				jpql += " where";
				isFirstCondition = false;
			} else {
				jpql += " and";
			}
			jpql += " m.name like :name";
		}
		TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000); // 최대 1000건
		if (orderSearch.getOrderStatus() != null) {
			query = query.setParameter("status", orderSearch.getOrderStatus());
		}
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			query = query.setParameter("name", orderSearch.getMemberName());
		}
		return query.getResultList();
		// JPQL 쿼리를 문자로 생성하는 방식으로 번거롭고, 실수로 인한 버그가 많을 수 있음
	}

	/*
	 * JPA Criteria
	 */
	public List<Order> findAllByCriteria(OrderSearch orderSearch) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> o = cq.from(Order.class);
		Join<Order, Member> m = o.join("member", JoinType.INNER); // 회원과 조인
		
		List<Predicate> criteria = new ArrayList<>();
		
		// 주문 상태 검색
		if (orderSearch.getOrderStatus() != null) {
			Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
			criteria.add(status);
		}
		
		// 회원 이름 검색
		if (StringUtils.hasText(orderSearch.getMemberName())) {
			Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
			criteria.add(name);
		}
		cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
		TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); // 최대1000건
		return query.getResultList();
		// JPA 표준 스펙이지만 실무에서 사용하기에 너무 복잡
	}
	/*
	 * Querydsl로 처리 : 이 방법 쓰자! 나중에
	 */
//	public List<Order> findAll(OrderSearch orderSearch){
//		QOrder order = QOrder.order;
//		QMember member = QMember.member;
//		
//		return query
//				.select(order);
//				.from(order)
//				.join(order.member, member)
//				.where(statuEq(orderSearch.getOrderStatus()),
//						nameLike(orderSearch.getMemberName))
//				.limit(1000)
//				.fetch();
//	}
//	private BooleanExpression statusEq(OrderStatus statusCond) {
//		if (statusCond == null) {
//			return null;
//		}
//		return order.status.eq(statusCond);
//	}
//	private BooleanExpression nameLike(String nameCond) {
//		if(!StringUtils.hasText(nameCond)) {
//			return null;
//		}
//		return member.name.like(nameCond);
//	}
}
