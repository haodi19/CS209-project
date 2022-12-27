package com.example.springproject.web;

import com.example.springproject.domain.Developer;
import com.example.springproject.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/fastjson/repo")
public class RepoController {
    @Autowired
    private RepoService repoService;

    @GetMapping("/developerNums")
    public Integer getDevelopNums(){
        return repoService.getDeveloperNums();
    }

    @GetMapping("/top10ActiveDevelopers")
    public List<Developer> getTop10ActiveDevelopers(){
        return repoService.getTop10ActiveDevelopers();
    }

    @GetMapping("/openIssueNums")
    public Integer getOpenIssueNums(){
        return repoService.getOpenIssueNums();
    }

    @GetMapping("/closedIssueNums")
    public Integer getClosedIssueNums(){
        return repoService.getClosedIssueNums();
    }

    @GetMapping("/typicalIssueResolutionTime")
    public Double[] getTypicalIssueResolutionTime() {
        return repoService.getTypicalIssueResolutionTime();
    }

    @GetMapping("/releaseNums")
    public Integer getReleaseNums(){
        return repoService.getReleaseNums();
    }

    @GetMapping("/commitNumsBetweenReleases")
    public List[] getCommitNumsBetweenReleases(){
        return repoService.getCommitNumsBetweenReleases();
    }

    @GetMapping("/commitNums")
    public Integer getCommitNums(){
        return repoService.getCommitNums();
    }

    @GetMapping("/commitNumsDistributionInDayOfWeek")
    public Integer[] getCommitNumsDistributionInDayOfWeek(){
        return repoService.getCommitNumsDistributionInDayOfWeek();
    }

    @GetMapping("/commitNumsDistributionInTimeOfDay")
    public Integer[] getCommitNumsDistributionInTimeOfDay(){
        return repoService.getCommitNumsDistributionInTimeOfDay();
    }

    @GetMapping("/top25Keywords")
    public Map<String, Integer> getTop25Keywords(){
        return repoService.getTop25Keywords();
    }

    @GetMapping("/top5Topic")
    public Map<String, Integer> getTop5Topic(){
        return repoService.getTop5Topic();
    }
}
