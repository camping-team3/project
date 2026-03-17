package com.camping.erp.domain.qna;

import com.camping.erp.domain.qna.enums.QnaCategory;
import com.camping.erp.domain.user.User;

import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class QnaResponse {

    @Getter @Setter
    public static class ListDTO {
        private Long id;
        private String title;
        private String username;
        private String categoryName;
        private Integer hits;
        private String createdAt;
        private Boolean isAnswered;
        private Boolean isOwner; // 본인 글 여부

        public ListDTO(Qna qna, User sessionUser) {
            this.id = qna.getId();
            this.title = qna.getTitle();
            this.username = qna.getUser() != null ? qna.getUser().getUsername() : "알 수 없음";
            this.categoryName = qna.getCategory() != null ? qna.getCategory().getValue() : "기타";
            this.hits = qna.getHits() != null ? qna.getHits() : 0;
            this.createdAt = qna.getCreatedAt() != null ? qna.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "-";
            this.isAnswered = qna.getIsAnswered() != null && qna.getIsAnswered();
            
            // 본인 확인 로직
            this.isOwner = sessionUser != null && qna.getUser() != null && qna.getUser().getId().equals(sessionUser.getId());
        }
    }

    @Getter @Setter
    public static class DetailDTO {
        private Long id;
        private String title;
        private String content;
        private String username;
        private String categoryName;
        private QnaCategory category;
        private Integer hits;
        private String createdAt;
        private Boolean isAnswered;
        private Boolean isOwner; // 본인 글 여부
        private List<CommentDTO> comments;

        public DetailDTO(Qna qna, User sessionUser) {
            this.id = qna.getId();
            this.title = qna.getTitle();
            this.content = qna.getContent();
            this.username = qna.getUser() != null ? qna.getUser().getUsername() : "알 수 없음";
            this.categoryName = qna.getCategory() != null ? qna.getCategory().getValue() : "기타";
            this.category = qna.getCategory();
            this.hits = qna.getHits() != null ? qna.getHits() : 0;
            this.createdAt = qna.getCreatedAt() != null ? qna.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "-";
            this.isAnswered = qna.getIsAnswered() != null && qna.getIsAnswered();
            this.comments = qna.getComments() != null ? qna.getComments().stream().map(CommentDTO::new).toList() : List.of();
            
            // 본인 확인 로직
            this.isOwner = sessionUser != null && qna.getUser() != null && qna.getUser().getId().equals(sessionUser.getId());
        }

        @Getter @Setter
        public static class CommentDTO {
            private Long id;
            private String adminName;
            private String content;
            private String createdAt;

            public CommentDTO(Comment comment) {
                this.id = comment.getId();
                this.adminName = comment.getAdmin() != null ? comment.getAdmin().getUsername() : "관리자";
                this.content = comment.getContent();
                this.createdAt = comment.getCreatedAt() != null ? comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) : "-";
            }
        }
    }
}
