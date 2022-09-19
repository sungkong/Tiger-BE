package com.tiger.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenerateRandomUtil {

    public String tel() {
        String a = "-" + ((int) ((Math.random() * (9999 - 1000 + 1)) + 1000));
        String b = "-" + ((int) ((Math.random() * (9999 - 1000 + 1)) + 1000));
        return "050" + a + b;
    }




}
