package com.example.usedTrade.Repository;

import com.example.usedTrade.Entity.Report;
import com.example.usedTrade.Entity.ReportReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report ,Long> {

    Page<Report> findByReason(Pageable pageable, ReportReason reason);
}
