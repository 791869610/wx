package com.atwjsw.wx.auth.servlet;

import com.atwjsw.wx.auth.util.AuthUtil;
import net.sf.json.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;


/**
 * Created by wenda on 6/15/2017.
 */
//@WebServlet("/WxAuth/callBack")
public class CallBackServlet extends HttpServlet {

    private String dbUrl = "jdbc:mysql://192.168.0.114:3306/auth";
    private String driverName = "com.mysql.jdbc.Driver";
    private String dbUserName = "root";
    private String dbPassword = "123456";
    private Connection conn = null;
    private Connection conn1 = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private String openid = "";
    private String Code = "";

    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            System.out.println("初始化");
            this.dbUrl=config.getInitParameter("dbUrl");
            this.driverName=config.getInitParameter("driverName");
            this.dbUserName=config.getInitParameter("userName");
            this.dbPassword=config.getInitParameter("passWord");
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 获取微信回调请求中的code
        System.out.println("in WxAuth/callBack");
        String code = req.getParameter("code");
        System.out.println("获取用户code==="+code);
        System.out.println("获取变量Code==="+Code);
        if(!"".equals(Code) && Code!=null){
            //变量Code有值（）
            if(!"".equals(code) && code!=null){
                if(Code.equals(code)){
                    //同一用户登录
                    System.out.println("同一用户登录");
                    System.out.println("Code="+Code);
                    System.out.println("code="+code);
                    System.out.println("openid="+openid);
                }else{
                    //不是同一个用户
                    System.out.println("不是同一个用户");
                    Code = code;
                    String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                            + "appid=" + AuthUtil.APPID
                            + "&secret=" + AuthUtil.APPSECRET
                            + "&code=" + Code
                            + "&grant_type=authorization_code";
                    //向微信发出请求，带上APPSCECRET和code，获取openid和access_toekn
                    JSONObject jsonObject = AuthUtil.doGetJson(url);
                    openid = jsonObject.getString("openid");
                    String token = jsonObject.getString("access_token");
                    System.out.println("Code="+Code);
                    System.out.println("code="+code);
                    System.out.println("openid="+openid);
                    //获取用户信息
                    String infoUrl = "https://api.weixin.qq.com/sns/userinfo?"
                            + "access_token=" + token
                            + "&openid=" + openid
                            + "&lang=zh_CN";
                    JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
                    System.out.println("用户信息:---"+userInfo);
                }
            }else{
                System.out.println("Code有值,获取不到用户code");
            }
        }else{
            //变量Code为空，判断用户code是否为空
            if(!"".equals(code) && code!=null){
                //用户code有值（用户首次授权或者code失效）
                //用户code赋值给变量Code
                Code = code;
                String url = "https://api.weixin.qq.com/sns/oauth2/access_token?"
                        + "appid=" + AuthUtil.APPID
                        + "&secret=" + AuthUtil.APPSECRET
                        + "&code=" + Code
                        + "&grant_type=authorization_code";
                //向微信发出请求，带上APPSCECRET和code，获取openid和access_toekn
                JSONObject jsonObject = AuthUtil.doGetJson(url);
                openid = jsonObject.getString("openid");
                String token = jsonObject.getString("access_token");
                System.out.println("用户首次授权登录");
                System.out.println("Code="+Code);
                System.out.println("code="+code);
                System.out.println("openid="+openid);
                //获取用户信息
                String infoUrl = "https://api.weixin.qq.com/sns/userinfo?"
                        + "access_token=" + token
                        + "&openid=" + openid
                        + "&lang=zh_CN";
                JSONObject userInfo = AuthUtil.doGetJson(infoUrl);
                System.out.println("用户信息:---"+userInfo);
            }else{
                System.out.println("Code为空,获取不到用户code");
            }
        }


        //1. 使用微信用户信息直接登录，无需注册和绑定
//        req.setAttribute("info", userInfo);
//        req.getRequestDispatcher("/index1.jsp").forward(req, resp);

