package com.tiger.domain.member;

import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean isValid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String tel;
}
