package com.gome.iam.service.secret;

/**
 * @author yintongjiang
 * @params
 * @since 2016/11/1
 */
public interface SecretService {
    String encodeText(String text);

    String decodeText(String text);
}
