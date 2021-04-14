package mx.restfulapp.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import mx.restfulapp.model.User;

public interface UserRepo extends PagingAndSortingRepository<User, Integer> {
	@Query("SELECT c FROM User c where c.username = :username")
	User findByUsername(@Param("username") String username);
	
	boolean existsByUsername(String username);
}
