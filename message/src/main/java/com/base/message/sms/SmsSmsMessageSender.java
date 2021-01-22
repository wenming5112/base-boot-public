package com.base.message.sms;

import com.base.message.MessageServiceProvider;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 17:42
 **/
public class SmsSmsMessageSender extends AbstractSmsMessageSender {


    private SmsSmsMessageSender() {

    }

    private SmsSmsMessageSender(AbstractSmsMessage message, MessageServiceProvider serviceProvider) {
        this.message = message;
        this.serviceProvider = serviceProvider;
    }

    @Override
    public void send() {

    }


}
