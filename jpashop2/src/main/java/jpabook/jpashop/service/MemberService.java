package jpabook.jpashop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true) //JPA의 데이터 변경이나 로직들은 가급적 트랜잭션 안에서 이루어져야함
@RequiredArgsConstructor
public class MemberService {
	
	private final MemberRepository memberRepository;
	
//	@Autowired
//	public MemberService(MemberRepository memberRepository) {
//		this.memberRepository = memberRepository;
//	}
	
	/*
	 * 회원가입
	 */	
	@Transactional
	public Long join(Member member) {
		validateDuplicateMember(member); //중복회원 검증
		memberRepository.save(member);
		return member.getId();
	}
	private void validateDuplicateMember(Member member) {
		//Exception
		List<Member> findMembers =  memberRepository.findByName(member.getName());
		if (!findMembers.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 회원입니다.");
		// 이 로직은 동시 같은 이름 가입시 잡지 못하므로 DB name 값 unique 처리
		}
	}
	
	/*
	 * 회원전체조회
	 */	
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}
	
	/*
	 *  회원 업데이트 RestAPI
	 */
	@Transactional
	public void update(Long id, String name) {
		Member member = memberRepository.findOne(id);
		member.setName(name);
	}
}
