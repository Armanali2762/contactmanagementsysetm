package com.e_commerce.made2automade.Dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.e_commerce.made2automade.Entities.User;
public interface UserRepositry extends CrudRepository<User, Integer> {
    
    @Query("select u from User u where u.email = :email")
    public User getUserByEmail(@Param("email") String email);
}
