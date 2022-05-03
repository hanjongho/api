package todo.api.controller.post;

import java.time.LocalDateTime;
import lombok.Getter;
import todo.api.model.Post;

@Getter
public class PostResponseDto {

  private Long id;

  private String item;

  private boolean completed;

  private LocalDateTime createdDate;

  private LocalDateTime modifiedDate;

  public PostResponseDto(Long id, String item, boolean completed, LocalDateTime createdDate,
      LocalDateTime modifiedDate) {
    this.id = id;
    this.item = item;
    this.completed = completed;
    this.createdDate = createdDate;
    this.modifiedDate = modifiedDate;
  }

  static public class Builder {

    private Long id;

    private String item;

    private boolean completed;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    public Builder(Post post) {
      this.id = post.getId();
      this.item = post.getItem();
      this.completed = post.isCompleted();
      this.createdDate = post.getCreatedDate();
      this.modifiedDate = post.getModifiedDate();
    }

    public PostResponseDto build() {
      return new PostResponseDto(id, item, completed, createdDate, modifiedDate);
    }
  }
}
