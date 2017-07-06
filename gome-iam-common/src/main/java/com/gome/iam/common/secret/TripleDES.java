package com.gome.iam.common.secret;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 * @author luoji
 * @params
 * @since 2016/11/1 0001
 */
public class TripleDES {

    private String secretKey;

    public TripleDES() {

    }

    public TripleDES(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public TripleDES setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public static TripleDES getSinglet() {
        return singlet;
    }

    public static void setSinglet(TripleDES singlet) {
        TripleDES.singlet = singlet;
    }

    public byte[] encrypt(String message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8; ) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        final byte[] plainTextBytes = message.getBytes("utf-8");
        final byte[] cipherText = cipher.doFinal(plainTextBytes);

        return cipherText;
    }

    public String decrypt(byte[] message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8; ) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        final byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }

    public String encryptHex(String message) throws Exception {
        return bytesToHex(encrypt(message));
    }

    public String decryptHex(String message) throws Exception {
        return decrypt(hexToBytes(message));
    }

    /**
     * Converts a String hex to a byte array.
     *
     * @param s The hex to convert.
     * @return The converted hex as a byte array.
     */
    private static byte[] hexToBytes(String s) {
        int length = s.length() / 2;
        byte[] buffer = new byte[length];
        for (int i = 0; i < length; i++) {
            buffer[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
        }
        return buffer;
    }

    /**
     * Converts a byte array to a hex as a String.
     *
     * @param b The byte[] to convert.
     * @return The converted byte[] as a String hex.
     */
    private static String bytesToHex(byte[] b) {
        final int length = b.length;
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if ((b[i] & 0xFF) < 16) {
                sb.append("0" + Integer.toHexString(b[i] & 0xff));
            } else {
                sb.append(Integer.toHexString(b[i] & 0xff));
            }
        }
        return sb.toString().toUpperCase();
    }

    private static TripleDES singlet = null;

    public static final TripleDES Singleton() throws Exception {
        if (singlet == null) {
            singlet = new TripleDES();
        }
        return singlet;
    }

    public static final TripleDES Singleton(String key) throws Exception {
        if (singlet == null) {
            singlet = new TripleDES(key);
        }
        return singlet;
    }
}
