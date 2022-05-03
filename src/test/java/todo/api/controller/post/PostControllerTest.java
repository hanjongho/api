package todo.api.controller.post;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import todo.api.exception.TodoNotFoundException;
import todo.api.exception.UserNotFoundException;
import todo.api.model.Post;
import todo.api.model.User;
import todo.api.service.PostService;
import todo.api.service.UserService;

@WebMvcTest(PostController.class)
@AutoConfigureMockMvc
class PostControllerTest {

  @Autowired
  MockMvc mockMvc;

  // WebMvcTest의 경우 스프링이 뜨기 때문에 Service 빈이 필요함. 고로 Mock으로 빈을 찾아서 주입해줌
  @MockBean
  PostService mockPostService;

  @MockBean
  UserService mockUserService;

  @Test
  void 유효한_유저에_할일이_저장되면_해당_할일번호가_반환된다() throws Exception {
    //given
    String inputDto = "{\"item\":\"todo\"}";
    when(mockPostService.savePost(any(), any())).thenReturn(1L);

    //then
    String expectResult = "{\"success\":true,\"response\":1,\"error\":null}";
    mockMvc.perform(post("/users/1/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputDto))
        .andExpect(status().isCreated())
        .andExpect(content().string(is(expectResult)));
  }

  @Test
  void 유효하지않은_유저에_할일이_저장되면_예외가_반환된다() throws Exception {
    //given
    String inputDto = "{\"item\":\"todo\"}";
    when(mockPostService.savePost(any(), any())).thenThrow(
        new UserNotFoundException("User Not Found"));

    //then
    String expectResult = "{\"success\":false,\"response\":null,\"error\":{\"message\":\"User Not Found\",\"status\":404}}";
    mockMvc.perform(post("/users/3456/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputDto))
        .andExpect(status().isOk())
        .andExpect(content().string(is(expectResult)));
  }

  @Test
  void 유효한_유저에_할일을_조회하면_할일리스트가_반환된다() throws Exception {
    //given
    String inputDto = "{\"item\":\"todo\"}";
    User mockUser = new User("aaron");
    mockUser.setPosts(Arrays.asList(
        new Post("todo1", false, null),
        new Post("todo2", false, null)
    ));
    when(mockUserService.findUserById(1L)).thenReturn(mockUser);

    //then
    String expectResult = "{\"success\":true,\"response\":[{\"id\":null,\"item\":\"todo1\",\"completed\":false,\"createdDate\":null,\"modifiedDate\":null},{\"id\":null,\"item\":\"todo2\",\"completed\":false,\"createdDate\":null,\"modifiedDate\":null}],\"error\":null}";
    mockMvc.perform(get("/users/1/todos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputDto))
        .andExpect(status().isOk())
        .andExpect(content().string(is(expectResult)));
  }

  @Test
  void 유효하지않은_유저에_할일을_조회하면_예외가_반환된다() throws Exception {
    //given
    when(mockUserService.findUserById(1L)).thenThrow(new UserNotFoundException("User Not Found"));

    //then
    String expectResult = "{\"success\":false,\"response\":null,\"error\":{\"message\":\"User Not Found\",\"status\":404}}";
    mockMvc.perform(get("/users/1/todos")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(is(expectResult)));
  }

  @Test
  void 유저가_유효하면_유저의_전체_할일이_삭제된다() throws Exception {
    //when
    mockMvc.perform(delete("/users/1/todos"));

    //then
    then(mockPostService).should().deleteAll(1L);
  }

  @Test
  void 유효한_할일을_삭제하면_해당_할일이_삭제된다() throws Exception {
    //when
    mockMvc.perform(delete("/todos/1"));

    //then
    then(mockPostService).should().delete(1L);
  }

  @Test
  void 유효하지_않은_할일을_삭제하면_예외가_반환된다() throws Exception {
    //given
    doThrow(new TodoNotFoundException("Todo Not Found")).when(mockPostService).delete(1L);

    //then
    String expectResult = "{\"success\":false,\"response\":null,\"error\":{\"message\":\"Todo Not Found\",\"status\":409}}";
    mockMvc.perform(delete("/todos/1"))
        .andExpect(content().string(is(expectResult)));
  }

  @Test
  void 유효한_할일을_업데이트하면_내용이_변경된다() throws Exception {
    //given
    String inputDto = "{\"item\":\"hello\"}";

    //then
    String expectResult = "{\"success\":true,\"response\":null,\"error\":null}";
    mockMvc.perform(put("/todos/1")
            .content(inputDto)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string(is(expectResult)));

    //then
    verify(mockPostService, times(1)).update(any(), any());
  }

  @Test
  void 유효하지않은_할일을_업데이트하면_예외를_반환한다() throws Exception {
    //given
    String inputDto = "{\"item\":\"hello\"}";
    doThrow(new TodoNotFoundException("Todo Not Found")).when(mockPostService).update(any(), any());

    //then
    String expectResult = "{\"success\":false,\"response\":null,\"error\":{\"message\":\"Todo Not Found\",\"status\":409}}";

    mockMvc.perform(put("/todos/1")
            .content(inputDto)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().string(is(expectResult)));
  }
}