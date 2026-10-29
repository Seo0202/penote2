package com.example.demo.Comment;

import com.example.demo.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private static final int COMMENT_REWARD_STARS = 10;

    private final CommentRepository commentRepository;
    private final UserService userService;

    // 댓글 저장 + 별 적립을 한 트랜잭션으로 처리 (둘 중 하나가 실패하면 함께 롤백)
    // userId가 null이면(비로그인) 댓글만 저장하고 별은 적립하지 않는다.
    @Transactional
    public void save(Long articleId, String content, Long userId) {

        Comment comment = new Comment(articleId, content);
        commentRepository.save(comment);

        boolean hasContent = content != null && !content.isBlank();
        if (userId != null && hasContent) {
            userService.addStars(userId, COMMENT_REWARD_STARS);
        }
    }

    public List<CommentResponse> findByArticleId(Long articleId)
    {
        return commentRepository.findByArticleId(articleId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }


    public CommentResponse findById(Long id) {
        CommentResponse commentResponse = CommentResponse.from(commentRepository.findById(id).orElse(null));
        return commentResponse;
    }

    public void update(Long id, CommentRequest request) {

        Comment comment = commentRepository.findById(id)
                .orElseThrow(()  ->
                        new IllegalArgumentException("댓글이 없습니다. id=" + id));
        //entity를 가져옴
        comment.update(request.getContent());
        //entity가 update(내용은 dto임)됨
        commentRepository.save(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public Long findArticleIdByCommentId(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다. id=" + id))
                .getArticleId();
    }
}
