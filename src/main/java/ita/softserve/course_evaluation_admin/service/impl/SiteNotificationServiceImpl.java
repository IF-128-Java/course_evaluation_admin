package ita.softserve.course_evaluation_admin.service.impl;

import ita.softserve.course_evaluation_admin.dto.SiteNotificationResponseDto;
import ita.softserve.course_evaluation_admin.dto.mapper.SiteNotificationResponseDtoMapper;
import ita.softserve.course_evaluation_admin.entity.SiteNotification;
import ita.softserve.course_evaluation_admin.entity.User;
import ita.softserve.course_evaluation_admin.repository.SiteNotificationRepository;
import ita.softserve.course_evaluation_admin.service.SiteNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class SiteNotificationServiceImpl implements SiteNotificationService {

    private final SiteNotificationRepository siteNotificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void processCreateSiteNotification(String header, String content, User user) {
        SiteNotification notification = SiteNotification.builder()
                .header(header)
                .content(content)
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();

        siteNotificationRepository.save(notification);
        log.info("Saved site notification with id {}!", notification.getId());
        sendSiteNotification(SiteNotificationResponseDtoMapper.toDto(notification), user.getId());
    }

    private void sendSiteNotification(SiteNotificationResponseDto response, Long userId){
        messagingTemplate.convertAndSend("/api/v1/admin/event/site-notifications/users/" + userId, response);
        log.info("Broadcast site notification with id {}!", response.getId());
    }
}