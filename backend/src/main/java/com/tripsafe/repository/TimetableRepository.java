package com.tripsafe.repository;

import com.tripsafe.model.DayOfWeekEnum;
import com.tripsafe.model.TimetableEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableEntry, Long> {
    List<TimetableEntry> findByDayOfWeekOrderByPeriodIndexAsc(DayOfWeekEnum dayOfWeek);
    List<TimetableEntry> findBySubjectId(Long subjectId);
    Optional<TimetableEntry> findByDayOfWeekAndPeriodIndex(DayOfWeekEnum dayOfWeek, int periodIndex);

    @Modifying
    @Query("DELETE FROM TimetableEntry t WHERE t.subject.id = :subjectId")
    void deleteBySubjectId(Long subjectId);
}
