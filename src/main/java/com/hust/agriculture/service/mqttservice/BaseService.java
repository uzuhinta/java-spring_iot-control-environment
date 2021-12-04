package com.hust.agriculture.service.mqttservice;

import com.hust.agriculture.common.Constant;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.springframework.core.env.Environment;

public abstract class BaseService implements MqttCallback {

    private String broker;
    private String serverInstanceID;
    private Logger logger;

    MqttConnectOptions connOpts = new MqttConnectOptions();
    MqttClient mqttClient;
    String subscribeTopic;

    BaseService(Logger logger, String clientId, String subscribeTopic, Environment env) {

        this.logger = logger;
        this.subscribeTopic = subscribeTopic;
        this.broker = env.getProperty(Constant.MQTT_SERVER);
        this.serverInstanceID = env.getProperty(Constant.SERVER_ID);

        MemoryPersistence persistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(broker, clientId + serverInstanceID, persistence);
            logger.info("Connecting to broker: " + broker);
            mqttClient.setCallback(this);
            setConnOpts();
            mqttClient.connect(connOpts);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    protected abstract int getQos();

    public abstract void setConnOpts();

    @Override
    public void connectionLost(Throwable throwable) {
        //logger.warn("Connection lost");
        while (!mqttClient.isConnected()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //logger.info("Trying to connect again");
                mqttClient.connect();
                mqttClient.setCallback(this);
                mqttClient.subscribe(subscribeTopic, getQos());
            } catch (MqttException me) {
                me.printStackTrace();
            }
        }
        //logger.info("Connected again");
    }

}
