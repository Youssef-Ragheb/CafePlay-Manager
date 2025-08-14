package com.playstation.manager.service;

import com.playstation.manager.entity.ActionLog;
import com.playstation.manager.entity.User;
import com.playstation.manager.repository.ActionLogRepository;
import com.playstation.manager.utilz.AuthUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActionLogService {

    private final ActionLogRepository logRepo;
    private final UserService userService;
    private final AuthUtil authUtil;

    @Transactional
    public void log(String actionType, String entityType, Long entityId) {
        ActionLog log = new ActionLog();
        log.setActionType(actionType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setTimestamp(LocalDateTime.now());
        log.setPerformedBy(userService.getCurrentUser()); // from JWT or session

        logRepo.save(log);
    }

    public Page<ActionLog> getLogsBetweenDates(LocalDate start, LocalDate end, int page, int size) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return logRepo.findByTimestampBetween(start.atStartOfDay(), end.atTime(23, 59), pageable);
    }
}
