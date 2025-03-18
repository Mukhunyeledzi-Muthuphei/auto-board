package com.example.autoboard.service;

import com.example.autoboard.entity.Comment;
import com.example.autoboard.entity.User;
import com.example.autoboard.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public Comment createComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(Long id) {
        return commentRepository.findById(id);
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment updateComment(Comment comment, User user) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id, User user) {
        commentRepository.deleteById(id);
    }

    public List<Comment> getCommentsByTaskId(long l, User user) {
        return commentRepository.findByTaskId(l);
    }
}