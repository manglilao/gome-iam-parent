package com.gome.iam.common.gomeplus;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.LoggerFactory;

/**
 * 
 * @author liuhaikun-ds
 */
public class LdapHelper {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(LdapHelper.class);

    private static DirContext dcx;

    public static boolean getCtx(String username,String password,String factory,String path,String base,String domain) {
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, factory);
            env.put(Context.PROVIDER_URL, path);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, domain+username );
            env.put(Context.SECURITY_CREDENTIALS, password);
            // 链接ldap
            dcx = new InitialDirContext(env);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("### GOMEPLUS LDAP验证失败 msg={} ###", e.getMessage());
            return false;
        }
    }  
      
    public static void closeCtx(){  
        try {
            dcx.close();
        } catch (NamingException ex) {  
            //logger.error(Level.SEVERE, null, ex);
            logger.error("ctx关闭出错：{}",ex.getMessage());
        }  
    }
}