package com.prob.pilot18091701.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
//import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AES256 {

    String iv;
    Key keySpec;
	private String key= "AES256-TEST-KEY!!!"; //16자 이상? 근데 이렇게 하면 그냥 인코딩이랑 무슨 차이지? account 마다 key를 다르게 해준다거나 그래야하나?
	//근데 그러면 이상태로는 빈으로 쓸 수 없을거같은데 생성자를 그냥 두고, 기능을 쓰는 계정마다 각각 다른 키를 줘서 키 스펙을 변경하도록 메서드를 따로 빼야하나?

    static Logger logger = LoggerFactory.getLogger(AES256.class);

    public AES256() {
        try {
            this.iv = key.substring(0, 16);
            byte[] keyBytes = new byte[16];
            byte[] b = key.getBytes("UTF-8");
            int len = b.length;
            if (len > keyBytes.length) {
                len = keyBytes.length;
            }
            System.arraycopy(b, 0, keyBytes, 0, len);
            this.keySpec = new SecretKeySpec(keyBytes, "AES");
        } catch (UnsupportedEncodingException e) {
            logger.error("UTF-8 을 지원하지 않아서 AES256Util객체를 생성하지 못 하였습니다.", e);
        }
    }

    // 암호화
    public String encrypt(String str) {
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
            byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(encrypted));
        } catch (NoSuchAlgorithmException e) {
            logger.error("AES/CBC/PKCS5Padding 해당 알고리즘을 지원하지 않습니다. ('" + str + "'를 암호화하는데 실패했습니다.)", e);
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error("UTF-8을 지원하지 않습니다. ('" + str + "'를 암호화하는데 실패했습니다.)", e);
            return null;
        } catch (GeneralSecurityException e) {
            logger.error("키가 잘못되었습니다. ('" + str + "'를 암호화하는데 실패했습니다.)", e);
            return null;
        }
    }

    // 복호화
    public String decrypt(String str) {
        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
            byte[] byteStr = Base64.decodeBase64(str.getBytes());
            return new String(c.doFinal(byteStr), "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            logger.error("AES/CBC/PKCS5Padding 해당 알고리즘을 지원하지 않습니다. ('" + str + "'를 복호화하는데 실패했습니다.)", e);
            return null;
        } catch (UnsupportedEncodingException e) {
            logger.error("UTF-8을 지원하지 않습니다. ('" + str + "'를 복호화하는데 실패했습니다.)", e);
            return null;
        } catch (GeneralSecurityException e) {
            logger.error("키가 잘못되었습니다. ('" + str + "'를 복호화하는데 실패했습니다.)", e);
            return null;
        }
    }
}
