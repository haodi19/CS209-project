package com.example.springproject.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class DataCollectorForArthasDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public static RestTemplate rt = new RestTemplate();
    public static Map<String, String> urlMap = new HashMap<>();
    public static HttpHeaders headers = new HttpHeaders();
    public static HttpEntity<MultiValueMap> httpEntity;


    static {
        initUrlMap();
        headers.add("Authorization", "Bearer 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");
        headers.setContentType(MediaType.APPLICATION_JSON);
        httpEntity = new HttpEntity<>(null, headers);
    }


    public static void initUrlMap() {
        urlMap.put("repo", "https://api.github.com/repos/alibaba/arthas");
        urlMap.put("contributors", "https://api.github.com/repos/alibaba/arthas/contributors");
        urlMap.put("issues", "https://api.github.com/repos/alibaba/arthas/issues");
        urlMap.put("releases", "https://api.github.com/repos/alibaba/arthas/releases");
        urlMap.put("commits", "https://api.github.com/repos/alibaba/arthas/commits");
        urlMap.put("comments", "https://api.github.com/repos/alibaba/arthas/issues/comments");
    }

    public void collectContributors() {

        int page = 1;
        String sql = "INSERT INTO contributors2 (name, commit_counts) VALUES (?, ?)";
        while (true) {
            String url = urlMap.get("contributors") + "?per_page=100&page=" + page++;
            ResponseEntity<String> responseEntity = rt.exchange(url, HttpMethod.GET, httpEntity, String.class);
            String data = responseEntity.getBody();
            if ("[]".equals(data)) {
                break;
            }
            JSONArray contributors = JSON.parseArray(data);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    JSONObject contributor = (JSONObject) contributors.get(i);
                    preparedStatement.setString(1, contributor.getString("login"));
                    preparedStatement.setInt(2, contributor.getIntValue("contributions"));
                }

                @Override
                public int getBatchSize() {
                    return contributors.size();
                }
            });
        }
    }


    public void collectIssues() {
        int page = 1;
        String sql = "INSERT INTO issues2 (issue_id, title, description, state, create_time, update_time, close_time, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        while (true) {
            String url = urlMap.get("issues") + "?state=all&per_page=100&page=" + page++;
            ResponseEntity<String> responseEntity = rt.exchange(url, HttpMethod.GET, httpEntity, String.class);
            String data = responseEntity.getBody();
            if ("[]".equals(data)) {
                break;
            }
            JSONArray issues = JSON.parseArray(data);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    JSONObject issue = (JSONObject) issues.get(i);
                    preparedStatement.setInt(1, issue.getIntValue("number"));
                    preparedStatement.setString(2, issue.getString("title"));
                    preparedStatement.setString(3, issue.getString("body"));
                    preparedStatement.setString(4, issue.getString("state"));
                    preparedStatement.setTimestamp(5, issue.getTimestamp("created_at"));
                    preparedStatement.setTimestamp(6, issue.getTimestamp("updated_at"));
                    preparedStatement.setTimestamp(7, issue.getTimestamp("closed_at"));
                    preparedStatement.setString(8, issue.containsKey("pull_request") ? "pull_request" : "issue");
                }

                @Override
                public int getBatchSize() {
                    return issues.size();
                }
            });
        }
    }

    public void collectReleases() {
        int page = 1;
        String sql = "INSERT INTO releases2 (tag_name, name, description, create_time, publish_time) VALUES (?, ?, ?, ?, ?)";
        while (true) {
            String url = urlMap.get("releases") + "?per_page=100&page=" + page++;
            ResponseEntity<String> responseEntity = rt.exchange(url, HttpMethod.GET, httpEntity, String.class);
            String data = responseEntity.getBody();
            if ("[]".equals(data)) {
                break;
            }
            JSONArray releases = JSON.parseArray(data);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    JSONObject release = (JSONObject) releases.get(i);
                    preparedStatement.setString(1, release.getString("tag_name"));
                    preparedStatement.setString(2, release.getString("name"));
                    preparedStatement.setString(3, release.getString("body"));
                    preparedStatement.setTimestamp(4, release.getTimestamp("created_at"));
                    preparedStatement.setTimestamp(5, release.getTimestamp("published_at"));
                }

                @Override
                public int getBatchSize() {
                    return releases.size();
                }
            });
        }
    }

    public void collectCommits() {
        int page = 1;
        String sql = "INSERT INTO commits2 (sha, message, time) VALUES (?, ?, ?)";
        while (true) {
            String url = urlMap.get("commits") + "?per_page=100&page=" + page++;
            ResponseEntity<String> responseEntity = rt.exchange(url, HttpMethod.GET, httpEntity, String.class);
            String data = responseEntity.getBody();
            if ("[]".equals(data)) {
                break;
            }
            JSONArray commits = JSON.parseArray(data);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    JSONObject commit = (JSONObject) commits.get(i);
                    JSONObject commitMsg = (JSONObject) commit.get("commit");
                    JSONObject committer = (JSONObject) commitMsg.get("committer");

                    preparedStatement.setString(1, commit.getString("sha"));
                    preparedStatement.setString(2, commitMsg.getString("message"));
                    preparedStatement.setTimestamp(3, committer.getTimestamp("date"));
                }

                @Override
                public int getBatchSize() {
                    return commits.size();
                }
            });
        }
    }

    public void collectComments() {
        int page = 1;
        String sql = "INSERT INTO comments2 (issue_id, description) VALUES (?, ?)";
        while (true) {
            String url = urlMap.get("comments") + "?per_page=100&page=" + page++;
            ResponseEntity<String> responseEntity = rt.exchange(url, HttpMethod.GET, httpEntity, String.class);
            String data = responseEntity.getBody();
            if ("[]".equals(data)) {
                break;
            }
            JSONArray comments = JSON.parseArray(data);
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                    JSONObject comment = (JSONObject) comments.get(i);
                    String issue_url = comment.getString("issue_url");
                    int issue_id = Integer.parseInt(issue_url.substring(issue_url.lastIndexOf("/") + 1));

                    preparedStatement.setInt(1, issue_id);
                    preparedStatement.setString(2, comment.getString("body"));
                }

                @Override
                public int getBatchSize() {
                    return comments.size();
                }
            });
        }
    }

    public void collectKeyWordsInIssueTitles() {

        Properties props = new Properties();

        try {
            props.load(this.getClass().getResourceAsStream("/StanfordCoreNLP-chinese.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Map<String, Integer> wordFrequency = new HashMap<>();
        List<String> marks = new ArrayList<>();
        marks.add("NR");
        marks.add("NN");
        marks.add("NT");
        marks.add("VV");
        marks.add("VA");
        marks.add("FW");
        marks.add("JJ");
        List<String> discardList = new ArrayList<>();
        discardList.add("\\");
        discardList.add("使用");
        discardList.add("问题");
        discardList.add("会");
        discardList.add("<li>");
        discardList.add("</a>");
        discardList.add("</li>");
        discardList.add("<code>");
        discardList.add("will");
        discardList.add("name");
        discardList.add("public");
        discardList.add("private");
        discardList.add("return");
        discardList.add("new");
        discardList.add("void");
        discardList.add("class");
        discardList.add("if");
        discardList.add("system.out.println");
        discardList.add("static");
        discardList.add("int");

        String sql = "select title from issues2";
        System.out.println("Analyzing texts...");
        List<Map<String, Object>> issueTitles = jdbcTemplate.queryForList(sql);
        for (int i = 0; i < issueTitles.size(); i++) {
            try {
                //            System.out.println("-------------------------");
                String title = (String) issueTitles.get(i).get("title");
//            System.out.print(title);

                if (title == null || title.length() == 0) {
                    continue;
                }

                // 创造一个空的Annotation对象
                Annotation document = new Annotation(title);
                // 对文本进行分析
                pipeline.annotate(document);
                //获取文本处理结果
                List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
                for (CoreMap sentence : sentences) {
                    // traversing the words in the current sentence
                    // a CoreLabel is a CoreMap with additional token-specific methods
                    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                        //词性标注
                        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                        if (!marks.contains(pos)) {
                            continue;
                        }
                        //词干化处理
                        String lema = token.get(CoreAnnotations.LemmaAnnotation.class);
//                    System.out.print("  " + pos + "  ");
//                    System.out.println(lema);

                        if (discardList.contains(lema)) {
                            continue;
                        }
                        if (!wordFrequency.containsKey(lema)) {
                            wordFrequency.put(lema, 1);
                        } else {
                            wordFrequency.put(lema, wordFrequency.get(lema) + 1);
                        }
                    }
                }
            } catch (StackOverflowError e) {
                System.out.println("Stack over flow!");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println("Analyze texts completed!");

        String sql2 = "INSERT INTO word_frequency2 (name, frequency) VALUES (?, ?)";
        Set<Map.Entry<String, Integer>> entries = wordFrequency.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            String word = entry.getKey();
            Integer frequency = entry.getValue();
            jdbcTemplate.update(sql2, word, frequency);
        }
    }

    public void collectKeyWordsInIssueDescriptions() {

        Properties props = new Properties();

        try {
            props.load(this.getClass().getResourceAsStream("/StanfordCoreNLP-chinese.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Map<String, Integer> wordFrequency = new HashMap<>();
        List<String> marks = new ArrayList<>();
        marks.add("NR");
        marks.add("NN");
        marks.add("NT");
        marks.add("VV");
        marks.add("VA");
        marks.add("FW");
        marks.add("JJ");
        List<String> discardList = new ArrayList<>();
        discardList.add("\\");
        discardList.add("使用");
        discardList.add("问题");
        discardList.add("会");
        discardList.add("<li>");
        discardList.add("</a>");
        discardList.add("</li>");
        discardList.add("<code>");
        discardList.add("will");
        discardList.add("name");
        discardList.add("public");
        discardList.add("private");
        discardList.add("return");
        discardList.add("new");
        discardList.add("void");
        discardList.add("class");
        discardList.add("if");
        discardList.add("system.out.println");
        discardList.add("static");
        discardList.add("int");

        String sql = "select description from issues2";
        System.out.println("Analyzing texts...");
        List<Map<String, Object>> issueDescriptions = jdbcTemplate.queryForList(sql);
        for (int i = 0; i < issueDescriptions.size(); i++) {
            try {
                System.out.println(i);
//                System.out.println("-------------------------");
                String description = (String) issueDescriptions.get(i).get("description");
//                System.out.print(description);
                if (description == null || description.length() == 0) {
                    continue;
                }
                // 创造一个空的Annotation对象
                Annotation document = new Annotation(description);
                // 对文本进行分析
                pipeline.annotate(document);
                //获取文本处理结果
                List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
                for (CoreMap sentence : sentences) {
                    // traversing the words in the current sentence
                    // a CoreLabel is a CoreMap with additional token-specific methods
                    for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                        //词性标注
                        String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                        if (!marks.contains(pos)) {
                            continue;
                        }
                        //词干化处理
                        String lema = token.get(CoreAnnotations.LemmaAnnotation.class);
//                        System.out.print("  " + pos + "  ");
//                        System.out.println(lema);
                        if (discardList.contains(lema)) {
                            continue;
                        }
                        if (!wordFrequency.containsKey(lema)) {
                            wordFrequency.put(lema, 1);
                        } else {
                            wordFrequency.put(lema, wordFrequency.get(lema) + 1);
                        }
                    }
                }
            } catch (StackOverflowError e) {
                System.out.println("Stack over flow!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Analyze texts completed!");

        String sql2 = "INSERT INTO word_frequency2 (name, frequency) VALUES (?, ?)";
        String sql3 = "UPDATE word_frequency2 SET frequency = frequency + ? where name = ?";
        Set<Map.Entry<String, Integer>> entries = wordFrequency.entrySet();
        for (Map.Entry<String, Integer> entry : entries) {
            String word = entry.getKey();
            Integer frequency = entry.getValue();

            Integer isRecorded = jdbcTemplate.queryForObject("select count(id) from word_frequency where name = ?", Integer.class, word);
            if (isRecorded == 0) {
                jdbcTemplate.update(sql2, word, frequency);
            } else {
                jdbcTemplate.update(sql3, frequency, word);
            }
        }

    }
}
