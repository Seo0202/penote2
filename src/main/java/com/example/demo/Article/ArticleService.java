package com.example.demo.Article;

import com.example.demo.User.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Long create(ArticleRequest request, User author) {
        Article article = request.toEntity();
        article.setAuthor(author);
        return articleRepository.save(article).getId();
    }

    public ArticleResponse findById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회차가 없습니다. id=" + id));
        return ArticleResponse.from(article);
    }

    public List<ArticleResponse> findAll() {
        return articleRepository.findAll().stream().map(ArticleResponse::from).toList();
    }

    public Page<ArticleResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return articleRepository.findAll(pageable).map(ArticleResponse::from);
    }

    public Page<ArticleResponse> findAll(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (keyword == null || keyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleResponse::from);
        }
        return articleRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                .map(ArticleResponse::from);
    }

    public Page<ArticleResponse> findAll(int page, int size, String keyword, String sortBy, String direction) {
        Pageable pageable = PageRequest.of(page, size, buildSort(sortBy, direction));
        if (keyword == null || keyword.isBlank()) {
            return articleRepository.findAll(pageable).map(ArticleResponse::from);
        }
        return articleRepository
                .findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword, pageable)
                .map(ArticleResponse::from);
    }

    private Sort buildSort(String sortBy, String direction) {
        String property = (sortBy == null) ? "id" : switch (sortBy) {
            case "title" -> "title";
            case "id" -> "id";
            default -> "id";
        };
        Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return Sort.by(dir, property);
    }

    @Transactional
    public Article update(Long id, ArticleRequest request) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 회차가 없습니다. id=" + id));
        article.update(request.getTitle(), request.getContent(), request.getGenre(), request.isPaid());
        return article;
    }

    @Transactional
    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }
}
