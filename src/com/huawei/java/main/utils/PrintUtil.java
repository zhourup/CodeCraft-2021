package com.huawei.java.main.utils;

import com.huawei.java.main.entity.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 输出决策信息工具类
 */
public class PrintUtil {

    /**
     * 输出迁移决策信息
     *
     * @param list
     */
    public static void printMigrateInfo(List<String> list) {
        if (list == null) {
            System.out.println("(migration,0)");
            return;
        }
        System.out.println("(migration," + list.size() + ")");
        for (int i = 0; i < list.size(); i++) {
            System.out.println("(" + list.get(i) + ")");
        }
    }

    /**
     * 输出扩容购买服务器信息
     *
     * @param serverList
     */
    public static void printExpandBuyInfo(List<Server> serverList) {
        if (serverList == null || serverList.size() == 0) {
            System.out.println("(purchase, 0)");
        } else {
            Map<String, Integer> serverNumMap = new HashMap<>();
            for (Server server : serverList) {
                serverNumMap.put(server.name, serverNumMap.getOrDefault(server.name, 0) + 1);
            }
            System.out.println("(purchase, " + serverNumMap.size() + ")");
            for (String ser : serverNumMap.keySet()) {
                System.out.println("(" + ser + ", " + serverNumMap.get(ser) + ")");
            }
//            System.out.println("(purchase, " + serverList.size() + ")");
//            for (int i = 0; i < serverList.size(); i++) {
//                System.out.println("("+serverList.get(i).name+",1)");
//            }
        }
    }

    /**
     * 输出部署虚拟机信息
     *
     * @param list
     */
    public static void printDeployInfo(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
