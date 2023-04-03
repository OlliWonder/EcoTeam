package com.sber.java13.ecoteam.repository;

import com.sber.java13.ecoteam.model.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends GenericRepository<Point> {
    
    Page<Point> findAllByIsDeletedFalse(Pageable pageable);
    
    Page<Point> findAllByCityContainsIgnoreCaseAndIsDeletedFalse(String city, Pageable pageable);
    
    @Query(value = """
          select case when count(p) > 0 then false else true end
          from Point p join p.wastes w
                        join Order o on w.id = o.waste.id
          where p.id = :pointId
          and o.isCompleted = false
          """)
    boolean checkPointForDeletion(final Long pointId);
}
