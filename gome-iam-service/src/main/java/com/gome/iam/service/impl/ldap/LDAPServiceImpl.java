package com.gome.iam.service.impl.ldap;

import java.util.Hashtable;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

import com.gome.iam.common.gomeplus.LdapHelper;
import com.gome.iam.common.gomeplus.WebServiceGetOAUserInfo;
import com.gome.iam.common.util.MyBeanUtils;
import com.gome.iam.domain.user.OAUserInfo;
import com.gome.iam.service.user.ISSOUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gome.iam.domain.user.SSOUser;
import com.gome.iam.service.ldap.LDAPService;
import org.springframework.stereotype.Service;

@Service
public class LDAPServiceImpl implements LDAPService {

    private final static Logger logger = LoggerFactory.getLogger(LDAPServiceImpl.class);
    static DirContext dc = null;
    @Value("#{ldapConfig['ldap.url']}")
    private String ldapUrl;
    @Value("#{ldapConfig['ldap.factory.path']}")
    private String ldapFactoryPath;
    @Value("#{ldapConfig['ldap.base']}")
    private String ldapBase;
    @Value("#{ldapConfig['ldap.domain']}")
    private String ldapDomain;
    @Value("#{ldapConfig['ldap.userName']}")
    private String userName;
    @Value("#{ldapConfig['ldap.password']}")
    private String password;
    @Value("#{ldapConfig['ldap.effect']}")
    private String effect;

    @Value("#{ldapConfig['ldapGomePlus.url']}")
    private String ldapGomePlusUrl;
    @Value("#{ldapConfig['ldapGomePlus.factory.path']}")
    private String ldapGomePlusFactoryPath;
    @Value("#{ldapConfig['ldapGomePlus.base']}")
    private String ldapGomePlusBase;
    @Value("#{ldapConfig['ldapGomePlus.domain']}")
    private String ldapGomePlusDomain;
    @Value("#{ldapConfig['ldapGomePlus.userName']}")
    private String userNameGomePlus;
    @Value("#{ldapConfig['ldapGomePlus.password']}")
    private String passwordGomePlus;
    @Autowired
    private ISSOUserService ssoUserService;

    @Override
    public SSOUser verifyUser(String userName, String password) {
        logger.info("### verifyUser param={} ###", userName, password);
        verify: try {
            if (!isEffect()) {
                break verify;
            }
            boolean check = this.checkLDAPUser(userName, password);
            if (!check) {
                break verify;
            }
            SSOUser ssoUser = ssoUserService.findByUserName(userName);
            if (ssoUser != null) {
                SSOUser s = new SSOUser();
                s.setLastLoginDate(ssoUser.getThisLoginDate());
                MyBeanUtils.copyBeanNotNull2Bean(s, ssoUser);
                ssoUserService.update(ssoUser);
                return ssoUser;
            } else {
                ssoUser = this.findLdapUserInfo(userName);
                if (ssoUser != null) {
                    ssoUser.setUserName(userName);
                    //                ssoUser.setPassword(password);
                } else {
                    return null;
                }
                if (ssoUser != null) {
                    ssoUserService.save(ssoUser);
                }
                return ssoUser;
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("### Bean Transformation Exception ###");
            logger.error(e.getMessage(), e);
        } finally {
            this.close();
        }
        return null;
    }


    @Override
    public boolean checkLDAPUser(String userName, String password) {
        try {
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, ldapFactoryPath);
            env.put(Context.PROVIDER_URL, ldapUrl);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL, ldapDomain + userName);
            env.put(Context.SECURITY_CREDENTIALS, password);
            dc = new InitialDirContext(env);    // 验证信息
            return true;
        } catch (Exception e) {
            logger.error("### LDAP验证失败 msg={} ###", e.getMessage());
            return false;
        }
    }

    /**
     * 关闭Ldap连接
     */
    public void close() {
        if (dc != null) {
            try {
                dc.close();
            } catch (NamingException e) {
                logger.info("NamingException in close():" + e);
            }
        }
    }


    /**
     * 按用户名查询
     *
     * @throws NamingException
     */
    public SSOUser findLdapUserInfo(String userName) {
        logger.info("### LDAP验证用户名 userName={} ###", userName);
        this.checkLDAPUser(this.userName, password);
        SearchControls searchCtls = new SearchControls();
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        String searchFilter = "sAMAccountName=" + userName;
        String returnedAtts[] = {"name", "distinguishedname", "displayName","sAMAccountName"};
        searchCtls.setReturningAttributes(returnedAtts);
        try {
            NamingEnumeration answer = dc.search(ldapBase, searchFilter, searchCtls);
            if (answer == null || answer.equals(null)) {
                System.out.println("answer is null");
            } else {
                while (answer.hasMoreElements()) {
                    SearchResult sr = (SearchResult) answer.next();
                    Attributes attributes = sr.getAttributes();
                    if (attributes != null) {
                        try {
                            SSOUser user = new SSOUser();
                            user.setOrganization(attributes.get("distinguishedname").get().toString());
                            user.setBindDn(attributes.get("distinguishedname").get().toString());
                            user.setUserTitle(attributes.get("displayName").get().toString());
                            user.setUserName(attributes.get("sAMAccountName").get().toString());
                            user.setBase(ldapBase);
                            return user;
                        } catch (NamingException e) {
                            logger.error("Throw Exception : " + e);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Throw Exception : " + e);
        } finally {
            this.close();
        }
        return null;
    }

    @Override
    public boolean isEffect() {
        logger.debug("ldap.effect is " + this.effect);
        return Boolean.parseBoolean(this.effect);
    }

    @Override
    public SSOUser verifyGomePlusUser(String userName, String password) {
        logger.info("### verifyGomePlusUser param={} ###", userName, password);
        verify: try {
            if (!isEffect()) {
                break verify;
            }
           boolean check = LdapHelper.getCtx(userName,password,ldapGomePlusFactoryPath,ldapGomePlusUrl,ldapGomePlusBase,ldapGomePlusDomain);
            if (!check) {
                break verify;
            }
            SSOUser ssoUser = ssoUserService.findByUserName(userName);
            if (ssoUser != null) {
                SSOUser s = new SSOUser();
                s.setLastLoginDate(ssoUser.getThisLoginDate());
                MyBeanUtils.copyBeanNotNull2Bean(s, ssoUser);
                ssoUserService.update(ssoUser);
                return ssoUser;
            } else {
                ssoUser = new SSOUser();
                String[] userNameArrs = {userName};
                List<OAUserInfo> oaUserInfos = WebServiceGetOAUserInfo.GetStaffList(userNameArrs);
                if (oaUserInfos != null && oaUserInfos.size()>0) {
                    ssoUser.setUserName(userName);
                    ssoUser.setOrganization(oaUserInfos.get(0).getDeptName());
                    ssoUser.setUserTitle(userName+"("+oaUserInfos.get(0).getUserName()+"."+oaUserInfos.get(0).getDeptName()+")");
                    ssoUser.setBase(ldapGomePlusBase);
                    //                ssoUser.setPassword(password);
                } else {
                    return null;
                }
                if (ssoUser != null) {
                    ssoUserService.save(ssoUser);
                }
                return ssoUser;
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("### Bean Transformation Exception ###");
            logger.error(e.getMessage(), e);
        } finally {
            this.close();
        }
        return null;
    }
}
