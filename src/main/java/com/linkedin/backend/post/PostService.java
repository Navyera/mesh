package com.linkedin.backend.post;

import com.linkedin.backend.connection.ConnectionService;
import com.linkedin.backend.dto.CommentDTO;
import com.linkedin.backend.user.AppUserService;
import com.linkedin.backend.user.dao.AppUser;
import com.linkedin.backend.user.handlers.PostNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final AppUserService appUserService;
    private final ConnectionService friendService;
    private final CommentRepository commentRepository;

    public PostService(PostRepository postRepository, AppUserService appUserService, ConnectionService friendService, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.appUserService = appUserService;
        this.friendService = friendService;
        this.commentRepository = commentRepository;
    }


    public Post findPostById(Integer id) throws PostNotFoundException {
        return postRepository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }

    public Comment postComment(Integer postId, AppUser user, CommentDTO commentDto) throws PostNotFoundException{
        Post post = findPostById(postId);

        Comment comment = new Comment();

        comment.setUser(user);
        comment.setBody(commentDto.getBody());
        comment.setPost(post);

        comment.setUser(user);

        return commentRepository.save(comment);
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }
}
