package com.mwronski.hateoas.model.builder;

import com.mwronski.hateoas.model.Message;

/**
 * Helper for building messages
 *
 * @author Michal Wronski
 * @date 27-05-2014
 */
public final class MessageBuilder {

    private Message message = new Message();

    public MessageBuilder withTitle(String title) {
        message.setTitle(title);
        return this;
    }

    public MessageBuilder withContent(String content) {
        message.setContent(content);
        return this;
    }

    public MessageBuilder withSender(String sender) {
        message.setSender(sender);
        return this;
    }

    public Message build() {
        try {
            return message;
        } finally {
            message = new Message();
        }
    }

    MessageBuilder() {
        //from this package only
    }

}
