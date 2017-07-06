package com.gome.iam.service.impl.secret;

import com.gome.iam.common.secret.Blowfish;
import com.gome.iam.service.secret.SecretService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author luoji
 * @params
 * @since 2016/11/1 0001
 */
@Service("blowfishSecretServiceImpl")
public class BlowfishSecretServiceImpl implements SecretService {

    @Value("#{config['blowfish.SecretKey']}")
    private String secretKey;

    @Override
    public String encodeText(String text) {
        try {
            Blowfish blowfish = Blowfish.Singleton(secretKey);
            return blowfish.encrypt(text);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String decodeText(String text) {
        try {
            Blowfish blowfish = Blowfish.Singleton(secretKey);
            return blowfish.decrypt(text);
        } catch (Exception e) {
            return null;
        }
    }
}
