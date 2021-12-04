package com.hust.agriculture.repository;

import com.hust.agriculture.model.AppParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppParamRepository extends JpaRepository<AppParam, Long> {

    AppParam findByCode(String code);
}
