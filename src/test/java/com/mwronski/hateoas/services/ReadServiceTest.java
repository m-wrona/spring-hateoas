package com.mwronski.hateoas.services;

import com.mwronski.hateoas.model.ResourceEntity;
import com.mwronski.hateoas.repositories.Repository;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test cases for generic functionality of read service. <br/>
 * Test are based on mock MVC provided by Spring which are slow but allows to check services and their API from client's perspective. <br/>
 * Rest service related tests should be placed normally in separate package for slower tests, for instance: integration tests. <br/>
 * Unit tests should check API of services directly using mock approach.<br/>
 *
 * @param <T> type of entity supported by service
 * @author Michal Wronski
 * @date 31-05-2014
 * @see com.mwronski.hateoas.services.ReadService
 */
public abstract class ReadServiceTest<T extends ResourceEntity> {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );

    /**
     * Get mock of MVC
     *
     * @return non-nullable instance
     */
    protected abstract MockMvc mockMvc();

    /**
     * Get repository that mocks access to the data
     *
     * @return non-nullable mock of repository
     */
    protected abstract Repository<T> mockRepository();

    /**
     * Get accepted VND in proper version
     *
     * @return non-nullable string
     */
    protected abstract String acceptVndVersion();

    /**
     * Create element with sample fields
     *
     * @return non-nullable instance of element
     */
    protected abstract T sampleElement();

    @Test
    public void shouldFindElement() throws Exception {
        //given element can be found in repository
        T element = sampleElement();
        when(mockRepository().find(eq(sampleElement().getEntityId()), (String[]) anyVararg())).thenReturn(element);
        //when searching element with chosen ID using service in chosen version
        ResultActions result = mockMvc().perform(get("/messages/" + element.getEntityId()).accept(acceptVndVersion()));
        //then response is accepted
        result.andExpect(status().is(HttpStatus.OK.value()));
        //and response is in proper VND and version
        result.andExpect(content().contentType(acceptVndVersion()));
        //and data is taken from repository
        verify(mockRepository(), atLeastOnce()).find(eq(sampleElement().getEntityId()), (String[]) anyVararg());
        verifyNoMoreInteractions(mockRepository());
    }

    @Test
    public void shouldGetListOfElements() throws Exception {
        //given one element can be found in repository
        T element = sampleElement();
        List<T> elements = new ArrayList<T>();
        elements.add(element);
        when(mockRepository().get(eq(0), eq(ReadService.DEFAULT_PAGE_SIZE), (String[]) anyVararg())).thenReturn(elements);
        when(mockRepository().size()).thenReturn(elements.size());
        //when getting first page of elements using service in chosen version
        ResultActions result = mockMvc().perform(get("/messages/list/1").accept(acceptVndVersion()));
        //then response is accepted
        result.andExpect(status().is(HttpStatus.OK.value()));
        //and response is in proper VND and version
        result.andExpect(content().contentType(acceptVndVersion()));
        //and data is taken from repository
        verify(mockRepository(), atLeastOnce()).get(eq(0), eq(ReadService.DEFAULT_PAGE_SIZE), (String[]) anyVararg());
        verify(mockRepository(), atLeastOnce()).size();
        verifyNoMoreInteractions(mockRepository());
    }

}
