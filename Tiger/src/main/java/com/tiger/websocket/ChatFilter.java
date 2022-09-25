package com.tiger.websocket;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class ChatFilter {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filter;
}
