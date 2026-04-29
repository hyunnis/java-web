package org.acme; // 패키지 선언 org.acme = 하위 코드

import jakarta.ws.rs.GET; 
// import문, 외부 라이브러리나 다른 패키지의 클래스를 가지고 옴
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
// @ : 어노테이션, 클래스나 메서드에 부가 정보를 붙이는 문법 - 클래스 앞 항상 표기
// hello라는 경로로 입력받음
public class GreetingResource { 
    // 클래스 선언, 클래스 이름 정의. public은 접근 제한자, class는 키워드, {로 시작
    // GreetingResource : 이름 

    @GET 
    // 메서드 어노테이션, 메서드에 붙이는 어노테이션. 여러 개를 연속으로 붙일 수 있음
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() { 
        // 메서드(객체 안에 함수) 선언
        // public(접근 제한자) + String(반환 타입) + hello()(메서드명)
        return "Hello from Quarkus REST";
        // 리턴문
        // 메서드가 반환할 값을 지정. 반환타입 = String ->> 문자열 반환ㄴ
    }
}
