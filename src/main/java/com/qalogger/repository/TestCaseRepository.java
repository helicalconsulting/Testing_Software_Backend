package com.qalogger.repository;

import com.qalogger.model.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, String> {

    List<TestCase> findByProjectIdOrderBySrNoAsc(String projectId);

    void deleteByProjectId(String projectId);

    @Query("SELECT COALESCE(MAX(t.srNo), 0) + 1 FROM TestCase t WHERE t.projectId = :projectId")
    Integer getNextSrNo(@Param("projectId") String projectId);
}
