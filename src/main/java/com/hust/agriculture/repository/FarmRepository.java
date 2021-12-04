package com.hust.agriculture.repository;

import com.hust.agriculture.model.Device;
import com.hust.agriculture.model.Farm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> {

    @Query(
            value = "select f from Farm f where f.user.ID = :userId"
    )
    List<Farm> findByUserId(@Param("userId") Long userId);

    @Query(
            value = "select f from Farm f " +
                    "where f.status = :status " +
                    "and lower(f.user.username) like lower(concat('%', :key, '%')) order by f.ID desc"
    )
    Page<Farm> findFarms(Pageable pageable, @Param("status") Integer status, @Param("key") String key);

    @Query(
            value = "select f from Farm f " +
                    "where f.status = :status " +
                    "and lower(f.name) like lower(concat('%', :key, '%')) order by f.ID desc"
    )
    Page<Farm> findFarmsByName(Pageable pageable, @Param("status") Integer status, @Param("key") String key);

    @Query(
            value = "select f from Farm f " +
                    "where f.status = :status " +
                    "and f.user.username = :key  order by f.ID desc"
    )
    Page<Farm> findFarmsExactly(Pageable pageable, @Param("status") Integer status, @Param("key") String key);

    List<Farm> findByName(String name);
}
