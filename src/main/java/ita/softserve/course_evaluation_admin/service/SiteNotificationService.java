package ita.softserve.course_evaluation_admin.service;

public interface SiteNotificationService {
    void processCreateSiteNotification(String header, String content, Long userId);
}