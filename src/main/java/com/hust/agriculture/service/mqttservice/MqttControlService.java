package com.hust.agriculture.service.mqttservice;

import com.hust.agriculture.common.Constant;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MqttControlService extends BaseService implements MqttCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttControlService.class);

    Environment env;

    public MqttControlService(Environment env) {
        super(LOGGER, Constant.CONTROL_CLIENT_ID, null, env);
        if (mqttClient.isConnected()) {
            LOGGER.info("Connected. Ready to send command to devices");
        }
        this.env = env;
    }

    @Override
    protected int getQos() {
        return 2;
    }

    @Override
    public void setConnOpts() {
        connOpts.setCleanSession(true);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {

    }


    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public boolean publish(String topic, String data) {
        try {
            mqttClient.publish(topic, data.getBytes(), getQos(), false);
            return true;
        } catch (MqttException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

}