package com.behrend.contestmanager.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.behrend.contestmanager.repository.RulesetRepository;
import com.behrend.contestmanager.models.Rule;
import com.behrend.contestmanager.models.Ruleset;

@ExtendWith(MockitoExtension.class)
public class RulesetServiceUnitTests {
    
    @Mock
    private RulesetRepository rulesetRepository;

    @InjectMocks
    private RulesetServiceImpl rulesetService;

    private Ruleset ruleset;

    private ArrayList<Rule> rules;

    @BeforeEach
    public void setup() {
        Rule ruleOne = new Rule();
        ruleOne.setName("RuleOne");
        ruleOne.setAttribute("RuleAttributeOne");

        Rule ruleTwo = new Rule();
        ruleTwo.setName("RuleTwo");
        ruleTwo.setAttribute("RuleAttributeTwo");

        rules = new ArrayList<>();
        rules.add(ruleOne);
        rules.add(ruleTwo);

        ruleset = new Ruleset();
        ruleset.setName("RulesetOne");
        ruleset.setOrigin("rulesetOneOrigin");
        ruleset.setRules(rules);
    }

    // Save test
    @Test
    public void givenRulesetObject_whenSaveRuleset_thenReturnRulesetObject(){
        // given
        given(rulesetRepository.save(ruleset)).willReturn(ruleset);

        // when
        Ruleset savedRuleset = rulesetService.saveRuleset(ruleset);

        // then
        Assertions.assertNotNull(savedRuleset);
    }

    // Get All test
    @Test
    public void givenRulesets_whenGetAllRulesets_ReturnRulesetList() {

        Ruleset ruleset2 = new Ruleset();

        // given
        given(rulesetRepository.findAll()).willReturn(List.of(ruleset, ruleset2));

        // when
        List<Ruleset> rulesetList = rulesetService.findAllRulesets();

        // then
        Assertions.assertNotNull(rulesetList);
        Assertions.assertEquals(2, rulesetList.size());
    }

    // Get by name
    @Test
    public void givenString_whenGetByName_thenReturnRulesetWithName() {
        
        String name = "RulesetOne";
        // given
        given(rulesetRepository.findRulesetsByName(name)).willReturn(List.of(ruleset));

        // when
        List<Ruleset> foundRulesets = rulesetService.findRulesetsByName(name);

        // then
        Assertions.assertNotNull(foundRulesets);
        for (Ruleset r : foundRulesets) {
            Assertions.assertEquals(name, r.getName());
        }
    }

    @Test
    public void givenRulesetId_whenGetRulesetById_thenReturnRulesetWithId() {
        
        // given
        given(rulesetRepository.findById(ruleset.getRulesetId())).willReturn(Optional.of(ruleset));
        // when
        Ruleset foundRuleset = rulesetService.findRulesetById(ruleset.getRulesetId());
        // then
        Assertions.assertNotNull(foundRuleset);
        Assertions.assertEquals(ruleset.getRulesetId(), foundRuleset.getRulesetId());
    }

    // Update test
    @Test
    public void givenRulesetObject_whenUpdateRuleset_thenReturnUpdatedRuleset() {
       
        Ruleset rulesetUpdate = new Ruleset();
        rulesetUpdate.setName("UpdatedRuleset");
        rulesetUpdate.setOrigin("UpdatedOrigin");

        // given
        given(rulesetRepository.findById(ruleset.getRulesetId())).willReturn(Optional.of(ruleset));
        given(rulesetRepository.save(ruleset)).willReturn(ruleset);

        // when
        Ruleset updatedRuleset = rulesetService.updateRuleset(rulesetUpdate, ruleset.getRulesetId());

        // Then
        Assertions.assertNotNull(updatedRuleset);
        Assertions.assertEquals(rulesetUpdate.getName(), updatedRuleset.getName());
        Assertions.assertEquals(rulesetUpdate.getOrigin(), updatedRuleset.getOrigin());
    }


    // Delete test
    @Test
    public void givenRulesetId_whenDeleteRuleset_thenRulesetIsDeleted() {
         
        // given
        long rulesetId = 1L;

        willDoNothing().given(rulesetRepository).deleteById(rulesetId);

        // when
        rulesetService.deleteRulesetById(rulesetId);

        // then
        verify(rulesetRepository, times(1)).deleteById(rulesetId);
    }
}
