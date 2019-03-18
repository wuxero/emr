package com.zxt.emr;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import antlr.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class TransferInformation {
    private Object remoteCall(String content){
        JSONObject  jsonObject = new JSONObject();
        jsonObject.put("content", content);
        String str = jsonObject.toJSONString();
        // 访问服务进程的套接字
        Socket socket = null;

        try {
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket("192.168.1.119",12345);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
            String tmp = null;
            StringBuilder sb = new StringBuilder();
            // 读取内容
            while((tmp=br.readLine())!=null)
                sb.append(tmp).append('\n');
            // 解析结果
            JSONObject res = JSON.parseObject(sb.toString());
            String emr = res.getString("string");
            System.out.println("句子："+emr);
            System.out.println("包含的实体:");
            JSONArray entities = res.getJSONArray("entities");
            for(Object o :entities){
                JSONObject jo = (JSONObject)o;
                System.out.println("开始位置："+jo.getInteger("start")+"； 结束位置："+jo.getInteger("end")+"； 实体类型："+jo.getString("type")+"； 实体词："+jo.getString("word"));
            }

            return res;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {if(socket!=null) socket.close();} catch (IOException e) {}

        }
        return null;
    }

    public static  void  main(String[] args){
        TransferInformation transferInformation = new TransferInformation();
        Scanner scanner = new Scanner(System.in);

        while (true){
            String s = scanner.next();
            Object o = transferInformation.remoteCall(s);

        }
 }
}
