package com.example.releasetracker.repository;


import com.example.releasetracker.domain.entity.Release;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ReleaseRepository extends JpaRepository<Release, Long>, JpaSpecificationExecutor<Release> {
}
