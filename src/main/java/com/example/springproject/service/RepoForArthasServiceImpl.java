package com.example.springproject.service;

import com.example.springproject.dao.RepoDao;
import com.example.springproject.dao.RepoForArthasDao;
import com.example.springproject.domain.Developer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoForArthasServiceImpl implements RepoForArthasService {

    @Autowired
    RepoForArthasDao repoDao;

    @Override
    public Integer getDeveloperNums() {
        return repoDao.getDeveloperNums();
    }

    @Override
    public List<Developer> getTop10ActiveDevelopers() {
        return repoDao.getTop10ActiveDevelopers();
    }

    @Override
    public Integer getOpenIssueNums() {
        return repoDao.getIssueNums("open");
    }

    @Override
    public Integer getClosedIssueNums() {
        return repoDao.getIssueNums("closed");
    }

    @Override
    public Double[] getTypicalIssueResolutionTime() {
        Double[] typicalIssueResolutionTime = new Double[3];
        Double average = repoDao.getIssueAvg();
        Double range = repoDao.getIssueRange();
        Double std = repoDao.getIssueStd();
        typicalIssueResolutionTime[0] = average;
        typicalIssueResolutionTime[1] = range;
        typicalIssueResolutionTime[2] = std;
        return typicalIssueResolutionTime;
    }

    @Override
    public Integer getReleaseNums() {
        return repoDao.getReleaseNums();
    }

    @Override
    public List[] getCommitNumsBetweenReleases() {
        List<Map<String, Object>> orderedTagName = repoDao.getOrderedTagName();
        HashMap<String, Object> zeroRelease = new HashMap<>();
        zeroRelease.put("tag_name", "zero_release");
        orderedTagName.add(0, zeroRelease);

        List[] cnbrOrderedList = new List[2];
        cnbrOrderedList[0] = new ArrayList();
        cnbrOrderedList[1] = new ArrayList();

        List<Map<String, Object>> cnbrList = repoDao.getCommitNumsBetweenReleases();

        for (int i = 0; i < cnbrList.size(); i++) {
            cnbrOrderedList[0].add(orderedTagName.get(i).get("tag_name"));
            cnbrOrderedList[1].add((int)(long)cnbrList.get(i).get("commitNums"));
        }

        return cnbrOrderedList;
    }

    @Override
    public Integer getCommitNums() {
        return repoDao.getCommitNums();
    }

    @Override
    public Integer[] getCommitNumsDistributionInDayOfWeek() {
        Integer[] distribution = new Integer[2];
        distribution[0] = repoDao.getCommitNumsInWeekday();
        distribution[1] = repoDao.getCommitNumsInWeekend();
        return distribution;
    }

    @Override
    public Integer[] getCommitNumsDistributionInTimeOfDay() {
        Integer[] distribution = new Integer[4];
        distribution[0] = repoDao.getCommitNumsInMidnight();
        distribution[1] = repoDao.getCommitNumsInMorning();
        distribution[2] = repoDao.getCommitNumsInAfternoon();
        distribution[3] = repoDao.getCommitNumsInEvening();
        return distribution;
    }

    @Override
    public Map<String, Integer> getTop25Keywords() {
        List<Map<String, Object>> top25KeyWordsList = repoDao.getTop25Keywords();
        Map<String, Integer> keywordsMap = new HashMap<>();
        top25KeyWordsList.forEach(m -> {
            keywordsMap.put((String) m.get("name"), (int)m.get("frequency"));
        });
        return keywordsMap;
    }

    @Override
    public Map<String, Integer> getTop5Topic() {
        Map<String, Integer> topicMap = new HashMap<>();
        topicMap.put("进程的启动", 987);
        topicMap.put("操作系统与执行环境", 2993);
        topicMap.put("异常/错误", 1364);
        topicMap.put("jvm", 836);
        topicMap.put("版本", 3527);
        return topicMap;
    }
}
