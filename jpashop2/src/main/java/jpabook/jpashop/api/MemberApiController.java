package jpabook.jpashop.api;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
public class MemberApiController {
	
	private final MemberService memberService;
	/*
	 * 조회 V1: 응답 값으로 엔티티를 직접 외부에 노출한다.
	 * 문제점
	 * - 엔티티에 프레젠테이션 계층을 위한 로직이 추가된다.
	 * - 기본적으로 엔티티의 모든 값이 노출된다.
	 * - 응답 스펙을 맞추기 위해 로직이 추가된다. (@JsonIgnore, 별도의 뷰 로직 등등)
	 * - 실무에서는 같은 엔티티에 대해 API가 용도에 따라 다양하게 만들어지는데,
	 *   한 엔티티에 각각의 API를 위한 프레젠테이션 응답 로직을 담기는 어렵다.
	 * - 엔티티가 변경되면 API 스펙이 변한다.
	 * - 추가로 컬렉션을 직접 반환하면 항후 API 스펙을 변경하기 어렵다.(별도의 Result 클래스 생성으로 해결)
	 * 
	 * 결론
	 * - API 응답 스펙에 맞추어 별도의 DTO를 반환한다.
	 */
	//조회 V1: 안 좋은 버전, 모든 엔티티가 노출,
	// 엔티티에 @JsonIgnore -> 이건 정말 최악, api가 이거 하나인가! 화면에 종속적이지 마라!
	@GetMapping("/api/v1/members")
	public List<Member> membersV1(){
		return memberService.findMembers();
	}
	/*
	 * 조회 V2: 응답 값으로 엔티티가 아닌 별도의 DTO를 반환한다.
	 * - 엔티티가 변해도 API 스펙이 변경되지 않는다
	 * - 추가로 Result 클래스로 컬렉션을 감싸서 향후 필요한 필드를 추가할 수 있다.
	 */
	@GetMapping("/api/v2/members")
	public Result membersV2() {
		List<Member> findMembers = memberService.findMembers();
		// 엔티티 -> DTO 변환
		List<MemberDto> collect = findMembers.stream()
				.map(m -> new MemberDto(m.getName()))
				.collect(Collectors.toList());
		
		return new Result(collect.size(),collect); 
	}
	
	@Data
	@AllArgsConstructor
	static class Result<T>{
		private int count;
		private T data;
	}
	
	@Data
	@AllArgsConstructor
	static class MemberDto{
		private String name;
	}
	
	/*
	 * 등록 V1: 요청 값으로 Member 엔티티를 직접 받는다. 
	 * 문제점 
	 * - 엔티티에 프레젠테이션 계층(화면에서 들어오는 validation)을 위한 로직이 추가된다. 
	 * - 엔티티에 API 검증을 위한 로직이 들어간다. (@NotEmpty 등등) 
	 * - 실무에서는 회원 엔티티를 위한 API가 다양하게 만들어지는데,
	 *   한 엔티티에 각각의 API를 위한 모든 요청 요구사항을 담기는 어렵다. 
	 * - 엔티티가 변경되면 API 스펙이 변한다!! => 실무에서는 엔티티를 API 스펙에 노출하면 안된다!
	 * 결론 
	 * - API 요청 스펙에 맞추어 별도의 DTO를 파라미터로 받는다.
	 */
	@PostMapping("/api/v1/members") // 회원등록 api
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	@Data
	static class CreateMemberResponse {
		private Long id;
		
		public CreateMemberResponse(Long id) {
			this.id = id;
		}
		
	}
	
	/*
	 * 등록 V2: 요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
	 *  이 방법으로 하자!!!
	 */
	@PostMapping("/api/v2/members")
	public CreateMemberResponse savememberV2(@RequestBody @Valid CreateMemberRequest request) {
		
		Member member = new Member();
		member.setName(request.getName());
		
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	@Data
	static class CreateMemberRequest {
		@NotEmpty
		private String name;
	}
	
	/*
	 * 수정 API 
	 * - MemberService에 update 비즈니스 로직 추가
	 */
	@PutMapping("/api/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(
			@PathVariable("id") Long id,
			@RequestBody @Valid UpdateMemberRequest request){
		
		// 커맨드(상태를 변경시키는 작업)와 쿼리(정보조회)를 분리한다!!!
		memberService.update(id, request.getName()); // 커맨드 : 상태(멤버의 이름)을 변경
		Member findMember = memberService.findOne(id); // 쿼리 : 멤버의 정보를 조회
		return new UpdateMemberResponse(id, findMember.getName());
	}

	@Data
	static class UpdateMemberRequest {
		private String name;
	}
	
	@Data
	@AllArgsConstructor //엔티티에는 getter정도만, DTO에는 롬복 어노테이션 막 써도 됨
	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}
	

}
