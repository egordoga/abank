package com.abank.task.repository;

import com.abank.task.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {

    List<Payment> findAllByDestAcc_IdAndSourceAcc_IdAndDestAcc_Client_IdAndSourceAcc_Client_Id(
            Long destAccId, Long srcAccId, Long recipId, Long payerId);
}
