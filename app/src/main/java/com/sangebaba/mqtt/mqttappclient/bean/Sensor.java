package com.sangebaba.mqtt.mqttappclient.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lzy on 2016/12/23.
 */

public class Sensor {

    @SerializedName("pm2.5")
    public Integer pm2_5;       //"pm2.5": 当前室内pm2.5值,
    public Integer co2;         //"co2": 当前室内 co2 值，
    public Integer temp;         //"temp": 当前室内温度，
    public Integer temp_out;    //"temp_out": 当前室外温度 // 后加_out表示室外数据，其它项若有室外传感器也可加此后缀。
    public Integer humidity;     //"humidity": 当前室内湿度，
}
