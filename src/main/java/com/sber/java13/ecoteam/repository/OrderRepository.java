package com.sber.java13.ecoteam.repository;

import com.sber.java13.ecoteam.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends GenericRepository<Order> {
    
    @Query("""
            select case when count(o) > 0 then false else true end
            from Order o
            where o.id = :id
            and o.isCompleted = false
            and o.isInWork = true
            """)
    boolean checkOrderForDeletion(final Long id);
    
    Page<Order> findAllByIsDeletedFalse(Pageable pageable);
    
    Page<Order> getOrderByUserId(Long userId, Pageable pageable);
}
