package com.gome.iam.web.controller.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.LdapName;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;  
  
/**
 * 
 * @author liuhaikun-ds
 *	ldap.url=ldap://auth.ldap.api:389
	ldap.factory.path=com.sun.jndi.ldap.LdapCtxFactory
	ldap.base=DC=ds,DC=gome,DC=com,DC=cn
	ldap.domain=DS\\
	ldap.userName=xbcsso
	ldap.password=69.#81ge++119+y#
 */
public class LdapHelper {
    private static DirContext ctx;  
    @SuppressWarnings(value = "unchecked")  
    public static DirContext getCtx() {  
        String account = "xbcsso"; //binddn   
        String password = "69.#81ge++119+y#";    //bindpwd  
        String root = "DC=ds,DC=gome,DC=com,DC=cn"; // root  
        String domain="DS\\";
        Hashtable env = new Hashtable();  
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");  
        env.put(Context.PROVIDER_URL, "ldap://auth.ldap.api:389/" + root);   //ldap://10.128.15.208:389 auth.ldap.api
        env.put(Context.SECURITY_AUTHENTICATION, "simple");  
        env.put(Context.SECURITY_PRINCIPAL, domain+account );  
        env.put(Context.SECURITY_CREDENTIALS, password);  
        try {  
            // 链接ldap  
            ctx = new InitialDirContext(env);  
            System.out.println("认证成功");  
        } catch (javax.naming.AuthenticationException e) {  
            System.out.println("认证失败");  
        } catch (Exception e) {  
            System.out.println("认证出错：");  
            e.printStackTrace();  
        }  
        return ctx;  
    }  
      
    public static void closeCtx(){  
        try {  
            ctx.close();  
        } catch (NamingException ex) {  
            Logger.getLogger(LdapHelper.class.getName()).log(Level.SEVERE, null, ex);  
        }  
    }  
      
    public static boolean verifySHA(String ldappw, String inputpw)  
            throws NoSuchAlgorithmException {  
        MessageDigest md = MessageDigest.getInstance("SHA-1");  
        // 取出加密字符  
        if (ldappw.startsWith("{SSHA}")) {  
            ldappw = ldappw.substring(6);  
        } else if (ldappw.startsWith("{SHA}")) {  
            ldappw = ldappw.substring(5);  
        }  
        // 解码BASE64  
        byte[] ldappwbyte = Base64.decode(ldappw);  
        byte[] shacode;  
        byte[] salt;  
        // 前20位是SHA-1加密段，20位后是最初加密时的随机明文  
        if (ldappwbyte.length <= 20) {  
            shacode = ldappwbyte;  
            salt = new byte[0];  
        } else {  
            shacode = new byte[20];  
            salt = new byte[ldappwbyte.length - 20];  
            System.arraycopy(ldappwbyte, 0, shacode, 0, 20);  
            System.arraycopy(ldappwbyte, 20, salt, 0, salt.length);  
        }  
  
        // 把用户输入的密码添加到摘要计算信息  
        md.update(inputpw.getBytes());  
        // 把随机明文添加到摘要计算信息  
        md.update(salt);  
  
        // 按SSHA把当前用户密码进行计算  
        byte[] inputpwbyte = md.digest();  
  
        // 返回校验结果  
        return MessageDigest.isEqual(shacode, inputpwbyte);  
    }  
  
    public static void main(String[] args){  
    	authenticate("liuhaikun-ds", "1q2w3e4r##");
    }  
    public static boolean authenticate(String usr, String pwd) {
        String ldap = "DC=ds,DC=gome,DC=com,DC=cn";
        boolean success = false;
        DirContext ctx = null;
        try {  
            ctx = LdapHelper.getCtx();
            SearchControls constraints = new SearchControls();  
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            String searchFilter = "DS\\" + usr;
            String returnedAtts[] = {"name", "distinguishedname", "displayName","sAMAccountName"};
            constraints.setReturningAttributes(returnedAtts);

            NamingEnumeration en = ctx.search(ldap, searchFilter, constraints); // 查询所有用户
            while (en != null && en.hasMoreElements()) {  
                Object obj = en.nextElement();  
                if (obj instanceof SearchResult) {  
                    SearchResult si = (SearchResult) obj;  
                    System.out.println("name:   " + si.getName());  
                    Attributes attrs = si.getAttributes();  
                    if (attrs == null) {  
                        System.out.println("No attributes");  
                    } else {  
                        Attribute attr = attrs.get("userPassword");  
                        Object o = attr.get();  
                        byte[] s = (byte[]) o;  
                        String pwd2 = new String(s);  
                        success = LdapHelper.verifySHA(pwd2, pwd);  
                        return success;  
                    }  
                } else {  
                    System.out.println(obj);  
                }  
                System.out.println();  
            }  
            ctx.close();  
        } catch (NoSuchAlgorithmException ex) {  
            try {  
                if (ctx != null) {  
                    ctx.close();  
                }  
            } catch (NamingException namingException) {  
                namingException.printStackTrace();  
            }  
        } catch (NamingException ex) {  
            try {  
                if (ctx != null) {  
                    ctx.close();  
                }  
            } catch (NamingException namingException) {  
                namingException.printStackTrace();  
            }  
            Logger.getLogger(LdapHelper.class.getName()).log(Level.SEVERE, null, ex);  
        }  
        return false;  
    }  
}