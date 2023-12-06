package jpabook.jpashop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j // log 찍는 어노테이션
public class HomeController {
	
	// Logger log = LoggerFactory.getLogger(getClass()); // lombok 제공
	@RequestMapping("/")
	public String home() {
		log.info("home controller");
		return "home";
		
	}
}
