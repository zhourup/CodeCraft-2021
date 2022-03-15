package com.huawei.java.main.utils;

import com.huawei.java.main.entity.Server;
import com.huawei.java.main.entity.VirtualM;

import java.util.List;
import java.util.Random;

/**
 * 购买以及扩容服务器
 */
public class ExpandUtil {

    /**
     * 购买服务器cpu/ram最接近
     * @param servers
     * @param vm
     * @return
     */
    public static Server buyServerByCRRatio(List<Server> servers, VirtualM vm){
        //若是单节点部署
        if (vm.deploy == 0) {
            //索引i 找到第一个能放下虚拟机的服务器
            int i = 0;
            while (i < servers.size()) {
                if (servers.get(i).aCpu >= vm.cpu && servers.get(i).aRam >= vm.ram) {
                    break;
                }
                i++;
                //System.out.println("i:"+i);
            }
            //找到第一个能放下虚拟机的服务器
            if (i < servers.size()) {
                //记录最佳节点
                int index=i;
                //部署节点
                vm.loc = "A";
                //维护一个最近ratio值
                float min = Math.abs(vm.ratio - servers.get(index).ratio);
                for (int j = i+1; j < servers.size(); j++) {
                    if(servers.get(j).aCpu>=vm.cpu && servers.get(j).aRam>=vm.ram){
                        float diff = Math.abs(vm.ratio - servers.get(j).ratio);
                        if (diff < min) {
                            index=j;
                            min = diff;
                        }
                    }
                    /*if (diff==0){
                        break;
                    }*/
                }
                Server bestServer = new Server(servers.get(index).id,servers.get(index).name,servers.get(index).cpu,servers.get(index).ram,servers.get(index).hardware,servers.get(index).dCos);
                return bestServer;
            }
        } else {//若是双节点部署
            //索引i 找到第一个能放下虚拟机的服务器
            int i = 0;
            while (i < servers.size()) {
                if (servers.get(i).cpu>= vm.cpu && servers.get(i).ram >= vm.ram) {
                    break;
                }
                i++;
                //System.out.println("i:"+i);
            }
            //找到第一个能放下虚拟机的服务器
            if (i < servers.size()) {
                //维护一个目标服务器
                int index=i;
                vm.loc = "AB";
                //维护一个最近ratio值
                float min = Math.abs(vm.ratio - servers.get(index).ratio);
                for (int j = i+1; j < servers.size(); j++) {
                    if(servers.get(j).cpu>=vm.cpu && servers.get(j).ram>=vm.ram){
                        float diff = Math.abs(vm.ratio - servers.get(j).ratio);
                        if (diff < min) {
                            index = j;
                            min = diff;
                        }
                    }
                    /*if (diff==0){
                        break;
                    }*/
                }
                Server bestServer = new Server(servers.get(index).id,servers.get(index).name,servers.get(index).cpu,servers.get(index).ram,servers.get(index).hardware,servers.get(index).dCos);
                return bestServer;
            }
        }
        return null;
    }

