package com.mwronski.hateoas.services.v2;

import com.mwronski.hateoas.RestApplicationTest;
import com.mwronski.hateoas.model.Message;
import com.mwronski.hateoas.model.ResourceEntity;
import com.mwronski.hateoas.repositories.Repository;
import com.mwronski.hateoas.services.ReadServiceTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test cases for message service in version 2.x
 *
 * @author Michal Wronski
 * @version 1.0
 * @date 31-05-2014
 * @see com.mwronski.hateoas.services.v2.MessageServiceV2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestApplicationTest.class})
@WebAppConfiguration
public class MessageServiceV2Test extends ReadServiceTest<Message> {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webAppContext;
    @Autowired
    @Qualifier("messageRepository")
    private Repository<Message> mockRepository;


    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void shouldFindElementWithProperFieldsReturned() throws Exception {
        //given element can be found in repository
        Message message = sampleElement();
        when(mockRepository().find(eq(sampleElement().getEntityId()), (String[]) anyVararg())).thenReturn(message);
        //when searching element with chosen ID using service in chosen version
        ResultActions result = mockMvc().perform(get("/messages/" + message.getEntityId()).accept(acceptVndVersion()).header("Accept", acceptVndVersion()));
        //then response is accepted
        result.andExpect(status().is(HttpStatus.OK.value()));
        //and response is in proper VND and version
        result.andExpect(content().contentType(acceptVndVersion()));
        //and elements have proper fields set
        result.andExpect(jsonPath("$.entityId").value("123-v2"));
        result.andExpect(jsonPath("$.title").value("Title-v2"));
        result.andExpect(jsonPath("$.content").value("Message-v2"));
        result.andExpect(jsonPath("$.sender").value("localhost/v2"));
        //and self link is set properly
        result.andExpect(jsonPath("$.links[0].rel").value("self"));
        result.andExpect(jsonPath("$.links[0].href").value("http://localhost/messages/" + message.getEntityId()));
        //and data is taken from repository
        verify(mockRepository(), atLeastOnce()).find(eq(sampleElement().getEntityId()), (String[]) anyVararg());
        verifyNoMoreInteractions(mockRepository());
    }

    @Test
    public void shouldGetListOfElementsWithProperFieldsReturned() throws Exception {
        //given one element can be found in repository
        Message message = sampleElement();
        List<Message> elements = new ArrayList<Message>();
        elements.add(message);
        when(mockRepository().get(eq(0), eq(10), (String[]) anyVararg())).thenReturn(elements);
        when(mockRepository().size()).thenReturn(elements.size());
        //when getting first page of elements using service in chosen version
        ResultActions result = mockMvc().perform(get("/messages/list/1").accept(acceptVndVersion()));
        //then response is accepted
        result.andExpect(status().is(HttpStatus.OK.value()));
        //and response is in proper VND and version
        result.andExpect(content().contentType(acceptVndVersion()));
        //and elements have proper fields set
        result.andExpect(jsonPath("$.resources[0].entityId").value("123-v2"));
        result.andExpect(jsonPath("$.resources[0].title").value("Title-v2"));
        result.andExpect(jsonPath("$.resources[0].content").value("Message-v2"));
        result.andExpect(jsonPath("$.resources[0].sender").value("localhost/v2"));
        //and self link of message is set properly
        result.andExpect(jsonPath("$.resources[0].links[0].rel").value("self"));
        result.andExpect(jsonPath("$.resources[0].links[0].href").value("http://localhost/messages/" + message.getEntityId()));
        //and links for paging are set properly
        result.andExpect(jsonPath("$.links[0].rel").value("self"));
        result.andExpect(jsonPath("$.links[0].href").value("http://localhost/messages/list/1"));
        //and data is taken from repository
        verify(mockRepository(), atLeastOnce()).get(eq(0), eq(10), (String[]) anyVararg());
        verify(mockRepository(), atLeastOnce()).size();
        verifyNoMoreInteractions(mockRepository());
    }

