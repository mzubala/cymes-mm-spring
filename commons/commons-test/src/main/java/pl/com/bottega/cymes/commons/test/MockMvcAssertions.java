package pl.com.bottega.cymes.commons.test;

import lombok.SneakyThrows;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MockMvcAssertions {

    @SneakyThrows
    public static void assertSuccess(ResultActions resultActions) {
        resultActions.andExpect(status().is2xxSuccessful());
    }

    public static void assertSuccess(ResultActions... resultActions) {
        assertSuccess(Arrays.asList(resultActions));
    }

    public static void assertSuccess(Iterable<ResultActions> resultActions) {
        resultActions.forEach(MockMvcAssertions::assertSuccess);
    }

}
