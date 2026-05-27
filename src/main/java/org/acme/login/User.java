package org.acme.login;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

// @Entity
// @Table(name = "users") // 예약어 충돌 방지: "user" -> "users"
// public class User extends PanacheEntity {
//     public String username;
//     public String password; 

//     // 사용자명으로 조회하는 정적 메서드
//     public static User findByUsername(String username) {
//         return find("username", username).firstResult();
//     }

@Entity
@Table(name = "users")
    public class User extends PanacheEntity {
        public String username;
        public String password;

        @Column(unique = true)
        public String email;
        public String phone;

        // 신규 추가 : 프로필 사진 파일명
        public String profileImage; // 저장된 파일명

        public static User findByUsername(String username) {
            return find("username", username).firstResult();
        }

        public static User findByEmail(String email) {
            return find("email", email).firstResult();
        }
    }