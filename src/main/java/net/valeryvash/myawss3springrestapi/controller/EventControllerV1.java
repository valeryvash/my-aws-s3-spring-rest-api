package net.valeryvash.myawss3springrestapi.controller;

import net.valeryvash.myawss3springrestapi.dto.UserEventsDto;
import net.valeryvash.myawss3springrestapi.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO add pagination version
 */
@RestController
@RequestMapping("/api/v1/")
public class EventControllerV1 {

    private final EventService eventService;

    public EventControllerV1(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("events")
    public ResponseEntity<UserEventsDto> getEventForLoggedUser() {
        String userName = getLoggedUser_UserName();

        List<UserEventsDto.EventDto> eventDtos = eventService.getUserEventsByUserName(userName);

        UserEventsDto dto = new UserEventsDto(userName, eventDtos);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dto);
    }


    @GetMapping("moder/events/{userName}")
    public ResponseEntity<UserEventsDto> getEventsForSpecifiedUser(@PathVariable("userName") String userName) {
        List<UserEventsDto.EventDto> eventDtos = eventService.getUserEventsByUserName(userName);

        UserEventsDto dto = new UserEventsDto(userName, eventDtos);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(dto);
    }

    private String getLoggedUser_UserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        return userDetails.getUsername();
    }

}
