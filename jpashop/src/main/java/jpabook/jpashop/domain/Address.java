package jpabook.jpashop.domain;

import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable //다른 엔터티에 포함될 수 있는 타입, 재사용 가능
@Getter
//@AllArgsConstructor
//@NoArgsConstructor(access= AccessLevel.PROTECTED) 
public class Address {
	
	private String city;
	private String street;
	private String zipcode;
	
	protected Address() {
	}
	
	public Address(String city, String street, String zipcode) {
		this.city = city;
		this.street = street;
		this.zipcode = zipcode;
	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(city, street, zipcode);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Address other = (Address) obj;
//		return Objects.equals(city, other.city) && Objects.equals(street, other.street)
//				&& Objects.equals(zipcode, other.zipcode);
//	}
	
	
	// 임베디드 타입(Embeddable Type, 값 타입) : 
	// 		식별자를 가지지 않고, 엔터티 타입과 달리 독립적으로 존재할 수 없으며
	//		항상 엔터티에 포함된 것!
	// 클레스에는 @Embeddable로 표시, 엔터티 필드에는 @Embedded로 표시
	// 값 타입은 변경 불가능 => @Setter 를 제거하고, 생성자에서 값을 모두 초기화
	
	// - 기본생성자가 없으면 나중에 reflection 이나 proxy 기술 못 쓰니까 만듬
	// - JPA 스펙상 엔티티나 임베디드 타입 기본생성자 를 public 또는 protected 로 설정해야 함 test시에만 public
}
