package com.example.springproject.service;

import com.example.springproject.domain.Developer;

import java.util.List;
import java.util.Map;

public interface RepoForArthasService {

    Integer getDeveloperNums();

    List<Developer> getTop10ActiveDevelopers();

    Integer getOpenIssueNums();

    Integer getClosedIssueNums();

    Double[] getTypicalIssueResolutionTime();

    Integer getReleaseNums();

    List[] getCommitNumsBetweenReleases();

    Integer getCommitNums();

    Integer[] getCommitNumsDistributionInDayOfWeek();

    Integer[] getCommitNumsDistributionInTimeOfDay();

    Map<String,Integer> getTop25Keywords();

    Map<String, Integer> getTop5Topic();
}
