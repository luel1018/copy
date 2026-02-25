package com.example.demo.board.model;

import com.example.demo.reply.model.ReplyDto;
import lombok.*;

import java.util.List;

public class BoardDto {
    @Getter
    public static class RegReq {
        private String title;
        private String contents;

        public Board toEntity() {
            return Board.builder()
                    .title(this.title)
                    .contents(this.contents)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class RegRes {
        private Long idx;
        private String title;
        private String contents;
        private String writer;

        public static RegRes from(Board entity) {
            return RegRes.builder()
                    .idx(entity.getIdx())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .writer(entity.getUser() == null ? null : entity.getUser().getName())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ListRes {
        private Long idx;
        private String title;
        private String writer;
        private int replyCount;

        public static ListRes from(Board entity) {
            int count = (entity.getReplyList() == null) ? 0 : entity.getReplyList().size();

            return ListRes.builder()
                    .idx(entity.getIdx())
                    .title(entity.getTitle())
                    .writer(entity.getUser() == null ? null : entity.getUser().getName())
                    .replyCount(count)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ReadRes {
        private Long idx;
        private String title;
        private String contents;
        private String writer;
        private List<ReplyDto.ListRes> replyList;

        public static ReadRes from(Board entity) {
            List<ReplyDto.ListRes> replies = (entity.getReplyList() == null)
                    ? List.of()
                    : entity.getReplyList().stream().map(ReplyDto.ListRes::from).toList();

            return ReadRes.builder()
                    .idx(entity.getIdx())
                    .title(entity.getTitle())
                    .contents(entity.getContents())
                    .writer(entity.getUser() == null ? null : entity.getUser().getName())
                    .replyList(replies)
                    .build();
        }
    }
}