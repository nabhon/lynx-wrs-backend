package com.lynx.lynx_wrs.http.domain.dashboard;

import com.lynx.lynx_wrs.http.domain.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/overview")
    public ResponseEntity<?> getOverview() {
        return ResponseEntity.ok(dashboardService.getOverviewStats());
    }

    @GetMapping("/user-growth")
    public ResponseEntity<?> getUserGrowth(@RequestParam int year) {
        return ResponseEntity.ok(dashboardService.getUserGrowth(year));
    }
}
