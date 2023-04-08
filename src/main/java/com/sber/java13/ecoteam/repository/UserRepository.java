package com.sber.java13.ecoteam.repository;

import com.sber.java13.ecoteam.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                    where u.first_name ilike '%' || btrim(coalesce(:firstName, '')) || '%'
                    and u.last_name ilike '%' || btrim(coalesce(:lastName, '')) || '%'
                    and u.login ilike '%' || btrim(coalesce(:login, '')) || '%'
                    """)
    Page<User> searchUsers(String firstName, String lastName, String login, Pageable pageable);
    
    @Query(nativeQuery = true,
            value = """
                    select distinct email
                    from users u
                    left join points p on u.point_id = p.id
                    join orders o on p.id = o.point_id
                    where o.is_in_work = false
                    and o.is_completed = false
                    """)
    List<String> getAgentsEmails();
}