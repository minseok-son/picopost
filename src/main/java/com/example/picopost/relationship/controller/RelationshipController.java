package com.example.picopost.relationship.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.picopost.relationship.dto.FollowRequest;

@RestController
@RequestMapping("api/v1/relationship")
public class RelationshipController {

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(@RequestBody FollowRequest followRequest) {
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/unfollow/{followerId}/{followeeId}")
    public ResponseEntity<Void> unfollow(@PathVariable Long followerId, @PathVariable Long followeeId) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
