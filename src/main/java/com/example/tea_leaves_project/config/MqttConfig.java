package com.example.tea_leaves_project.config;

import com.example.tea_leaves_project.Payload.Request.QRScannerData;
import com.example.tea_leaves_project.Payload.Response.QrResponse;
import com.example.tea_leaves_project.Service.helper.QRServiceHelper;
import com.example.tea_leaves_project.Service.imp.WarehouseServiceImp;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import com.example.tea_leaves_project.Util.*;
@Slf4j
@Configuration
public class MqttConfig {

//    @Bean
//    public IMqttClient mqttClient() throws MqttException {
//        final String mqttServer = "7e44c054e3384872bc6b019a4185eb18.s1.eu.hivemq.cloud";
//        IMqttClient instance = new MqttClient(mqttServer, "tealeaves-mqtt");
//        MqttConnectOptions options = new MqttConnectOptions();
//        options.setAutomaticReconnect(true);
//        options.setCleanSession(true);
//        options.setConnectionTimeout(10);
//
//        if (!instance.isConnected()) {
//            instance.connect(options);
//        }
//        return instance;
//    }
    @Autowired
    QRServiceHelper qrServiceHelper;
    @Autowired
    WarehouseServiceImp warehouseService;
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        options.setServerURIs(new String[]{"ssl://7e44c054e3384872bc6b019a4185eb18.s1.eu.hivemq.cloud:8883"});
        options.setUserName("iec-local");
        options.setPassword("Aa12345678".toCharArray());
        factory.setConnectionOptions(options);

        return factory;
    }

    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(
                        "myclient",
                        mqttPahoClientFactory(),
                        "esp32/data",
                        "esp32_1/data");
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(0);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }


    // hàm subscribe topic qrcode từ mqtt
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {
            String topic = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
            String payload = (String) message.getPayload();
            log.info("Received topic: {}, and payload: {}", topic, payload);
            QRScannerData data = MapperUtil.parseJson(payload, QRScannerData.class);
            warehouseService.scanQrCode(data);
        };
    }
}
