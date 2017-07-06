package com.gome.iam.common.secret;

/**
 * @author luoji
 * @params
 * @since 2016/11/1 0001
 */

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * AES instance with one key and one iv for preformance.
 * mode: AES/CBC/PKCS5Padding
 * text input encoding: utf-8
 * text output encoding: base64
 */
public class Aes {

    private Aes(byte[] key, byte[] iv) throws Exception {
        this.encCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        this.decCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivc = new IvParameterSpec(iv);
        this.encCipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivc);
        this.decCipher.init(Cipher.DECRYPT_MODE, skeySpec, ivc);
    }

    private Cipher encCipher = null;
    private Cipher decCipher = null;

    public byte[] encBytes(byte[] srcBytes) throws Exception {
        byte[] encrypted = encCipher.doFinal(srcBytes);
        return encrypted;
    }

    public byte[] decBytes(byte[] srcBytes) throws Exception {
        byte[] decrypted = decCipher.doFinal(srcBytes);
        return decrypted;
    }

    public String encBytes(String src) throws Exception {
        byte[] encrypted = encCipher.doFinal(src.getBytes());
        return bytesToHex(encrypted);
    }

    public String decBytes(String src) throws Exception {

        byte[] decrypted = decCipher.doFinal(hexToBytes(src));
        return new String(decrypted);
    }

    public String encText(String srcStr) throws Exception {
        byte[] srcBytes = srcStr.getBytes("utf-8");
        byte[] encrypted = encBytes(srcBytes);
        return Base64.encode(encrypted);
    }

    public String decText(String srcStr) throws Exception {
        byte[] srcBytes = Base64.decode(srcStr);
        byte[] decrypted = decBytes(srcBytes);
        return new String(decrypted, "utf-8");
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

    public static final Aes getInstance(byte[] key, byte[] iv) throws Exception {
        Aes sub = new Aes(key, iv);
        return sub;
    }

    public static final Aes getInstance(String key, String iv) throws Exception {
        Aes sub = new Aes(key.getBytes(), iv.getBytes());
        return sub;
    }
}

