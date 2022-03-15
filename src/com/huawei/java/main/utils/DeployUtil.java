package com.huawei.java.main.utils;

import com.huawei.java.main.entity.Server;
import com.huawei.java.main.entity.VirtualM;

/**
 * 根据虚拟机请求 选择部署服务器
 */
public class DeployUtil {

    /**
     * 执行部署虚拟机到指定服务器
     *
     * @param server 服务器
     * @param newVM 带部署虚拟机
     */
    public static void installVM(Server server, VirtualM newVM){

        //若是单节点部署
        if ("A".equals(newVM.loc)) {
            server.aCpu -= newVM.cpu;
            server.aRam -= newVM.ram;

        } else if ("B".equals(newVM.loc)) {
            server.bCpu -= newVM.cpu;
            server.bRam -= newVM.ram;

        } else {
            server.aCpu -= newVM.cpu / 2;
            server.aRam -= newVM.ram / 2;
            server.bCpu -= newVM.cpu / 2;
            server.bRam -= newVM.ram / 2;

        }

        //虚拟机记录服务器
        newVM.pId = server.id;
        //服务器记录虚拟机
        server.vMList.add(newVM);

    }

    /**
     * 释放虚拟机
     *
     * @param server
     * @param oldVM
     */
    public static void uninstallVM(Server server, VirtualM oldVM){

        //服务器将虚拟机从列表卸载
        if ("A".equals(oldVM.loc)) {
            server.aCpu += oldVM.cpu;
            server.aRam += oldVM.ram;
        } else if ("B".equals(oldVM.loc)) {
            server.bCpu += oldVM.cpu;
            server.bRam += oldVM.ram;
        } else {
            server.aCpu += oldVM.cpu / 2;
            server.aRam += oldVM.ram / 2;
            server.bCpu += oldVM.cpu / 2;
            server.bRam += oldVM.ram / 2;
        }

        server.vMList.remove(oldVM);

    }
}
