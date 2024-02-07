package org.algorhythm.bankingmanagmentsystem.dataaccess.repository;

import org.algorhythm.bankingmanagmentsystem.dataaccess.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findTransactionByAccount_Id(long id);
}
