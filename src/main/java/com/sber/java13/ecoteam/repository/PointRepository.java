package com.sber.java13.ecoteam.repository;

import com.sber.java13.ecoteam.model.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends GenericRepository<Point> {
    
    Page<Point> findAllByIsDeletedFalse(Pageable pageable);
    
    @Query(nativeQuery = true,
            value = """
                    select p.*
                    from points p
                    where p.title ilike '%' || btrim(coalesce(:title, '')) || '%'
                    and p.city ilike '%' || btrim(coalesce(:city, '')) || '%'
                    and p.address ilike '%' || btrim(coalesce(:address, '')) || '%'
                    and p.is_deleted = false
                         """)
    Page<Point> searchPoints(@Param(value = "title") String title,
                             @Param(value = "city") String city,
                             @Param(value = "address") String address,
                             Pageable pageable);
    
    @Query(value = """
          select case when count(p) > 0 then false else true end
          from Point p join p.wastes w
                        join Order o on w.id = o.waste.id
          where p.id = :pointId
          and o.isCompleted = false
          """)
    boolean checkPointForDeletion(final Long pointId);
}
