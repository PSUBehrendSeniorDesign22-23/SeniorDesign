package com.behrend.contestmanager.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.ArrayList;
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
    private ArrayList<Rule> rules;
}
