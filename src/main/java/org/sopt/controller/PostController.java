package org.sopt.controller;

import org.sopt.model.Post;
import org.sopt.view.PostView;

import java.util.ArrayList;
import java.util.List;

public class PostController {

    private final PostView postView;
    private final List<Post> posts = new ArrayList<>();

    public PostController(PostView postView) {
        this.postView = postView;
    }

    // 게시글 작성
    public void writePost() {
        String title = postView.readText("제목: ");
        String content = postView.readText("내용: ");

        Post post = new Post(title, content);
        posts.add(post);

        postView.printMessage("게시글이 작성되었습니다.");
    }

    // 게시글 목록 조회
    public void readPostList() {
        postView.printMessage("\n=== 게시글 목록 ===");

        if (posts.isEmpty()) {
            postView.printMessage("게시글이 없습니다.");
            return;
        }

        for (int i = 0; i < posts.size(); i++) {
            Post currentPost = posts.get(i);
            postView.printMessage((i + 1) + ". " + currentPost.getTitle());
        }
    }

    // 게시글 단건 조회
    public void readPost(){
        if (posts.isEmpty()) {
            postView.printMessage("게시글이 없습니다.");
            return;
        }

        int readIndex = postView.readPostNumber("조회할 게시글 번호: ");

        if (readIndex < 0 || readIndex >= posts.size()) {
            postView.printMessage("존재하지 않는 게시글입니다.");
            return;
        }

        Post readPost = posts.get(readIndex);

        postView.printMessage("\n=== 게시글 ===");
        postView.printMessage("제목: " + readPost.getTitle());
        postView.printMessage("내용: " + readPost.getContent());
    }

    // 게시글 수정
    public void editPost(){
        if (posts.isEmpty()) {
            postView.printMessage("게시글이 없습니다.");
            return;
        }

        int updateIndex = postView.readPostNumber("수정할 게시글 번호: ");

        if (updateIndex < 0 || updateIndex >= posts.size()) {
            postView.printMessage("존재하지 않는 게시글입니다.");
            return;
        }

        Post updatePost = posts.get(updateIndex);

        String newTitle = postView.readText("새로운 제목: ");

        String newContent = postView.readText("새로운 내용: ");

        updatePost.update(newTitle, newContent);

        postView.printMessage("게시글이 수정되었습니다.");
    }

    // 게시글 삭제
    public void removePost(){
        // 게시글 삭제
        if (posts.isEmpty()) {
            postView.printMessage("게시글이 없습니다.");
            return;
        }

        int deleteIndex = postView.readPostNumber("삭제할 게시글 번호: ");

        if (deleteIndex < 0 || deleteIndex >= posts.size()) {
            postView.printMessage("존재하지 않는 게시글입니다.");
            return;
        }

        posts.remove(deleteIndex);

        postView.printMessage("게시글이 삭제되었습니다.");
    }

    // 프로그램 종료
    public void quitProgram(){
        postView.printMessage("프로그램을 종료합니다.");
    }

    // 잘못된 입력
    public void wrongInput(){
        postView.printMessage("잘못된 입력입니다.");
    }
}