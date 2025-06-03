package com.stk.transaction.repo;
import com.stk.transaction.entity.PaymentInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoRepository extends JpaRepository<PaymentInfo,String> {
}
