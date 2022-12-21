package com.example.springproject.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.springproject.service.DataProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/restful/fastjson")
public class DataProviderController {

    @Autowired
    DataProviderService dataProviderService;

    @GetMapping("/top10ActiveDevelopers")
    public JSONArray provideTop10ActiveContributors() {
        return dataProviderService.provideTop10ActiveContributors();
    }

    @GetMapping("/typicalIssueResolutionTime")
    public JSONObject provideTypicalIssueResolutionTime() {
        return dataProviderService.provideTypicalIssueResolutionTime();
    }

    @GetMapping("/commitNumsDistribution")
    public JSONObject provideCommitNumsDistribution() {
        return dataProviderService.provideCommitNumsDistribution();
    }


}
