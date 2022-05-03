package todo.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todo.api.controller.post.PostRequestDto;
import todo.api.exception.TodoAlreadyExistException;
import todo.api.exception.TodoNotFoundException;
import todo.api.model.Post;
import todo.api.model.User;
import todo.api.repository.PostRepository;

@Service
public class PostService {

  private PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Long savePost(PostRequestDto postRequestDto, User user) {
    boolean isExist = user.getPosts().stream()
        .anyMatch(post -> post.getItem().equals(postRequestDto.getItem()));
    if (isExist) {
      throw new TodoAlreadyExistException("Todo already Exist");
    }

    postRequestDto.setUser(user);

    Post post = new Post.Builder(postRequestDto).build();

    return postRepository.save(post).getId();
  }

  public void delete(Long postId) {
    postRepository.delete(postRepository.findById(postId)
        .orElseThrow(() -> new TodoNotFoundException("Todo Not Found"))
    );
  }

  @Transactional
  public void deleteAll(Long userId) {
    postRepository.deleteAllByUserId(userId);
  }

  @Transactional
  public void update(Long postId, PostRequestDto postRequestDto) {
    Post findPost = postRepository.findById(postId)
        .orElseThrow(() -> new TodoNotFoundException("Todo Not Found"));
    Post post = new Post.Builder(postRequestDto).build();

    if (findPost.isCompleted() != post.isCompleted()) {
      findPost.setCompleted(post.isCompleted());
    } else {
      boolean isExist = findPost.getUser().getPosts().stream()
          .anyMatch(p -> p.getItem().equals(post.getItem()));
      if (isExist) {
        throw new TodoAlreadyExistException("Todo already Exist");
      }
      findPost.setItem(post.getItem());
    }
  }

}
