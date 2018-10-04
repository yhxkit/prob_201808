package com.home.test180924.util;


import org.springframework.stereotype.Component;

@Component //이 애노테이션 없어도 잘 돌아는가네..?
public interface PasswordEncryptUtil {
    String encrypt(String str);
    default String decrypt(String str){ throw new RuntimeException(); }

}
