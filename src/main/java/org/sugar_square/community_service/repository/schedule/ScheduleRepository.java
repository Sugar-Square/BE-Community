package org.sugar_square.community_service.repository.schedule;

import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.schedule.Schedule;
import org.sugar_square.community_service.repository.BaseRepository;

@Repository
public interface ScheduleRepository extends BaseRepository<Schedule, Long> {

  List<Schedule> findScheduleByScheduleDateBetween(Instant after, Instant before);
}
