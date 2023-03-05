package pl.com.bottega.cymes;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import pl.com.bottega.cymes.commons.test.PageTestImpl;
import pl.com.bottega.cymes.users.requests.LoginRequest;
import pl.com.bottega.cymes.users.requests.LoginResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RequiredArgsConstructor
abstract class Api {

    private static String authToken;

    protected final MockMvc mockMvc;

    protected final ObjectMapper objectMapper;

    void logIn(String email, String password) {
        authToken = post("/login", new LoginRequest(email, password)).getObject(LoginResponse.class).token();
    }

    void logOut() {
        authToken = null;
    }

    @SneakyThrows
    protected ExtendedResultActions post(String uriTemplate, Object body, Object... uriVariables) {
        return new ExtendedResultActions(
            mockMvc.perform(
                withAuth(MockMvcRequestBuilders
                    .post(uriTemplate, uriVariables)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(body))
                )
            )
        );
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(String uriTemplate, Class<T> elementClass, Object... uriVariables) {
        return getList(get(uriTemplate, uriVariables), elementClass);
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    protected <T> List<T> getList(MockHttpServletRequestBuilder requestBuilder, Class<T> elementClass) {
        return new ExtendedResultActions(mockMvc.perform(withAuth(requestBuilder))).getList(elementClass);
    }

    @SneakyThrows
    protected <T> T getObject(String uriTemplate, Class<T> objClass, Object... uriVariables) {
        return new ExtendedResultActions(mockMvc.perform(withAuth(get(uriTemplate, uriVariables)))).getObject(objClass);
    }

    @SneakyThrows
    protected <T> PageTestImpl<T> getPage(String uriTemplate, Class<T> pageElementClass, Object... uriVariables) {
        return new ExtendedResultActions(mockMvc.perform(withAuth(get(uriTemplate, uriVariables)))).getPage(pageElementClass);
    }

    private MockHttpServletRequestBuilder withAuth(MockHttpServletRequestBuilder builder) {
        if (authToken == null) {
            return builder;
        } else {
            return builder.header("Authorization", "Bearer " + authToken);
        }
    }

    @RequiredArgsConstructor
    class ExtendedResultActions implements ResultActions {

        private final ResultActions decorated;

        @Override
        public ExtendedResultActions andExpect(ResultMatcher matcher) throws Exception {
            return new ExtendedResultActions(decorated.andExpect(matcher));
        }

        @Override
        public ExtendedResultActions andExpectAll(ResultMatcher... matchers) throws Exception {
            return new ExtendedResultActions(decorated.andExpectAll(matchers));
        }

        @Override
        public ExtendedResultActions andDo(ResultHandler handler) throws Exception {
            return new ExtendedResultActions(decorated.andDo(handler));
        }

        @Override
        public ExtendedMvcResult andReturn() {
            return new ExtendedMvcResult(decorated.andReturn());
        }

        @SneakyThrows
        <T> T getObject(Class<T> klass) {
            return andExpect(status().is2xxSuccessful()).andReturn().getObject(klass);
        }

        @SneakyThrows
        <T> List<T> getList(Class<T> elementClass) {
            return andExpect(status().is2xxSuccessful()).andReturn().getList(elementClass);
        }

        @SneakyThrows
        <T> PageTestImpl<T> getPage(Class<T> pageElementClass) {
            return andExpect(status().is2xxSuccessful()).andReturn().getPage(pageElementClass);
        }
    }

    @RequiredArgsConstructor
    class ExtendedMvcResult implements MvcResult {
        private final MvcResult decorated;

        <T> T getObject(Class<T> klass) {
            return getResponse().getObject(klass);
        }

        <T> List<T> getList(Class<T> elementClass) {
            return getResponse().getList(elementClass);
        }

        <T> PageTestImpl<T> getPage(Class<T> pageElementClass) {
            return getResponse().getPage(pageElementClass);
        }

        @Override
        public MockHttpServletRequest getRequest() {
            return decorated.getRequest();
        }

        @Override
        public ExtendedMockHttpServletResponse getResponse() {
            return new ExtendedMockHttpServletResponse(decorated.getResponse());
        }

        @Override
        @Nullable
        public Object getHandler() {
            return decorated.getHandler();
        }

        @Override
        @Nullable
        public HandlerInterceptor[] getInterceptors() {
            return decorated.getInterceptors();
        }

        @Override
        @Nullable
        public ModelAndView getModelAndView() {
            return decorated.getModelAndView();
        }

        @Override
        @Nullable
        public Exception getResolvedException() {
            return decorated.getResolvedException();
        }

        @Override
        public FlashMap getFlashMap() {
            return decorated.getFlashMap();
        }

        @Override
        public Object getAsyncResult() {
            return decorated.getAsyncResult();
        }

        @Override
        public Object getAsyncResult(long timeToWait) {
            return decorated.getAsyncResult(timeToWait);
        }
    }

    @RequiredArgsConstructor
    class ExtendedMockHttpServletResponse extends MockHttpServletResponse {

        private final MockHttpServletResponse decorated;

        @SneakyThrows
        <T> T getObject(Class<T> klass) {
            var content = decorated.getContentAsString();
            return objectMapper.readValue(content, klass);
        }

        @SneakyThrows
        <T> List<T> getList(Class<T> elementClass) {
            var content = decorated.getContentAsString();
            return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, elementClass));
        }

