package com.huawei.java.main.utils;

import com.huawei.java.main.entity.Server;
import com.huawei.java.main.entity.VirtualM;

import java.util.List;

/**
 * 查找工具类
 */
public class FindUtil {

    /**
     * 根据request虚拟机类型 查找VirtualM对象
     * @param virtualMList 虚拟机列表
     * @param vmName 虚拟机请求
     * @return
     */
    public static VirtualM findVMByName(List<VirtualM> virtualMList, String vmName){
        for (VirtualM virtualM : virtualMList) {
            if (vmName.equals(virtualM.name)){
                return virtualM;
            }
        }
        return null;
    }

    /**
     * 通过id在运行服务器中寻找server
     *
     * @param ServerList 在运行的服务器
     * @param id 服务器id
     * @return
     */
    public static Server findServerById(List<Server> ServerList, int id){
        for (Server server : ServerList) {
            if (server.id == id){
                return server;
            }
        }
        return null;
    }

    /**
     * 根据虚拟机id查找虚拟机对象
     * @param virtualMList
     * @param vid
     * @return
     */
    public static VirtualM findVMById(List<VirtualM> virtualMList,String vid){
        for (VirtualM virtualM : virtualMList) {
            if (virtualM.id.equals(vid)){
                return virtualM;
            }
        }
        return null;
    }
}
