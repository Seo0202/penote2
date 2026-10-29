package com.example.demo.Comment;

import com.example.demo.Article.ArticleResponse;
import com.example.demo.Article.ArticleService;
import com.example.demo.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {
 private final CommentRepository commentRepository;
 private final CommentService commentService;
 private final ArticleService articleService;

    @PostMapping("/articles/{id}/comments")
    public String createComment(@PathVariable Long id,
                                @RequestParam String content,
                                @AuthenticationPrincipal User user)
    {
        // 로그인하지 않았다면 user는 null
        Long userId = (user != null) ? user.getId() : null;
        commentService.save(id, content, userId);

        return "redirect:/articles/" + id;
    }

    @GetMapping("/comments/{id}/edit")
    public String editComment(@PathVariable Long id, Model model) {

        CommentResponse comment = commentService.findById(id);

        model.addAttribute("comment", comment);

        return "comment/edit";
    }
    @PutMapping("/comments/{id}")
    public String updateComment(@PathVariable Long id,
                                @ModelAttribute CommentRequest request, Model model) {

        commentService.update(id, request);
        ArticleResponse article = articleService.findById(request.getArticleId());
        List<CommentResponse> commentList =
                commentService.findByArticleId(request.getArticleId());

        model.addAttribute("article", article);
        model.addAttribute("commentList", commentList);

        return "article/show";
    }

    @DeleteMapping("/comments/{id}")
    public String delete(@PathVariable Long id) {
        Long articleId = commentService.findArticleIdByCommentId(id); // 삭제 전에 먼저 조회
        commentService.deleteById(id);
        return "redirect:/articles/" + articleId;
    }
}
