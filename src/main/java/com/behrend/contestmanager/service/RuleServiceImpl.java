package com.behrend.contestmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.behrend.contestmanager.models.Rule;
import com.behrend.contestmanager.repository.RuleRepository;

import java.util.ArrayList;

@Service
public class RuleServiceImpl implements RuleService {
    
    @Autowired
    private RuleRepository ruleRepository;

    // Create
    @Override
    public Rule saveRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    // Read
    @Override
    public ArrayList<Rule> findAllRules() {
        return (ArrayList<Rule>) ruleRepository.findAll();
    }

    @Override
    public Rule findRuleById(long ruleId) {
        return ruleRepository.findById(ruleId).get();
    }

    @Override
    public Rule findRuleByName(String name) {
        return ruleRepository.findRuleByName(name);
    }

    // Update
    @Override
    public Rule updateRule(Rule rule, long ruleId) {
        Rule currentRule = ruleRepository.findById(ruleId).get();

        if (!"".equals(rule.getName())) {
            currentRule.setName(rule.getName());
        }

        if (!"".equals(rule.getAttribute())) {
            currentRule.setAttribute(rule.getAttribute());
        }

        return ruleRepository.save(currentRule);
    }

    // Delete
    @Override
    public void deleteRuleById(long ruleId) {
        ruleRepository.deleteById(ruleId);
    }
}
