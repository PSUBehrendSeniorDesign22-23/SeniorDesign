package com.behrend.contestmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Rule")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ruleId;

    @Column
    private String name;

    @Column
    private String attributeOne;

    @Column
    private String attributeTwo;

    @Column
    private String attributeThree;

    public long getRuleId() {
        return ruleId;
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
