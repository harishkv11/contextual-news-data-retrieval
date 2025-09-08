package com.harish.inshorts.configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harish.inshorts.models.NewsArticle;
import com.harish.inshorts.repository.NewsArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@Slf4j
public class DataLoader {

    @Bean
    CommandLineRunner loadData(NewsArticleRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                ObjectMapper mapper = new ObjectMapper();
                mapper.findAndRegisterModules();

                try (InputStream inputStream = getClass().getResourceAsStream("/news_data.json")) {
                    List<NewsArticle> articles = mapper.readValue(
                            inputStream,
                            new TypeReference<List<NewsArticle>>() {}
                    );

                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    articles.forEach(a -> {
                        if (a.getPublicationDate() == null) {
                            try {
                                a.setPublicationDate(LocalDateTime.parse(
                                        a.getPublicationDate().toString(), formatter));
                            } catch (Exception e) {
                                log.error("Error while loading news article : {}", e.getMessage());
                            }
                        }
                    });

                    repo.saveAll(articles);
                    System.out.println("✅ Loaded " + articles.size() + " news articles into DB");
                }
            }
        };
    }
}