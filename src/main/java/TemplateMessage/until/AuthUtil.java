package TemplateMessage.until;

import TemplateMessage.entity.TemplateEntity;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

/**
 * Created by wenda on 6/15/2017.
 */
public class AuthUtil {

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

    /**
     * post请求
     * @param url         url地址
     * @param jsonParam     参数
     * @param noNeedResponse    不需要返回结果
     * @return
     */
    private static JSONObject httpPost(String url,JSONObject jsonParam, boolean noNeedResponse){
        //post请求返回结果
        DefaultHttpClient httpClient = new DefaultHttpClient();
        JSONObject jsonResult = null;
        HttpPost method = new HttpPost(url);
        try {
            if (null != jsonParam) {
                //解决中文乱码问题
                StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                method.setEntity(entity);
            }
            HttpResponse result = httpClient.execute(method);
            url = URLDecoder.decode(url, "UTF-8");
            /**请求发送成功，并得到响应**/
            if (result.getStatusLine().getStatusCode() == 200) {
                String str = "";
                try {
                    /**读取服务器返回过来的json字符串数据**/
                    str = EntityUtils.toString(result.getEntity());
                    if (noNeedResponse) {
                        return null;
                    }
                    /**把json字符串转换成json对象**/
                    jsonResult = JSONObject.fromObject(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * 正则匹配获取content的参数
     * @param content
     * @return
     */
    public static List<String> StringtoArray(String content){
        //正则表达式
        String regEx = "\\{\\{(\\w)*(\\.)(DATA\\}\\})";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(content);
        boolean rs = true;
        //匹配到的正则
        List<String> list = new ArrayList<String>();
        //匹配项里需要的参数
        List<String> para = new ArrayList<String>();
        while(rs){
            rs = matcher.find();
            if(!rs){
                break;
            }
            String s = matcher.group();
            list.add(s);
        }
        for(int i = 0 ; i < list.size() ; i++) {
            //获取正则匹配值{{name.DATA}}
            String s = list.get(i);
            //获取.的下标
            int j = s.indexOf('.');
            //取出需要参数
            String pa = s.substring(2,j);
            //放进List集合
            para.add(s.substring(2,j));
        }

        return para;
    }

    /**
     * 重组Map集合，参数名=参数值  ==》    参数名=｛value=参数值，color=参数显示颜色｝
     * @param map
     * @return
     */
    public static Map<String,Map<String,String>> reformMap(Map<String,String> map){
        Map<String,Map<String,String>> AcceptDada = new HashMap<String,Map<String,String>>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Map<String,String> AcceptDada_s = new HashMap<String,String>();
            String key = entry.getKey();
            String value = entry.getValue();
            AcceptDada_s.put("value",value);
            AcceptDada_s.put("color","#173177");
            AcceptDada.put(key,AcceptDada_s);
        }
        return AcceptDada;
    }

    /**
     * 根据传递参数和模板参数，组装data的Json对象
     * @param AcceptDada  传递参数Map集合
     * @param parameter 模板参数List集合
     * @return
     */
    public static JSONObject structureJsonData(Map<String,Map<String,String>> AcceptDada, List<String> parameter){
        //组装消息内容data
        // data=｛
        //      模板参数1 = ｛
        //                  value = 传递参数1 ，
        //                  color = 参数显示颜色
        //                  ｝ ，
        //      模板参数2 = ｛
        //                  value = 传递参数2 ，
        //                  color = 参数显示颜色
        //                  ｝
        //      ......
        //      ｝
        //1.创建data的JSON对象
        JSONObject dataData = new JSONObject();

        //2.循环模板参数List集合
        for (int i=0;i<parameter.size();i++){

            //3.创建模板参数的JSON对象
            JSONObject parameterData = new JSONObject();

            //4.遍历获取参数Map集合
            for (Map.Entry<String, Map<String,String>> entry : AcceptDada.entrySet()) {
                Map<String,String> value = entry.getValue();
                String key = entry.getKey();

                //5.判断模板参数名和传递过来的参数名是否一样，对应的放进模板参数的JSON对象中
                if(key.equals( parameter.get(i))){
                    for (Map.Entry<String, String> entry_s : value.entrySet()){
                        String key_s = entry_s.getKey();
                        String value_s = entry_s.getValue();
                        parameterData.put(key_s,value_s);
                    }

                    //6.将“模板参数的JSON对象”放进data的JSON对象中
                    dataData.put(parameter.get(i),parameterData);
                    break;
                }
            }
        }
        return dataData;
    }

    /**
     * 获取access_token
     * @param APPID
     * @param APPSECRET
     * @return
     * @throws IOException
     */
    public static String getAccess_token(String APPID,String APPSECRET) throws IOException{
        String url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential" +
                "&appid="+APPID +
                "&secret="+APPSECRET;
        JSONObject jsonObject = doGetJson(url);
        String token = jsonObject.getString("access_token");
        return token;
    }

    /**
     * 获取所有模板列表
     * @param access_token
     * @return
     * @throws IOException
     */
    public static JSONArray getAllTemplate(String access_token) throws IOException{
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token="+access_token;
        JSONObject jsonResult = doGetJson(url);
        JSONArray jsonArray =jsonResult.getJSONArray("template_list");
        return jsonArray;
    }

    /**
     * 根据template_id , 找到对应的模板
     * @param tempArray
     * @param template_id
     * @return
     */
    public static TemplateEntity findTempalteByTemplate_id(JSONArray tempArray,String template_id){
        TemplateEntity temp = new TemplateEntity();
        List<TemplateEntity> list=new ArrayList<TemplateEntity>();
        if(template_id!=null){
            for (int i = 0;i<tempArray.size();i++){
                JSONObject subObject=tempArray.getJSONObject(i);
                String _template_id=subObject.getString("template_id");
                String _content=subObject.getString("content");
                if(template_id.equals(_template_id)){
                    temp = new TemplateEntity(_template_id,_content);
                    list.add(temp);
                    break;
                }
            }
        }
        return temp;
    }

    /**
     * 创建outData的JSON对象
     * @param openid
     * @param template_id
     * @param url
     * @param appid
     * @param pagepath
     * @param color
     * @param dataData
     * @return
     */
    public static JSONObject structureJsonOutdata(String openid,String template_id,String url,String appid,String pagepath,String color,JSONObject dataData){
        JSONObject outData = new JSONObject();
        outData.put("touser", openid);
        outData.put("template_id", template_id);
        outData.put("url",url);
        outData.put("appid",appid);
        outData.put("pagepath",pagepath);
        outData.put("color",color);
        outData.put("data", dataData);
        return outData;
    }


    /**
     * 发送模板数据到微信
     * @param access_token
     * @param outData
     * @param boo
     * @return
     */
    public static JSONObject sendOutDataToWx(String access_token,JSONObject outData,boolean boo){
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?"
                +"access_token="+access_token;
        JSONObject jsonResult_Send = AuthUtil.httpPost(url,outData,boo);
        return jsonResult_Send;
    }


    /**
     * 模板消息推送
     * @param APPID
     * @param APPSECRET
     * @param acceptAttribute
     * @param template_id
     * @param openid
     * @param url
     * @param appid
     * @param pagepath
     * @param color
     * @return
     * @throws IOException
     */
    public static JSONObject sendTemplateMessage
            (String APPID,String APPSECRET,Map<String,String> acceptAttribute,String template_id,String openid,String url,String appid,String pagepath,String color)
            throws IOException{

        //传递参数Map集合   参数名=｛value=参数值，color=参数显示颜色｝
        Map<String,Map<String,String>> AcceptDada = AuthUtil.reformMap(acceptAttribute);

        //获取access_token
        String access_token = getAccess_token(APPID,APPSECRET);

        //获取所有模板列表
        JSONArray tempArray =AuthUtil.getAllTemplate(access_token);

        //匹配template_id , 找到对应的模板
        TemplateEntity temp = AuthUtil.findTempalteByTemplate_id(tempArray,template_id);

        //获取模板的content
        String template_content = temp.getContent();

        //获取模板参数List集合
        List<String> parameter =AuthUtil.StringtoArray(template_content);

        //创建data的JSON对象
        JSONObject dataData = AuthUtil.structureJsonData(AcceptDada,parameter);

        //创建outData的JSON对象
        JSONObject outData = AuthUtil.structureJsonOutdata(openid,template_id,url,appid,pagepath,color,dataData);

        //发送模板数据到微信
        JSONObject jsonResult_Send = AuthUtil.sendOutDataToWx(access_token,outData,false);
        return null;
    }


}
