package com.harish.inshorts.configs;

import com.harish.inshorts.models.UserEvent;
import com.harish.inshorts.repository.UserEventRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Random;

@Configuration
public class EventSimulator {
    @Bean
    CommandLineRunner simulateEvents(UserEventRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Random random = new Random();
                for (int i = 0; i < 50; i++) {
                    UserEvent event = new UserEvent();
                    event.setArticleId("b1793e11-85f1-47f4-b836-ddc21dd8991e");
                    event.setEventType(random.nextBoolean() ? "view" : "click");
                    event.setLatitude(37.7749 + random.nextDouble() * 0.1);
                    event.setLongitude(-122.4194 + random.nextDouble() * 0.1);
                    event.setEventTime(LocalDateTime.now().minusHours(random.nextInt(48)));
                    repo.save(event);
                }
                System.out.println("✅ Simulated 50 user events");
            }
        };
    }
}
