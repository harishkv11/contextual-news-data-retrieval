package com.harish.inshorts.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news_articles")
public class NewsArticle {
    @Id
    private String id;

    private String title;
    private String description;
    private String url;
    @JsonProperty("publication_date")
    private LocalDateTime publicationDate;
    @JsonProperty("source_name")
    private String sourceName;

    @ElementCollection
    private List<String> category;

    @JsonProperty("relevance_score")
    private double relevanceScore;
    private double latitude;
    private double longitude;

    @Transient
    private String llmSummary;
}
