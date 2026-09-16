package com.qalogger.controller;

import com.qalogger.model.TestCase;
import com.qalogger.repository.TestCaseRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/testcases")
@CrossOrigin(origins = "*")
public class TestCaseController {

    private final TestCaseRepository testCaseRepository;

    public TestCaseController(TestCaseRepository testCaseRepository) {
        this.testCaseRepository = testCaseRepository;
    }

    @GetMapping
    public ResponseEntity<?> getTestCasesByProject(@RequestParam(required = false) String projectId) {
        if (projectId == null || projectId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "projectId parameter is required"));
        }
        List<TestCase> cases = testCaseRepository.findByProjectIdOrderBySrNoAsc(projectId);
        return ResponseEntity.ok(cases);
    }

    @GetMapping("/next-sr")
    public ResponseEntity<?> getNextSrNo(@RequestParam String projectId) {
        if (projectId == null || projectId.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "projectId parameter is required"));
        }
        Integer nextSrNo = testCaseRepository.getNextSrNo(projectId);
        return ResponseEntity.ok(Map.of("nextSrNo", nextSrNo != null ? nextSrNo : 1));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestCase> getTestCaseById(@PathVariable String id) {
        return testCaseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createTestCase(@RequestBody TestCase testCase) {
        if (testCase.getProjectId() == null || testCase.getProjectId().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "projectId is required"));
        }
        if (testCase.getTitle() == null || testCase.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "test case title is required"));
        }

        String now = Instant.now().toString();
        if (testCase.getId() == null || testCase.getId().isBlank()) {
            testCase.setId("tc-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 5));
        }

        if (testCase.getSrNo() == null) {
            testCase.setSrNo(testCaseRepository.getNextSrNo(testCase.getProjectId()));
        }

        if (testCase.getStatus() == null || testCase.getStatus().isBlank()) {
            testCase.setStatus("Pending");
        }

        testCase.setCreatedAt(now);
        testCase.setUpdatedAt(now);

        TestCase saved = testCaseRepository.save(testCase);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTestCase(@PathVariable String id, @RequestBody TestCase updated) {
        return testCaseRepository.findById(id)
                .map(existing -> {
                    if (updated.getModule() != null) existing.setModule(updated.getModule());
                    if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
                    if (updated.getExpectedResult() != null) existing.setExpectedResult(updated.getExpectedResult());
                    if (updated.getStatus() != null) existing.setStatus(updated.getStatus());
                    if (updated.getAssignedBy() != null) existing.setAssignedBy(updated.getAssignedBy());
                    if (updated.getRemarks() != null) existing.setRemarks(updated.getRemarks());
                    if (updated.getLinkedIssueId() != null) existing.setLinkedIssueId(updated.getLinkedIssueId());
                    if (updated.getSrNo() != null) existing.setSrNo(updated.getSrNo());

                    existing.setUpdatedAt(Instant.now().toString());
                    TestCase saved = testCaseRepository.save(existing);
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> {
                    updated.setId(id);
                    String now = Instant.now().toString();
                    if (updated.getCreatedAt() == null) updated.setCreatedAt(now);
                    updated.setUpdatedAt(now);
                    if (updated.getSrNo() == null && updated.getProjectId() != null) {
                        updated.setSrNo(testCaseRepository.getNextSrNo(updated.getProjectId()));
                    }
                    if (updated.getStatus() == null || updated.getStatus().isBlank()) {
                        updated.setStatus("Pending");
                    }
                    TestCase saved = testCaseRepository.save(updated);
                    return ResponseEntity.ok(saved);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTestCase(@PathVariable String id) {
        if (!testCaseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        testCaseRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Test case deleted"));
    }
}
