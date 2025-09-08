package com.harish.inshorts.repository;

import com.harish.inshorts.models.NewsArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticle, String> {
    List<NewsArticle> findByCategoryContainingIgnoreCase(String category);
    List<NewsArticle> findBySourceNameContainingIgnoreCase(String sourceName);
    List<NewsArticle> findByRelevanceScoreGreaterThan(double threshold);
    List<NewsArticle> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String desc);
}
