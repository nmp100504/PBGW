package com.example.BuildPC.controller;

import com.example.BuildPC.dto.PostDto;
import com.example.BuildPC.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
public class StatisticsController {

    @Autowired
    private PostService postService;

    @GetMapping("/posts/statistics")
    public List<PostDto> getPostsStatistics(@RequestParam String period) {
        LocalDateTime startDateTime;
        LocalDateTime endDateTime = LocalDateTime.now();

        switch (period.toLowerCase()) {
            case "day":
                startDateTime = LocalDateTime.now().toLocalDate().atStartOfDay();
                break;
            case "week":
                startDateTime = LocalDateTime.now().with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY)).toLocalDate().atStartOfDay();
                break;
            case "month":
                startDateTime = LocalDateTime.now().with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
                break;
            default:
                throw new IllegalArgumentException("Invalid period: " + period);
        }

        return postService.findPostsByDateRange(startDateTime, endDateTime);
    }
}
