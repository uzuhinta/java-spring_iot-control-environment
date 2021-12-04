package com.hust.agriculture.repository;

import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.Plant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    @Query(
            value = "select p from Plant p " +
                    "where p.status = :status " +
                    "and lower(p.name) like lower(concat('%', :key, '%')) order by p.ID desc"
    )
    Page<Plant> findPlants(Pageable pageable, @Param("status") Integer status, @Param("key") String key);

}
