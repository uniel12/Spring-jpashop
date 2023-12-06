//package jpabook.jpashop;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.transaction.annotation.Transactional;
//
//@ExtendWith(SpringExtension.class) // 
//@SpringBootTest
//public class MemberRepository2Test {
//
//	@Autowired
//	MemberRepository2 memberRepository2;
//
//	
//	 // 엔티티 매니저를 통한 모든 데이터 변경은 트랜잭션 안에서 이루어져야함
//	 // @Transactional : 트랜잭션을 관리할 수 있게 하는 SpringFrame 의 어노테이션
//	 // 트랜잭션 :  "쪼갤 수 없는 업무 처리의 최소 단위"를 의미, 데이터베이스에서는 상태를 변화시키기 위해 수행하는 작업의 단위를 의미
//	 // Test 에 있으면 @Transactional은 항상 test 끝내고 ROllBACK 
//	@Test 
//	@Transactional 
//	@Rollback(false) // 롤백방지
//	public void test_name() throws Exception {
//		// given
//		Member2 member2 = new Member2();
//		member2.setUsername("memberA");
//		
//		// when
//		Long saveId = memberRepository2.save(member2);
//		Member2 findMember = memberRepository2.find(saveId);
//		// then
//		assertThat(findMember.getId()).isEqualTo(member2.getId());
//		assertThat(findMember.getUsername()).isEqualTo(member2.getUsername());
//		assertThat(findMember).isEqualTo(member2);
//		System.out.println("findMember == member"+(findMember == member2));
//		
//
//	}
//}
