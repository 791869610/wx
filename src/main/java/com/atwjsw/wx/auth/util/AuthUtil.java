package com.atwjsw.wx.auth.util;

import com.atwjsw.wx.auth.entity.JData;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.net.URLDecoder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
/**
 * Created by wenda on 6/15/2017.
 */
public class AuthUtil {

    public static final String APPID = "wx702a49497f13cdc3";
    public static final String APPSECRET = "e73496e127f6b77e3e70d46124125725";

    /**
     * send http request and convert resposne into json object
     * @param url
     * @return
     * @throws IOException
     */
    public static JSONObject doGetJson(String url) throws IOException {
        JSONObject jsonObject = null;
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        if(entity !=null) {
            String result = EntityUtils.toString(entity, "UTF-8");
            System.out.println("doGet方法获取的result：==="+result);
            jsonObject = JSONObject.fromObject(result);
        }
        return jsonObject;
    }

}
