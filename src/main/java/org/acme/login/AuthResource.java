package org.acme.login;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.io.InputStream;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.ext.web.RoutingContext;
import java.util.Map;


@Path("/") // 기본 경로가 최상위 /
public class AuthResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response mainPage() {
        String loginUser = context.session().get("loginUser");

        System.out.println("=== [GET /] 세션 ID : " + context.session().id());
        System.out.println("=== [GET /] loginUser : " + loginUser);

        String htmlPath = (loginUser !=null)
            ? "META-INF/resources/login/main_after_login.html"
            : "META-INF/resources/main_index.html";
        
        InputStream html =
    getClass().getClassLoader().getResourceAsStream(htmlPath);
        return Response.ok(html).build();
    }

    // GET / login -> 로그인 HTML 페이지 반환
    @GET
    @Path("/login") // 경로 명시
    @Produces(MediaType.TEXT_HTML) // 서버 -> 클라우드

    public Response loginPage() {
        InputStream html = getClass()
            .getClassLoader()
            .getResourceAsStream("META-INF/resources/login/login.html");
        return Response.ok(html).build();
    }
    @Inject
    RoutingContext context; // Quarkus Ver.x 세션 접근

    @POST // 아이디, 패스워드 전송 받음
    @Path("/login_check")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // 클라 -> 서버
    public Response loginCheck(
        @FormParam("username") String username,
        @FormParam("password") String password) {
        
        User user = User.findByUsername(username); // 아이디 조회
        if (user == null || !user.password.equals(password)) { // 존재 확인
            return Response
                .seeOther(URI.create("/login?error=1"))
                .build();
        }
        // 세션에 로그인 정보 저장
        context.session().put("loginUser",username);

        return Response
            .seeOther(URI.create("/after_login"))
            .build();
        }
    @GET
    @Path("/after_login")
    @Produces(MediaType.TEXT_HTML)
    public Response afterLogin() {
        //세션 체크 : 로그인 안 한 사용자 차단
        String loginUser = context.session().get("loginUser");

        // 세션 내용 로그 출력
        System.out.println("=== 세션 ID : " + context.session().id());
        System.out.println("=== loginUser : " + loginUser);
        
        if (loginUser == null) {
            // 세션 없음 -> 로그인 페이지로 강제 이동
            return Response
                .seeOther(URI.create("/login"))
                .build();
        }
        // 세션 있음 -> 로그인 후 HTML 반환
        InputStream html = getClass()
            .getClassLoader()
            .getResourceAsStream("META-INF/resources/login/main_after_login.html");

        return Response.ok(html).build();
    }
    @GET
    @Path("/logout")
    
    public Response logout() {
        // 로그아웃 전 세션 정보 출력
        System.out.println("=== 로그아웃 전 세션 ID : " + context.session().id());
        System.out.println("=== 로그아웃 전 loginUser : " + context.session().get("loginUser"));

        // 세션 전체 삭제
        context.session().destroy();

        // 로그아웃 후 세션 정보 출력
        System.out.println("=== 로그아웃 후 세션 ID : " + context.session().id());
        System.out.println("=== 로그아웃 후 loginUser : " + context.session().get("loginUser"));

        return Response
            .seeOther(URI.create("/"))
            .build();
        }
    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_HTML)
    public Response registerPage() {
        InputStream html = getClass()
            .getClassLoader()
            .getResourceAsStream(
                "META-INF/resources/login/register.html");
        return Response.ok(html).build();
    }
    @POST
    @Path("/register_check")
    @Transactional
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)

    public Response registerCheck(
    @FormParam("username") String username,
    @FormParam("password") String password, // SHA-256 해시값
    @FormParam("email") String email,
    @FormParam("phone") String phone) {
        
    // 1. 아이디 중복 체크
    if (User.findByUsername(username)!= null) {
        return Response
            .seeOther(URI.create("/register?error=duplicate_username"))
            .build();
    }
    // 2. 이메일 중복 체크
    if (User.findByEmail(email) != null) {
        return Response
            .seeOther(URI.create("/register?error=duplicate_email"))
            .build();
    }
    // 3. DB 삽입
    User newUser = new User();
    newUser.username = username;
    newUser.password = password; // 해시값 저장
    newUser.email = email;
    newUser.phone = phone;
    newUser.persist();

    // 4. 가입 완료 페이지로 이동
    return Response
        .seeOther(URI.create("/register_success"))
        .build();
    }

    @GET
    @Path("/register_success")
    @Produces(MediaType.TEXT_HTML)

    public Response registerSuccess() {
        InputStream html = getClass()
            .getClassLoader()
            .getResourceAsStream(
            "META-INF/resources/login/register_success.html");
        return Response.ok(html).build();
    }

    @GET
    @Path("/profile")
    @Produces(MediaType.TEXT_HTML)

    public Response profilePage() {
        // 1. 세션 체크(로그인 안 한 사용자 차단)
        String loginUser = context.session().get("loginUser");
        if (loginUser == null) {
            return Response
                .seeOther(URI.create("/login"))
                .build();
        }
        // 2.DB에서 사용자 정보 조회
        User user = User.findByUsername(loginUser);

        // 3. 세션에 사용자 정보 저장(HTML에서 활용)
        context.session().put("userEmail",user.email);
        context.session().put("userPhone", user.phone);
        context.session().put("profileImage",
            user.profileImage != null ? user.profileImage : "default.png");
            
        // 4. 프로필 페이지 반환
        InputStream html = getClass()
            .getClassLoader()
            .getResourceAsStream(
                "META-INF/resources/login/profile.html");
        return Response.ok(html).build();
        }
    
        @GET
        @Path("/profile/info")
        @Produces(MediaType.APPLICATION_JSON)

        public Response profileInfo() {
            // 세션 체크
            String loginUser = context.session().get("loginUser");
            if (loginUser == null) {
                return Response.status(401).build();
            }

            //DB조회
            User user = User.findByUsername(loginUser);

            //Json 응답
            return Response.ok(
                Map.of(
                    "username", user.username,
                    "email", user.email != null ? user.email : "",
                    "phone", user.phone != null ? user.phone : "",
                    "profileImage", user.profileImage != null ? user.profileImage : ""
                )
            ).build();
        }
}