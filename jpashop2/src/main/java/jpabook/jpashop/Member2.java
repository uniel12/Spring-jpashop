package jpabook.jpashop;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member2 { // 환경설정 TEST용 테이블
	
	@Id @GeneratedValue // 식별자(기본 키,primary key), 자동생성
	private Long id;
	private String username;
	
	// @Entity : JPA에서 사용하는 어노테이션으로 클래스가 엔터티임을 나타냄
	//			 데이터베이스와의 매핑을 위한 용도
	//			 Spring Data JPA와 같은 라이브러리를 사용할 때 Spring 컨테이너에서 관리
	// @Entity가 붙으면, JPA의 핵심 컴포넌트인 엔터티 매니저를 통해 관리

	
	
//	  @GeneratedValue : 기본 키(primary key) 값의 자동생성
//	  strategy :  
//			- GenerationType.AUTO (기본값) : 생략가능!	
//				JPA 구현체(예: Hibernate, EclipseLink)가 데이터베이스의 종류와 설정에 따라 자동으로 적절한 전략을 선택
//				단, 개발초기 단계에서는 문제가 없어보일 수 있으나 데이터베이스 변경시 문제가 발생할 수 있음
//		
//			- GenerationType.SEQUENCE : 시퀀스 사용 - <Oracle, H2>
//	              @SequenceGenerator 애노테이션과 함께 사용될 수 있어 시퀀스의 이름, 초기값, 증가값 등을 지정 가능
//		
//	        - GenerationType.IDENTITY : 자동 증가(auto-increment) 컬럼을 사용 -<MySQL, H2>
//	
//			- GenerationType.TABLE : 별도의 테이블을 사용하여 기본 키 값을 생성
//					@TableGenerator 애노테이션과 함께 사용하여 테이블 이름, 컬럼 이름 등을 지정 가능
//					시퀀스나 자동 증가 기능을 제공하지 않는 데이터베이스에서 사용
		
// 			ex) @GeneratedValue(strategy = GenerationType.IDENTITY) 
	
	
	
	
}
