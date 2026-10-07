package org.sopt;

import org.sopt.controller.PostController;
import org.sopt.repository.PostRepository;
import org.sopt.service.PostService;
import org.sopt.view.PostView;

import java.util.NoSuchElementException;

public class Main {

    public static void main(String[] args) {
        PostView postView = new PostView();
        PostRepository postRepository = new PostRepository();
        PostService postService = new PostService(postRepository);

        PostController postController =
                new PostController(postView, postService);

        while (true) {
            try {
                int command = postView.readCommand();

                switch (command) {
                    case 1:
                        postController.writePost();
                        break;

                    case 2:
                        postController.readPostList();
                        break;

                    case 3:
                        postController.readPost();
                        break;

                    case 4:
                        postController.editPost();
                        break;

                    case 5:
                        postController.removePost();
                        break;

                    case 6:
                        postController.quitProgram();
                        return;

                    default:
                        postController.wrongInput();
                }
            } catch (IllegalArgumentException | NoSuchElementException e) {
                postView.printMessage("오류: " + e.getMessage());
            }
        }
    }
}