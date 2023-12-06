package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.datatype.hibernate5.jakarta.Hibernate5JakartaModule;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		Hello hello = new Hello();
		hello.setData("hello");
		String data = hello.getData();
		System.out.println("data = " + data);
		SpringApplication.run(JpashopApplication.class, args);
	}

	// 하이버네이트 모듈 빈 등록 :
	// jackson 라이브러리가 프록시 객체를 json으로 어떻게 생성하는지 몰라서 예외 발생 -> 해결을 위한 것
	// 기본적으로 초기화 된 프록시 객체만 노출
//	@Bean
//	Hibernate5JakartaModule hibernate5Module() {
//	    return new Hibernate5JakartaModule();
//	}

	// 강제 지연 로딩 설정을 하기 위해서는 이와 같이 추가해주면 된다.
	@Bean
	Hibernate5JakartaModule hibernate5JakartaModule() {
	    Hibernate5JakartaModule hibernate5JakartaModule = new Hibernate5JakartaModule();
//	    hibernate5JakartaModule.configure(Hibernate5JakartaModule.Feature.FORCE_LAZY_LOADING, true);
	    return hibernate5JakartaModule;
	}
}
