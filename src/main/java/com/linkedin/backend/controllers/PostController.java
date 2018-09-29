package com.linkedin.backend.controllers;

import com.linkedin.backend.dto.CommentDTO;
import com.linkedin.backend.dto.PostDTO;
import com.linkedin.backend.entities.like.Like;
import com.linkedin.backend.entities.post.Post;
import com.linkedin.backend.entities.post.PostService;
import com.linkedin.backend.entities.post.PostType;
import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.entities.user.AppUserService;
import com.linkedin.backend.handlers.exception.FileStorageException;
import com.linkedin.backend.handlers.exception.PostNotFoundException;
import com.linkedin.backend.handlers.exception.UserNotFoundException;
import com.linkedin.backend.knn.PostKNNService;
import com.linkedin.backend.storage.ContentService;
import com.linkedin.backend.storage.File;
import com.linkedin.backend.storage.FileStorageService;
import com.linkedin.backend.utils.JSONReturn;
import com.linkedin.backend.utils.JWTUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class PostController {
    private final AppUserService appUserService;
    private final FileStorageService fileStorageService;
    private final PostService postService;
    private final ContentService contentService;
    private final PostKNNService postKnnService;

    public PostController(AppUserService appUserService, FileStorageService fileStorageService, PostService postService, ContentService contentService, PostKNNService postKnnService) {
        this.appUserService = appUserService;
        this.fileStorageService = fileStorageService;
        this.postService = postService;
        this.contentService = contentService;
        this.postKnnService = postKnnService;
    }

    @GetMapping("/post/{postId}")
    public PostDTO getPost(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer postId)
                                                                                     throws UserNotFoundException, PostNotFoundException{
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Post post = postService.findPostById(postId);

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

        return new JSONReturn<>(newPost.getId());
    }

    @PostMapping("/post/media")
    public JSONReturn<Integer> createImagePost(@Valid @RequestHeader(value="Authorization") String auth,
                                               @RequestPart("file") MultipartFile file,
                                               @RequestPart("post") PostDTO post) throws UserNotFoundException, FileStorageException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        // Create file and store metadata in DB
        String fileName = fileStorageService.storeFile(file);

        File fileMetadata = new File();

        fileMetadata.setContentId(fileName);
        fileMetadata.setOwner(user);
        fileMetadata.setContentLength(file.getSize());
        fileMetadata.setMimeType(file.getContentType());

        fileMetadata = contentService.addFile(fileMetadata);

        Post newPost = new Post();

        newPost.setType(PostType.MEDIA);
        newPost.setBody(post.getBody());
        newPost.setUser(user);
        newPost.setFile(fileMetadata);

        newPost.setUser(user);
        newPost = postService.savePost(newPost);

        return new JSONReturn<>(newPost.getId());
    }

    @PostMapping("/post/comment/{postId}")
    public CommentDTO postComment(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer postId, @Valid @RequestBody CommentDTO comment)
                                                                                            throws UserNotFoundException, PostNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return new CommentDTO(postService.postComment(postId, user, comment));
    }

    @PostMapping("/post/toggle-like/{postId}")
    public JSONReturn<Integer> toggleLike(@Valid @RequestHeader(value="Authorization") String auth, @Valid @PathVariable Integer postId)
                                                                                         throws UserNotFoundException, PostNotFoundException{
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        Post post = postService.findPostById(postId);

        if (!user.getLikes().removeIf(p -> p.getUser().getId().equals(user.getId()) && p.getPost().getId().equals(post.getId())))
            user.getLikes().add(new Like(user, post));

        appUserService.addUser(user);

        return new JSONReturn<>(user.getId());
    }

    @GetMapping("/feed")
    public List<Integer> getUserFeed(@Valid @RequestHeader(value="Authorization") String auth) throws UserNotFoundException {
        JWTUtils token = new JWTUtils(auth);
        AppUser user = appUserService.findUserById(token.getUserID());

        return postKnnService.generateUserKNN(user).stream().map(Post::getId).collect(Collectors.toList());
    }
}
