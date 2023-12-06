package jpabook.jpashop.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	// 회원가입 폼 이동
	@GetMapping("/members/new") //value ="/members/new" 생략형
	public String createForm(Model model) {
		model.addAttribute("memberForm",new MemberForm());
		return "members/createMemberForm";
	}
	// 회원가입
	@PostMapping("members/new")
	public String create(@Valid MemberForm form, BindingResult result) {
		// @Valid : validation(검증) - MemberForm에서 @Notnull @size등을 확인
		// Spring MVC에서는 @Valid와 BindingResult를 함께 사용하여 컨트롤러에서 데이터 바인딩이나 검증 오류를 처리 가능
		// 유효성 검증에 실패시 해당 오류들은 BindingResult에 저장, 이를 활용해 사용자에게 오류 메시지를 제공 가능
		if(result.hasErrors()) {
			return "members/createMemberForm";
		}
		
		Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
		Member member = new Member();
		member.setName(form.getName());
		member.setAddress(address);
		
		memberService.join(member); // 저장
		
		return "redirect:/";
	}
	// 회원목록 조회
	@GetMapping("/members")
	public String list(Model model) {
		List<Member> members = memberService.findMembers(); 
		// 등록시엔 MemberForm을 썼는데 뿌릴 때는 Member를 쓰는 이유? => 단순하니까 한거고 실무에서는 DTO를 만들어서 화면에 뿌리길 권장
		// API를 만들 떄는 절대 엔티티를 넘기면 안된다!!! =>w? ex) 엔티티에 pw필드 추가시 패스워드 노출 + API 자체의 스펙이 변함  
		model.addAttribute("members",members);
//		model.addAttribute("members",memberService.findMembers()); // 인라인 스타일로 줄이기
		return "members/memberList";
	}
}
