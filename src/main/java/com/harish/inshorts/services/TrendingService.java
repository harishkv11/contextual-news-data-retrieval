package com.harish.inshorts.services;

import com.harish.inshorts.models.NewsArticle;
import com.harish.inshorts.models.UserEvent;
import com.harish.inshorts.repository.NewsArticleRepository;
import com.harish.inshorts.repository.UserEventRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrendingService {
    private final UserEventRepository eventRepo;
    private final NewsArticleRepository articleRepo;

    public TrendingService(UserEventRepository eventRepo, NewsArticleRepository articleRepo) {
        this.eventRepo = eventRepo;
        this.articleRepo = articleRepo;
    }

    public List<NewsArticle> getTrending(double lat, double lon, int limit, double radiusKm) {
        double latRange = radiusKm / 111.0;
        double lonRange = radiusKm / 111.0;

        List<UserEvent> events = eventRepo.findByLatitudeBetweenAndLongitudeBetween(
                lat - latRange, lat + latRange,
                lon - lonRange, lon + lonRange
        );

        Map<String, Double> trendingScores = new HashMap<>();
        for (UserEvent e : events) {
            double score = 1.0;
            if ("click".equalsIgnoreCase(e.getEventType())) score = 2.0;

            long hoursAgo = Duration.between(e.getEventTime(), LocalDateTime.now()).toHours();
            double recencyFactor = Math.max(0.1, 1.0 - (hoursAgo / 24.0));

            trendingScores.merge(e.getArticleId(), score * recencyFactor, Double::sum);
        }

        return trendingScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limit)
                .map(entry -> articleRepo.findById(entry.getKey()).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
