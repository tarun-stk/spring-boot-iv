package com.stk.transaction.repo;

import com.stk.transaction.entity.PassengerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerInfoRepository extends JpaRepository<PassengerInfo,Long> {
}