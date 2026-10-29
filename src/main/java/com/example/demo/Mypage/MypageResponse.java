package com.example.demo.Mypage;

import com.example.demo.User.User;
import lombok.Getter;

@Getter
public class MypageResponse {

    private Long id;
    private String email;
    private String nickname;
    private String profileImageUrl;
    private int starCount;

    public MypageResponse(
            Long id,
            String email,
            String nickname,
            String profileImageUrl,
            int starCount
    ) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.starCount = starCount;
    }

    public static MypageResponse from(User user) {
        return new MypageResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname(),
                user.getProfileImageUrl(),
                user.getStarCount()
        );
    }
}