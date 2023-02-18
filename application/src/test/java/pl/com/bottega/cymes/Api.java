package pl.com.bottega.cymes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.com.bottega.cymes.commons.test.PageTestImpl;
import pl.com.bottega.cymes.movies.dto.StarDto;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
abstract class Api {

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    @SneakyThrows
    protected ResultActions post(String uriTemplate, Object body, Object... uriVariables) {
        return mockMvc.perform(MockMvcRequestBuilders.post(uriTemplate, uriVariables).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body)));
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(String uriTemplate, Class<T> elementClass, Object... uriVariables) {
        return getList(get(uriTemplate, uriVariables), elementClass);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(RequestBuilder requestBuilder, Class<T> elementClass) {
        var content = mockMvc.perform(requestBuilder).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass));
    }

    @SneakyThrows
    protected <T> T getObject(String uriTemplate, Class<T> objClass, Object... uriVariables) {
        var content = mockMvc.perform(get(uriTemplate, uriVariables)).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, objClass);
    }

    @SneakyThrows
    protected <T> PageTestImpl<T> getPage(String uriTemplate, Class<T> pageElementClass, Object... uriVariables) {
        var content = mockMvc.perform(get(uriTemplate, uriVariables)).andExpect(status().is2xxSuccessful()).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, objectMapper.getTypeFactory().constructParametricType(PageTestImpl.class, pageElementClass));
    }
}
