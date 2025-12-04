package ru.skypro.homework;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.controller.*;

@SpringBootTest()
class HomeworkApplicationTests {

    @Autowired
    private UserController userController;

    @Autowired
    private CommentsController commentsController;

    @Autowired
    private AuthController authController;

    @Autowired
    private AdsController adsController;



    @Test
    void contextLoads() {
        Assertions.assertThat(userController).isNotNull();
        Assertions.assertThat(commentsController).isNotNull();
        Assertions.assertThat(authController).isNotNull();
        Assertions.assertThat(adsController).isNotNull();
    }

}
