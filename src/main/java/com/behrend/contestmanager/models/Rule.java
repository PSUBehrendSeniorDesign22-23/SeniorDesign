package com.behrend.contestmanager.models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private long id;

    @Column
    private String name;

    @Column
    private String attributeOne;

    @Column
    private String attributeTwo;

    @Column
    private String attributeThree;

    @ManyToMany(mappedBy = "rules")
    private List<Ruleset> rulesets;

    public long getRuleId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttributeOne() {
        return attributeOne;
    }

    public void setAttributeOne(String attribute) {
        this.attributeOne = attribute;
    }

    public String getAttributeTwo() {
        return attributeTwo;
    }

    public void setAttributeTwo(String attribute) {
        this.attributeTwo = attribute;
    }

    public String getAttributeThree() {
        return attributeThree;
    }

    public void setAttributeThree(String attribute) {
        this.attributeThree = attribute;
    }
}
