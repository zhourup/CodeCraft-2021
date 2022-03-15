package com.huawei.java.main.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务器结构体
 */
public class Server {
    public int id;
    public String name;
    public int cpu;
    public int ram;
    public int hardware;
    public int dCos;

    public int aCpu;
    public int aRam;
    public int bCpu;
    public int bRam;
    public List<VirtualM> vMList;
    /**
     * 服务器cpu/ram比
     */
    public float ratio;

    public Server(int id, String name, int cpu, int ram, int hardware, int dCos) {
        this.id = id;
        this.name = name;
        this.cpu = cpu;
        this.ram = ram;
        this.aCpu = cpu / 2;
        this.bCpu = cpu / 2;
        this.aRam = ram / 2;
        this.bRam = ram / 2;
        this.hardware = hardware;
        this.dCos = dCos;
        vMList = new ArrayList<>();
        this.ratio = cpu / ram;
    }

    /**
     * 完全复制服务器（可能有负载）
     *
     * @param copy
     */
    public void setServer(Server copy) {
        this.id = copy.id;
        this.name = copy.name;
        this.cpu = copy.cpu;
        this.ram = copy.ram;
        this.hardware = copy.hardware;
        this.dCos = copy.dCos;
        this.aCpu = copy.aCpu;
        this.aRam = copy.aRam;
        this.bCpu = copy.bCpu;
        this.bRam = copy.bRam;
        this.vMList = copy.vMList;
        this.ratio = cpu / ram;
    }

    @Override
    public String toString() {
        return "Server{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpu=" + cpu +
                ", ram=" + ram +
                ", hardware=" + hardware +
                ", dCos=" + dCos +
                ", aCpu=" + aCpu +
                ", aRam=" + aRam +
                ", bCpu=" + bCpu +
                ", bRam=" + bRam +
                '}';
    }

    /**
     * 剩余cpu
     *
     * @return
     */
    public int restCpu() {
        return this.aCpu + this.bCpu;
    }

    /**
     * 剩余内存
     *
     * @return
     */
    public int restRam() {
        return this.aRam + this.bRam;
    }
}
