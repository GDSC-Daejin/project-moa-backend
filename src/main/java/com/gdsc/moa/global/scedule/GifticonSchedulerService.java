package com.gdsc.moa.global.scedule;

import com.gdsc.moa.domain.gifticon.entity.GifticonEntity;
import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
import com.gdsc.moa.domain.user.dto.FCMNotificationRequest;
import com.gdsc.moa.domain.user.entity.UserEntity;
import com.gdsc.moa.domain.user.repository.UserRepository;
import com.gdsc.moa.domain.user.service.FcmService;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifticonSchedulerService {
    private final GifticonRepository gifticonRepository;
    private final UserRepository userRepository;
    private final FcmService fcmService;


    @Scheduled(cron = "0 0 0 * * *")//매일 자정마다 실행
    public void checkGifticonDueDate() {
        gifticonRepository.updateGifticonsWithDueDateToday();

    }

    @Scheduled(cron = "0 0 10 * * *")//매일 오전 10시마다 실행
    public void checkDueDateAndSendFcmMessage() {
        //1. 멤버별 기프티콘 만료일 조회
        List<UserEntity> users = userRepository.findAll();
        //2. 만료일이 설정한 날짜 이내인 기프티콘 조회
        for(UserEntity user : users) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -user.getReminderDay());
            Date date = new Date(calendar.getTimeInMillis());

            List<GifticonEntity> gifticonList = gifticonRepository.findByUserAndDueDate(user, date);
            //3. FCM 메시지 전송
            if(gifticonList.size() > 0) {
                FCMNotificationRequest request = new FCMNotificationRequest(user.getId(),
                        "기프티콘 만료일이 " + user.getReminderDay() + "일 남았습니다.",
                        "기프티콘 만료일이 " + user.getReminderDay() + "일 남았습니다.");
                fcmService.sendMessageTo(request);
            }
        }
    }
}
