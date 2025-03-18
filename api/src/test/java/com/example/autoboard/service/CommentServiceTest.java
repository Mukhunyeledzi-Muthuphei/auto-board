// package com.example.autoboard.service;

// import com.example.autoboard.entity.Comment;
// import com.example.autoboard.repository.CommentRepository;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class CommentServiceTest {

//     @Mock
//     private CommentRepository commentsRepository;

//     @InjectMocks
//     private CommentService commentService;

//     private Comment comment1, comment2;

//     @BeforeEach
//     void setUp() {
//         comment1 = new Comment();
//         comment1.setId(1L);
//         comment1.setContent("Test comment 1");
//         comment1.setUserId(10L);
//         comment1.setTaskId(100L);
//         comment1.setCreatedAt(LocalDateTime.now().minusHours(1));

//         comment2 = new Comment();
//         comment2.setId(2L);
//         comment2.setContent("Test comment 2");
//         comment2.setUserId(20L);
//         comment2.setTaskId(100L);
//         comment2.setCreatedAt(LocalDateTime.now());
//     }

//     @Test
//     void createComment_shouldSaveCommentAndSetCreatedAt() {
//         when(commentsRepository.save(any(Comment.class))).thenReturn(comment1);

//         Comment savedComment = commentService.createComment(comment1);

//         assertNotNull(savedComment.getCreatedAt());
//         verify(commentsRepository, times(1)).save(any(Comment.class));
//     }

//     @Test
//     void getCommentById_shouldReturnCommentIfExists() {
//         when(commentsRepository.findById(1L)).thenReturn(Optional.of(comment1));

//         Optional<Comment> foundComment = commentService.getCommentById(1L);

//         assertTrue(foundComment.isPresent());
//         assertEquals(comment1.getContent(), foundComment.get().getContent());
//     }

//     @Test
//     void getCommentById_shouldReturnEmptyOptionalIfNotExists() {
//         when(commentsRepository.findById(1L)).thenReturn(Optional.empty());

//         Optional<Comment> foundComment = commentService.getCommentById(1L);

//         assertFalse(foundComment.isPresent());
//     }

//     @Test
//     void getAllComments_shouldReturnListOfComments() {
//         when(commentsRepository.findAll()).thenReturn(Arrays.asList(comment1, comment2));

//         List<Comment> comments = commentService.getAllComments();

//         assertEquals(2, comments.size());
//         assertEquals(comment1.getContent(), comments.get(0).getContent());
//         assertEquals(comment2.getContent(), comments.get(1).getContent());
//     }

//     @Test
//     void updateComment_shouldSaveUpdatedComment() {
//         when(commentsRepository.save(comment1)).thenReturn(comment1);

//         Comment updatedComment = commentService.updateComment(comment1);

//         assertEquals(comment1.getContent(), updatedComment.getContent());
//         verify(commentsRepository, times(1)).save(comment1);
//     }

//     @Test
//     void deleteComment_shouldDeleteCommentById() {
//         commentService.deleteComment(1L);

//         verify(commentsRepository, times(1)).deleteById(1L);
//     }

//     @Test
//     void getCommentsByTaskId_shouldReturnCommentsByTaskId() {
//         when(commentsRepository.findByTaskId(100L)).thenReturn(Arrays.asList(comment1, comment2));

//         List<Comment> comments = commentService.getCommentsByTaskId(100L);

//         assertEquals(2, comments.size());
//         assertEquals(comment1.getContent(), comments.get(0).getContent());
//         assertEquals(comment2.getContent(), comments.get(1).getContent());
//     }
// }