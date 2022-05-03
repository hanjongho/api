package todo.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import todo.api.controller.post.PostRequestDto;
import todo.api.exception.TodoAlreadyExistException;
import todo.api.exception.TodoNotFoundException;
import todo.api.model.Post;
import todo.api.model.User;
import todo.api.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @Mock
  private PostRepository postRepository;

  @InjectMocks
  private PostService subject;

  @Test
  void 저장할_할일이_유저_할일리스트에_존재하면_예외가_반환된다() {
    //given
    User aaron = new User("aaron");
    aaron.setPosts(Arrays.asList(new Post("test", false, null)));

    //then
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setItem("test");
    assertThrows(TodoAlreadyExistException.class, () -> subject.savePost(postRequestDto, aaron));
  }

  @Test
  void 저장할_할일이_유저_할일리스트에_존재하지않으면_할일의_번호가_반환된다() {
    Post mockPost = new Post("test2", false, null);
    mockPost.setId(2L);
    when(postRepository.save(any())).thenReturn(mockPost);
    User aaron = new User("aaron");
    aaron.setPosts(Arrays.asList(new Post("test1", false, null)));

    //when
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setItem("test2");
    Long postId = subject.savePost(postRequestDto, aaron);

    //then
    assertThat(postId, is(2L));
  }

  @Test
  void 삭제할_할일이_전체_할일리스트에_존재하지않으면_예외가_반환된다() {
    //given
    when(postRepository.findById(1L)).thenThrow(new TodoNotFoundException("Todo Not Found"));

//    doThrow(new TodoNotFoundException("Todo Not Found")).when(postRepository).delete(any());

//    Post mockPost = new Post("test", false, null);
//    mockPost.setId(1L);
//    when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

    //when
    assertThrows(TodoNotFoundException.class, () -> subject.delete(1L));
  }

  @Test
  void 삭제할_할일이_전체_할일리스트에_존재하면_Delete함수가_호출되어_삭제된다() {
    //given
    Post mockPost = new Post("test", false, null);
    mockPost.setId(1L);
    when(postRepository.findById(1L)).thenReturn(Optional.of(mockPost));

    //when
    subject.delete(1L);

    //then
    verify(postRepository, times(1)).delete(mockPost);
  }

  @Test
  void 유저의_할일리스트를_전체_삭제된다() {
    //when
    subject.deleteAll(1L);

    //then
    verify(postRepository, times(1)).deleteAllByUserId(1L);
  }

  @Test
  void 업데이트할_할일이_전체_할일리스트에_존재하지않으면_예외가_반환된다() {
    //given
    when(postRepository.findById(1L)).thenThrow(new TodoNotFoundException("Todo Not Found"));

    //then
    assertThrows(TodoNotFoundException.class, () -> subject.update(1L, new PostRequestDto()));
  }

  @Test
  void 업데이트할_할일이_유저_할일리스트에_존재하면_예외가_반환된다() {
    //given
    // 연관관계 편의메서드 + @Data 어노테이션이 Entity에 달려있어서 toString, equals, hashcode가
    // 재 정의하기때문에 stackoverflow문제가 발생함 이때 다쪽에서 @ToString(exclude)를 통해 해결
    User aaron = new User("aaron");
    Post post = new Post("exist", false);
    post.setUser(aaron);

    Optional<Post> mockPost = Optional.of(post);
    when(postRepository.findById(1L)).thenReturn(mockPost);

    //then
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setItem("exist");
    assertThrows(TodoAlreadyExistException.class, () -> subject.update(1L, postRequestDto));

  }

  @Test
  void isCompleted값이_변경된경우_isCompleted를_변경한다() {
    //given
    Optional<Post> mockPost = Optional.of(new Post("exist", false));
    when(postRepository.findById(1L)).thenReturn(mockPost);

    //when
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setCompleted(true);
    subject.update(1L, postRequestDto);

    //then
    assertThat(mockPost.get().isCompleted(), is(true));
  }

  @Test
  void 업데이트할_할일이_정상적으로_변경된다() {
    //given
    User aaron = new User("aaron");
    Post post = new Post("exist", false);
    post.setUser(aaron);

    Optional<Post> mockPost = Optional.of(post);
    when(postRepository.findById(1L)).thenReturn(mockPost);

    //when
    PostRequestDto postRequestDto = new PostRequestDto();
    postRequestDto.setItem("change");
    subject.update(1L, postRequestDto);

    //then
    assertThat(mockPost.get().getItem(), is("change"));
  }

}