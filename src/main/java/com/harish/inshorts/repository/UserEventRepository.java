package com.harish.inshorts.repository;

import com.harish.inshorts.models.UserEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserEventRepository extends JpaRepository<UserEvent, Long> {
    List<UserEvent> findByLatitudeBetweenAndLongitudeBetween(
            double latMin, double latMax,
            double lonMin, double lonMax
    );
}
