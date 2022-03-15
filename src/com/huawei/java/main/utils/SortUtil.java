package com.huawei.java.main.utils;

import com.huawei.java.main.entity.Server;
import com.huawei.java.main.entity.VirtualM;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 排序工具类
 */
public class SortUtil {

    /**
     * 自定义比较器
     */
    public static class comparator implements Comparator<Server> {
        @Override
        public int compare(Server o1, Server o2) {
//            double weight1=o1.hardware/(o1.cpu*0.5+o1.ram*0.5);
//            double weight2=o1.hardware/(o2.cpu*0.5+o2.ram*0.5);
//            return (int)weight2-(int)weight1;
            double weight1=o1.cpu*0.2+o1.ram*0.5+o1.hardware*0.3+o1.dCos*0.2;
            double weight2=o2.cpu*0.2+o2.ram*0.5+o2.hardware*0.3+o2.dCos*0.2;
            return (int)weight2-(int)weight1;
//            if (o1.ram != o2.ram) {
//                return o2.restRam() - o1.restRam();
//            } else {
//                return o2.restCpu() - o1.restCpu();
//            }
        }
    }


    /**
     * 通过规则对服务器资源进行排序 优先级：日功耗成本升序排列->cpu资源升序排列->ram资源升序排列
     */
    public static class sortServerForMigrate implements Comparator<Server> {

        @Override
        public int compare(Server s1, Server s2) {
            if (s1.restCpu() != s2.restCpu()){
                return s1.restCpu() - s2.restCpu();
            }else if (s1.dCos!=s2.dCos){
                return s1.dCos-s2.dCos;
            }{
                return s2.restRam() - s1.restRam();
            }
        }
    }

    /**
     * 按内存剩余量、CPU剩余量排序当前正在运行的服务器
     *
     * @param servers
     * @return
     */
    public static List<Server> sortRunServers(List<Server> servers) {
        Collections.sort(servers, new comparator());
        return servers;
    }

    /**
     * 通过规则对正在运行的虚拟机进行排序  按cpu资源升序，内存资升序
     *
     */
    public static class sortVirtualMForMigrate implements Comparator<VirtualM>{
        @Override
        public int compare(VirtualM v1, VirtualM v2) {
            if (v1.cpu!=v2.cpu){
                return v1.cpu-v2.cpu;
            }else {
                return v2.ram-v1.ram;
            }
        }
    }
}
