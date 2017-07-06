package com.gome.iam.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by qiaowentao on 2017/6/18.
 */
public class HttpClientUtil {

    private final static Log log = LogFactory.getLog(HttpClientUtil.class);
    private static final String timeout = "60000";
    private static final int MIN_ERROR_REQUEST_SPEND_TIME = 200;

    /**
     * 发送多参数的post请求
     *
     * @param url
     * @param params
     * @return
     * @throws IOException
     */
    public static String sendPost(String url,Map<String,Object> params) throws IOException {
        String body = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost method = new HttpPost(url);
        method.addHeader("Content-type","application/json;charset=utf-8");
        method.setHeader("Accept","application/json");
        method.setEntity(new StringEntity(JSON.toJSONString(params),"utf-8"));
        CloseableHttpResponse response = httpClient.execute(method);
            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    //按指定编码转换结果实体为String类型
                    body = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);
                }
            }
        response.close();
        return body;
    }

    public static void main(String[] args) {
        String url = "http://10.112.1.4:8083/v1/sms/?method=falcon_send_sms";
        //String url = "http://10.112.1.4:8083/v1/sms/?method=falcon_send_ses";
        Map<String,Object> map = new HashMap<>();
        map.put("tos","18636967836");
        //map.put("tos","qiaowentao@gomeplus.com");
        map.put("content","您此次的操作的验证码为"+"98745699");
        //map.put("subject","密码找回");
        try {
            //String str = sendPost(url,map);
            String str = HttpRequestUtil.doPost(url,map);
            System.out.println(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
