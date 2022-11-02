package net.valeryvash.myawss3springrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.valeryvash.myawss3springrestapi.model.Event;
import net.valeryvash.myawss3springrestapi.model.EventType;
import net.valeryvash.myawss3springrestapi.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link User} entity
 */
@AllArgsConstructor
@Getter
public class UserEventsDto implements Serializable {
    private final String userName;
    private final List<EventDto> events;

    /**
     * A DTO for the {@link Event} entity
     */
    @AllArgsConstructor
    @Getter
    public static class EventDto implements Serializable {
        private final EventType eventType;
        private final Date created;
        private final String fileFileName;

        public static EventDto fromEvent(Event event) {
            return new EventDto(event.getEventType(), event.getCreated(), event.getFile().getFileName());
        }
    }

    public static UserEventsDto fromUser(User user) {
        UserEventsDto dto = new UserEventsDto(user.getUserName(), new ArrayList<>());

        user.getEvents().forEach(
                event -> dto.addEventDto(EventDto.fromEvent(event))
        );

        return dto;
    }

    private void addEventDto(EventDto dto) {
        this.getEvents().add(dto);
    }
}