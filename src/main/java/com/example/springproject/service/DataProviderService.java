package com.example.springproject.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface DataProviderService {

    JSONArray provideTop10ActiveContributors();

    JSONObject provideTypicalIssueResolutionTime();

    JSONObject provideCommitNumsDistribution();

}
