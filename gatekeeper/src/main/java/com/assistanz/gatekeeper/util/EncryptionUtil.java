package com.assistanz.gatekeeper.util;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.context.MessageSource;

import com.assistanz.gatekeeper.constants.GenericConstants;
import com.assistanz.gatekeeper.util.error.Errors;
import com.assistanz.gatekeeper.util.error.exception.ApplicationException;


/**
 * Encrypt and Decrypt the string using the secret key.
 */
public class EncryptionUtil {

    /**
     * Encrypted using cipher algorithm.
     */
    private static Cipher cipher;

    /**
     * To encrypt text using 'AES' algorithm.
     *
     * @param encryptText encryption text
     * @param secretKey secret key value
     * @param messageSource message source
     * @return encrypted text
     * @throws Exception raise if error
     */
    public static synchronized String encrypt(String encryptText, SecretKey secretKey,
        MessageSource messageSource) throws Exception {
        try {
            cipher = Cipher.getInstance(GenericConstants.ENCRYPT_ALGORITHM);
            byte[] plainTextByte = encryptText.getBytes();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            String encryptedText = encoder.encodeToString(encryptedByte);
            return encryptedText;
        } catch (Exception e) {
            Errors errors = new Errors(messageSource);
            errors.addGlobalError("invalid.token.request");
            throw new ApplicationException(errors);
        }
    }

    /**
     * To decrypt text using 'AES' algorithm.
     *
     * @param decryptText decryption text
     * @param secretKey secret key value
     * @param messageSource message source
     * @return encrypted text
     * @throws Exception raise if error
     */
    public static synchronized String decrypt(String decryptText, SecretKey secretKey,
        MessageSource messageSource) throws Exception {
        try {
            decryptText = decryptText.replaceAll("\\s", "+");
            cipher = Cipher.getInstance(GenericConstants.ENCRYPT_ALGORITHM);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] encryptedTextByte = decoder.decode(decryptText);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
            String decryptedText = new String(decryptedByte);
            return decryptedText;
        } catch (Exception e) {
            Errors errors = new Errors(messageSource);
            errors.addGlobalError("invalid.token.request");
            throw new ApplicationException(errors);
        }
    }

    /**
     * Convert string to encrypted text.
     *
     * @param encryptText encryption text
     * @param secretKey secret key
     * @param messageSource message source
     * @return encrypted text
     * @throws Exception raise if error
     */
    public static synchronized String convertStringToEncrypt(String encryptText, String secretKey,
        MessageSource messageSource) throws Exception {
        String strEncoded = Base64.getEncoder().encodeToString(secretKey.getBytes(GenericConstants.CHARACTER_ENCODING));
        byte[] decodedKey = Base64.getDecoder().decode(strEncoded);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, GenericConstants.ENCRYPT_ALGORITHM);
        String encryptedText = new String(EncryptionUtil.encrypt(encryptText, originalKey, messageSource));
        return encryptedText;
    }

    /**
     * Convert encrypted text to string.
     *
     * @param decryptText decryption text
     * @param secretKey secret key
     * @param messageSource message source
     * @return decrypted text
     * @throws Exception raise if error
     */
    public static synchronized String convertEncryptToString(String decryptText, String secretKey,
        MessageSource messageSource) throws Exception {
        String strEncoded = Base64.getEncoder().encodeToString(secretKey.getBytes(GenericConstants.CHARACTER_ENCODING));
        byte[] decodedKey = Base64.getDecoder().decode(strEncoded);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, GenericConstants.ENCRYPT_ALGORITHM);
        String decryptedText = new String(EncryptionUtil.decrypt(decryptText, originalKey, messageSource));
        return decryptedText;
    }


    public static String whmcsEncrypt(final String plaintext,  String secretKey) throws GeneralSecurityException {
        SecretKeySpec sks = new SecretKeySpec(hexStringToByteArray(secretKey), GenericConstants.ENCRYPT_ALGORITHM);
        Cipher cipher = Cipher.getInstance(GenericConstants.ENCRYPT_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
        return byteArrayToHexString(encrypted);
    }

    public static String whmcsDecrypt(final String plaintext,  String secretKey) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeySpec sks = new SecretKeySpec(hexStringToByteArray(secretKey), GenericConstants.ENCRYPT_ALGORITHM);
        Cipher cipher = Cipher.getInstance(GenericConstants.ENCRYPT_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, sks, cipher.getParameters());
        byte[] encrypted = cipher.doFinal(hexStringToByteArray(plaintext));
        return new String(encrypted);
    }

    public static byte[] hexStringToByteArray(String s) {
        byte[] b = new byte[s.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String byteArrayToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            int v = b[i] & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * To encrypt text using 'AES' algorithm.
     *
     * @param plainText encryption text
     * @param secretKey secret key value
     * @return encrypted text
     * @throws Exception raise if error
     */
    public static synchronized String encrypt(String plainText, SecretKey secretKey) throws Exception {
        cipher = Cipher.getInstance("AES");
        byte[] plainTextByte = plainText.getBytes();
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
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
    public static synchronized String decrypt(String plainText, SecretKey secretKey) throws Exception {
        cipher = Cipher.getInstance("AES");
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] encryptedTextByte = decoder.decode(plainText);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
        String decryptedText = new String(decryptedByte);
        return decryptedText;
    }
}
