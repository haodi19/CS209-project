package com.example.springproject.dao;

import com.example.springproject.domain.Developer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class RepoForArthasDao {
    @Autowired
    JdbcTemplate jdbcTemplate;


    public Integer getDeveloperNums() {
        String sql = "select count(id) from contributors2";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Developer> getTop10ActiveDevelopers() {
        String sql = "select * from contributors2 order by commit_counts desc limit 10";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Developer>(Developer.class));
    }

    public Integer getIssueNums(String state) {
        String sql = "select count(id) from issues2 where state = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, state);
    }

    public Double getIssueAvg() {
        String sql = "select ROUND((AVG(timestampdiff(second, create_time, close_time)) / (60 * 60 * 24)), 2) from issues2 where state = 'closed'";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public Double getIssueRange() {
        String sql = "with diff as (select timestampdiff(second, create_time, close_time) as dif from issues2 where state = 'closed')\n" +
                "select ROUND((Max(dif) - MIN(dif)) / (60 * 60 * 24), 2)\n" +
                "from diff";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public Double getIssueStd() {
        String sql = "select ROUND(STD(timestampdiff(second, create_time, close_time)) / (60 * 60 * 24), 2) from issues2 where state = 'closed'";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public Integer getReleaseNums() {
        String sql = "select count(id) from releases2";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Map<String, Object>> getOrderedTagName(){
        String sql = "select tag_name from releases2 order by create_time";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getCommitNumsBetweenReleases() {
        String sql = "with commit_release as (with releasetime as (\n" +
                "    with ranktime as (select create_time, row_number() over (order by create_time) -1 r from releases2)\n" +
                "    select create_time,\n" +
                "           ifnull((select create_time from ranktime where r + 1 = rt.r),\n" +
                "                  cast(\"2005-01-01 00:00:00\" as datetime)) last_create_time,\n" +
                "           r\n" +
                "    from ranktime rt)\n" +
                "                        select time,\n" +
                "                               ifnull((select rt.r\n" +
                "                                       from releasetime rt\n" +
                "                                       where time >= rt.last_create_time and time < rt.create_time), (select count(id) from releases2)) release_id\n" +
                "                        from commits2)\n" +
                "select cast(release_id as signed) releaseId, count(time) commitNums\n" +
                "from commit_release\n" +
                "group by release_id\n" +
                "order by release_id";

        return jdbcTemplate.queryForList(sql);
    }

    public Integer getCommitNums() {
        String sql = "select count(id) from commits2";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getCommitNumsInWeekday() {
        String sql = "select count(day)\n" +
                "from (select date_format(time, '%W') day from commits2) days\n" +
                "where day in ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday')";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getCommitNumsInWeekend() {
        String sql = "select count(day)\n" +
                "from (select date_format(time, '%W') day from commits2) days\n" +
                "where day in ('Saturday', 'Sunday')";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getCommitNumsInMidnight() {
        String sql = "select count(id) from commits2 where HOUR(time)>=0 and HOUR(time)<6";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getCommitNumsInMorning() {
        String sql = "select count(id) from commits2 where HOUR(time)>=6 and HOUR(time)<12";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getCommitNumsInAfternoon() {
        String sql = "select count(id) from commits2 where HOUR(time)>=12 and HOUR(time)<18";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public Integer getCommitNumsInEvening() {
        String sql = "select count(id) from commits2 where HOUR(time)>=18 and HOUR(time)<24";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public List<Map<String, Object>> getTop25Keywords() {
        String sql = "select * from word_frequency2 order by frequency desc limit 25";
        return jdbcTemplate.queryForList(sql);
    }

}
