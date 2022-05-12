package org.webworks.datatool.Utility;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by JOHNBOSCO on 1/6/2018.
 * This class handles encryption and decryption of client personal data
 */

public abstract class Encryption {


    public static SecretKey generateKey(){
        final String encodeKey = "Pa55w0rd";
        byte[] KEY = encodeKey.getBytes();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            KEY = digest.digest(KEY);
            KEY = Arrays.copyOf(KEY, 16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(KEY, "AES");
    }

    public static byte[] encryptMsg(String message, SecretKey secret) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] cipherText = new byte[0];
            cipherText = cipher.doFinal(message.getBytes("UTF-8"));
            return cipherText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static String decryptMsg(byte[] cipherText, SecretKey secret) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            String decryptedString = new String(cipher.doFinal(cipherText), "UTF-8");
            return  decryptedString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return "";
    }
}
