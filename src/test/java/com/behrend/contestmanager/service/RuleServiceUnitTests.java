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
import java.util.Optional;

import com.behrend.contestmanager.repository.RuleRepository;
import com.behrend.contestmanager.models.Rule;

@ExtendWith(MockitoExtension.class)
public class RuleServiceUnitTests {
    
    @Mock
    private RuleRepository ruleRepository;

    @InjectMocks
    private RuleServiceImpl ruleService;

    private Rule rule;

    @BeforeEach
    public void setup() {
        rule = new Rule();
        rule.setName("RuleOne");
        rule.setAttribute("attributeOne");
    }

    // Save test
    @Test
    public void givenRuleObject_whenSaveRule_thenReturnRuleObject(){
        // given
        given(ruleRepository.save(rule)).willReturn(rule);

        // when
        Rule savedRule = ruleService.saveRule(rule);

        // then
        Assertions.assertNotNull(savedRule);
    }

    // Get All test
    @Test
    public void givenRules_whenGetAllRules_ReturnRuleList() {

        Rule rule2 = new Rule();

        // given
        given(ruleRepository.findAll()).willReturn(List.of(rule, rule2));

        // when
        List<Rule> ruleList = ruleService.findAllRules();

        // then
        Assertions.assertNotNull(ruleList);
        Assertions.assertEquals(2, ruleList.size());
    }

    // Get by tournament
    @Test
    public void givenRules_whenGetByName_thenReturnRuleWithName() {
        
        // given
        given(ruleRepository.findRulesByName(rule.getName())).willReturn(List.of(rule));

        // when
        List<Rule> foundRules = ruleService.findRulesByName(rule.getName());

        // then
        Assertions.assertNotNull(foundRules);
        Assertions.assertEquals(1, foundRules.size());
        for (Rule ruleItem : foundRules){
            Assertions.assertEquals(ruleItem.getName(), rule.getName());
        }
    }

    @Test
    public void givenPlayerObject_whenGetRuleById_thenReturnRuleWithId() {
        
        // given
        given(ruleRepository.findById(rule.getRuleId())).willReturn(Optional.of(rule));
        // when
        Rule foundRule = ruleService.findRuleById(rule.getRuleId());
        // then
        Assertions.assertNotNull(foundRule);
        Assertions.assertEquals(rule.getRuleId(), foundRule.getRuleId());
    }

    // Update test
    @Test
    public void givenRuleObject_whenUpdateRule_thenReturnUpdatedRule() {
       
        Rule ruleUpdate = new Rule();
        ruleUpdate.setName("UpdatedRule");
        ruleUpdate.setAttribute("UpdatedAttribute");

        // given
        given(ruleRepository.findById(rule.getRuleId())).willReturn(Optional.of(rule));
        given(ruleRepository.save(rule)).willReturn(rule);

        // when
        Rule updatedRule = ruleService.updateRule(ruleUpdate, rule.getRuleId());

        // Then
        Assertions.assertNotNull(updatedRule);
        Assertions.assertEquals(ruleUpdate.getName(), updatedRule.getName());
        Assertions.assertEquals(ruleUpdate.getAttribute(), updatedRule.getAttribute());
    }


    // Delete test
    @Test
    public void givenRuleId_whenDeleteRule_thenRuleIsDeleted() {
         
        // given
        long ruleId = 1L;

        willDoNothing().given(ruleRepository).deleteById(ruleId);

        // when
        ruleService.deleteRuleById(ruleId);

        // then
        verify(ruleRepository, times(1)).deleteById(ruleId);
    }
}
