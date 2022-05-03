package todo.api.controller.post;

import static java.util.stream.Collectors.toList;
import static todo.api.controller.ApiResult.OK;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import todo.api.controller.ApiResult;
import todo.api.controller.post.PostResponseDto.Builder;
import todo.api.model.User;
import todo.api.service.PostService;
import todo.api.service.UserService;

@RestController
public class PostController {

  private UserService userService;
  private PostService postService;

  public PostController(UserService userService, PostService postService) {
    this.userService = userService;
    this.postService = postService;
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/users/{id}/todos")
  public ApiResult savePost(@PathVariable("id") Long userId,
      @RequestBody PostRequestDto postRequestDto) {
    User user = userService.findUserById(userId);

    Long postId = postService.savePost(postRequestDto, user);

    return OK(postId);
  }

  @GetMapping("/users/{id}/todos")
  public ApiResult<List<PostResponseDto>> getUserPost(@PathVariable("id") Long userId) {
    User user = userService.findUserById(userId);

    return OK(user.getPosts().stream().map(post -> new Builder(post).build()).collect(toList()));
  }

  @DeleteMapping("/users/{id}/todos")
  public ApiResult deleteUserAllPost(@PathVariable("id") Long userId) {
    postService.deleteAll(userId);

    return OK();
  }

  @DeleteMapping("/todos/{id}")
  public ApiResult deleteOnePost(@PathVariable("id") Long postId) {
    postService.delete(postId);

    return OK();
  }

  // DTO 서비스로 이전
  @PutMapping("/todos/{id}")
  public ApiResult updateTodo(@PathVariable("id") Long postId,
      @RequestBody PostRequestDto postRequestDto) {
    postService.update(postId, postRequestDto);

    return OK();
  }
}