    /**
     * 查找服务器中是否适合插入此虚拟机
     *
     * @param runServerList 正在运行的服务器
     * @param vm         待插入的虚拟机
     * @return 返回一个服务器 若为null则没有找到合适的服务器
     */
    public static Server GetAServerByRatio(List<Server> runServerList, VirtualM vm) {
        //倘若是第一天 则直接退出
        if (runServerList.size() == 0) {
            return null;
        }
        //若是单节点部署
        if (vm.deploy == 0) {
            //索引i 找到第一个能放下虚拟机的服务器
            int i = 0;
            while (i < runServerList.size()) {
                if (runServerList.get(i).aCpu >= vm.cpu && runServerList.get(i).aRam >= vm.ram){
                    vm.loc="A";
                    break;
                }else if (runServerList.get(i).bCpu >= vm.cpu && runServerList.get(i).bRam >= vm.ram){
                    vm.loc="B";
                    break;
                }
                i++;
            }
            //找到第一个能放下虚拟机的服务器
            if (i < runServerList.size()) {
                //维护一个目标服务器
//                Server targetServer = runServerList.get(i);
                int index = i;
                //维护一个最近ratio值
                float min = Math.abs(vm.ratio - runServerList.get(index).ratio);
                for (int j = i+1; j < runServerList.size(); j++) {
                    //负载均衡部署
                    if (runServerList.get(j).aCpu>=vm.cpu && runServerList.get(j).aRam>=vm.ram && runServerList.get(j).aCpu > runServerList.get(j).bCpu){
                        float diff = Math.abs(vm.ratio - runServerList.get(j).ratio);
                        if (diff < min) {
                            index = j;
                            min = diff;
                            vm.loc="A";
                        }
                    }else if (runServerList.get(j).bCpu>=vm.cpu && runServerList.get(j).bRam>=vm.ram){
                        float diff = Math.abs(vm.ratio - runServerList.get(j).ratio);
                        if (diff < min) {
                            index = j;
                            min = diff;
                            vm.loc="B";
                        }
                    }
                    /*if (diff==0){
                        break;
                    }*/
                }
                return runServerList.get(index);
            }
        } else {//若是双节点部署
            //索引i 找到第一个能放下虚拟机的服务器
            int i = 0;
            while (i < runServerList.size()) {
                if (runServerList.get(i).aCpu >=vm.cpu/2 && runServerList.get(i).aRam >= vm.ram/2
                        && runServerList.get(i).bCpu >=vm.cpu/2 && runServerList.get(i).bRam >= vm.ram/2) {
                    break;
                }
                i++;
//                System.out.println("i:"+i);
            }
            //找到第一个能放下虚拟机的服务器
            if (i < runServerList.size()) {
                int index = i;
                vm.loc = "AB";
                //维护一个最近ratio值
                float min = Math.abs(vm.ratio - runServerList.get(index).ratio);
                for (int j = i+1; j < runServerList.size(); j++) {
                    if (runServerList.get(j).aCpu >=vm.cpu/2 && runServerList.get(j).aRam >= vm.ram/2
                            && runServerList.get(j).bCpu >=vm.cpu/2 && runServerList.get(j).bRam >= vm.ram/2){
                        float diff = Math.abs(vm.ratio - runServerList.get(j).ratio);
                        if (diff < min) {
                            index = j;
                            min = diff;
                        }
                    }
                    /*if (diff==0){
                        break;
                    }*/
                }
                return runServerList.get(index);
            }
        }
        return null;
    }

    /**
     * 查找服务器中是否适合插入此虚拟机
     *
     * @param runServerList 正在运行的服务器
     * @param newVM         待插入的虚拟机
     * @return 返回一个服务器 若为null则没有找到合适的服务器
     */
    public static Server GetAServer(List<Server> runServerList, VirtualM newVM) {

        //若是单节点部署
        if (newVM.deploy == 0) {
            for (Server server : runServerList) {
                //TODO：优化考虑 负载均衡
                //若不满足server.aCpu > server.bCpu && server.aRam > server.bRam 则全放在b节点
                if (server.aCpu >= newVM.cpu && server.aRam >= newVM.ram && server.aCpu > server.bCpu && server.aRam > server.bRam) {
                    //部署a
                    newVM.loc = "A";
                    return server;
                } else if (server.bCpu >= newVM.cpu && server.bRam >= newVM.ram) {
                    newVM.loc = "B";
                    return server;
                }
            }
        } else { //若是双节点部署
            for (Server server : runServerList) {
                if (server.aCpu >= newVM.cpu / 2 && server.aRam >= newVM.ram / 2
                        && server.bCpu >= newVM.cpu / 2 && server.bRam >= newVM.ram / 2) {
                    newVM.loc = "AB";
                    return server;
                }
            }
        }
        return null;
    }

    /**
     * 返回一个能容纳虚拟机的服务器
     *
     * @param servers
     * @param virtualM
     * @return
     */
    public static Server buyServer(List<Server> servers, VirtualM virtualM) {
        if (virtualM.deploy == 0) {
            virtualM.loc = "A";
        } else {
            virtualM.loc = "AB";
        }
//        Random random=new Random();
//        while (true){
//            int loc=random.nextInt(servers.size()-1);
//            //System.out.println("循环中:"+loc);
//            if (servers.get(loc).cpu>virtualM.cpu&&servers.get(loc).ram>virtualM.ram){
//                Server server = new Server(servers.get(loc).id, servers.get(loc).name, servers.get(loc).cpu, servers.get(loc).ram,
//                        servers.get(loc).hardware, servers.get(loc).dCos);
//                return server;
//            }
//        }
//        后期加决策信息，选择一个合适的服务器
        for (int i = 5; i < servers.size(); i++) {
            if (servers.get(i).cpu > virtualM.cpu && servers.get(i).ram > virtualM.ram) {
                Server server = new Server(servers.get(i).id, servers.get(i).name, servers.get(i).cpu, servers.get(i).ram,
                        servers.get(i).hardware, servers.get(i).dCos);
                return server;
            }
        }
        return null;
    }

}
