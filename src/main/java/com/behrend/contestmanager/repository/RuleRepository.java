package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.behrend.contestmanager.models.Rule;

public interface RuleRepository extends CrudRepository<Rule, Long> {
    
    Rule findRuleByName(String name);

}