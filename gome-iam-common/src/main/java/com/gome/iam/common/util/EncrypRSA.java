package com.gome.iam.common.util;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class EncrypRSA {
    public static final String KEY_ALGORITHM = "RSA";


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }


    /**
     * BASE64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * @throws NoSuchAlgorithmException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static void printHexString(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }

    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException,
            NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, Exception {
        EncrypRSA rsa = new EncrypRSA();
        String msg = "公钥和私钥";
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey oraprivateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey orapublicKey = (RSAPublicKey) keyPair.getPublic();

        byte[] publicKeybyte = orapublicKey.getEncoded();
        String publicKeyString = encryptBASE64(publicKeybyte);
        printHexString(publicKeybyte);
        System.out.println(publicKeyString);

        byte[] privateKeybyte = oraprivateKey.getEncoded();
        String privateKeyString = encryptBASE64(privateKeybyte);
        System.out.println(privateKeyString);

        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PKCS8EncodedKeySpec privatekcs8KeySpec = new PKCS8EncodedKeySpec(decryptBASE64(privateKeyString));
        PrivateKey privateKey = keyFactory.generatePrivate(privatekcs8KeySpec);

        X509EncodedKeySpec publicpkcs8KeySpec = new X509EncodedKeySpec(decryptBASE64(publicKeyString));
        PublicKey publicKey = keyFactory.generatePublic(publicpkcs8KeySpec);

        // 用公钥加密
        byte[] srcBytes = msg.getBytes();
        long start1 = System.currentTimeMillis();
        byte[] resultBytes = rsa.encrypt((RSAPublicKey) publicKey, srcBytes);
        long end1 = System.currentTimeMillis();
        String base64Msg = encryptBASE64(resultBytes);
        System.out.println(end1 - start1);
        byte[] base64MsgD = decryptBASE64(base64Msg);

        // 用私钥解密
        long start = System.currentTimeMillis();
        byte[] decBytes = rsa.decrypt((RSAPrivateKey) privateKey, base64MsgD);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        System.out.println("明文是:" + msg);
        System.out.println("双重加密后是:" + base64Msg);
        System.out.println("解密后是:" + new String(decBytes));
    }

    /**
     * 加密
     *
     * @param publicKey
     * @param srcBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    protected byte[] encrypt(RSAPublicKey publicKey, byte[] srcBytes) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (publicKey != null) {
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] resultBytes = cipher.doFinal(srcBytes);
            return resultBytes;
        }
        return null;
    }

    /**
     * 解密
     *
     * @param privateKey
     * @param srcBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    protected byte[] decrypt(RSAPrivateKey privateKey, byte[] srcBytes) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        if (privateKey != null) {
            // Cipher负责完成加密或解密工作，基于RSA
            Cipher cipher = Cipher.getInstance("RSA");
            // 根据公钥，对Cipher对象进行初始化
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] resultBytes = cipher.doFinal(srcBytes);
            return resultBytes;
        }
        return null;
    }
}
