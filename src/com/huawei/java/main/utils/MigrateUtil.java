package com.huawei.java.main.utils;

import com.huawei.java.main.entity.Server;
import com.huawei.java.main.entity.VirtualM;
import com.huawei.java.main.entity.Request;

import java.util.*;

/**
 * 迁移工具类
 */
public class MigrateUtil {

    /**
     * 迁移服务器
     *
     * @param runServerList 已购买服务器列表
     * @param runVirtualMList 运行时虚拟机列表
     * @param requestList 请求序列
     * @return
     */
    public static List<String> getMigrateOrderList(List<Server> runServerList, List<VirtualM> runVirtualMList, List<Request> requestList) {
        List<String> migrateOrderList = new ArrayList<>();
        List<Integer> migratedVMList = new ArrayList<>(); //维护已经迁移过的虚拟机列表，一台虚拟机一天只迁一次
        Map<Integer,Server> updatedServerMap = new HashMap<>(); //维护被迁出虚拟机的服务器的数据更新
        //定义每天最大的迁移次数（不超过当前虚拟机数的千分之五）
        int maxMigrateTimes = 5*runVirtualMList.size()/1000;
        //int maxMigrateTimes = 0;
        //若当日迁移次数为0，则不迁移，返回空集合
        if (maxMigrateTimes == 0) {
            return null;
        }
        //否则进行迁移调度
        //服务器排序
//        System.out.println(runServerList);
//        System.out.println(runVirtualMList);
        Collections.sort(runServerList, new SortUtil.sortServerForMigrate());
        //虚拟机排序
        Collections.sort(runVirtualMList, new SortUtil.sortVirtualMForMigrate());
//        System.out.println(runServerList);
//        System.out.println(runVirtualMList);
//        System.out.println(FindUtil.findVMById(runVirtualMList,"527039061"));
        while(maxMigrateTimes>0){
            int i;
            for(i=0; i<runServerList.size()/20; i++){
                int aCpuNum,aRamNum,bCpuNum,bRamNum;
                Server server;
                if(updatedServerMap.containsKey(runServerList.get(i).id)){
                    server = updatedServerMap.get(runServerList.get(i).id);
                }else{
                    server =  runServerList.get(i);
                }
                aCpuNum = server.aCpu;
                aRamNum = server.aRam;
                bCpuNum = server.bCpu;
                bRamNum = server.bRam;

                for(int j=0; j<runVirtualMList.size()/30; j++){
                    if(migratedVMList.contains(j)){ //在寻找可迁的机子时，过滤掉已经迁过的
                        continue;
                    }
                    int flag = 0;
                    Server virtualM_Server = FindUtil.findServerById(runServerList,runVirtualMList.get(j).pId);
                    VirtualM virturalM = null; //待迁移的虚拟机
                    if(runServerList.indexOf(virtualM_Server) > i){
                        virturalM = runVirtualMList.get(j);
                        if(virturalM.deploy == 1 && virturalM.pId!=server.id && aRamNum>=virturalM.ram/2
                                && bRamNum>=virturalM.ram/2 && aCpuNum>=virturalM.cpu/2 && bCpuNum>=virturalM.cpu/2 ){
                            aCpuNum -= virturalM.cpu/2;
                            bCpuNum -= virturalM.cpu/2;
                            aRamNum -= virturalM.ram/2;
                            bRamNum -= virturalM.ram/2;
                            migrateOrderList.add(virturalM.id+","+server.id);
                            maxMigrateTimes--;
                            flag = 1;
                        }else if(virturalM.deploy == 0){
                            if(aCpuNum >= bCpuNum && (aCpuNum>virturalM.cpu && aRamNum>virturalM.ram)){
                                aCpuNum -= virturalM.cpu;
                                aRamNum -= virturalM.ram;
                                maxMigrateTimes--;
                                migrateOrderList.add(virturalM.id+","+server.id+",A");
                                flag = 1;
                            }else if(aCpuNum <= bCpuNum && (bCpuNum>virturalM.cpu && bRamNum>virturalM.ram)){
                                bCpuNum -= virturalM.cpu;
                                bRamNum -= virturalM.ram;
                                maxMigrateTimes--;
                                migrateOrderList.add(virturalM.id+","+server.id+",B");
                                flag = 1;
                            }
                        }
                    }
                    if(flag == 1){
                        migratedVMList.add(j);
                        Server updatedServer = new Server(0,"",1,1,0,0);
                        updatedServer.id = virtualM_Server.id;
                        updatedServer.aCpu = virtualM_Server.aCpu;
                        updatedServer.aRam = virtualM_Server.aRam;
                        updatedServer.bCpu = virtualM_Server.bCpu;
                        updatedServer.bRam = virtualM_Server.bRam;
                        if(virturalM != null){
                            if(virturalM.deploy == 1){
                                updatedServer.aCpu += virturalM.cpu/2;
                                updatedServer.aRam += virturalM.ram/2;
                                updatedServer.bCpu += virturalM.cpu/2;
                                updatedServer.bRam += virturalM.ram/2;
                            }else{
                                if(virturalM.loc.equals("A")){
                                    updatedServer.aCpu += virturalM.cpu;
                                    updatedServer.aRam += virturalM.ram;
                                }else if(virturalM.loc.equals("B")){
                                    updatedServer.bCpu += virturalM.cpu;
                                    updatedServer.bRam += virturalM.ram;
                                }
                            }
                        }
                        updatedServerMap.put(updatedServer.id,updatedServer);
                        break;
                    }
                }
                if(maxMigrateTimes == 0){
                    break;
                }
            }
            if(i == runServerList.size()/20){ //无法再迁移了
                break;
            }
        }
        return migrateOrderList;
    }

