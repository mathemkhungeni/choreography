package com.emkay.saga.payment.repository;

import com.emkay.saga.payment.entity.UserBalance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance,Integer> {
}
