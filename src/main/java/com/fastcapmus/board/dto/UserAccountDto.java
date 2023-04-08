package com.fastcapmus.board.dto;

import com.fastcapmus.board.domain.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        String createdBy,
        LocalDateTime createdAt,
        String modifiedBy,
        LocalDateTime modifiedAt
) {

    public static UserAccountDto of(String userId, String userPassword, String email, String nickname, String memo, String createdBy, LocalDateTime createdAt, String modifiedBy, LocalDateTime modifiedAt) {
        return new UserAccountDto(userId, userPassword, email, nickname, memo, createdBy, createdAt, modifiedBy, modifiedAt);
    }

    public static UserAccountDto of(String userId, String userPassword, String email, String nickname, String memo) {
        return new UserAccountDto(userId, userPassword, email, nickname, memo, null, null, null, null);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getUserId(),
                entity.getUserPassword(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getMemo(),
                entity.getCreatedBy(),
                entity.getCreatedAt(),
                entity.getModifiedBy(),
                entity.getModifiedAt()
        );
    }

    public UserAccount toEntity() {
        return UserAccount.of(userId, userPassword, email, nickname, memo);
    }
}
