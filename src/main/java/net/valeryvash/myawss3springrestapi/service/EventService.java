package net.valeryvash.myawss3springrestapi.service;

import net.valeryvash.myawss3springrestapi.dto.UserEventsDto;

import java.util.List;

public interface EventService {
    List<UserEventsDto.EventDto> getUserEventsByUserName(String userName);

}
