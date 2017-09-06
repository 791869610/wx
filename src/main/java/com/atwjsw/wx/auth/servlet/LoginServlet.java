package com.atwjsw.wx.auth.servlet;

import com.atwjsw.wx.auth.util.AuthUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by wenda on 6/15/2017.
 */
@WebServlet("/WxAuth/wxLogin")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String backUrl = "http://2714jh.imwork.net/wxCallBack";
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?"
                + "appid=" + AuthUtil.APPID
                + "&redirect_uri=" + URLEncoder.encode(backUrl)
                + "&response_type=code"
                + "&scope=snsapi_userinfo"
                + "&state=STATE"
                + "#wechat_redirect";
//        重定向用户请求到微信授权URL
//        System.out.println("url"+url);
        resp.sendRedirect(url);
//        super.doGet(req, resp);
    }
}