    @Test
    public void shouldFilterMessagesWithChosenFields() throws Exception {
        //given one element can be found in repository
        Message message = sampleElement();
        message.setTitle(null);
        message.setSender(null);
        List<Message> elements = new ArrayList<Message>();
        elements.add(message);
        when(mockRepository().get(eq(0), eq(10), (String[]) anyVararg())).thenReturn(elements);
        when(mockRepository().size()).thenReturn(elements.size());
        //when getting first page of filtered elements using service in chosen version
        ResultActions result = mockMvc().perform(get("/messages/filterList/1").accept(acceptVndVersion()).param("includeFields", "content"));
        //then response is accepted
        result.andExpect(status().is(HttpStatus.OK.value()));
        //and response is in proper VND and version
        result.andExpect(content().contentType(acceptVndVersion()));
        //and elements have proper fields set
        result.andExpect(jsonPath("$.resources[0].entityId").value("123-v2"));
        result.andExpect(jsonPath("$.resources[0].title").doesNotExist());
        result.andExpect(jsonPath("$.resources[0].content").value("Message-v2"));
        result.andExpect(jsonPath("$.resources[0].sender").doesNotExist());
        //and self link of message is set properly
        result.andExpect(jsonPath("$.resources[0].links[0].rel").value("self"));
        result.andExpect(jsonPath("$.resources[0].links[0].href").value("http://localhost/messages/" + message.getEntityId()));
        //and links for paging are set properly
        result.andExpect(jsonPath("$.links[0].rel").value("self"));
        result.andExpect(jsonPath("$.links[0].href").value("http://localhost/messages/list/1"));
        //and data is taken from repository
        verify(mockRepository(), atLeastOnce()).get(eq(0), eq(10), (String[]) anyVararg());
        verify(mockRepository(), atLeastOnce()).size();
        verifyNoMoreInteractions(mockRepository());
    }

    @Test
    public void shouldCreateMessage() throws Exception {
        //given message that will be created
        Message message = sampleElement();
        when(mockRepository().create(any(Message.class))).thenReturn(message);
        Message msgId = new Message();
        msgId.setEntityId(message.getEntityId());
        when(mockRepository.find(message.getEntityId(), ResourceEntity.COLUMN_ENTITY_ID)).thenReturn(msgId);
        //when creating message using service in chosen version
        ResultActions result = mockMvc().perform(post("/messages/create").accept(acceptVndVersion())
                .param("title", message.getTitle()).param("content", message.getContent()));
        //then message is created
        result.andExpect(status().is(HttpStatus.CREATED.value()));
        //and response is in proper VND and version
        result.andExpect(content().contentType(acceptVndVersion()));
        //and response has proper fields set
        result.andExpect(jsonPath("$.entityId").value("123-v2"));
        result.andExpect(jsonPath("$.title").doesNotExist());
        result.andExpect(jsonPath("$.content").doesNotExist());
        result.andExpect(jsonPath("$.sender").doesNotExist());
        //and self link of created message is set properly
        result.andExpect(jsonPath("$.links[0].rel").value("self"));
        result.andExpect(jsonPath("$.links[0].href").value("http://localhost/messages/" + message.getEntityId()));
        //and data is created using repository
        verify(mockRepository(), atLeastOnce()).create(any(Message.class));
        verify(mockRepository(), atLeastOnce()).find(message.getEntityId(), ResourceEntity.COLUMN_ENTITY_ID);
        verifyNoMoreInteractions(mockRepository());
    }

    @Override
    protected Repository<Message> mockRepository() {
        return mockRepository;
    }

    @Override
    protected MockMvc mockMvc() {
        return mockMvc;
    }

    @Override
    protected String acceptVndVersion() {
        return "application/vnd.messages-v2+json";
    }

    @Override
    protected Message sampleElement() {
        Message sample = new Message();
        sample.setEntityId("123-v2");
        sample.setTitle("Title-v2");
        sample.setContent("Message-v2");
        sample.setSender("localhost/v2");
        return sample;
    }
}
