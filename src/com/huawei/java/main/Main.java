package com.huawei.java.main;

import com.huawei.java.main.entity.Server;
import com.huawei.java.main.entity.VirtualM;
import com.huawei.java.main.entity.Request;
import com.huawei.java.main.utils.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static com.huawei.java.main.utils.ExpandUtil.*;
import static com.huawei.java.main.utils.ParseInputData.*;


public class Main {

    //可以采购的服务器类型数量
    private static int N = 0;
    //每一种类型的服务器描述
    private static List<Server> servers = new ArrayList<>();
    //售卖的虚拟机类型数量
    private static int M = 0;
    //每一种类型的虚拟机的描述
    private static List<VirtualM> virtualMS = new ArrayList<>();
    //T天的请求序列
    private static int T = 0;
    //每天的请求数量
    private static int R = 0;
    //服务器描述id
    private static int sid = 0;
    //虚拟机描述id
    private static int vid = 0;
    //请求序列
    private static Map<Integer, List<Request>> requestMap = new HashMap<>();
    //请求天数每一天的key
    private static int rKey = 0;
    //正在运行的服务器集合
    private static List<Server> runServerList = new ArrayList<>();
    //记录正在运行时服务器的id
    private static int zid = 0;
    //正在运行时的虚拟机
    private static List<VirtualM> runVirtualMList = new ArrayList<>();


    public static void main(String[] args) throws IOException{
//        input();
        //主逻辑部分
        readFile();
        Collections.sort(servers,new SortUtil.comparator());
//        for (int i = 0; i < servers.size(); i++) {
//            System.out.println(servers.get(i));
//        }
        int n = 0;
        while (n < requestMap.size()) {
            //List<String> migrateOrderList = MigrateUtil.getMigrateOrderList(runServerList, runVirtualMList, requestMap.get(n));
            List<String> migrateOrderList = MigrateUtil.getMigrateOrderList2(runServerList, runVirtualMList, requestMap.get(n));
            //执行迁移方案migrateVm();
            //migrateVM(migrateOrderList);

            List<String> deployInfo = expansionServer(requestMap.get(n));
            //输出迁移决策信息
            //System.out.println("(migration,0)");
            PrintUtil.printMigrateInfo(migrateOrderList);
            //输出虚拟机部署信息
            PrintUtil.printDeployInfo(deployInfo);
            n++;

//            System.out.println(n+"日服务器数"+runServerList.size());
        }

        //所有服务器
//        for (Server server : runServerList) {
//            System.out.println(server);
//        }

        System.out.flush();
    }

    /**
     * 读取文件方法，暂时替代input，用于测试
     *
     * @throws IOException
     */
    private static void readFile() throws IOException {
        ArrayList<String> dataList = new ArrayList<>();
        File file = new File("C:\\Users\\Administrator\\Desktop\\training-data\\training-1.txt");
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);
        String str;
        //按行读取
        while ((str=bf.readLine())!=null){
            dataList.add(str);
        }
        bf.close();
        fr.close();
        //解析服务器类型信息
        N= Integer.parseInt(dataList.get(0));
        for (int i = 1; i < N+1; i++) {
            String s1 = dataList.get(i);
            Server s = parseServerData(sid++, s1);
            servers.add(s);
        }
        //解析虚拟机类型信息
        M = Integer.parseInt(dataList.get(N+1));
        System.out.println(M);
        for (int i = N+2; i < M+N+2; i++) {
            String s2 = dataList.get(i);
            VirtualM vir = parseVirtualData(vid++, s2);
            virtualMS.add(vir);
        }

