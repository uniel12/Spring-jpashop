package jpabook.jpashop.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
public class MemberServiceTest {

	@Autowired
	MemberService memberService;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	EntityManager em;

	@Test
	// @Rollback(false) // 자동롤백해지
	public void 회원가입() throws Exception {
		// given
		Member member = new Member();
		member.setName("kim");
		// when
		Long saveId = memberService.join(member);
		// then
		em.flush(); // insert문 보기만
		assertEquals(member, memberRepository.findOne(saveId));
	}

	@Test
	public void 중복_회원_예외() throws Exception {

		// given
		Member member1 = new Member();
		member1.setName("kim");
		Member member2 = new Member();
		member2.setName("kim");

		// when
		memberService.join(member1);


		// then
		// fail("예외가 발생해야한다.");
		// 중복 예외가 발생하는 부분
		assertThatThrownBy(() -> memberService.join(member2))
				.isInstanceOf(IllegalStateException.class);

		// 위에서 예외 발생 여부를 검증하므로 삭제
		// Assertions.fail("예외가 발생해야 합니다.");
	}
}
