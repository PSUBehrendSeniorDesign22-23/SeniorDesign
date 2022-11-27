package com.behrend.contestmanager.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Rule")
public class Rule {
    @Id
    private long ruleId;
}
