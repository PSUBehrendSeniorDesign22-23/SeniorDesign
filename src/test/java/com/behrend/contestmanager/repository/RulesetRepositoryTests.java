package com.behrend.contestmanager.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.behrend.contestmanager.models.Ruleset;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Sql(scripts = "/create-ruleset-data.sql")
@Sql(scripts = "/cleanup-ruleset-data.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class RulesetRepositoryTests {
    @Autowired
    private RulesetRepository rulesetRepository;

    @Test
    void findAllRulesets() {
        ArrayList<Ruleset> rulesets = (ArrayList<Ruleset>) rulesetRepository.findAll();
        Assertions.assertEquals(2, rulesets.size());
    }

    @Test
    void findRulesetById() {
        ArrayList<Long> ids = new ArrayList<>();
        ids.add(11L);
        Iterable<Ruleset> rulesets = rulesetRepository.findAllById(ids);
        for (Ruleset ruleset : rulesets)
        {
            Assertions.assertEquals(ruleset.getRulesetId(), ids.get(0));
        }
    }

    @Test
    void findRulesetByName() {
        String name = "RulesetOne";
        Ruleset ruleset = rulesetRepository.findRulesetByName(name);
        Assertions.assertEquals(name, ruleset.getName());
    }

    @Test 
    void findRulesetsByOrigin()
    {
        String origin = "Japan";
        List<Ruleset> rulesets = rulesetRepository.findRulesetsByOrigin(origin);
        for (Ruleset ruleset : rulesets)
        {
            Assertions.assertEquals(origin, ruleset.getOrigin());
        }
    }
}
