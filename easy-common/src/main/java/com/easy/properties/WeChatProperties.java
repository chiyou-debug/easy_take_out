package com.easy.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "easy.wechat")
public class WeChatProperties {

    private String appid; // AppID of the WeChat mini-program
    private String secret; // Secret key of the WeChat mini-program
    private String mchid; // Merchant ID
    private String mchSerialNo; // Serial number of the merchant API certificate
    private String privateKeyFilePath; // Path to the merchant's private key file
    private String apiV3Key; // Key for decrypting the certificate
    private String weChatPayCertFilePath; // Path to the WeChat payment certificate
    private String notifyUrl; // Callback URL for successful payments
    private String refundNotifyUrl; // Callback URL for successful refunds

}
