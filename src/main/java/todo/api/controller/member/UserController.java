package todo.api.controller.member;

import static todo.api.controller.ApiResult.OK;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import todo.api.controller.ApiResult;
import todo.api.model.User;
import todo.api.service.UserService;

@RestController
public class UserController {

  private UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/users")
  public ApiResult<User> login(@RequestBody UserRequestDto userRequestDto) {
    return OK(userService.login(userRequestDto));
  }

  @GetMapping("/healthCheck")
  public ApiResult<Long> healthCheck() {
    return OK(System.currentTimeMillis());
  }

}
