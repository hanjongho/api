package todo.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import todo.api.controller.member.UserRequestDto;
import todo.api.exception.UserNotFoundException;
import todo.api.model.Post;
import todo.api.model.User;
import todo.api.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @InjectMocks
  private UserService subject;

  @Test
  void 존재하는_유저로_로그인하면_존재하는_유저가_반환된다() {
    //given
    User aaron = new User("aaron");
    aaron.setPosts(Arrays.asList(new Post("test", false, null)));
    when(userRepository.save(any())).thenReturn(aaron);

    //when
    UserRequestDto userRequestDto = new UserRequestDto();
    userRequestDto.setName("aaron");
    User loginUser = subject.login(userRequestDto);

    //then
    assertThat(loginUser, is(aaron));
    assertThat(loginUser.getPosts().get(0).getItem(), is("test"));
    assertThat(loginUser.getPosts().get(0).isCompleted(), is(false));
  }

  @Test
  void 존재하지않는_유저로_로그인하면_새로운_유저가_반환된다() {
    //given
    when(userRepository.save(any())).thenReturn(new User("newbie"));

    //when
    UserRequestDto userRequestDto = new UserRequestDto();
    userRequestDto.setName("newbie");
    User loginUser = subject.login(userRequestDto);

    //then
    assertThat(loginUser.getName(), is("newbie"));
  }

  @Test
  void 유저가_존재하지않으면_예외가_반환된다() {
    //given
    when(userRepository.findById(1L)).thenThrow(new UserNotFoundException("User Not Found"));

    //then
    Assertions.assertThrows(UserNotFoundException.class, () -> subject.findUserById(1L));
  }

  @Test
  void 유저가_존재하면_유저가_반환된다() {
    //given
    User oldbie = new User("oldbie");
    when(userRepository.findById(1L)).thenReturn(Optional.of(oldbie));

    //when
    Optional<User> findUser = userRepository.findById(1L);

    //then
    assertThat(findUser.get().getName(), is("oldbie"));
  }
}