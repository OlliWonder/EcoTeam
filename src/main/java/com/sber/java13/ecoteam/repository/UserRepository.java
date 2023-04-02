package com.sber.java13.ecoteam.repository;

import com.sber.java13.ecoteam.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericRepository<User> {
    
    User findUserByLogin(String login);
    
    User findUserByEmail(String email);
    
    User findUserByChangePasswordToken(String token);
    
    User findUserByLoginAndIsDeletedFalse(String login);
    
    @Query(nativeQuery = true,
            value = """
                 select u.*
                 from users u
                 where u.first_name ilike '%' || coalesce(:firstName, '%') || '%'
                 and u.last_name ilike '%' || coalesce(:lastName, '%') || '%'
                 and u.login ilike '%' || coalesce(:login, '%') || '%'
                  """)
    Page<User> searchUsers(String firstName, String lastName, String login, Pageable pageable);
}