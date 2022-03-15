package com.huawei.java.main.entity;

/**
 * 虚拟机结构体
 */
public class VirtualM {

    /**
     * 虚拟机id
     */
    public String id;
    /**
     * 虚拟机名称
     */
    public String name;
    public int cpu;
    public int ram;
    /**
     * 0单节点 1双节点
     */
    public int deploy;

    /**
     * 服务器id
     */
    public int pId;
    /**
     * 节点部署位置，a或b ab
     */
    public String loc;
    /**
     * 虚拟机cpu/ram比
     */
    public float ratio;

    public VirtualM(String id, String name, int cpu, int ram, int deploy) {
        this.id = id;
        this.name = name;
        this.cpu = cpu;
        this.ram = ram;
        this.deploy = deploy;
    }

    @Override
    public String toString() {
        return "virtualM{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cpu=" + cpu +
                ", ram=" + ram +
                ", deploy=" + deploy +
                '}';
    }
}