        //解析每天请求
        T = Integer.parseInt(dataList.get(N+M+2));
        int sum=N+M+3;
        for (int i = 0; i < T; i++) {
            int R=Integer.parseInt(dataList.get(sum));
            List<Request> dRequests = new ArrayList<>(R);
            for (int j = sum+1; j < R+sum+1; j++) {
                String s3 = dataList.get(j);
                Request request = parseRequestData(s3);
                dRequests.add(request);
            }
            sum=sum+R+1;
            requestMap.put(rKey++, dRequests);
        }
    }


    /**
     * 输入数据
     */
    private static void input() {
        Scanner in = new Scanner(System.in);
        //解析服务器类型信息
        N = in.nextInt();
        in.nextLine();

        for (int i = 0; i < N; i++) {
            String s1 = in.nextLine();
            Server s = parseServerData(sid++, s1);
            servers.add(s);
        }
        //解析虚拟机类型信息
        M = in.nextInt();
        in.nextLine();
        for (int i = 0; i < M; i++) {
            String s2 = in.nextLine();
            VirtualM vir = parseVirtualData(vid++, s2);
            virtualMS.add(vir);
        }
        T = in.nextInt();
        in.nextLine();
        for (int i = 0; i < T; i++) {
            R = in.nextInt();
            in.nextLine();
            List<Request> dRequests = new ArrayList<>(R);
            for (int j = 0; j < R; j++) {
                String s3 = in.nextLine();
                Request request = parseRequestData(s3);
                dRequests.add(request);
            }
            requestMap.put(rKey++, dRequests);
        }
    }


    /**
     * 迁移虚拟机
     *
     * @param migrateOrderList 迁移指令集
     */
    public static void migrateVM(List<String> migrateOrderList) {
        if(migrateOrderList == null){
            return;
        }        //获取迁移次数
        int migrateTimes = migrateOrderList.size();
        for (int i = 0; i < migrateTimes; i++) {
            String[] migrateStep = migrateOrderList.get(i).split(",");
            if (migrateStep.length == 2) { //双节点的迁移
                VirtualM virtualM = FindUtil.findVMById(runVirtualMList, migrateStep[0]);
                Server sourceServer = FindUtil.findServerById(runServerList, virtualM.pId);
                sourceServer.aCpu += virtualM.cpu / 2;
                sourceServer.aRam += virtualM.ram / 2;
                sourceServer.bCpu += virtualM.cpu / 2;
                sourceServer.bRam += virtualM.ram / 2;
                sourceServer.vMList.remove(virtualM);

                Server aimServer = FindUtil.findServerById(runServerList, Integer.parseInt(migrateStep[1]));
                aimServer.aCpu -= virtualM.cpu / 2;
                aimServer.aRam -= virtualM.ram / 2;
                aimServer.bCpu -= virtualM.cpu / 2;
                aimServer.bRam -= virtualM.ram / 2;

                virtualM.pId = aimServer.id;
                aimServer.vMList.add(virtualM);

            } else if (migrateStep.length == 3) {//单节点的迁移
                VirtualM virtualM = FindUtil.findVMById(runVirtualMList, migrateStep[0]);
                Server sourceServer = FindUtil.findServerById(runServerList, virtualM.pId);
                String loc = migrateStep[2];
                if ("A".equals(virtualM.loc)) {
                    sourceServer.aCpu += virtualM.cpu;
                    sourceServer.aRam += virtualM.ram;
                } else {
                    sourceServer.bCpu += virtualM.cpu;
                    sourceServer.bRam += virtualM.ram;
                }
                sourceServer.vMList.remove(virtualM);

                Server aimServer = FindUtil.findServerById(runServerList, Integer.parseInt(migrateStep[1]));
                if ("A".equals(loc)) {
                    aimServer.aCpu -= virtualM.cpu;
                    aimServer.aRam -= virtualM.ram;
                    virtualM.loc = "A";
                } else {
                    aimServer.bCpu -= virtualM.cpu;
                    aimServer.bRam -= virtualM.ram;
                    virtualM.loc = "B";
                }
                virtualM.pId = aimServer.id;

                aimServer.vMList.add(virtualM);


            }
        }
    }

    /**
     * 扩容服务器
     *
     * @param requestList 一天请求序列
     * @return
     */
    public static List<String> expansionServer(List<Request> requestList) {
        List<String> deployInfo = new ArrayList<>();
        List<Server> addServers = new ArrayList<>();
        for (Request request : requestList) {
            if ("add".equals(request.type)) {
                //获得虚拟机描述信息
                VirtualM vmByName = FindUtil.findVMByName(virtualMS, request.model);
                //新的虚拟机对象
                VirtualM newVM = new VirtualM(request.vid, vmByName.name, vmByName.cpu, vmByName.ram, vmByName.deploy);
                Server getAServer = GetAServerByRatio(runServerList, newVM);

                //判断是否够空间
                if (getAServer != null) {
                    //部署
                    DeployUtil.installVM(getAServer, newVM);
                    //存储打印信息
                    if ("AB".equals(newVM.loc)) {
                        deployInfo.add("(" + getAServer.id + ")");
                    } else {
                        deployInfo.add("(" + getAServer.id + "," + newVM.loc + ")");
                    }
                } else {
                    //buy,部署
                    getAServer = buyServerByCRRatio(servers, newVM);
                    System.out.println("购买的服务器信息："+getAServer);
                    getAServer.id = zid++;
                    //服务器的id修改
                    DeployUtil.installVM(getAServer, newVM);
                    //存储打印信息
                    if ("AB".equals(newVM.loc)) {
                        deployInfo.add("(" + getAServer.id + ")");
                    } else {
                        deployInfo.add("(" + getAServer.id + "," + newVM.loc + ")");
                    }
                    addServers.add(getAServer);
                    runServerList.add(getAServer);
                }

                //部署后在列表中注册
                runVirtualMList.add(newVM);
                //将信息放入deployInfo
            } else {
                //释放虚拟机
                VirtualM vm = FindUtil.findVMById(runVirtualMList, request.vid);
                Server server = FindUtil.findServerById(runServerList, vm.pId);
                DeployUtil.uninstallVM(server, vm);
                runVirtualMList.remove(vm);
            }
        }
        PrintUtil.printExpandBuyInfo(addServers);
        return deployInfo;
    }

}