    public static List<String> getMigrateOrderList2(List<Server> runServerList, List<VirtualM> runVirtualMList, List<Request> requestList) {
        List<String> migrateOrderList = new ArrayList<>();
        List<Integer> migratedVMList = new ArrayList<>(); //维护已经迁移过的虚拟机列表，一台虚拟机一天只迁一次
        //Map<Integer,Server> updatedServerMap = new HashMap<>(); //维护被迁出虚拟机的服务器的数据更新
        //定义每天最大的迁移次数（不超过当前虚拟机数的千分之五）
        int maxMigrateTimes = 5 * runVirtualMList.size() / 1000;
        //int maxMigrateTimes = 0;
        //若当日迁移次数为0，则不迁移，返回空集合
        if (maxMigrateTimes == 0) {
            return null;
        }
        //否则进行迁移调度
        //服务器排序
//        System.out.println(runServerList);
//        System.out.println(runVirtualMList);
        Collections.sort(runServerList, new SortUtil.sortServerForMigrate());
        //虚拟机排序
        Collections.sort(runVirtualMList, new SortUtil.sortVirtualMForMigrate());
//        System.out.println(runServerList);
//        System.out.println(runVirtualMList);
//        System.out.println(FindUtil.findVMById(runVirtualMList,"527039061"));
        while (maxMigrateTimes > 0) {
            //System.out.println("-------------");
            int i;
            for (i = 0; i < runServerList.size()/10; i++) {
                Server server = runServerList.get(i);

                if (server.restCpu() == 0 || server.restRam() == 0 ||
                        (server.aCpu * server.aRam == 0 && server.bCpu * server.bRam == 0)) {
                    continue;
                }
                for (int j = 0; j < runVirtualMList.size()/10; j++) {
                    if (migratedVMList.contains(j)) { //在寻找可迁的机子时，过滤掉已经迁过的
                        continue;
                    }
                    int flag = 0;
                    VirtualM virturalM = runVirtualMList.get(j);//待迁移的虚拟机
                    if (virturalM.cpu > server.restCpu()){ //之后的虚拟机都没法插入这个server了
                        break;
                    }
                    Server virtualM_Server = FindUtil.findServerById(runServerList, runVirtualMList.get(j).pId); //虚拟机原来在的服务器
                    if (runServerList.indexOf(virtualM_Server) > i) { //若优先级小于当前server，表示可以迁

                        if (virturalM.deploy == 1 && virturalM.pId != server.id && server.aRam >= virturalM.ram / 2
                                && server.bRam >= virturalM.ram / 2 && server.aCpu >= virturalM.cpu / 2 && server.bCpu >= virturalM.cpu / 2) {

//                            //------------------------------------------------------------
//                            //迁之前服务器信息输出
//
//                            System.out.println(virturalM.toString());
//                            System.out.println(server.toString());
//                            for(int x=0; x<server.vMList.size(); x++){
//                                System.out.println(server.vMList.get(x));
//                            }
//                            System.out.println(virtualM_Server.toString());
//                            for(int x=0; x<virtualM_Server.vMList.size(); x++){
//                                System.out.println(virtualM_Server.vMList.get(x));
//                            }
//                            //------------------------------------------------------------

                            migratedVMList.add(j); //记录已经迁过的机子
                            server.aCpu -= virturalM.cpu / 2;
                            server.bCpu -= virturalM.cpu / 2;
                            server.aRam -= virturalM.ram / 2;
                            server.bRam -= virturalM.ram / 2;
                            virtualM_Server.aCpu += virturalM.cpu / 2;
                            virtualM_Server.bCpu += virturalM.cpu / 2;
                            virtualM_Server.aRam += virturalM.ram / 2;
                            virtualM_Server.bRam += virturalM.ram / 2;

                            virturalM.pId = server.id;
                            virturalM.loc = "AB";
                            server.vMList.add(virturalM);
                            virtualM_Server.vMList.remove(virturalM);

                            migrateOrderList.add(virturalM.id + ", " + server.id);
                            maxMigrateTimes--;
                            flag = 1;

                            /*//------------------------------------------------------------
                            //迁之后服务器信息输出
                            System.out.println(server.toString());
                            for(int x=0; x<server.vMList.size(); x++){
                                System.out.println(server.vMList.get(x));
                            }
                            System.out.println(virtualM_Server.toString());
                            for(int x=0; x<virtualM_Server.vMList.size(); x++){
                                System.out.println(virtualM_Server.vMList.get(x));
                            }
                            //------------------------------------------------------------*/

                        } else if (virturalM.deploy == 0) {
                            int canMigrate = 0; //标记能否迁
                            String updateLoc = ""; //TODO：很关键，导致了放置虚拟机错误的问题
                            if (server.aCpu >= server.bCpu && (server.aCpu >= virturalM.cpu && server.aRam >= virturalM.ram)) {

                                migratedVMList.add(j);
                                server.aCpu -= virturalM.cpu;
                                server.aRam -= virturalM.ram;
                                updateLoc = "A";
                                virturalM.pId = server.id;
                                server.vMList.add(virturalM);
                                maxMigrateTimes--;
                                migrateOrderList.add(virturalM.id + ", " + server.id + ", A");
                                canMigrate = 1;
                            } else if (server.aCpu < server.bCpu && (server.bCpu >= virturalM.cpu && server.bRam >= virturalM.ram)) {

                                migratedVMList.add(j);
                                server.bCpu -= virturalM.cpu;
                                server.bRam -= virturalM.ram;
                                updateLoc = "B";
                                virturalM.pId = server.id;
                                server.vMList.add(virturalM);
                                maxMigrateTimes--;
                                migrateOrderList.add(virturalM.id + ", " + server.id + ", B");
                                canMigrate = 1;
                            }
                            if (canMigrate == 1) {
                                if ("A".equals(virturalM.loc)) {
                                    virtualM_Server.aCpu += virturalM.cpu;
                                    virtualM_Server.aRam += virturalM.ram;
                                } else if("B".equals(virturalM.loc)){
                                    virtualM_Server.bCpu += virturalM.cpu;
                                    virtualM_Server.bRam += virturalM.ram;
                                }
                                virturalM.loc = updateLoc;
                                virtualM_Server.vMList.remove(virturalM);
                                flag = 1;
                            }
                        }
                        if (flag == 1) {
                            break;
                        }
                    }

                }
                if (maxMigrateTimes == 0) {
                    break;
                }
            }
            if (i == runServerList.size()/10) { //无法再迁移了
                break;
            }
        }
        return migrateOrderList;
    }


}