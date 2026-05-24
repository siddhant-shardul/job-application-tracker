package com.siddhant.backend.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalStudents;

    private long totalCompanies;

    private long totalApplications;

    private long appliedCount;

    private long shortlistedCount;

    private long interviewCount;

    private long selectedCount;

    private long rejectedCount;
}