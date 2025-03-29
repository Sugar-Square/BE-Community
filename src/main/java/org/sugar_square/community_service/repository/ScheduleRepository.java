package org.sugar_square.community_service.repository;

import org.springframework.stereotype.Repository;
import org.sugar_square.community_service.domain.schedule.Schedule;

@Repository
public interface ScheduleRepository extends BaseRepository<Schedule, Long>{

}
