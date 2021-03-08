package com.zhouchengang.backuponline;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.UUID;


public class UploadUtil {
    private static final String TAG = "uploadFile";
    private static final int TIME_OUT = 10*1000;   //超时时间
    private static final String CHARSET = "utf-8"; //设置编码


    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 在对sd卡进行读写操作之前调用这个方法
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    /**
     * 获取cache路径
     */
    public static String getDiskCachePath() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return FoneActivity.sContext.getExternalCacheDir().getPath();
        } else {
            return FoneActivity.sContext.getCacheDir().getPath();
        }
    }

    public static String pathtoname( String path ) {
        return path.substring(path.lastIndexOf("/")+1);
    }


    public static String getFileMD5s(File file,int radix) {
        if (!file.isFile()) {
            return "null";
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return "null";
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return ""+(bigInt.toString(radix));
    }

    public static String pathtodir( String path ) {
        return path.substring(0,path.lastIndexOf("/")+1);
    }

    public static String sjc2str(String sjc) {

        Calendar tms = Calendar.getInstance();
        tms.setTimeInMillis(Long.valueOf(sjc).longValue());
        return tms.get(Calendar.YEAR) + "-" + (tms.get(Calendar.MONTH)+1)
                + "-" + tms.get(Calendar.DAY_OF_MONTH) + " " + tms.get(Calendar.HOUR_OF_DAY)
                + ":" + tms.get(Calendar.MINUTE)
                + ":" + tms.get(Calendar.SECOND);
    }

    public static void writemessagetolocal(Long logname, String conent) {

        //if(true) return ;
        Log.e("write",conent);
        String localpath=getDiskCachePath();
        File filedir=new File(localpath+"/record/");
        if(!filedir.exists()) {
            Log.d(TAG, "dir no exists");
            filedir.mkdirs();
        }

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(localpath+"/record/"+logname/(1000*60)+".txt", true)));
            out.write("\r\n");//写入换行
            out.write(""+logname+"  "+conent);
            out.write("-------");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将InputStream写入本地文件
     */
    private static void writeToLocal(String destinationdir,String filename, InputStream input) throws IOException {
        File fil=new File(destinationdir);
        if(!fil.exists()) {
            Log.d(TAG, "dir no exists");
            fil.mkdirs();
        }
        int index;
        byte[] bytes = new byte[1024];
        FileOutputStream downloadFile = new FileOutputStream(destinationdir+filename);
        while ((index = input.read(bytes)) != -1) {
            downloadFile.write(bytes, 0, index);
            downloadFile.flush();
        }
        downloadFile.close();
        input.close();
    }

    /**
     * 从服务器下载文件
     */
    public static void  downloadFile(String path) {
        String picfilename=pathtoname(path);
        String  path2=pathtodir(path);
        String localpath=getDiskCachePath();
        try {
            URL url = new URL("http://"+FoneActivity.read.getString("ip", "")+":8888/"+path2+"/"+picfilename);
            InputStream iis = url.openStream();
            writeToLocal(localpath+"/uploads/"+path2+"/",picfilename,iis);
            iis.close();

            mymoveFile(localpath+"/uploads/"+path2+"/"+picfilename,path2,picfilename);
            return ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }


    /**
     * 上传文件到服务器

    public static String uploadFile(String  path) {
        String RequestURL="https://"+FoneActivity.read.getString("ip", "")+":1231/dopost";
        String result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        try {
            FileInputStream isBmtoinst = new FileInputStream(path);
            URL url = new URL(RequestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            DataOutputStream dos = new DataOutputStream( conn.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            //sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+filename+"\""+LINE_END);

            sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+path+"\""+LINE_END);
            sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());
            byte[] bytes = new byte[1024];
            int len = 0;
            int t=0;
            while((len=isBmtoinst.read(bytes))!=-1)
            {
                t++;
                if(t>200000)
                    return "false";
                dos.write(bytes, 0, len);
            }
            isBmtoinst.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            int res = conn.getResponseCode();
            InputStream input =  conn.getInputStream();
            StringBuffer sb1= new StringBuffer();
            int ss ;
            while((ss=input.read())!=-1)
            {
                sb1.append((char)ss);
            }
            result = sb1.toString();

            isBmtoinst.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
        */



    /**
     * 上传文件到服务器
     */
    public static String uploadFile(String  path) {
        String RequestURL="http://"+FoneActivity.read.getString("ip", "")+":1231/dopost";
        String result = null;
        String  BOUNDARY =  UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--" , LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型
        try {
            FileInputStream isBmtoinst = new FileInputStream(path);


            byte[] bytes = new byte[80000000];
            int len = 0;
            int t=0;
            while(len>=0)
            {
                URL url = new URL(RequestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(TIME_OUT);
                conn.setConnectTimeout(TIME_OUT);
                conn.setDoInput(true);  //允许输入流
                conn.setDoOutput(true); //允许输出流
                conn.setUseCaches(false);  //不允许使用缓存
                conn.setRequestMethod("POST");  //请求方式
                conn.setRequestProperty("Charset", CHARSET);  //设置编码
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
                t++;
                DataOutputStream dos = new DataOutputStream( conn.getOutputStream());

                len=isBmtoinst.read(bytes);
                Log.e("lem",""+len);
                if(len==-1) t=-t;
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                if(t<0)
                    sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+path   +".zhoutype"+"0"+(-t)+"\""+LINE_END);
                else
                    sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""+path   +".zhoutype"+t+"\""+LINE_END);
                sb.append("Content-Type: application/octet-stream; charset="+CHARSET+LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                if(len!=-1)
                    dos.write(bytes, 0, len);


                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX+BOUNDARY+PREFIX+LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                int res = conn.getResponseCode();
                InputStream input =  conn.getInputStream();
                StringBuffer sb1= new StringBuffer();
                int ss ;
                while((ss=input.read())!=-1)
                {
                    sb1.append((char)ss);
                }
                result = sb1.toString();
            }
            isBmtoinst.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 复制单个文件
     *

     */

    public static void  mymoveFile(String oldpath, String dir,String filename) {

        Log.e("oldpath",oldpath);
        Log.e("dir",dir);
        Log.e("filename",filename);


        File file=new File(dir);
        if(!file.exists()) {
            Log.d(TAG, "dir no exists");
            file.mkdirs();
        }

        try {
            File oldFile = new File(oldpath);
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return ;
            }
            FileInputStream fileInputStream = new FileInputStream(oldpath);
            FileOutputStream fileOutputStream = new FileOutputStream(dir+filename);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();

            File file1=new File(oldpath);
            file1.delete();

            FoneActivity.sContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + dir+filename)));

            return ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

