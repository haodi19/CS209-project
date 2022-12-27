package com.example.springproject.domain;

public class Developer {
    private String name;
    private Integer commitCounts;

    public Developer() {
    }

    public Developer(String name, Integer commitCounts) {
        this.name = name;
        this.commitCounts = commitCounts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCommitCounts() {
        return commitCounts;
    }

    public void setCommitCounts(Integer commitCounts) {
        this.commitCounts = commitCounts;
    }

    @Override
    public String toString() {
        return "Developer{"
                + "name='" + name + '\'' +
                ", commitCounts=" + commitCounts +
                '}';
    }
}
