package com.huawei.java.main.entity;

public class Request {

    /**
     * 请求类型
     */
    public String type;
    /**
     * 虚拟机ID
     */
    public String vid;
    /**
     * 虚拟机型号
     */
    public String model;

    /**
     * del构造函数
     *
     * @param type
     * @param vid
     */
    public Request(String type, String vid) {
        this.type = type;
        this.vid = vid;
        this.model = "";
    }

    /**
     * add构造函数
     *
     * @param type
     * @param vid
     * @param model
     */
    public Request(String type, String vid, String model) {
        this.type = type;
        this.vid = vid;
        this.model = model;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type='" + type + '\'' +
                ", vid=" + vid +
                ", model='" + model + '\'' +
                '}';
    }
}
