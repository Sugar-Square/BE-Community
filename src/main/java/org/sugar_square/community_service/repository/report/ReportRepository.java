package org.sugar_square.community_service.repository.report;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.report.Report;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface ReportRepository extends BaseRepository<Report, Long> {

}
