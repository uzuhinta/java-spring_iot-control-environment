package com.hust.agriculture.service.mqttservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.agriculture.common.Constant;
import com.hust.agriculture.model.User;
import com.hust.agriculture.payload.request.DataMQTT;
import com.hust.agriculture.repository.DeviceRepository;
import com.hust.agriculture.repository.UserRepository;
import com.hust.agriculture.service.httpservice.CachingService;
import com.hust.agriculture.service.httpservice.InvoiceService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class MqttCommonService extends BaseService {

    public static final Logger LOGGER = LoggerFactory.getLogger(MqttCommonService.class);

    DeviceRepository deviceRepository;

    MqttControlService mqttControlService;

    Environment env;

    CachingService cachingService;

    InvoiceService invoiceService;

    public MqttCommonService(DeviceRepository deviceRepository,
                             Environment env,
                             MqttControlService mqttControlService,
                             CachingService cachingService,
                             InvoiceService invoiceService) {
        super(LOGGER, Constant.COMMON_CLIENT_ID, Constant.COMMON_TOPIC, env);
        if (mqttClient.isConnected())
            LOGGER.info("Connected. Subscribing the data request from devices...");
        try {
            mqttClient.subscribe(subscribeTopic, getQos());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        this.deviceRepository = deviceRepository;
        this.env = env;
        this.mqttControlService = mqttControlService;
        this.cachingService = cachingService;
        this.invoiceService = invoiceService;
    }

    @Override
    protected int getQos() {
        return 0;
    }

    @Override
    public void setConnOpts() {

    }

    /*
    Dinh dang message:
    type=1: keep alive
    type=2: data
    type=3: request control
    {
        "deviceId":1,
        "type":1,
        "data":{}
    }
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String msg = new String(message.getPayload());
        LOGGER.info("Have an message: " + msg);
        if (!topic.equals(subscribeTopic)) {
            LOGGER.info("Message Arrived not subscribed common topic");
        } else {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DataMQTT data = mapper.readValue(msg, DataMQTT.class);

                if(data.getType() == Constant.TYPE_DATA){
                    // cache data
                    cachingService.cacheData(data);

                    // send data
                    String token = data.getToken();
                    User user = deviceRepository.findUserByDeviceId(Long.parseLong(token));
                    if(user != null && user.getTopicName() != null){
                        if(!invoiceService.isHasInvoiceOverDueDate(user.getUsername())){
                            mqttControlService.publish(user.getTopicName(), mapper.writeValueAsString(data));
                        }
                    }
                }

            } catch (Exception e) {
                LOGGER.error("Data request is not required format");
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
