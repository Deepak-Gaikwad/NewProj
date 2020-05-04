package com.Clover.moneykit_repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Clover.Model_moneykit.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	@Transactional
	@Query(name = "findByUsername", value = "select * from user_report where username= :username and password= :password and product=:product", nativeQuery = true)
	public User validateUser(@Param("username") String username, @Param("password") String password, @Param("product")String product);

}
