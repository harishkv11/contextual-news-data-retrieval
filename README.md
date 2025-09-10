# 📰 Contextual News Retrieval System  

## 📌 Overview  
This project is a **Spring Boot backend system** that retrieves, processes, and enriches news articles.  
It uses the **Cohere LLM API** to:  
- Understand user queries (entities, concepts, intent).  
- Select the best retrieval strategy (category, source, nearby, etc.).  
- Generate summaries for each article.  

It also includes a **bonus endpoint for trending news by location** based on simulated user events.  

---

## 🚀 Features  
- **LLM-Powered Query Understanding** with Cohere API  
- **News Retrieval & Filtering**: category, source, score, search, nearby  
- **Ranking Logic** (recency, score, distance, match relevance)  
- **LLM Summarization** for enriched articles  
- **Trending News Feed** (location-based, cached)  
