package com.gdsc.moa.global.scedule;

import com.gdsc.moa.domain.gifticon.repository.GifticonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifticonSchedulerService {
    private final GifticonRepository gifticonRepository;

    @Scheduled(cron = "0 0 0 * * *")//매일 자정마다 실행
    public void checkGifticonDueDate() {
        gifticonRepository.updateGifticonsWithDueDateToday();

    }
}
