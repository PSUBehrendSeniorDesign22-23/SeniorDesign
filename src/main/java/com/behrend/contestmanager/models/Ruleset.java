package com.behrend.contestmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.Set;
@Entity
@Table(name = "Ruleset")
public class Ruleset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long rulesetId;

    @Column
    private String name;

    @Column
    private String origin;

    @ManyToMany
    @JoinColumn(referencedColumnName = "rule_id")
    private Set<Rule> rules;

    public long getRulesetId() {
        return this.rulesetId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }
}   
