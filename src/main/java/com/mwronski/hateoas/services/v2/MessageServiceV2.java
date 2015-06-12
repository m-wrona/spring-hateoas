package com.mwronski.hateoas.services.v2;

import com.google.common.collect.Sets;
import com.mwronski.hateoas.model.Message;
import com.mwronski.hateoas.model.ResourceEntity;
import com.mwronski.hateoas.model.Resources;
import com.mwronski.hateoas.repositories.Repository;
import com.mwronski.hateoas.services.ReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.mwronski.hateoas.log.Tracer.tracer;
import static com.mwronski.hateoas.model.builder.Builders.message;
import static com.mwronski.hateoas.services.ServiceUtils.normalize;
import static com.mwronski.hateoas.services.ServiceUtils.pageToIndex;

/**
 * Service manages messages kept in the application. <br/>
 * It's an extension of version 1.0 that was created to support more features
 * related with messages and keep backward compatibility at the same time.<br/>
 * Service provides access to all fields of messages. <br/>
 * Moreover services allows to decide which fields should be returned while getting list of message (pageable access). <br/>
 * Note: class cannot be final because of controller link builder
 *
 * @author Michal Wronski
 * @version 2.0
 * @date 27-05-2014
 */
@RequestMapping(value = "/", produces = "application/vnd.messages-v2+*",
        headers = {"Accept=application/vnd.messages-v2+json", "Accept=application/vnd.messages-v2+xml"})
@RestController
public class MessageServiceV2 extends ReadService<Message> {

    @Autowired
    private Repository<Message> messageRepository;

    /**
     * Create message
     *
     * @param request with address of the sender
     * @param title   title of new message
     * @param content content of new message
     * @return ID of created message
     */
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<Message> create(HttpServletRequest request, @RequestParam String title, @RequestParam String content) {
        tracer(this).info("Creating new message - title: %s, content: %s", title, content);
        Message msg = message()
                .withTitle(title)
                .withContent(content)
                .withSender(request.getRemoteAddr())
                .build();
        msg = messageRepository.create(msg);
        Message msgId = getRepository().find(msg.getEntityId(), ResourceEntity.COLUMN_ENTITY_ID);
        addSelfLink(msgId);
        return new ResponseEntity<Message>(msgId, HttpStatus.CREATED);
    }

    @Override
    protected List<Message> findElements(int startIndex, int count, String... includeFields) {
        System.out.println(Arrays.toString(includeFields));
        Set<String> columns = includeFields != null ? Sets.newHashSet(includeFields) : new HashSet<String>();
        columns.add(ResourceEntity.COLUMN_ENTITY_ID); //ID always must be included
        tracer(this).debug("Filter - included columns: %s", columns);
        return messageRepository.get(startIndex, DEFAULT_PAGE_SIZE, columns.toArray(new String[columns.size()]));
    }

    @Override
    protected final Repository<Message> getRepository() {
        return messageRepository;
    }

}
