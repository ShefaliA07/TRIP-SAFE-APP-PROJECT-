package com.tripsafe.service;

import com.tripsafe.dto.SubjectDto;
import com.tripsafe.model.RiskStatus;
import com.tripsafe.model.Subject;
import com.tripsafe.repository.SubjectRepository;
import com.tripsafe.repository.TimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {

    private final SubjectRepository subjectRepository;
    private final TimetableRepository timetableRepository;
    private final AttendanceProjector attendanceProjector;
    private final RiskAnalyzer riskAnalyzer;

    public AttendanceService(SubjectRepository subjectRepository,
                             TimetableRepository timetableRepository,
                             AttendanceProjector attendanceProjector,
                             RiskAnalyzer riskAnalyzer) {
        this.subjectRepository = subjectRepository;
        this.timetableRepository = timetableRepository;
        this.attendanceProjector = attendanceProjector;
        this.riskAnalyzer = riskAnalyzer;
    }

    public List<SubjectDto> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public SubjectDto getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + id));
        return mapToDto(subject);
    }

    public SubjectDto createSubject(SubjectDto dto) {
        Subject.validateAttendanceValues(dto.getClassesConducted(), dto.getClassesAttended());
        Subject subject = new Subject(
                dto.getName().trim(),
                dto.getCode() != null ? dto.getCode().trim().toUpperCase() : "",
                dto.getClassesConducted(),
                dto.getClassesAttended(),
                dto.getMinAttendancePercentage() > 0 ? dto.getMinAttendancePercentage() : 75.0,
                dto.getColor()
        );
        Subject saved = subjectRepository.save(subject);
        return mapToDto(saved);
    }

    public SubjectDto updateSubject(Long id, SubjectDto dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subject not found with id: " + id));

        Subject.validateAttendanceValues(dto.getClassesConducted(), dto.getClassesAttended());

        subject.setName(dto.getName().trim());
        subject.setCode(dto.getCode() != null ? dto.getCode().trim().toUpperCase() : "");
        subject.setClassesConducted(dto.getClassesConducted());
        subject.setClassesAttended(dto.getClassesAttended());
        subject.setMinAttendancePercentage(dto.getMinAttendancePercentage() > 0 ? dto.getMinAttendancePercentage() : 75.0);
        if (dto.getColor() != null && !dto.getColor().isBlank()) {
            subject.setColor(dto.getColor());
        }

        Subject updated = subjectRepository.save(subject);
        return mapToDto(updated);
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subject not found with id: " + id);
        }
        // Clean up timetable entries associated with this subject first
        timetableRepository.deleteBySubjectId(id);
        subjectRepository.deleteById(id);
    }

    public SubjectDto mapToDto(Subject subject) {
        SubjectDto dto = new SubjectDto();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setCode(subject.getCode());
        dto.setClassesConducted(subject.getClassesConducted());
        dto.setClassesAttended(subject.getClassesAttended());
        dto.setMinAttendancePercentage(subject.getMinAttendancePercentage());
        dto.setColor(subject.getColor());

        double currentPct = attendanceProjector.calculateCurrentPercentage(
                subject.getClassesAttended(),
                subject.getClassesConducted()
        );
        dto.setCurrentAttendancePercentage(currentPct);
        dto.setCurrentStatus(riskAnalyzer.evaluateStatus(currentPct, subject.getMinAttendancePercentage()));
        return dto;
    }
}
