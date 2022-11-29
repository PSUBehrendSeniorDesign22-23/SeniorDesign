package com.behrend.contestmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import java.util.List;
@Entity
public class Ruleset {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruleset_id")
    private long id;

    @Column
    private String name;

    @Column
    private String origin;

    @ManyToMany
    @JoinTable(name = "ruleset_rule",
                joinColumns = @JoinColumn(name = "ruleset_id", referencedColumnName = "ruleset_id"),
                inverseJoinColumns = @JoinColumn(name = "rule_id", referencedColumnName = "rule_id"))
    private List<Rule> rules;

    public long getRulesetId() {
        return this.id;
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
