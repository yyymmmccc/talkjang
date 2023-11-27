package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller  // 컨트롤러의 역할을 할 수 있게해줌
// 사용자가 요청을 보냈을 때 출발하는 곳이 controller
public class UsrHomeController {
	int cnt = 0;
	
	@RequestMapping("/usr/home/main") // 도메인 주소를 매핑해줌
	@ResponseBody // 리턴의 내용이 화면에 그려짐
	public String showMain() {
		return "안녕하세요234";
	}
	
	@RequestMapping("/usr/home/test1")
	@ResponseBody // 리턴의 내용이 화면에 그려짐
	public int Cnt() {
		return cnt++;
	}
}
