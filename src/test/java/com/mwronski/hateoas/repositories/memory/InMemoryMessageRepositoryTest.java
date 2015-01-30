package com.mwronski.hateoas.repositories.memory;

import com.mwronski.hateoas.model.Message;
import com.mwronski.hateoas.model.ResourceEntity;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test cases for in-memory message repository
 *
 * @author Michal Wronski
 * @date 31-05-2014
 * @see com.mwronski.hateoas.repositories.memory.InMemoryMessageRepository
 */
public class InMemoryMessageRepositoryTest {

    private final InMemoryMessageRepository repository = new InMemoryMessageRepository();

    @Test
    public void shouldCreateMessage() {
        //given sample message
        Message newMsg = sampleMessage();
        //when creating message
        repository.create(newMsg);
        //then message has set ID
        assertThat(newMsg.getEntityId(), is(notNullValue()));
        //and message is stored in repository
        assertThat(repository.size(), is(1));
    }

    @Test
    public void shouldFindMessagesWithAllFields() {
        //given sample message is stored in repository
        Message msg = sampleMessage();
        repository.create(msg);
        assertThat(repository.size(), is(1));
        //when searching message with chosen ID and all fields
        Message foundMsg = repository.find(msg.getEntityId());
        //then message is found
        assertThat(foundMsg, is(notNullValue()));
        //and all fields in message are set
        assertThat(foundMsg.getEntityId(), is(msg.getEntityId()));
        assertThat(foundMsg.getTitle(), is(msg.getTitle()));
        assertThat(foundMsg.getContent(), is(msg.getContent()));
        assertThat(foundMsg.getSender(), is(msg.getSender()));
    }

    @Test
    public void shouldFindMessagesWithChosenFields() {
        //given sample message is stored in repository
        Message msg = sampleMessage();
        repository.create(msg);
        assertThat(repository.size(), is(1));
        //when searching message with chosen ID and chosen fields
        Message foundMsg = repository.find(msg.getEntityId(), ResourceEntity.COLUMN_ENTITY_ID, "title");
        //then message is found
        assertThat(foundMsg, is(notNullValue()));
        //and only chosen fields are set
        assertThat(foundMsg.getEntityId(), is(msg.getEntityId()));
        assertThat(foundMsg.getTitle(), is(msg.getTitle()));
        assertThat(foundMsg.getContent(), is(nullValue()));
        assertThat(foundMsg.getSender(), is(nullValue()));
    }

    @Test
    public void shouldGetMessagesWithAllFields() {
        //given sample message is stored in repository
        Message msg = sampleMessage();
        repository.create(msg);
        assertThat(repository.size(), is(1));
        //when getting messages with all fields
        List<Message> foundMsgs = repository.get(0, 10);
        //then messages are found
        assertThat(foundMsgs, is(notNullValue()));
        assertThat(foundMsgs.size(), is(1));
        //and messages have all fields set
        Message firstMsg = foundMsgs.get(0);
        assertThat(firstMsg.getEntityId(), is(msg.getEntityId()));
        assertThat(firstMsg.getTitle(), is(msg.getTitle()));
        assertThat(firstMsg.getContent(), is(msg.getContent()));
        assertThat(firstMsg.getSender(), is(msg.getSender()));
    }

    @Test
    public void shouldGetMessagesWithChosenFields() {
        //given sample message is stored in repository
        Message msg = sampleMessage();
        repository.create(msg);
        assertThat(repository.size(), is(1));
        //when getting messages with chosen fields
        List<Message> foundMsgs = repository.get(0, 10, ResourceEntity.COLUMN_ENTITY_ID, "content");
        //then messages are found
        assertThat(foundMsgs, is(notNullValue()));
        assertThat(foundMsgs.size(), is(1));
        //and messages have set only chosen fields
        Message firstMsg = foundMsgs.get(0);
        assertThat(firstMsg.getEntityId(), is(msg.getEntityId()));
        assertThat(firstMsg.getTitle(), is(nullValue()));
        assertThat(firstMsg.getContent(), is(msg.getContent()));
        assertThat(firstMsg.getSender(), is(nullValue()));
    }

    /**
     * Create message with sample fields
     *
     * @return non-nullable instance of message
     */
    private Message sampleMessage() {
        Message sample = new Message();
        sample.setTitle("Title666");
        sample.setContent("Test");
        sample.setSender("10.11.12.13/sender");
        return sample;
    }
}
