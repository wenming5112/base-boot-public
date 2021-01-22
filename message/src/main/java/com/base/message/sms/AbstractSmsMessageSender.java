package com.base.message.sms;

import com.base.message.MessageServiceProvider;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/6 17:28
 **/
public abstract class AbstractSmsMessageSender {


    /**
     * mail message / sms message & other message type
     */
    public AbstractSmsMessage message;

    public MessageServiceProvider serviceProvider;

    public abstract void send();

}
