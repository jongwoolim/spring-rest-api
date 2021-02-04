package me.whiteship.demorestapi.index;

import me.whiteship.demorestapi.common.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class IndexControllerTest  extends BaseControllerTest {

    @Test
    public void index() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());

    }
}
