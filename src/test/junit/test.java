import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import TemplateMessage.until.AuthUtil;
public class test {
    public static void main(String[] args){
        String content = "这是测试模板的名字{{name.DATA}},\n还要有信息{{info.DATA}}";
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
        System.out.println(para);
        System.out.println(para.get(0));
        System.out.println(para.get(1));
    }

}
