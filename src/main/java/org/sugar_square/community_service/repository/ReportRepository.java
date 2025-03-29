package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.report.Report;

@Repository
public interface ReportRepository extends BaseRepository<Report, Long> {

}
