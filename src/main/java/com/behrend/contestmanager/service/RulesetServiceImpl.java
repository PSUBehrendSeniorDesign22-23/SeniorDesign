package com.behrend.contestmanager.service;

import com.behrend.contestmanager.repository.RulesetRepository;
import com.behrend.contestmanager.models.Ruleset;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
        return rulesetRepository.findById(rulesetId).orElse(null);
    }

    @Override
    public List<Ruleset> findAllRulesets() {
        return (List<Ruleset>) rulesetRepository.findAll();
    }

    @Override
    public List<Ruleset> findRulesetsByName(String name) {
        return (List<Ruleset>) rulesetRepository.findRulesetsByName(name);
    }

    @Override
    public List<Ruleset> findRulesetsByOrigin(String origin) {
        return (List<Ruleset>) rulesetRepository.findRulesetsByOrigin(origin);
    }

    // Update
    public Ruleset updateRuleset(Ruleset ruleset, long rulesetId) {
        Ruleset currentRuleset = rulesetRepository.findById(rulesetId).orElse(null);

        if (currentRuleset == null) {
            return null;
        }

        if (!"".equalsIgnoreCase(ruleset.getName())) {
            currentRuleset.setName(ruleset.getName());
        }

        if (!"".equalsIgnoreCase(ruleset.getOrigin())) {
            currentRuleset.setOrigin(ruleset.getOrigin());
        }

        if (ruleset.getRules() != null && !ruleset.getRules().equals(currentRuleset.getRules())) {
            currentRuleset.setRules(ruleset.getRules());
        }

        return rulesetRepository.save(currentRuleset);
    }

    // Delete
    public void deleteRulesetById(long rulesetId) {
        rulesetRepository.deleteById(rulesetId);
    }
}
