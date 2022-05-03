package todo.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import todo.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  User save(User member);

  Optional<User> findByName(String name);
}
