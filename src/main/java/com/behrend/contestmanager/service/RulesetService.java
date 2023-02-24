package com.behrend.contestmanager.service;

import com.behrend.contestmanager.models.Ruleset;

import java.util.List;

public interface RulesetService {
    // Create
    Ruleset saveRuleset(Ruleset ruleset);

    // Read
    Ruleset findRulesetById(long rulesetId);

    List<Ruleset> findAllRulesets();

    List<Ruleset> findRulesetsByName(String name);

    List<Ruleset> findRulesetsByOrigin(String origin);

    // Update
    Ruleset updateRuleset(Ruleset ruleset, long rulesetId);

    // Delete
    void deleteRulesetById(long rulesetId);
}
