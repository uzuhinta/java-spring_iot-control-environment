package com.hust.agriculture.repository;

import com.hust.agriculture.model.Device;
import com.hust.agriculture.model.Farm;
import com.hust.agriculture.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Device findByDeviceName(String deviceName);
    Device findByTopicName(String topicName);

    @Query(
            value = "select d from Device d where d.farm.user.ID = :userId"
    )
    List<Device> findByUserId(@Param("userId") Long userId);

    @Query(
            value = "select d from Device d where d.farm.ID = :farmId and d.farm.user.ID = :userId"
    )
    List<Device> findByFarmId(@Param("userId") Long userId, @Param("farmId") Long farmId);

    @Query(
            value = "select d.farm.user from Device d where d.topicName = :topicName"
    )
    User findUserByTopicName(@Param("topicName") String topicName);

    @Query(
            value = "select d.farm.user from Device d where d.ID = :deviceId"
    )
    User findUserByDeviceId(@Param("deviceId") Long deviceId);

    @Query(
            value = "select d from Device d where d.ID = :deviceId and d.farm.user.ID = :userId"
    )
    Device findByUserIdAndDeviceId(@Param("deviceId") Long deviceId, @Param("userId") Long userId);

    @Query(
            value = "select d from Device d where d.status = :status " +
                    "and d.farm.name = :key order by d.ID desc"
    )
    Page<Device> findDevicesExactly(Pageable pageable, @Param("status") Integer status, @Param("key") String key);

    @Query(
            value = "select distinct d from Device d where d.status = :status " +
                    "and lower(d.farm.name) like lower(concat('%', :key, '%')) order by d.ID desc"
    )
    Page<Device> findDevices(Pageable pageable, @Param("status") Integer status, @Param("key") String key);
}
