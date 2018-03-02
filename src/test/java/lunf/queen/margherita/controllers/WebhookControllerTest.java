package lunf.queen.margherita.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Value("${messenger4j.verifyToken}")
    private String verifyToken;

    private String invalidVerifyToken = "random-not-true-value";

    private String mode = "subscribe";

    @Value("${messenger4j.appSecret}")
    private String appSecret;

    //hub.verify_token=<YOUR_VERIFY_TOKEN>&hub.challenge=CHALLENGE_ACCEPTED&hub.mode=subscribe


    @Test
    public void testFailValidateHook() throws Exception {

        this.mockMvc.perform(get("/webhook")).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidParamValidateHook() throws Exception {

        // Invalid verify token
        String params = String.format("?hub.verify_token=%s&hub.challenge=%s&hub.mode=%s",
                this.invalidVerifyToken, this.appSecret, this.mode);

        String url = String.format("/webhook%s", params);

        this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isForbidden());
    }

    @Test
    public void testValidParamValidateHook() throws Exception {
        String params = String.format("?hub.verify_token=%s&hub.challenge=%s&hub.mode=%s",
                this.verifyToken, this.appSecret, this.mode);

        String url = String.format("/webhook%s", params);

        this.mockMvc.perform(get(url)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString(this.appSecret)));
    }
}
