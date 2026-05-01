package com.wenjie.modules.visa.mapper;

import com.wenjie.modules.visa.dto.VisaNotificationQueryDTO;
import com.wenjie.modules.visa.entity.VisaReminderNotification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VisaReminderNotificationMapper {
    int insert(VisaReminderNotification notification);
    int updateSendResult(VisaReminderNotification notification);
    int markRead(@Param("id") Long id, @Param("recipientUserId") Long recipientUserId);
    long countPage(@Param("query") VisaNotificationQueryDTO query);
    List<VisaReminderNotification> selectPage(@Param("query") VisaNotificationQueryDTO query);
    List<VisaReminderNotification> selectByUserId(@Param("recipientUserId") Long recipientUserId);
    List<VisaReminderNotification> selectByAlertId(@Param("alertId") Long alertId);
}
