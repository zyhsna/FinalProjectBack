package edu.zyh.finalproject.util;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 *
 */
public class SendMessage {
    public static String HOST = "";
    public static int PORT_TASK_LEVEL = 12345;
    public static int PORT_ACCOMPLISHMENT_LEVEL = 12346;

    /**
     * 与Python程序交互，获得训练好的模型数据
     *
     * @param price    价格
     * @param distance 距离
     * @return int
     */
    public static int getTaskLevel(double price, double distance) {
        HashMap<String, Double> params = new HashMap<>();
        params.put("price", price);
        params.put("distance", distance);
        String str = JsonUtil.objectToJson(params);
        // 访问服务进程的套接字
        Socket socket = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            HOST = addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket(HOST, PORT_TASK_LEVEL);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String tmp = null;

            StringBuilder sb = new StringBuilder();
            // 读取内容
            while (true) {
                while ((tmp = br.readLine()) != null) {
                    sb.append(tmp).append('\n');
                }
                break;
            }
            // 解析结果
            String result = sb.toString();
            for (int i = 0; i < result.length(); i++) {
                char c = result.charAt(i);
                if (Character.isDigit(c)) {
                    System.out.println(c);
                    return Integer.parseInt(String.valueOf(c));
                }
            }

            return -1;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ignored) {
            }
            System.out.println("调用结束");
        }
        return -1;
    }

    public static int getAccomplishmentLevel(int taskLevel, double completeTime, double distance) {
        HashMap<String, Double> params = new HashMap<>();
        params.put("completeTime", 500.0);
        params.put("taskLevel", (double) 2);
        params.put("totalDistance", 15500.0);
        String str = JsonUtil.objectToJson(params);
        // 访问服务进程的套接字
        Socket socket = null;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            HOST = addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            // 初始化套接字，设置访问服务的主机和进程端口号，HOST是访问python进程的主机名称，可以是IP地址或者域名，PORT是python进程绑定的端口号
            socket = new Socket(HOST, PORT_ACCOMPLISHMENT_LEVEL);
            // 获取输出流对象
            OutputStream os = socket.getOutputStream();
            PrintStream out = new PrintStream(os);
            // 发送内容
            out.print(str);
            // 告诉服务进程，内容发送完毕，可以开始处理
            out.print("over");
            // 获取服务进程的输入流
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String tmp = null;
            StringBuilder sb = new StringBuilder();
            // 读取内容
            while (true) {
                while ((tmp = br.readLine()) != null) {
                    sb.append(tmp).append('\n');
                }
                break;
            }
            // 解析结果
            String result = sb.toString();
            for (int i = 0; i < result.length(); i++) {
                char c = result.charAt(i);
                if (Character.isDigit(c)) {
                    System.out.println(c);
                    return Integer.parseInt(String.valueOf(c));
                }
            }
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ignored) {
            }
            System.out.println("调用结束");
        }
        return -1;
    }
}
