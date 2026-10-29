package com.example.demo.Article;

import com.example.demo.Comment.CommentResponse;
import com.example.demo.Comment.CommentService;
import com.example.demo.Purchase.EpisodePurchaseService;
import com.example.demo.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final EpisodePurchaseService purchaseService;

    @GetMapping("/articles/new")
    public String newArticle() {
        return "article/form";
    }

    @PostMapping("/articles")
    public String create(@ModelAttribute ArticleRequest request,
                         @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }
        Long id = articleService.create(request, user);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}")
    public String show(@PathVariable Long id,
                       Model model,
                       @AuthenticationPrincipal User user) {
        ArticleResponse article = articleService.findById(id);
        Long userId = user == null ? null : user.getId();
        boolean purchased = userId != null && purchaseService.hasPurchased(userId, id);
        boolean isAuthor = userId != null && userId.equals(article.getAuthorId());
        boolean locked = article.isPaid() && !purchased && !isAuthor;

        model.addAttribute("article", locked ? article.withoutContent() : article);
        model.addAttribute("locked", locked);
        model.addAttribute("loggedIn", user != null);
        model.addAttribute("episodePrice", EpisodePurchaseService.EPISODE_PRICE);

        List<CommentResponse> commentList = commentService.findByArticleId(id);
        model.addAttribute("commentList", commentList);
        return "article/show";
    }

    @PostMapping("/articles/{id}/purchase")
    public String purchase(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }
        ArticleResponse article = articleService.findById(id);
        if (user.getId().equals(article.getAuthorId())) {
            return "redirect:/articles/" + id;
        }
        purchaseService.purchase(user.getId(), id);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles")
    public String showAll(Model model,
                          @AuthenticationPrincipal User user,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(defaultValue = "id") String sortBy,
                          @RequestParam(defaultValue = "desc") String direction) {
        Page<ArticleResponse> articlePage = articleService.findAll(page, size, keyword, sortBy, direction);
        model.addAttribute("articlePage", articlePage);
        model.addAttribute("currentUserId", user == null ? null : user.getId());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", articlePage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        return "article/showAll";
    }

    @GetMapping("/articles/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article/edit";
    }

    @PutMapping("/articles/{id}")
    public String update(@PathVariable Long id, @ModelAttribute ArticleRequest request) {
        articleService.update(id, request);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String delete(@PathVariable Long id) {
        articleService.deleteById(id);
        return "redirect:/articles";
    }
}
