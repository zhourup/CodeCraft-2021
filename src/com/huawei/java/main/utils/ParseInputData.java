package com.huawei.java.main.utils;

import com.huawei.java.main.entity.Server;
import com.huawei.java.main.entity.VirtualM;
import com.huawei.java.main.entity.Request;

public class ParseInputData {

    /**
     * 解析输入服务器数据
     *
     * @param sid 服务器id
     * @param s   输入的一行服务器信息
     * @return 服务器
     */
    public static Server parseServerData(int sid, String s) {
        if (s == null || s.equals("")) {

        }
        String[] datas = s.replace(" ","").replace("(", "").replace(")", "").split(",");
        Server server = new Server(sid, datas[0], Integer.parseInt(datas[1]), Integer.parseInt(datas[2]),
                Integer.parseInt(datas[3]), Integer.parseInt(datas[4]));
        return server;
    }

    /**
     * 解析输入虚拟机数据
     *
     * @param vid 虚拟机id
     * @param s   输入的一行虚拟机信息
     * @return 虚拟机
     */
    public static VirtualM parseVirtualData(int vid, String s) {
        if (s == null || s.equals("")) {
            //do something
        }
        String[] datas = s.replace(" ","").replace("(", "").replace(")", "").split(",");
        VirtualM vir = new VirtualM(String.valueOf(vid), datas[0], Integer.parseInt(datas[1]), Integer.parseInt(datas[2]), Integer.parseInt(datas[3]));
        return vir;
    }

    /**
     * 封装创建虚拟机或删虚拟机请求
     *
     * @param s
     * @return
     */
    public static Request parseRequestData(String s) {
        if (s == null || s.equals("")) {

        }
        String[] datas = s.replace(" ","").replace("(", "").replace(")", "").split(",");
        Request request = null;
        if (datas.length == 2) {
            request = new Request("del", datas[1]);
        } else {
            request = new Request("add", datas[2], datas[1]);
        }
        return request;
    }
}
