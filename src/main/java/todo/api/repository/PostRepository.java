package todo.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import todo.api.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Modifying
  @Query("delete from Post p where p.user.id = :userId")
  void deleteAllByUserId(Long userId);
}
