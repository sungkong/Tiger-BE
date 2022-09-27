package com.tiger;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class TigerApplicationTests {

    @Test
    void contextLoads() {
        LocalDate now = LocalDate.now();
        System.out.println(now.compareTo(LocalDate.parse("2022-09-01")));
    }

}
