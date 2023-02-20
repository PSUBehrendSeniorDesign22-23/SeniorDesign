package com.behrend.contestmanager.service;

import com.behrend.contestmanager.repository.RulesetRepository;
import com.behrend.contestmanager.models.Ruleset;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;


public class RulesetServiceImpl implements RulesetService {
    
    @Autowired
    private RulesetRepository rulesetRepository;

    // Create
    @Override
    public Ruleset saveRuleset(Ruleset ruleset) {
        return rulesetRepository.save(ruleset);
    }

    // Read
    @Override
    public Ruleset findRulesetById(long rulesetId) {
        return rulesetRepository.findById(rulesetId).get();
    }

    @Override
    public ArrayList<Ruleset> findAllRulesets() {
        return (ArrayList<Ruleset>) rulesetRepository.findAll();
    }

    @Override
    public ArrayList<Ruleset> findRulesetsByName(String name) {
        return (ArrayList<Ruleset>) rulesetRepository.findRulesetsByName(name);
    }

    @Override
    public ArrayList<Ruleset> findRulesetsByOrigin(String origin) {
        return (ArrayList<Ruleset>) rulesetRepository.findRulesetsByOrigin(origin);
    }

    // Update
    public Ruleset updateRuleset(Ruleset ruleset, long rulesetId) {
        Ruleset currentRuleset = rulesetRepository.findById(rulesetId).get();

        if (!"".equalsIgnoreCase(ruleset.getName())) {
            currentRuleset.setName(ruleset.getName());
        }

        if (!"".equalsIgnoreCase(ruleset.getOrigin())) {
            currentRuleset.setOrigin(ruleset.getOrigin());
        }

        if (!ruleset.getRules().equals(currentRuleset.getRules())) {
            currentRuleset.setRules(ruleset.getRules());
        }

        return rulesetRepository.save(currentRuleset);
    }

    // Delete
    public void deleteRulesetById(long rulesetId) {
        rulesetRepository.deleteById(rulesetId);
    }
}
