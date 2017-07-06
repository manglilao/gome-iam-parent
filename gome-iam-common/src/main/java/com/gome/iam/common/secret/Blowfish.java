package com.gome.iam.common.secret;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author luoji
 * @params
 * @since 2016/11/1 0001
 */
public class Blowfish {
    /**
     * This is the algorithm this service uses.
     */
    private static final String ALGORITHM = Blowfish.class.getSimpleName();

    /**
     * The SecretKey instance, this is used to encrypting and decrypting the
     * encrypt data.
     */
    private SecretKey secretkey;

    public Blowfish(String key) {
        secretkey = new SecretKeySpec(key.getBytes(), ALGORITHM);
    }

    /**
     * Encrypts the text passed in the parameters with blowfish.
     *
     * @param text The text to encrypt
     * @return The encrypted text
     * @throws Exception If there was an error encrypting the text using blowfish.
     */
    public String encrypt(String text) throws Exception {
        StringBuilder encryptedText = new StringBuilder();

        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretkey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        encryptedText.append(bytesToHex(encrypted));
        return encryptedText.toString();
    }

    /**
     * Decrypts the string passed in the parameters.
     *
     * @param s The String to decrypt.
     * @return The decrypted String.
     * @throws Exception If the String could not be decrypted.
     */
    public String decrypt(String s) throws Exception {
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretkey);
        return new String(cipher.doFinal(hexToBytes(s)));
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

    public static final Blowfish getInstance(String key) throws Exception {
        Blowfish sub = new Blowfish(key);
        return sub;
    }

    private static Blowfish singlet = null;

    public static final Blowfish Singleton(String key) throws Exception {
        if (singlet == null) {
            singlet = new Blowfish(key);
        }
        return singlet;
    }
}