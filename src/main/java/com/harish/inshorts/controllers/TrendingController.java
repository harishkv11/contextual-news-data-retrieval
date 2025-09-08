package com.harish.inshorts.controllers;

import com.harish.inshorts.models.NewsArticle;
import com.harish.inshorts.services.TrendingService;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class TrendingController {
    private final TrendingService trendingService;

    public TrendingController(TrendingService trendingService) {
        this.trendingService = trendingService;
    }

    @GetMapping("/trending")
    public List<NewsArticle> trending(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(defaultValue = "10") double radiusKm
    ) {
        return trendingService.getTrending(lat, lon, limit, radiusKm);
    }
}
