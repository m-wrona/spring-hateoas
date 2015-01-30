package com.mwronski.hateoas.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.Objects;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import static com.google.common.base.Objects.toStringHelper;

/**
 * Basic identifiable entity
 *
 * @author Michal Wronski
 * @date 5/30/14.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResourceEntity extends ResourceSupport {

    public static final String COLUMN_ENTITY_ID = "entityId";

    @XmlAttribute
    private String entityId;

    public final void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public final String getEntityId() {
        return entityId;
    }

    /**
     * Append additional information about entity that will be displayed in toString method
     *
     * @param string
     */
    protected void toStringInfo(Objects.ToStringHelper string) {
        //empty
    }

    @Override
    public final String toString() {
        Objects.ToStringHelper toStringHelper = toStringHelper(this).add("entityId", entityId);
        toStringInfo(toStringHelper);
        return toStringHelper.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        else if (obj == this) {
            return true;
        }
        else if (!(obj instanceof ResourceEntity)) {
            return false;
        }
        ResourceEntity other = (ResourceEntity) obj;
        return Objects.equal(entityId, other.entityId);
    }
}
