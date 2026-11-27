package com.hamzaerfan.devaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hamzaerfan.devaccess.entity.Scan;

public interface ScanRepository extends JpaRepository<Scan, Long> {

    List<Scan> findAllByOrderByScannedAtDesc();
}
