package com.example.springproject.web;

import com.example.springproject.dao.DataCollectorDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/fastjson/dataCollector")
public class DataCollectorController {

    @Autowired
    DataCollectorDao dataCollectorDao;

    @GetMapping("/collectContributors")
    public void collectContributors(){
        System.out.println("Collecting contributors...");
        dataCollectorDao.collectContributors();
        System.out.println("Collecting contributors completed!");
    }

    @GetMapping("/collectIssues")
    public void collectIssues(){
        System.out.println("Collecting issues...");
        dataCollectorDao.collectIssues();
        System.out.println("Collecting issues completed!");
    }

    @GetMapping("/collectReleases")
    public void collectReleases(){
        System.out.println("Collecting releases...");
        dataCollectorDao.collectReleases();
        System.out.println("Collecting releases completed!");
    }

    @GetMapping("/collectCommits")
    public void collectCommits(){
        System.out.println("Collecting commits...");
        dataCollectorDao.collectCommits();
        System.out.println("Collecting commits completed!");
    }

    @GetMapping("/collectComments")
    public void collectComments(){
        System.out.println("Collecting comments...");
        dataCollectorDao.collectComments();
        System.out.println("Collecting comments completed!");
    }

    @GetMapping("/collectKeywords")
    public void collectKeywords(){
        System.out.println("Collecting keywords...");
        dataCollectorDao.collectKeyWordsInIssueTitles();
        dataCollectorDao.collectKeyWordsInIssueDescriptions();
        System.out.println("Collecting keywords completed!");
    }




}
