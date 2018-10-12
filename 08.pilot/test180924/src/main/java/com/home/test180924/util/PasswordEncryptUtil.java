package com.home.test180924.util;


public interface PasswordEncryptUtil {
    String encrypt(String str);
    default String decrypt(String str){ throw new RuntimeException(); }

}
