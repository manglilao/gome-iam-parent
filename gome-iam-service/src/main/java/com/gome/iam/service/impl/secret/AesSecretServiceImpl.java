package com.gome.iam.service.impl.secret;

import com.gome.iam.common.secret.Aes;
import com.gome.iam.service.secret.SecretService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/1
 */
@Service
public class AesSecretServiceImpl implements SecretService {
    @Value("#{config['ase.SecretKey']}")
    private String secretKey;
    @Value("#{config['ase.SecretValue']}")
    private String secretValue;

    @Override
    public String encodeText(String text) {
        try {
            Aes aes = Aes.getInstance(secretKey, secretValue);
            return aes.encBytes(text);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String decodeText(String text) {
        try {
            Aes aes = Aes.getInstance(secretKey, secretValue);
            return aes.decBytes(text);
        } catch (Exception e) {
            return null;
        }
    }
}
