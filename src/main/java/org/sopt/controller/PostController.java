package org.sopt.controller;

import org.sopt.model.Category;
import org.sopt.model.Post;
import org.sopt.service.PostService;
import org.sopt.view.PostView;

import java.util.List;

public class PostController {

    private final PostView postView;
    private final PostService postService;

    public PostController(PostView postView, PostService postService) {
        this.postView = postView;
        this.postService = postService;
    }

    public void writePost() {
        String title = postView.readText("제목: ");
        String content = postView.readText("내용: ");
        Category category = postView.readCategory();

        postService.writePost(title, content, category);

        postView.printMessage("게시글이 작성되었습니다.");
    }

    public void readPostList() {
        List<Post> posts = postService.readPostList();

        postView.printMessage("\n=== 게시글 목록 ===");

        if (posts.isEmpty()) {
            postView.printMessage("게시글이 없습니다.");
            return;
        }

        for (int i = 0; i < posts.size(); i++) {
            postView.printMessage(
                    (i + 1) + ". " + posts.get(i).getTitle()
            );
        }
    }

    public void readPost() {
        int index = postView.readPostNumber("조회할 게시글 번호: ");
        Post post = postService.readPost(index);

        postView.printMessage("\n=== 게시글 ===");
        postView.printMessage("제목: " + post.getTitle());
        postView.printMessage("내용: " + post.getContent());
        postView.printMessage("카테고리: " + post.getCategory());
        postView.printMessage("작성 시간: " + post.getCreatedAt());
    }

    public void editPost() {
        int index = postView.readPostNumber("수정할 게시글 번호: ");

        postService.readPost(index);

        String title = postView.readText("새로운 제목: ");
        String content = postView.readText("새로운 내용: ");

        postService.editPost(index, title, content);

        postView.printMessage("게시글이 수정되었습니다.");
    }

    public void removePost() {
        int index = postView.readPostNumber("삭제할 게시글 번호: ");

        postService.removePost(index);

        postView.printMessage("게시글이 삭제되었습니다.");
    }

    public void quitProgram() {
        postView.printMessage("프로그램을 종료합니다.");
    }

    public void wrongInput() {
        postView.printMessage("잘못된 입력입니다.");
    }
}