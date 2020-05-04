package com.Clover.slonkit_repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Clover.Model_slonkit.Product;

@Repository
public interface ProductRepoSlonkit extends JpaRepository<Product, Long> {
	
	@Transactional
	@Query(name = "findByDate", value = "select * from product_slonkit where createdDate> :fromDate and createdDate< :toDate", nativeQuery = true)
	public List<Product> getProducts(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
}
