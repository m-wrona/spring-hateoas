package com.mwronski.hateoas.model;

import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Container for resources that can be returned by the application
 *
 * @author Michal Wronski
 * @date 27-05-2014
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resources<T extends ResourceEntity> extends ResourceSupport {

    @XmlElement
    private final List<T> resources = new ArrayList<T>();

    public void add(T... newEntities) {
        for (T newEntity : newEntities) {
            resources.add(newEntity);
        }
    }

    public <C extends Collection<T>> void add(C collection) {
        for (T newEntity : collection) {
            resources.add(newEntity);
        }
    }


    public List<T> getResources() {
        return resources;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("size", resources.size()).toString();
    }
}
