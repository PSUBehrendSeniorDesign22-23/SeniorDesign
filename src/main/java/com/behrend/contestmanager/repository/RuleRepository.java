package com.behrend.contestmanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.behrend.contestmanager.models.Rule;

@Repository
public interface RuleRepository extends CrudRepository<Rule, Long> {
    
    Rule findRuleByName(String name);

}