        //2. 将微信与当前的系统账号绑定
        try {
            System.out.println("帐号首次绑定到openid:==="+openid);
            String nickName = getNickNameByOpenid(openid);
            if (!"".equals(nickName)){
                //已绑定。直接跳转登录成功页面
//                req.setAttribute("nickname", nickName);
                req.setAttribute("openid", openid);
                req.setAttribute("APPID", AuthUtil.APPID);
                req.setAttribute("APPSECRET", AuthUtil.APPSECRET);
                req.setAttribute("name", "小明");
                req.setAttribute("info", "学生");
                req.setAttribute("template_id", "dvWbzO_cdyHC-MzcE4XajSgnuNT90P13TYK-XrtwhqA");
                System.out.println("首次帐号绑定成功的openid==="+openid);
//                req.getRequestDispatcher("/index2.jsp").forward(req, resp);
                req.getRequestDispatcher("/TemplateMessage/SendMessageServlet").forward(req, resp);
            } else {
//                未绑定。 跳转到绑定页面，要求用户输入账户密码
//                更新数据库openid
                System.out.println("未绑定");
                System.out.println("帐号未绑定，跳转到绑定页面是需要传递到openid：==="+openid);
                req.setAttribute("openid", openid);
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
//
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String account =req.getParameter("account");
        String password =req.getParameter("password");
        String nickname =req.getParameter("nickname");
        String openid =req.getParameter("openid");
        String reg =req.getParameter("reg");
        System.out.println("绑定页面跳转，传递过来的openid：==="+openid);
        try {
            if("1".equals(reg)){
                System.out.println("账号注册");
                int temp1 =addUser(openid, account,nickname ,password);
                if(temp1 > 0){
                    System.out.println("注册成功");
                    req.setAttribute("openid", openid);
                    req.setAttribute("password", password);
                    req.setAttribute("account", account);
                    req.setAttribute("openid", openid);
                    req.getRequestDispatcher("/login.jsp").forward(req, resp);
                }else {
                    req.getRequestDispatcher("/index4.jsp").forward(req, resp);
                }
            }else {
                System.out.println("账号绑定");
                String nickName = getNickName(openid);
                System.out.println("帐号绑定的nickname：==="+nickName);
                if (!"".equals(nickName)) {
                    //已注册。进行账号绑定
                    System.out.println("账号已注册");
                    System.out.println("帐号已经注册的nickname"+nickName);
                    int temp = updateUser(openid, account, password);
                    if (temp > 0) {
                        System.out.println("账号绑定成功");
                        req.setAttribute("nickname：===", nickName);
                        System.out.println("注册后绑定到nickname：==="+nickName);
                        System.out.println("注册后绑定到openid：===="+openid);
                        req.setAttribute("openid", openid);
                        req.getRequestDispatcher("/WxAuth/SendMessageServlet").forward(req, resp);
//                        req.getRequestDispatcher("/index2.jsp").forward(req, resp);
                    } else {
                        System.out.println("账号绑定失败");
                        req.getRequestDispatcher("/index5.jsp").forward(req, resp);
                    }
                } else {
//                未注册。 跳转到注册页面，要求用户输入账户密码
                    System.out.println("账号未注册");
                    System.out.println("帐号未注册的openid：==="+openid);
                    req.setAttribute("openid", openid);
                    req.getRequestDispatcher("/index3.jsp").forward(req, resp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
//
    public String getNickNameByOpenid(String openid) throws SQLException {
        System.out.println("获取openid:--"+openid);
        String nickName = "";
        ResultSet rs1 = null;

        conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        String sql = "select nickname from user where openid=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        rs1 = ps.executeQuery();
        while (rs1.next()) {
            nickName = rs1.getString("NICKNAME");
        }
        System.out.println("获取nickName"+nickName);
        rs1.close();
        ps.close();
        conn.close();
        return nickName;
    }

    public String getNickName(String openid) throws SQLException {
        String nickName = "";
        conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        String sql = "select nickname from user where openid=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        rs = ps.executeQuery();
        while (rs.next()) {
            nickName = rs.getString("NICKNAME");
        }
        rs.close();
        ps.close();
        conn.close();
        return nickName;
    }
//
    public int updateUser(String openid, String account, String password) throws SQLException {

        conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        String sql = "update user set OPENID=? where ACCOUNT=? and PASSWORD=?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        ps.setString(2, account);
        ps.setString(3, password);
        int temp = ps.executeUpdate();
        ps.close();
        conn.close();
        return temp;
    }

    public int addUser(String openid, String account,String nickname, String password) throws SQLException {

        conn = DriverManager.getConnection(dbUrl, dbUserName, dbPassword);
        String sql = "INSERT user (openid ,account ,password,nickname) VALUE (?,?,?,?)";
        ps = conn.prepareStatement(sql);
        ps.setString(1, openid);
        ps.setString(2, account);
        ps.setString(3, password);
        ps.setString(4, nickname);
        int temp = ps.executeUpdate();
        ps.close();
        conn.close();
        return temp;
    }
}
