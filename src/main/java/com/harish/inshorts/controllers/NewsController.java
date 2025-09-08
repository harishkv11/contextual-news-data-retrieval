package com.harish.inshorts.controllers;

import com.harish.inshorts.models.NewsArticle;
import com.harish.inshorts.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
public class NewsController {
    private final NewsService service;

    @Autowired
    public NewsController(NewsService service) {
        this.service = service;
    }

    @GetMapping("/category")
    public List<NewsArticle> byCategory(@RequestParam String category) {
        return service.getByCategory(category);
    }

    @GetMapping("/source")
    public List<NewsArticle> bySource(@RequestParam String source) {
        return service.getBySource(source);
    }

    @GetMapping("/score")
    public List<NewsArticle> byScore(@RequestParam(defaultValue = "0.7") double threshold) {
        return service.getByScore(threshold);
    }

    @GetMapping("/search")
    public List<NewsArticle> search(@RequestParam String query) {
        return service.searchArticles(query);
    }
}
