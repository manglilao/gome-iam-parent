package com.gome.iam.domain.token;

import java.io.Serializable;

/**
 * @author luoji
 * @params
 * @since 2016/10/24 0024
 */
public class TokenEntity implements Serializable {

    private static final long serialVersionUID = -3114855600527277794L;

    private String token;

    private TokenValue tokenValue = new TokenValue();

    public TokenEntity() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TokenValue getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(TokenValue tokenValue) {
        this.tokenValue = tokenValue;
    }

    @Override
    public String toString() {
        return "TokenEntity{" +
                "token='" + token + '\'' +
                ", tokenValue=" + tokenValue.toString() +
                '}';
    }
}
