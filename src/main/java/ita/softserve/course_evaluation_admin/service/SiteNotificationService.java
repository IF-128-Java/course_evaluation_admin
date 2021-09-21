package ita.softserve.course_evaluation_admin.service;

import ita.softserve.course_evaluation_admin.entity.User;

public interface SiteNotificationService {
    void processCreateSiteNotification(String header, String content, User user);
}