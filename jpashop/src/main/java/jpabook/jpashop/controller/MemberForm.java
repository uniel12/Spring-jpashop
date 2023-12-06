package jpabook.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
	// 유지보수를 하기 쉽게 하기 위해 Member 엔티티를 쓰지 않고 MemberForm을 만듬
	// JPA 에서는 엔티티를 최대한 순수하게 유지하는게 중요!
	// 엔티티는 핵심 비지니스 로직만 있고 화면을 위한 건 없는게 맞음
	// 화면을 위한 건 폼 객체나 DTO를 써야함
	@NotEmpty(message ="회원 이름은 필수 입니다.")
	private String name;
	
	private String city;
	private String street;
	private String zipcode;
}
