package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Ruleset;

import java.util.List;

@Repository
public interface RulesetRepository extends CrudRepository<Ruleset, Long> {

    Ruleset findRulesetByName(String name);

    List<Ruleset> findRulesetsByOrigin(String origin);
    
}