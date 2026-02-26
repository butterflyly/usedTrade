package com.example.usedTrade.Controller.API.Alram;

import com.example.usedTrade.Service.Alram.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/{id}/read")
    public ResponseEntity<Void> read(@PathVariable Long id,
                                     @AuthenticationPrincipal UserDetails userDetails) {

        notificationService.read(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}