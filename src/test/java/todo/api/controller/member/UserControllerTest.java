package todo.api.controller.member;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import todo.api.model.Post;
import todo.api.model.User;
import todo.api.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  UserService userService;

  @Test
  void 존재하지않은_유저로_로그인하면_빈유저가_반환된다() throws Exception {
    //given
    String inputDto = "{\"name\":\"newbie\"}";
    when(userService.login(any())).thenReturn(new User("newbie"));

    //then
    String expectResult = "{\"success\":true,\"response\":{\"createdDate\":null,\"modifiedDate\":null,\"id\":null,\"name\":\"newbie\",\"posts\":[]},\"error\":null}";
    mockMvc.perform(post("/users")
            .content(inputDto)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(is(expectResult)));
  }

  @Test
  void 존재하는_유저로_로그인하면_기존유저가_반환된다() throws Exception {
    //given
    String inputDto = "{\"name\":\"oldbie\"}";
    User oldbie = new User("oldbie");
    oldbie.setPosts(Arrays.asList(new Post("task", false, null)));
    when(userService.login(any())).thenReturn(oldbie);

    //then
    String expectResult = "{\"success\":true,\"response\":{\"createdDate\":null,\"modifiedDate\":null,\"id\":null,\"name\":\"oldbie\",\"posts\":[{\"createdDate\":null,\"modifiedDate\":null,\"id\":null,\"item\":\"task\",\"completed\":false}]},\"error\":null}";

    mockMvc.perform(post("/users")
            .content(inputDto)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(is(expectResult)));
  }

  @Test
  void 헬스체크_테스트() throws Exception {
    //given
    mockMvc.perform(get("/healthCheck"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$..['response']", not(containsString("null"))));
  }

}