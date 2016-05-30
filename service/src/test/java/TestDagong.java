import com.alibaba.dubbo.config.annotation.Reference;
import com.dagong.mapper.UserMapper;
import com.dagong.pojo.Company;
import com.dagong.pojo.JobType;
import com.dagong.user.service.UserClientImpl;
import com.dagong.user.vo.UserVO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuchang on 16/1/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:base/all.xml")
public class TestDagong {
    private MockMvc mockMvc;
    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserClientImpl userClient;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void test() {
        Map<String, Object> userInfo = userClient.getUserInfo("1");
        System.out.println("userInfo = " + userInfo);
        UserVO userVO = userClient.getUserByUserId("1");
        System.out.println("userVO = " + userVO);
    }

//    @Test
//    public void testSearchJob() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/job/forUser.do")
//                .cookie(new Cookie("user", "123")))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print()).andReturn();
//
//    }


}
