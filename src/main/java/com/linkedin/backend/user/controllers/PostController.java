package com.linkedin.backend.user.controllers;

import com.linkedin.backend.dto.PostDTO;
import com.linkedin.backend.post.Post;
import com.linkedin.backend.post.PostType;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class PostController {
    private final AppUserService appUserService;

    public PostController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/feed")
    public List<PostDTO> getUserFeed(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        List<PostDTO> userFeed = user.getUserFeed();

        userFeed.sort((Comparator.comparing(PostDTO::getDate)));

        return userFeed;
    }

    @PostMapping("/post/text")
    public JSONStatus createTextPost(@Valid @RequestHeader(value="Authorization") String auth, @Valid @RequestBody PostDTO post) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Post newPost = new Post();

        newPost.setType(PostType.TEXT);
        newPost.setBody(post.getBody());
        newPost.setUser(user);

        user.addPost(newPost);

        appUserService.addUser(user);

        return new JSONStatus("Post was successfully created.");
    }
}
