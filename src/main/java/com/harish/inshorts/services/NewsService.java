package com.harish.inshorts.services;

import com.harish.inshorts.models.NewsArticle;
import com.harish.inshorts.repository.NewsArticleRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class NewsService {
    private final NewsArticleRepository repo;
    private final CohereService cohereService;

    public NewsService(NewsArticleRepository repo, CohereService cohereService) {
        this.repo = repo;
        this.cohereService = cohereService;
    }

    private List<NewsArticle> fetchAndSummarize(List<NewsArticle> articles, Comparator<NewsArticle> comparator, int limit) {
        return articles.stream()
                .sorted(comparator.reversed())
                .limit(limit)
                .peek(a -> {
                    try {
                        a.setLlmSummary(summarizeAsync(a.getDescription()).join());
                    } catch (Exception e) {
                        a.setLlmSummary("Summary unavailable");
                    }
                })
                .collect(Collectors.toList());
    }

    public List<NewsArticle> getByCategory(String category) {
        List<NewsArticle> articles = repo.findByCategoryContainingIgnoreCase(category);
        return fetchAndSummarize(articles, Comparator.comparing(NewsArticle::getPublicationDate), 5);
    }

    public List<NewsArticle> getBySource(String source) {
        List<NewsArticle> articles = repo.findBySourceNameContainingIgnoreCase(source);
        return fetchAndSummarize(articles, Comparator.comparing(NewsArticle::getPublicationDate), 5);
    }

    public List<NewsArticle> getByScore(double threshold) {
        List<NewsArticle> articles = repo.findByRelevanceScoreGreaterThan(threshold);
        return fetchAndSummarize(articles, Comparator.comparing(NewsArticle::getRelevanceScore), 5);
    }

    public List<NewsArticle> searchArticles(String query) {
        List<NewsArticle> articles = repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        return fetchAndSummarize(articles, Comparator.comparing(NewsArticle::getRelevanceScore), 5);
    }

    @Async
    public CompletableFuture<String> summarizeAsync(String text) {
        try {
            return CompletableFuture.completedFuture(cohereService.summarizeArticle(text));
        } catch (Exception e) {
            return CompletableFuture.completedFuture("Summary unavailable");
        }
    }
}
