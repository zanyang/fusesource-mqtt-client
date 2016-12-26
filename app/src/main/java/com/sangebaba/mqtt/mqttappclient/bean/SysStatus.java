package com.sangebaba.mqtt.mqttappclient.bean;

/**
 * Created by lzy on 2016/12/23.
 */

public class SysStatus {
    public Integer power;       //"power": 1－开机；0－关机,
    public Integer level;       // 0～n，档位, 速度由低到高, 0为待机档位
    public Integer ptc;         // 1－加热开启；0－加热关闭, -1-加热不可开启,
    public Integer mode;        // 1～6：内循环、外循环、智能、夜间(睡眠)、自由、手动。
    public Integer led;         //"led": 屏显状态 0-关闭, 1-开启,
    public Integer childlock;   //"childlock": 儿童锁 0-关闭, 1-开启
}
