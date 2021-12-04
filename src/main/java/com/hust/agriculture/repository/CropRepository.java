package com.hust.agriculture.repository;

import com.hust.agriculture.model.Crop;
import com.hust.agriculture.model.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long> {

    @Query(
            value = "select distinct c.* from crop c, plant p, device d " +
                    "where c.plant_id = p.id and c.id = d.crop_id " +
                    "and c.status = 1 " +
                    "and c.payment_type = 1 " +
                    "and not exists (" +
                    "   select * from invoice i " +
                    "   where i.device_id = d.id " +
                    "   and i.payment_type = c.payment_type " +
                    "   and date(now()) = date(i.created_at))",
            nativeQuery = true
    )
    List<Crop> findCropsMustPaidByDay();

    @Query(
            value = "select distinct c.* from crop c, plant p, device d  " +
                    "where c.plant_id = p.id and c.id = d.crop_id " +
                    "and c.status = 0 " +
                    "and c.payment_type = 2 " +
                    "and c.end_time < now() " +
                    "and not exists (" +
                    "   select * from invoice i " +
                    "   where i.device_id = d.id " +
                    "   and i.payment_type = c.payment_type " +
                    "   and date(now()) = date(i.created_at))",
            nativeQuery = true
    )
    List<Crop> findCropsMustPaidByCrop();


    @Query(
            value = "select distinct c from Crop c join c.devices where c.status = :status " +
                    "and lower(c.name) like lower(concat('%', :key, '%')) order by c.ID desc"
    )
    Page<Crop> findCrops(Pageable pageable, Integer status, String key);
}
