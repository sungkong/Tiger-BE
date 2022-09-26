package com.tiger.config;

import com.tiger.utils.CheckUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityAuditorAware implements AuditorAware<String> {
    private final CheckUtil checkUtil;

    @Override
    public Optional<String> getCurrentAuditor() {

        return Optional.ofNullable(checkUtil.validateMember().getName());

    }

}
