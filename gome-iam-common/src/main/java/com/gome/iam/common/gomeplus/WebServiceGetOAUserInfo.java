package com.gome.iam.common.gomeplus;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import com.gome.iam.domain.user.OAUserInfo;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author liuhaikun-ds
 */
public class WebServiceGetOAUserInfo {
	public static void main(String[] args) {
		String[] userArr = {"liuhaikun-ds"};
		List<OAUserInfo> list = GetStaffList(userArr);
		System.out.println(list.get(0).getUserName());
	}
	/**
	 * 
	 * @param userArr 格式要求{"liuhaikun-ds","zhouyaliang","chenglei-ds1","zhangsongmei"};
	 * @return
	 */
	public static List<OAUserInfo> GetStaffList(String[] userArr){
		List<OAUserInfo> OAUserInfoList = null;
		StringBuilder user = new StringBuilder();
		user.append("[");
		if(userArr!=null && userArr.length>0){
			for (int i = 0; i < userArr.length; i++) {
				user.append("\""+userArr[i]+"\"");
				if(i<userArr.length-1){
					user.append(",");
				}
			}
		}else{
			throw new RuntimeException("用户信息不能为空！");
		}
		user.append("]");
		InputStream is = null;
      	HttpClient client = new HttpClient();
		PostMethod method = new PostMethod("http://portal.oa.yolo24.com/dswebapp/Gome.OA.ProcessService/Service/Service0004.asmx/GetStaffList");
		method.setRequestHeader("Host", "portal.oa.yolo24.com");
		method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		method.setParameter("userADs", user.toString());
		try {
		client.executeMethod(method);
		is = method.getResponseBodyAsStream();
		Document document = Jsoup.parse(is,"utf8","");
		Elements elements = document.select("string");
		String text = elements.text();
		HashMap<String,Object> map = (HashMap<String,Object>) JsonUtils.Json2Object(text,HashMap.class);
		if(map!=null && map.get("list")!=null){
			String json = JsonUtils.Object2Json(map.get("list"));
			OAUserInfoList = JsonUtils.Json2List(json, OAUserInfo.class);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			method.releaseConnection();
			try {
				if(is!=null){
				is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return OAUserInfoList;
	}
}
