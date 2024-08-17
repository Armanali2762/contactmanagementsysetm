package com.e_commerce.made2automade.Dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.e_commerce.made2automade.Entities.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    @Query("select p from Product p where p.user.id = :userId")
    public Page<Product> getAllContactsByUserId(@Param("userId") int id, Pageable pageable);
}