        @SneakyThrows
        <T> PageTestImpl<T> getPage(Class<T> pageElementClass) {
            return objectMapper.readValue(getContentAsString(), objectMapper.getTypeFactory().constructParametricType(PageTestImpl.class, pageElementClass));
        }

        @Override
        public void setOutputStreamAccessAllowed(boolean outputStreamAccessAllowed) {
            decorated.setOutputStreamAccessAllowed(outputStreamAccessAllowed);
        }

        @Override
        public boolean isOutputStreamAccessAllowed() {
            return decorated.isOutputStreamAccessAllowed();
        }

        @Override
        public void setWriterAccessAllowed(boolean writerAccessAllowed) {
            decorated.setWriterAccessAllowed(writerAccessAllowed);
        }

        @Override
        public boolean isWriterAccessAllowed() {
            return decorated.isWriterAccessAllowed();
        }

        @Override
        public void setDefaultCharacterEncoding(String characterEncoding) {
            decorated.setDefaultCharacterEncoding(characterEncoding);
        }

        @Override
        public boolean isCharset() {
            return decorated.isCharset();
        }

        @Override
        public void setCharacterEncoding(String characterEncoding) {
            decorated.setCharacterEncoding(characterEncoding);
        }

        @Override
        public String getCharacterEncoding() {
            return decorated.getCharacterEncoding();
        }

        @Override
        public ServletOutputStream getOutputStream() {
            return decorated.getOutputStream();
        }

        @Override
        public PrintWriter getWriter() throws UnsupportedEncodingException {
            return decorated.getWriter();
        }

        @Override
        public byte[] getContentAsByteArray() {
            return decorated.getContentAsByteArray();
        }

        @Override
        public String getContentAsString() throws UnsupportedEncodingException {
            return decorated.getContentAsString();
        }

        @Override
        public String getContentAsString(Charset fallbackCharset) throws UnsupportedEncodingException {
            return decorated.getContentAsString(fallbackCharset);
        }

        @Override
        public void setContentLength(int contentLength) {
            decorated.setContentLength(contentLength);
        }

        @Override
        public int getContentLength() {
            return decorated.getContentLength();
        }

        @Override
        public void setContentLengthLong(long contentLength) {
            decorated.setContentLengthLong(contentLength);
        }

        @Override
        public long getContentLengthLong() {
            return decorated.getContentLengthLong();
        }

        @Override
        public void setContentType(String contentType) {
            decorated.setContentType(contentType);
        }

        @Override
        @Nullable
        public String getContentType() {
            return decorated.getContentType();
        }

        @Override
        public void setBufferSize(int bufferSize) {
            decorated.setBufferSize(bufferSize);
        }

        @Override
        public int getBufferSize() {
            return decorated.getBufferSize();
        }

        @Override
        public void flushBuffer() {
            decorated.flushBuffer();
        }

