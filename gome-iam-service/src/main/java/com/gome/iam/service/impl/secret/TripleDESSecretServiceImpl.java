package com.gome.iam.service.impl.secret;

import com.gome.iam.common.secret.TripleDES;
import com.gome.iam.service.secret.SecretService;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author luoji
 * @params
 * @since 2016/11/1 0001
 */
public class TripleDESSecretServiceImpl implements SecretService {

    @Value("#{config['tripleDES.SecretKey']}")
    private String secretKey;

    @Override
    public String encodeText(String text) {
        try {
            TripleDES tripleDES = TripleDES.Singleton(secretKey);
            return tripleDES.encryptHex(text);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String decodeText(String text) {
        try {
            TripleDES tripleDES = TripleDES.Singleton(secretKey);
            return tripleDES.decryptHex(text);
        } catch (Exception e) {
            return null;
        }
    }
}
