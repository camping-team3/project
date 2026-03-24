package com.camping.erp.domain.qna;

import com.camping.erp.domain.qna.enums.QnaCategory;
import com.camping.erp.domain.user.User;
import lombok.Getter;
import lombok.Setter;

public class QnaRequest {

    @Getter @Setter
    public static class SaveDTO {
        private String title;
        private String content;
        private QnaCategory category;

        public Qna toEntity(User user) {
            return Qna.builder()
                    .user(user)
                    .title(title)
                    .content(content)
                    .category(category)
                    .build();
        }
    }

    @Getter @Setter
    public static class UpdateDTO {
        private String title;
        private String content;
        private QnaCategory category;
    }
}