        @Override
        public void resetBuffer() {
            decorated.resetBuffer();
        }

        @Override
        public void setCommitted(boolean committed) {
            decorated.setCommitted(committed);
        }

        @Override
        public boolean isCommitted() {
            return decorated.isCommitted();
        }

        @Override
        public void reset() {
            decorated.reset();
        }

        @Override
        public void setLocale(Locale locale) {
            decorated.setLocale(locale);
        }

        @Override
        public Locale getLocale() {
            return decorated.getLocale();
        }

        @Override
        public void addCookie(Cookie cookie) {
            decorated.addCookie(cookie);
        }

        @Override
        public Cookie[] getCookies() {
            return decorated.getCookies();
        }

        @Override
        @Nullable
        public Cookie getCookie(String name) {
            return decorated.getCookie(name);
        }

        @Override
        public boolean containsHeader(String name) {
            return decorated.containsHeader(name);
        }

        @Override
        public Collection<String> getHeaderNames() {
            return decorated.getHeaderNames();
        }

        @Override
        @Nullable
        public String getHeader(String name) {
            return decorated.getHeader(name);
        }

        @Override
        public List<String> getHeaders(String name) {
            return decorated.getHeaders(name);
        }

        @Override
        @Nullable
        public Object getHeaderValue(String name) {
            return decorated.getHeaderValue(name);
        }

        @Override
        public List<Object> getHeaderValues(String name) {
            return decorated.getHeaderValues(name);
        }

        @Override
        public String encodeURL(String url) {
            return decorated.encodeURL(url);
        }

        @Override
        public String encodeRedirectURL(String url) {
            return decorated.encodeRedirectURL(url);
        }

        @Override
        public void sendError(int status, String errorMessage) throws IOException {
            decorated.sendError(status, errorMessage);
        }

        @Override
        public void sendError(int status) throws IOException {
            decorated.sendError(status);
        }

        @Override
        public void sendRedirect(String url) throws IOException {
            decorated.sendRedirect(url);
        }

        @Override
        @Nullable
        public String getRedirectedUrl() {
            return decorated.getRedirectedUrl();
        }

        @Override
        public void setDateHeader(String name, long value) {
            decorated.setDateHeader(name, value);
        }

        @Override
        public void addDateHeader(String name, long value) {
            decorated.addDateHeader(name, value);
        }

        @Override
        public long getDateHeader(String name) {
            return decorated.getDateHeader(name);
        }

        @Override
        public void setHeader(String name, String value) {
            decorated.setHeader(name, value);
        }

        @Override
        public void addHeader(String name, String value) {
            decorated.addHeader(name, value);
        }

        @Override
        public void setIntHeader(String name, int value) {
            decorated.setIntHeader(name, value);
        }

        @Override
        public void addIntHeader(String name, int value) {
            decorated.addIntHeader(name, value);
        }

        @Override
        public void setStatus(int status) {
            decorated.setStatus(status);
        }

        @Override
        public int getStatus() {
            return decorated.getStatus();
        }

        @Override
        @Nullable
        public String getErrorMessage() {
            return decorated.getErrorMessage();
        }

        @Override
        public void setForwardedUrl(String forwardedUrl) {
            decorated.setForwardedUrl(forwardedUrl);
        }

        @Override
        @Nullable
        public String getForwardedUrl() {
            return decorated.getForwardedUrl();
        }

        @Override
        public void setIncludedUrl(String includedUrl) {
            decorated.setIncludedUrl(includedUrl);
        }

        @Override
        @Nullable
        public String getIncludedUrl() {
            return decorated.getIncludedUrl();
        }

        @Override
        public void addIncludedUrl(String includedUrl) {
            decorated.addIncludedUrl(includedUrl);
        }

        @Override
        public List<String> getIncludedUrls() {
            return decorated.getIncludedUrls();
        }

        @Override
        public void setTrailerFields(Supplier<Map<String, String>> supplier) {
            decorated.setTrailerFields(supplier);
        }

        @Override
        public Supplier<Map<String, String>> getTrailerFields() {
            return decorated.getTrailerFields();
        }
    }
}