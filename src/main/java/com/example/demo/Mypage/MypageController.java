package com.example.demo.Mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.demo.User.User;

@Controller
@RequiredArgsConstructor
public class MypageController {
    private final MypageService mypageService;

    @GetMapping("/mypage/{id}")
    public String mypage(@PathVariable Long id,
                         @AuthenticationPrincipal User user,
                         Model model) {
        if (user == null || !user.getId().equals(id)) {
            return "redirect:/login";
        }
        model.addAttribute("user_info", mypageService.findById(id));
        model.addAttribute("purchasedEpisodes", mypageService.findPurchasedEpisodes(id));
        return "mypage";
    }

    @PostMapping("/mypage/{id}/profile-image")
    public String updateProfileImage(@PathVariable Long id,
                                     @RequestParam("profileImage") MultipartFile profileImage,
                                     @AuthenticationPrincipal User user) {
        if (user == null || !user.getId().equals(id)) {
            return "redirect:/login";
        }
        mypageService.updateProfileImage(id, profileImage);
        return "redirect:/mypage/" + id;
    }
}
