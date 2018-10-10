package com.home.test180924.util;


import org.springframework.stereotype.Component;


public interface PasswordEncryptUtil {
    String encrypt(String str);
    default String decrypt(String str){ throw new RuntimeException(); }

}
