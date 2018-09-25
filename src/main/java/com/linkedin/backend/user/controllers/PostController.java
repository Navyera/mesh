package com.linkedin.backend.user.controllers;

import com.linkedin.backend.dto.CommentDTO;
import com.linkedin.backend.dto.PostDTO;
import com.linkedin.backend.post.Post;
import com.linkedin.backend.user.handlers.PostNotFoundException;
import com.linkedin.backend.post.PostService;
import com.linkedin.backend.post.PostType;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.handlers.UserNotFoundException;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class PostController {
    private final AppUserService appUserService;
    private final PostService postService;

    public PostController(AppUserService appUserService, PostService postService) {
        this.appUserService = appUserService;
        this.postService = postService;
    }

    @GetMapping("/feed")
    public List<PostDTO> getUserFeed(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        List<PostDTO> userFeed = user.getUserFeed();

        userFeed.sort((Comparator.comparing(PostDTO::getDate)).reversed());

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

    @PostMapping("/post/comment/{postId}")
    public JSONStatus postComment(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer postId, @Valid @RequestBody CommentDTO comment)
                                                                                            throws UserNotFoundException, PostNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        postService.postComment(postId, user, comment);

        return new JSONStatus("Comment was successfully submitted");
    }
}
