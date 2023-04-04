package com.sber.java13.ecoteam.repository;

import com.sber.java13.ecoteam.model.Waste;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteRepository extends GenericRepository<Waste> {
    
    Page<Waste> findAllByIsDeletedFalse(Pageable pageable);
    
    @Query(nativeQuery = true,
            value = """
                    select w.*
                    from wastes w
                    where w.title ilike '%' || btrim(coalesce(:title, '')) || '%'
                    and w.short_title ilike '%' || btrim(coalesce(:short_title, '')) || '%'
                    and w.code ilike '%' || btrim(coalesce(:code, '')) || '%'
                    and w.is_deleted = false
                         """)
    Page<Waste> searchWastes(@Param(value = "title") String title,
                             @Param(value = "short_title") String short_title,
                             @Param(value = "code") String code,
                             Pageable pageable);
    
    @Query(value = """
            select case when count(w) > 0 then false else true end
            from Waste w join w.orders o on w = o.waste
            where w.id = :wasteId
            and o.isCompleted = false
            """)
    boolean checkWasteForDeletion(final Long wasteId);
}
