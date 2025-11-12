package com.example.picopost.relationship.dto;

import lombok.Data;

@Data
public class FollowRequest {
    private Long followerId;
    private Long followeeId;
}
