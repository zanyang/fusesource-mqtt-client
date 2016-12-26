package com.sangebaba.mqtt.mqttappclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sangebaba.mqtt.mqttappclient.bean.Sensor;
import com.sangebaba.mqtt.mqttappclient.util.JsonAPI;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


    private final static String CONNECTION_STRING = "";
    private final static boolean CLEAN_START = true;
    private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
    private final static String CLIENT_ID = "publishService";
    public static Topic[] topics = {
            new Topic("", QoS.AT_LEAST_ONCE)};
    public final static long RECONNECTION_ATTEMPT_MAX = 6;
    public final static long RECONNECTION_DELAY = 2000;
    public final static int SEND_BUFFER_SIZE = 2 * 1024 * 1024;//发送最大缓冲为2M
    private final static String USER = "testuser1";
    private final static String PSW = "sangebaba906";
    CallbackConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_mqtt_sent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Send a message to a topic
                connection.publish("", "".getBytes(), QoS.AT_LEAST_ONCE, false, new Callback<Void>() {
                    public void onSuccess(Void v) {
                        // the pubish operation completed successfully.
                        System.out.println("the pubish operation completed successfully =============");
                    }

                    public void onFailure(Throwable value) {
                        System.out.println("publish failed ======");
                    }
                });

                // To disconnect..
//                connection.disconnect(new Callback<Void>() {
//                    public void onSuccess(Void v) {
//                        System.out.println(" called once the connection is disconnected =========");
//                        // called once the connection is disconnected.
//                    }
//
//                    public void onFailure(Throwable value) {
//                        System.out.println(" Disconnects never fail =========");
//                        // Disconnects never fail.
//                    }
//                });


            }
        });


        findViewById(R.id.btn_mqtt_future).setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                //创建MQTT对象
                MQTT mqtt = new MQTT();
                try {
                    //设置mqtt broker的ip和端口
                    mqtt.setHost(CONNECTION_STRING);
                    //连接前清空会话信息
                    mqtt.setCleanSession(CLEAN_START);
                    //设置重新连接的次数
                    mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);
                    //设置重连的间隔时间
                    mqtt.setReconnectDelay(RECONNECTION_DELAY);
                    //设置心跳时间
                    mqtt.setKeepAlive(KEEP_ALIVE);
                    //设置缓冲的大小
                    mqtt.setSendBufferSize(SEND_BUFFER_SIZE);
                    mqtt.setUserName(USER);
                    mqtt.setPassword(PSW);
                    connection = mqtt.callbackConnection();
                    connection.listener(new Listener() {

                        public void onDisconnected() {
                            System.out.println("connection listener onDisconnected ==================== ");
                        }

                        public void onConnected() {
                            System.out.println("connection listener onConnected ==================== ");
                        }

                        public void onPublish(UTF8Buffer topic, Buffer payload, Runnable ack) {
                            // You can now process a received message from a topic.
                            // Once process execute the ack runnable.
                            ack.run();
                            final byte[] data = payload.toByteArray();
                            final byte[] bytes = topic.toByteArray();
                            final String mPaylode = new String(data);
                            final String mTopic = new String(bytes);
                            System.out.println("received message from a topic topic = " + mTopic + "     payload =" + mPaylode);
                            try {
                                dealMessage(mTopic, mPaylode);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        public void onFailure(Throwable value) {
                            System.out.println("connection  listener onFailure ==========");
                        }
                    });
                    connection.connect(new Callback<Void>() {
                        public void onFailure(Throwable value) {
                            System.out.println("onFailure");
                        }

                        // Once we connect..
                        public void onSuccess(Void v) {
                            System.out.println("Start Subscribe to a topic =============== ");

                            // Subscribe to a topic
                            connection.subscribe(topics, new Callback<byte[]>() {
                                public void onSuccess(byte[] qoses) {
                                    System.out.println("Subscribe to a topic onSuccess ========= ");
                                    // The result of the subcribe request.

                                }

                                public void onFailure(Throwable value) {
                                    System.out.println("Subscribe to a topic onFailure =========");
                                }
                            });

                        }
                    });

                } catch (URISyntaxException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }


            }
        });

    }

    /**
     * 处理接收到的消息
     *
     * @param topic
     * @param payload
     */
    private static void dealMessage(String topic, String payload) throws Exception {
        String[] split = topic.split("/");
        String device = split[1];
        String mac = split[2];
        String action = split[3];
        System.out.println(" action = " + action);
        System.out.println(" payload = " + payload);
        switch (action) {
            case "sensor": {
                Sensor sensor = JsonAPI.parseJsonToObj(payload, Sensor.class);
                System.out.println(" dealMessage = " + sensor.co2);
                System.out.println(" dealMessage = " + sensor.humidity);
//                    EventBus.getDefault().post(cmdBean);
            }
            break;
        }

    }

}
