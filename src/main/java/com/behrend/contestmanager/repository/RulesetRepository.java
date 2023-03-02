package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.behrend.contestmanager.models.Ruleset;

import java.util.List;

public interface RulesetRepository extends CrudRepository<Ruleset, Long> {

    List<Ruleset> findRulesetsByName(String name);

    List<Ruleset> findRulesetsByOrigin(String origin);

}