package com.example.tea_leaves_project.controller;

import com.example.tea_leaves_project.dto.QRScannerData;
import com.example.tea_leaves_project.util.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mqtt")
@RequiredArgsConstructor
public class MqttTestPubController {

    private final MessageChannel mqttOutboundChannel;

    @PostMapping
    public boolean sendToMqtt() {
        String jsonData = MapperUtil.writeValueAsString(
                QRScannerData.builder()
                        .humidity(39)
                        .temperature(25)
                        .qrCode("8934868171843")
                .build()
        );
        Message<String> message = MessageBuilder.withPayload(jsonData)
                .setHeader("mqtt_topic", "esp32/data")
                .setHeader("mqtt_qos", 1)
                .build();
        return mqttOutboundChannel.send(message);
    }
}
