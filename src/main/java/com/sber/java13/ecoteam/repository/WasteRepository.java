package com.sber.java13.ecoteam.repository;

import com.sber.java13.ecoteam.model.Waste;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteRepository extends GenericRepository<Waste> {
    
    Page<Waste> findAllByIsDeletedFalse(Pageable pageable);
    
    Page<Waste> findAllByTitleContainsIgnoreCaseAndDeletedFalse(String fio, Pageable pageable);
    
    @Query(value = """
          select case when count(w) > 0 then false else true end
          from Waste w join w.orders o on w = o.waste
          where w.id = :wasteId
          and o.isCompleted = false
          """)
    boolean checkWasteForDeletion(final Long wasteId);
}
