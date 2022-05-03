package todo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;
import todo.api.controller.post.PostRequestDto;

@Data
@Entity
@ToString(exclude = "user")
public class Post extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 2)
  private String item;

  private boolean completed;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public Post() {
  }

  public Post(String item, boolean completed) {

    this.item = item;
    this.completed = completed;
  }

  public void setUser(User user) {
    this.user = user;
    user.getPosts().add(this);
  }

  public Post(String item, boolean completed, User user) {
    this.item = item;
    this.completed = completed;
    this.user = user;
  }

  static public class Builder {

    private String item;

    private boolean completed;

    private User user;

    public Builder(PostRequestDto postRequestDto) {
      this.item = postRequestDto.getItem();
      this.completed = postRequestDto.isCompleted();
      this.user = postRequestDto.getUser();
    }

    public Post build() {
      return new Post(item, completed, user);
    }
  }
}
