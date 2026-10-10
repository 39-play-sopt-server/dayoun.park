package org.sopt.controller;

import org.sopt.model.Category;
import org.sopt.model.Post;
import org.sopt.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/posts")
public class PostController {

    // 생성자를 이용한 의존성 주입
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public String writePost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") Category category
    )
    {
        postService.writePost(title, content, category);
        return "게시글이 작성되었습니다.";
    }

    @GetMapping
    public List<Post> readPostList() {
        return postService.readPostList();
    }

    @GetMapping("/{postId}")
    public Post readPost(@PathVariable("postId") int postId) {
        return postService.readPost(postId - 1);
    }

    @PutMapping("/{postId}")
    public String editPost(
            @PathVariable("postId") int postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content
    ) {
        postService.editPost(postId-1, title, content);
        return "게시글이 수정되었습니다.";
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> removePost(
            @PathVariable("postId") int postId
    ) {
        postService.removePost(postId-1);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("삭제 완료");
    }
}