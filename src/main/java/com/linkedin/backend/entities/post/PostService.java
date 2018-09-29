package com.linkedin.backend.entities.post;

import com.linkedin.backend.entities.comment.Comment;
import com.linkedin.backend.entities.comment.CommentRepository;
import com.linkedin.backend.entities.connection.ConnectionService;
import com.linkedin.backend.dto.CommentDTO;
import com.linkedin.backend.entities.user.AppUserService;
import com.linkedin.backend.entities.user.AppUser;
import com.linkedin.backend.handlers.exception.PostNotFoundException;
import org.springframework.stereotype.Service;

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
