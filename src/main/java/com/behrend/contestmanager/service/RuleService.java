package com.behrend.contestmanager.service;

import com.behrend.contestmanager.models.Rule;

import java.util.List;

public interface RuleService {
    // Create
    Rule saveRule(Rule rule);
    
    // Read
    List<Rule> findAllRules();

    Rule findRuleById(long ruleId);

    List<Rule> findRulesByName(String name);
    
    Rule findRuleByNameAndAttribute(String name, String attribute);
    
    // Update
    Rule updateRule(Rule rule, long ruleId);

    // Delete
    void deleteRuleById(long ruleId);
}
 