package com.behrend.contestmanager.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Ruleset")
public class Ruleset {
    @Id
    private long rulesetId;
}
