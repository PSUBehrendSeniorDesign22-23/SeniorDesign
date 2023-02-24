package com.behrend.contestmanager.service;

import com.behrend.contestmanager.models.Rule;

import java.util.List;

public interface RuleService {
    // Create
    Rule saveRule(Rule rule);
    
    // Read
    List<Rule> findAllRules();

    Rule findRuleById(long ruleId);

    Rule findRuleByName(String name);
    
    // Update
    Rule updateRule(Rule rule, long ruleId);

    // Delete
    void deleteRuleById(long ruleId);
}
 