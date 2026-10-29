package org.sopt.controller;

import org.sopt.dto.PostResponse;
import org.sopt.model.Category;
import org.sopt.model.Post;
import org.sopt.service.PostService;
import org.sopt.common.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@Tag(name = "게시글", description = "게시글 작성, 조회, 수정, 삭제 API")
@RestController
@RequestMapping(path = "/posts")
public class PostController {

    // 생성자를 이용한 의존성 주입
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary= "게시글 작성")
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> writePost(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("category") Category category,
            @RequestParam("memberId") Long memberId
    )
    {
        postService.writePost(title, content, category, memberId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>success(
                        "POST_CREATED",
                        "게시글이 작성되었습니다.",
                        null
                ));
    }

    @Operation(summary= "게시글 목록 조회")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostResponse>>> readPostList() {
        List<PostResponse> posts = postService.readPostList();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "POST_LIST_READ",
                        "게시글 목록을 조회했습니다.",
                        posts
                )
        );
    }

    @Operation(
            summary = "게시글 상세 조회",
            description = "게시글을 상세 조회하면 조회수가 1 증가합니다."
    )
    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostResponse>> readPost(
            @PathVariable("postId") Long postId
    ) {
        PostResponse postResponse = postService.readPost(postId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "POST_READ",
                        "게시글을 조회했습니다.",
                        postResponse
                )
        );
    }

    @Operation(summary = "게시글 제목과 본문 수정")
    @PatchMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> editPost(
            @PathVariable("postId") Long postId,
            @RequestParam("title") String title,
            @RequestParam("content") String content
    ) {
        postService.editPost(postId, title, content);

        return ResponseEntity.ok(
                ApiResponse.<Void>success(
                        "POST_UPDATED",
                        "게시글이 수정되었습니다.",
                        null
                )
        );
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<Void>> removePost(
            @PathVariable("postId") Long postId
    ) {
        postService.removePost(postId);

        return ResponseEntity.ok(
                ApiResponse.<Void>success(
                        "POST_DELETED",
                        "게시글이 삭제되었습니다.",
                        null
                )
        );
    }
}