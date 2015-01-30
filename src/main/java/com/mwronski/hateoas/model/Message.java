package com.mwronski.hateoas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Basic message that can be received by the application
 *
 * @author Michal Wronski
 * @date 27-05-2014
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message extends ResourceEntity {

    @XmlAttribute
    private String title;
    @XmlAttribute
    private String content;
    @XmlAttribute
    private String sender;

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void toStringInfo(Objects.ToStringHelper string) {
        string.add("title", title).add("content", content).add("sender", sender);
    }

}
