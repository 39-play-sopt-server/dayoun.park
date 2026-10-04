package org.sopt;

import org.sopt.controller.PostController;
import org.sopt.view.PostView;

public class Main {

    public static void main(String[] args) {


        PostView postView = new PostView();
        PostController postController = new PostController(postView);

        while (true) {
            int command = postView.readCommand();

            switch (command) {
                case 1:
                    // 게시글 작성
                    postController.writePost();
                    break;

                case 2:
                    postController.readPostList();
                    break;

                case 3:
                    postController.readPost();
                    break;

                case 4:
                    // 게시글 수정
                    postController.editPost();
                    break;

                case 5:
                    // 게시글 삭제
                    postController.removePost();
                    break;

                case 6:
                    postController.quitProgram();
                    return;

                default:
                    postController.wrongInput();
            }
        }
    }
}