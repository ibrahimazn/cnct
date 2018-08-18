package com.appfiss.account.security;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import com.appfiss.account.constants.GenericConstants;


/**
 * Encrypt and Decrypt the string using the secret key.
 */
public class EncryptionUtil {

    /**
     * Encrypted text.
     */
    private static Cipher cipher;

    /**
     * To encrypt text using 'AES' algorithm.
     *
     * @param plainText encryption text
     * @param secretKey secret key value
     * @return encrypted text
     * @throws Exception raise if error
     */
    public static synchronized String encrypt(String plainText, String secretKey) throws Exception {
    	String strEncoded = Base64.getEncoder()
                .encodeToString(secretKey.getBytes(GenericConstants.CHARACTER_ENCODING));
        byte[] decodedKey = Base64.getDecoder().decode(strEncoded);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length,
                GenericConstants.ENCRYPT_ALGORITHM);
        cipher = Cipher.getInstance("AES");
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] encryptedByte = cipher.doFinal(plainTextByte);
        Base64.Encoder encoder = Base64.getEncoder();
        String encryptedText = encoder.encodeToString(encryptedByte);
        return encryptedText;
    }

    /**
     * To decrypt text using 'AES' algorithm.
     *
     * @param plainText encryption text
     * @param secretKey secret key value
     * @return encrypted text
     * @throws Exception raise if error
     */
    public static synchronized String decrypt(String plainText, String secretKey) throws Exception {
    	String strEncoded = Base64.getEncoder()
                .encodeToString(secretKey.getBytes(GenericConstants.CHARACTER_ENCODING));
        byte[] decodedKey = Base64.getDecoder().decode(strEncoded);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length,
                GenericConstants.ENCRYPT_ALGORITHM);
        cipher = Cipher.getInstance("AES");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(plainText);
        cipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}
