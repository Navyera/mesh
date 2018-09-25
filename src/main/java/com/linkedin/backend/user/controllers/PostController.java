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
import com.linkedin.backend.utils.JSONReturn;
import com.linkedin.backend.utils.JSONStatus;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<Integer> getUserFeed(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        List<Post> userFeed = user.getUserFeed();

        userFeed.sort((Comparator.comparing(Post::getDate)).reversed());

        return userFeed.stream().map(Post::getId).collect(Collectors.toList());
    }

    @GetMapping("/post/{postId}")
    public PostDTO getPost(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer postId)
                                                                                     throws UserNotFoundException, PostNotFoundException{
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Post post = postService.findPostById(postId);

        // TODO: Check if users are connected (or are the same person.

        return new PostDTO(post, user);
    }

    @PostMapping("/post/text")
    public JSONReturn<Integer> createTextPost(@Valid @RequestHeader(value="Authorization") String auth, @Valid @RequestBody PostDTO post) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Post newPost = new Post();

        newPost.setType(PostType.TEXT);
        newPost.setBody(post.getBody());
        newPost.setUser(user);

        newPost.setUser(user);

        newPost = postService.savePost(newPost);

        return new JSONReturn<Integer>(newPost.getId());
    }

    @PostMapping("/post/comment/{postId}")
    public JSONStatus postComment(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer postId, @Valid @RequestBody CommentDTO comment)
                                                                                            throws UserNotFoundException, PostNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        postService.postComment(postId, user, comment);

        return new JSONStatus("Comment was successfully submitted");
    }

    @PostMapping("/post/toggle-like/{postId}")
    public JSONReturn<Integer> toggleLike(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer postId)
                                                                                         throws UserNotFoundException, PostNotFoundException{
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Post post = postService.findPostById(postId);

        if (!user.getLikedPosts().removeIf(p -> p.getId().equals(post.getId())))
            user.getLikedPosts().add(post);

        appUserService.addUser(user);

        return new JSONReturn<>(user.getId());
    }
}
