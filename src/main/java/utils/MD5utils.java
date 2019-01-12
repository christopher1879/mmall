package utils;
import org.apache.commons.compress.utils.CharsetNames;
import org.apache.ibatis.javassist.bytecode.stackmap.BasicBlock;

import java.nio.charset.Charset;
import  java.security.MessageDigest;

public class MD5utils {
    private  static  String bytearraytohexstring(byte b[]){
        StringBuffer resultsb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultsb.append(bytetohexstring(b[i]));

        }
        return resultsb.toString();
    }

    private static String bytetohexstring(byte b){
        int n=b;
        if (n<0){
            n+=256;
            int d1 = n/16;
            int d2 =n%16;

        }
        return  hexdigit[d1]+hexdigit[d2];
    }

    private static  String md5encode(String origin,String charsetname){
        String resultstring = null;
        try {
            resultstring=new String(origin);
            MessageDigest md=MessageDigest.getInstance("md5");
           //判断字符串不为空或者不为null 对象
            if (charsetname==null||"".equals(charsetname)){
                resultstring=bytearraytohexstring(md.digest(resultstring.getBytes()));

            }else {
                resultstring=bytearraytohexstring(md.digest(resultstring.getBytes(charsetname)));
            }

        }catch (Exception e){

        }
        return resultstring.toUpperCase();
    }

    public static String md5encodeutf8(String origin){
        origin= origin+propertyutils.getProperty("password.salt","");
        return md5encode(origin,"utf-8");

    }


    //这是如何定义的 是一个string 的字符串类型吗
    private static  final String hexdigit[]={
            "",""
    };


}
