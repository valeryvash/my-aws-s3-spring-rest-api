package net.valeryvash.myawss3springrestapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.valeryvash.myawss3springrestapi.dto.UserEventsDto;
import net.valeryvash.myawss3springrestapi.repository.EventRepo;
import net.valeryvash.myawss3springrestapi.service.EventService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;

    public EventServiceImpl(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Override
    public List<UserEventsDto.EventDto> getUserEventsByUserName(String userName) {
        List<UserEventsDto.EventDto> resultList = eventRepo.findByUser_UserName(userName);
        log.info("Events for user with username {} found successfully. Events collection size is {} ", userName, resultList.size());
        return resultList;
    }

}
