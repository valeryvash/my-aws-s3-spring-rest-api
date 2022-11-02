package net.valeryvash.myawss3springrestapi.repository;

import net.valeryvash.myawss3springrestapi.model.Event;
import net.valeryvash.myawss3springrestapi.dto.UserEventsDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface EventRepo extends CrudRepository<Event,Long> {

    @Query("select e from Event e where e.user.userName = ?1")
    List<UserEventsDto.EventDto> findByUser_UserName(@NonNull String userName);

}
