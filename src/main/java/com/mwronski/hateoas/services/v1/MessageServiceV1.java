package com.mwronski.hateoas.services.v1;

import com.google.common.collect.Sets;
import com.mwronski.hateoas.model.Message;
import com.mwronski.hateoas.model.ResourceEntity;
import com.mwronski.hateoas.repositories.Repository;
import com.mwronski.hateoas.services.ReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

import static com.mwronski.hateoas.log.Tracer.tracer;
import static com.mwronski.hateoas.model.builder.Builders.message;

/**
 * Service manages messages kept in the application. <br/>
 * Service provides access only to title of messages (besides message ID). <br/>
 * Rest of fields are not supported by this version of service. <br/>
 * Note: class cannot be final because of controller link builder
 *
 * @author Michal Wronski
 * @version 1.0
 * @date 27-05-2014
 */
@RequestMapping(value = "/messages", produces = "application/vnd.messages-v1+*",
        headers = {"Accept=application/vnd.messages-v1+json", "Accept=application/vnd.messages-v1+xml"})
@RestController
public class MessageServiceV1 extends ReadService<Message> {

    private static final String[] DISPLAYED_COLUMNS = {ResourceEntity.COLUMN_ENTITY_ID, "title"};

    @Autowired
    private Repository<Message> messageRepository;

    /**
     * Create message
     *
     * @param request with address of the sender
     * @param title title of new message
     * @return ID of created message
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public HttpEntity<Message> create(HttpServletRequest request, @RequestParam String title) {
        tracer(this).info("Creating new message - title: %s", title);
        Message msg = message().withTitle(title).withSender(request.getRemoteAddr()).build();
        msg = messageRepository.create(msg);
        Message msgId = messageRepository.find(msg.getEntityId(), ResourceEntity.COLUMN_ENTITY_ID);
        addSelfLink(msgId);
        return new ResponseEntity<Message>(msgId, HttpStatus.CREATED);
    }

    @Override
    protected final Repository<Message> getRepository() {
        //wrap message repository so proper fields are filtered
        return new Repository<Message>() {
            @Override
            public Message create(Message entity) {
                return messageRepository.create(entity);
            }

            @Override
            public List<Message> get(int start, int rowCount, String... columns) {
                return messageRepository.get(start, rowCount, getDisplayedColumns(columns));
            }

            @Override
            public int size() {
                return messageRepository.size();
            }

            @Override
            public Message find(String id, String... columns) {
                return messageRepository.find(id, getDisplayedColumns(columns));
            }
        };
    }

    /**
     * Get all columns that should be displayed by this service
     *
     * @param columns columns to be displayed required by 3rd parties
     * @return non-nullable array with name of columns to be displayed
     */
    private String[] getDisplayedColumns(String... columns) {
        Set<String> allColumns = Sets.newHashSet(DISPLAYED_COLUMNS);
        allColumns.addAll(Sets.newHashSet(columns));
        return allColumns.toArray(new String[allColumns.size()]);
    }
}
