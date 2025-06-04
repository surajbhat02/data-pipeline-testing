package com.prophecy.testing.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * Utility class for performance testing operations
 */
public class PerformanceTestUtils {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceTestUtils.class);
    
    /**
     * Performance metrics container
     */
    public static class PerformanceMetrics {
        private final long executionTimeMs;
        private final boolean success;
        private final String errorMessage;
        
        public PerformanceMetrics(long executionTimeMs, boolean success, String errorMessage) {
            this.executionTimeMs = executionTimeMs;
            this.success = success;
            this.errorMessage = errorMessage;
        }
        
        public long getExecutionTimeMs() { return executionTimeMs; }
        public boolean isSuccess() { return success; }
        public String getErrorMessage() { return errorMessage; }
    }
    
    /**
     * Load test results container
     */
    public static class LoadTestResults {
        private final List<PerformanceMetrics> results;
        private final long totalExecutionTimeMs;
        private final int successCount;
        private final int failureCount;
        private final double averageResponseTimeMs;
        private final long minResponseTimeMs;
        private final long maxResponseTimeMs;
        
        public LoadTestResults(List<PerformanceMetrics> results, long totalExecutionTimeMs) {
            this.results = results;
            this.totalExecutionTimeMs = totalExecutionTimeMs;
            this.successCount = (int) results.stream().filter(PerformanceMetrics::isSuccess).count();
            this.failureCount = results.size() - successCount;
            this.averageResponseTimeMs = results.stream()
                    .mapToLong(PerformanceMetrics::getExecutionTimeMs)
                    .average()
                    .orElse(0.0);
            this.minResponseTimeMs = results.stream()
                    .mapToLong(PerformanceMetrics::getExecutionTimeMs)
                    .min()
                    .orElse(0L);
            this.maxResponseTimeMs = results.stream()
                    .mapToLong(PerformanceMetrics::getExecutionTimeMs)
                    .max()
                    .orElse(0L);
        }
        
        // Getters
        public List<PerformanceMetrics> getResults() { return results; }
        public long getTotalExecutionTimeMs() { return totalExecutionTimeMs; }
        public int getSuccessCount() { return successCount; }
        public int getFailureCount() { return failureCount; }
        public double getAverageResponseTimeMs() { return averageResponseTimeMs; }
        public long getMinResponseTimeMs() { return minResponseTimeMs; }
        public long getMaxResponseTimeMs() { return maxResponseTimeMs; }
        public double getSuccessRate() { return (double) successCount / results.size() * 100; }
        public double getThroughput() { return (double) results.size() / totalExecutionTimeMs * 1000; }
    }
    
    /**
     * Measure execution time of a runnable operation
     */
    public static PerformanceMetrics measureExecutionTime(Runnable operation) {
        Instant start = Instant.now();
        boolean success = true;
        String errorMessage = null;
        
        try {
            operation.run();
        } catch (Exception e) {
            success = false;
            errorMessage = e.getMessage();
            logger.error("Operation failed during performance measurement: {}", e.getMessage());
        }
        
        long executionTime = Duration.between(start, Instant.now()).toMillis();
        return new PerformanceMetrics(executionTime, success, errorMessage);
    }
    
    /**
     * Measure execution time of a supplier operation
     */
    public static <T> PerformanceMetrics measureExecutionTime(Supplier<T> operation) {
        Instant start = Instant.now();
        boolean success = true;
        String errorMessage = null;
        
        try {
            operation.get();
        } catch (Exception e) {
            success = false;
            errorMessage = e.getMessage();
            logger.error("Operation failed during performance measurement: {}", e.getMessage());
        }
        
        long executionTime = Duration.between(start, Instant.now()).toMillis();
        return new PerformanceMetrics(executionTime, success, errorMessage);
    }
    
    /**
     * Perform load testing with concurrent users
     */
    public static LoadTestResults performLoadTest(Callable<Void> operation, int numberOfUsers, int iterations) {
        logger.info("Starting load test with {} users and {} iterations", numberOfUsers, iterations);
        
        ExecutorService executor = Executors.newFixedThreadPool(numberOfUsers);
        List<Future<PerformanceMetrics>> futures = new ArrayList<>();
        List<PerformanceMetrics> results = new ArrayList<>();
        
        Instant testStart = Instant.now();
        
        try {
            // Submit all tasks
            for (int i = 0; i < iterations; i++) {
                futures.add(executor.submit(() -> {
                    Instant start = Instant.now();
                    boolean success = true;
                    String errorMessage = null;
                    
                    try {
                        operation.call();
                    } catch (Exception e) {
                        success = false;
                        errorMessage = e.getMessage();
                    }
                    
                    long executionTime = Duration.between(start, Instant.now()).toMillis();
                    return new PerformanceMetrics(executionTime, success, errorMessage);
                }));
            }
            
            // Collect results
            for (Future<PerformanceMetrics> future : futures) {
                try {
                    results.add(future.get());
                } catch (Exception e) {
                    logger.error("Error collecting load test result: {}", e.getMessage());
                    results.add(new PerformanceMetrics(0, false, e.getMessage()));
                }
            }
            
        } finally {
            executor.shutdown();
        }
        
        long totalTestTime = Duration.between(testStart, Instant.now()).toMillis();
        LoadTestResults loadTestResults = new LoadTestResults(results, totalTestTime);
        
        logger.info("Load test completed - Success Rate: {:.2f}%, Average Response Time: {:.2f}ms, Throughput: {:.2f} ops/sec",
                loadTestResults.getSuccessRate(),
                loadTestResults.getAverageResponseTimeMs(),
                loadTestResults.getThroughput());
        
        return loadTestResults;
    }
    
    /**
     * Assert that execution time is within acceptable limits
     */
    public static void assertExecutionTimeWithinLimit(PerformanceMetrics metrics, long maxExecutionTimeMs) {
        if (metrics.getExecutionTimeMs() > maxExecutionTimeMs) {
            throw new AssertionError(String.format(
                    "Execution time %dms exceeded maximum allowed time %dms",
                    metrics.getExecutionTimeMs(), maxExecutionTimeMs));
        }
    }
    
    /**
     * Assert that success rate meets minimum threshold
     */
    public static void assertSuccessRate(LoadTestResults results, double minSuccessRate) {
        if (results.getSuccessRate() < minSuccessRate) {
            throw new AssertionError(String.format(
                    "Success rate %.2f%% is below minimum threshold %.2f%%",
                    results.getSuccessRate(), minSuccessRate));
        }
    }
    
    /**
     * Assert that throughput meets minimum threshold
     */
    public static void assertThroughput(LoadTestResults results, double minThroughput) {
        if (results.getThroughput() < minThroughput) {
            throw new AssertionError(String.format(
                    "Throughput %.2f ops/sec is below minimum threshold %.2f ops/sec",
                    results.getThroughput(), minThroughput));
        }
    }
}