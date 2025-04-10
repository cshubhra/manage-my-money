package com.epam.managemymoney.repository;

import com.epam.managemymoney.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUserId(Long userId);

//    @Query("SELECT r FROM Report r WHERE r.user_id = :userId " +
//            "AND r.startDate >= :startDate AND r.endDate <= :endDate " +
//            "ORDER BY r.createdDate DESC")
//    List<Report> findUserReportsInDateRange(
//            @Param("userId") Long userId,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );

//    @Query("SELECT r FROM Report r WHERE r.user.id = :userId " +
//            "AND r.reportType = :reportType " +
//            "ORDER BY r.createdDate DESC")
//    List<Report> findByUserIdAndReportType(
//            @Param("userId") Long userId,
//            @Param("reportType") String reportType
//    );
//
//    @Query("SELECT r FROM Report r WHERE r.user.id = :userId " +
//            "AND r.reportType = :reportType " +
//            "AND r.startDate >= :startDate AND r.endDate <= :endDate " +
//            "ORDER BY r.createdDate DESC")
//    List<Report> findByUserIdAndReportTypeAndDateRange(
//            @Param("userId") Long userId,
//            @Param("reportType") String reportType,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate
//    );

    @Query(value = "SELECT DISTINCT r.* FROM reports r " +
            "JOIN report_categories rc ON r.id = rc.report_id " +
            "WHERE r.user_id = :userId " +
            "AND rc.category_id IN :categoryIds " +
            "ORDER BY r.created_date DESC",
            nativeQuery = true)
    List<Report> findByUserIdAndCategories(
            @Param("userId") Long userId,
            @Param("categoryIds") List<Long> categoryIds
    );
}