package org.acme.champion;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity // @어노테이션, Entity = Table 
public class Champion extends PanacheEntity {
    // extends PanacheEntity = id 컬럼 자동 추가 + perisit(), listAll()등 메서드 제공
    // PanacheEntity, 부모의 기능을 상속
    public String name; // 챔피언 이름
    // 컬럼 자동 생성 (별도 @Column 없이도 동작)
    public String role; // 역할 (전사, 마법사 등)
    public String line; // 라인 (탑, 미드 등)
}