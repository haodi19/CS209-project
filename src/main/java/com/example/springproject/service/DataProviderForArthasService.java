package com.example.springproject.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface DataProviderForArthasService {

    JSONArray provideTop10ActiveContributors();

    JSONObject provideTypicalIssueResolutionTime();

    JSONObject provideCommitNumsDistribution();

}