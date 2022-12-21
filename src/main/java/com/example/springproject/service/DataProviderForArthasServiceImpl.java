package com.example.springproject.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.example.springproject.dao.RepoForArthasDao;
import com.example.springproject.domain.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataProviderForArthasServiceImpl implements DataProviderForArthasService {

  @Autowired
  RepoForArthasDao repoDao;

  @Override
  public JSONArray provideTop10ActiveContributors() {
    List<Developer> top10ActiveDevelopers = repoDao.getTop10ActiveDevelopers();
    return JSONArray.parseArray(JSON.toJSONString(top10ActiveDevelopers));
  }

  @Override
  public JSONObject provideTypicalIssueResolutionTime() {
    Double average = repoDao.getIssueAvg();
    Double range = repoDao.getIssueRange();
    Double std = repoDao.getIssueStd();
    String json = String.format("{\"average\":%.2f,\"range\":%.2f,\"std\":%.2f}", average, range, std);
    return JSONObject.parseObject(json, Feature.OrderedField);
  }

  @Override
  public JSONObject provideCommitNumsDistribution() {
    Integer commitNums = repoDao.getCommitNums();

    Integer commitNumsInWeekday = repoDao.getCommitNumsInWeekday();
    Integer commitNumsInWeekend = repoDao.getCommitNumsInWeekend();

    Integer commitNumsInMidnight = repoDao.getCommitNumsInMidnight();
    Integer commitNumsInMorning = repoDao.getCommitNumsInMorning();
    Integer commitNumsInAfternoon = repoDao.getCommitNumsInAfternoon();
    Integer commitNumsInEvening = repoDao.getCommitNumsInEvening();

    String json = String.format("{\"total commits\":%d,\"weekday\":%d,\"weekend\":%d,\"midnight\":%d,\"morning\":%d,\"afternoon\":%d,\"evening\":%d}",
        commitNums, commitNumsInWeekday, commitNumsInWeekend, commitNumsInMidnight, commitNumsInMorning, commitNumsInAfternoon, commitNumsInEvening);

    return JSONObject.parseObject(json, Feature.OrderedField);
  }
}
