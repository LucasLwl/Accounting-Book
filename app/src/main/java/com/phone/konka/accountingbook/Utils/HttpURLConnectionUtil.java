package com.phone.konka.accountingbook.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by 廖伟龙 on 2017/12/21.
 */

public class HttpURLConnectionUtil {


    public static void doHttpPost(String url) throws IOException {

        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        connection.setConnectTimeout(5 * 1000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-type", "application/x-java-serialized-object");


//        设置参数
        OutputStream os = connection.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);
        String params = new String();
        params = "name" + URLEncoder.encode("", "GBK");
        objectOutputStream.writeBytes(params);
        objectOutputStream.flush();
        objectOutputStream.close();


//        接受返回
        InputStream is = connection.getInputStream();

    }


    public static void doHttpGet(String url) throws IOException {
        URL httpUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
        connection.setConnectTimeout(5 * 1000);
        connection.setUseCaches(false);


//        接受返回
        InputStream is = connection.getInputStream();

        is.available();

//        FileOutputStream fileOutputStream = new FileOutputStream();

    }
}
