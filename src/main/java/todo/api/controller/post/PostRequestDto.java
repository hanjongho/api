package todo.api.controller.post;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import todo.api.model.User;

@Getter
@Setter
public class PostRequestDto {

  private String item;

  private boolean completed;

  private LocalDateTime createdDate;

  private LocalDateTime modifiedDate;

  private User user;

}
