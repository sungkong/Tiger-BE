package com.tiger;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;
import java.util.logging.Logger;


class TigerApplicationTests {

    @Test
    void contextLoads() {
        LocalDate now = LocalDate.now();
        System.out.println(now.compareTo(LocalDate.parse("2022-09-01")));
    }

}
