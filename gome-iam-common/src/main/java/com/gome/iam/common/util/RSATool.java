package com.gome.iam.common.util;

import java.io.*;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;


public class RSATool {

    public static void makekeyfile(String pubkeyfile, String privatekeyfile)
            throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();

        // 生成私钥
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(privatekeyfile));
        oos.writeObject(privateKey);
        oos.flush();
        oos.close();

        oos = new ObjectOutputStream(new FileOutputStream(pubkeyfile));
        oos.writeObject(publicKey);
        oos.flush();
        oos.close();

        System.out.println("make file ok!");
    }


    /**
     * @param k
     * @param data
     * @param encrypt
     *            1 加密 0解密
     * @return
     * @throws NoSuchPaddingException
     * @throws Exception
     */
    public static byte[] handleData(Key k, byte[] data, int encrypt) throws Exception {

        if (k != null) {

            Cipher cipher = Cipher.getInstance("RSA");

            if (encrypt == 1) {
                cipher.init(Cipher.ENCRYPT_MODE, k);
                byte[] resultBytes = cipher.doFinal(data);
                return resultBytes;
            }
            else if (encrypt == 0) {
                cipher.init(Cipher.DECRYPT_MODE, k);
                byte[] resultBytes = cipher.doFinal(data);
                return resultBytes;
            }
            else {
                System.out.println("参数必须为: 1 加密 0解密");
            }
        }
        return null;
    }


    public static void main(String[] args) throws Exception {
        // makekeyfile("pub.key","pri.key");

        String pubfile = "F:\\workarea\\javaprj\\gome-inc-sso/gome_pub.key";
        String prifile = "F:\\workarea\\javaprj\\gome-inc-sso/gome.key";

        // makekeyfile(pubfile, prifile);

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(pubfile));
        RSAPublicKey pubkey = (RSAPublicKey) ois.readObject();
        ois.close();

        ois = new ObjectInputStream(new FileInputStream(prifile));
        RSAPrivateKey prikey = (RSAPrivateKey) ois.readObject();
        ois.close();

        // 使用公钥加密
        String msg = "~O(∩_∩)O哈哈~";
        String enc = "UTF-8";

        // 使用公钥加密私钥解密
        System.out.println("原文: " + msg);
        long start1 = System.currentTimeMillis();
        byte[] result = handleData(pubkey, msg.getBytes(enc), 1);
        long end1 = System.currentTimeMillis();
        System.out.println("加密: " + new String(result, enc) + " 时间---" + (end1 - start1));
        long start = System.currentTimeMillis();
        byte[] deresult = handleData(prikey, result, 0);
        long end = System.currentTimeMillis();
        System.out.println("解密: " + new String(deresult, enc) + " 时间---" + (end - start));

        msg = "嚯嚯";
        // 使用私钥加密公钥解密
        System.out.println("原文: " + msg);
        byte[] result2 = handleData(prikey, msg.getBytes(enc), 1);
        System.out.println("加密: " + new String(result2, enc));
        byte[] deresult2 = handleData(pubkey, result2, 0);
        System.out.println("解密: " + new String(deresult2, enc));

    }
}