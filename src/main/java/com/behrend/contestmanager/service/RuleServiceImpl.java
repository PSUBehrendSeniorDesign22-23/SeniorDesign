package com.behrend.contestmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.behrend.contestmanager.models.Rule;
import com.behrend.contestmanager.repository.RuleRepository;

import java.util.List;

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
    public List<Rule> findAllRules() {
        return (List<Rule>) ruleRepository.findAll();
    }

    @Override
    public Rule findRuleById(long ruleId) {
        return ruleRepository.findById(ruleId).get();
    }

    @Override
    public List<Rule> findRulesByName(String name) {
        return ruleRepository.findRulesByName(name);
    }

    @Override 
    public Rule findRuleByNameAndAttribute(String name, String attribute) {
        Rule foundRule = null;

        List<Rule> rulesWithName = ruleRepository.findRulesByName(name);
        for (Rule rule : rulesWithName) {
            if (rule.getAttribute().equals(attribute)) {
                foundRule = rule;
                break;
            }
        }

        return foundRule;
    }

    // Update
    @Override
    public Rule updateRule(Rule rule, long ruleId) {
        Rule currentRule = ruleRepository.findById(ruleId).orElse(null);

        if (currentRule == null) {
            return null;
        }

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
