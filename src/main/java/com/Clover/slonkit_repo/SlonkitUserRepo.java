package com.Clover.slonkit_repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.Clover.Model_slonkit.User;

public interface SlonkitUserRepo extends CrudRepository<User, Long> {

	@Transactional
	@Query(name = "findByUsername", value = "select * from user_report where username= :username and password= :password and product=:product", nativeQuery = true)
	public User validateUser(@Param("username") String username, @Param("password") String password,
			@Param("product") String product);

}
