package todo.api.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import todo.api.controller.member.UserRequestDto;
import todo.api.exception.UserNotFoundException;
import todo.api.model.User;
import todo.api.repository.UserRepository;

@Service
public class UserService {

  private UserRepository userRepository;

  public UserService(UserRepository memberRepository) {
    this.userRepository = memberRepository;
  }

  public User login(UserRequestDto userRequestDto) {
    String userName = userRequestDto.getName();
    Optional<User> member = userRepository.findByName(userName);
    if (!member.isPresent()) {
      return userRepository.save(new User(userName));
    }
    return member.get();
  }

  public User findUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("해당하는 유저가 없습니다."));
  }

}
