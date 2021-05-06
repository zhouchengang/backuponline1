package com.zhouchengang.backuponline.album;

/**
 * 描述:
 * demoone-
 *
 * @Author J_jiasheng@qq.com
 * @create 2020-12-15 9:51
 */
public class Utils {

    public static final String BASE_URL = "http://192.168.8.247:4545/";
    public static final String IMG_URL = "http://47.240.168.158:8888/";
//    public static final String UP_URL = "http://127.0.0.1:3000/";
    public static final String UP_URL = "http://192.168.0.113:3000/";

    public  static  boolean isDir(String s){
        if(null == s || s.isEmpty()){
            return false;
        }
        return s.charAt(s.length()-1)=='/';
    }

    public  static  boolean isNotDir(String s){
        if(null == s || s.isEmpty()){
            return false;
        }
        return s.charAt(s.length()-1)!='/';

    }

    public static String swiftAliToServer(String aliPath){
        String ret =  aliPath.substring(aliPath.indexOf("/")+1);
        ret =  ret.substring(ret.indexOf("/")+1);
        ret =  ret.substring(ret.indexOf("/")+1);
        return IMG_URL+ret;
    }
    public static String getCover(String aliPath){
        return aliPath+"?x-oss-process=image/resize,m_fill,h_100,w_100";
    }

}
