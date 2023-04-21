package com.behrend.contestmanager.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.behrend.contestmanager.models.Rule;

public interface RuleRepository extends CrudRepository<Rule, Long> {
    
    List<Rule> findRulesByName(String name);

}