package com.harish.inshorts.services;

import com.harish.inshorts.models.NewsArticle;
import com.harish.inshorts.repository.NewsArticleRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final NewsArticleRepository repo;
    private final CohereService cohereService;

    public NewsService(NewsArticleRepository repo, CohereService cohereService) {
        this.repo = repo;
        this.cohereService = cohereService;
    }

    public List<NewsArticle> getByCategory(String category) {
        return repo.findByCategoryContainingIgnoreCase(category)
                .stream()
                .sorted(Comparator.comparing(NewsArticle::getPublicationDate).reversed())
                .limit(5)
                .peek(a -> a.setLlmSummary(cohereService.summarizeArticle(a.getDescription())))
                .collect(Collectors.toList());
    }

    public List<NewsArticle> getBySource(String source) {
        return repo.findBySourceNameContainingIgnoreCase(source)
                .stream()
                .sorted(Comparator.comparing(NewsArticle::getPublicationDate).reversed())
                .limit(5)
                .peek(a -> a.setLlmSummary(cohereService.summarizeArticle(a.getDescription())))
                .collect(Collectors.toList());
    }

    public List<NewsArticle> getByScore(double threshold) {
        return repo.findByRelevanceScoreGreaterThan(threshold)
                .stream()
                .sorted(Comparator.comparing(NewsArticle::getRelevanceScore).reversed())
                .limit(5)
                .peek(a -> a.setLlmSummary(cohereService.summarizeArticle(a.getDescription())))
                .collect(Collectors.toList());
    }

    public List<NewsArticle> searchArticles(String query) {
        return repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query)
                .stream()
                .sorted(Comparator.comparing(NewsArticle::getRelevanceScore).reversed())
                .limit(5)
                .peek(a -> a.setLlmSummary(cohereService.summarizeArticle(a.getDescription())))
                .collect(Collectors.toList());
    }
}
