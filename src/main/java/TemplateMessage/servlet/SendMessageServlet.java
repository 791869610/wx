package TemplateMessage.servlet;

import TemplateMessage.until.AuthUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/TemplateMessage/SendMessageServlet")
public class SendMessageServlet extends HttpServlet {

    private static final String APPID = "wx702a49497f13cdc3";
    private static final String APPSECRET = "e73496e127f6b77e3e70d46124125725";

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取传递过来的参数
        String openid =(String)req.getAttribute("openid");
        String template_id =(String)req.getAttribute("template_id");
        String url =(String)req.getAttribute("url");
        String appid =(String)req.getAttribute("appid");
        String pagepath =(String)req.getAttribute("pagepath");
        String color =(String)req.getAttribute("color");

        //把其他传递过来的参数放进Map集合  参数名=参数值
        Enumeration e = req.getAttributeNames();
        Map<String,String> acceptAttribute = new HashMap<String,String>();
        while(e.hasMoreElements()){
            String o = (String) e.nextElement();
            if(o.equals("openid") || o.equals("template_id")){
                continue;
            }
            acceptAttribute.put(o,(String)req.getAttribute(o));
        }

        //模板消息推送
        JSONObject jsonObject = AuthUtil.sendTemplateMessage(APPID, APPSECRET, acceptAttribute, template_id, openid, url, appid, pagepath, color);

    }
}
