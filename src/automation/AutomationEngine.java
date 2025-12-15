package automation;

import java.util.ArrayList;
import java.util.List;

/**
 * Engine that manages and evaluates automation rules
 */
public class AutomationEngine {
    private List<AutomationRule> rules;

    public AutomationEngine() {
        this.rules = new ArrayList<>();
        System.out.println("✓ Automation Engine initialized");
    }

    /**
     * Adds a rule to the engine
     * @param rule The rule to add
     */
    public void addRule(AutomationRule rule) {
        rules.add(rule);
        System.out.println("✓ Added rule: " + rule.getRuleName());
    }

    /**
     * Removes a rule by name
     * @param ruleName The name of the rule to remove
     */
    public void removeRule(String ruleName) {
        rules.removeIf(rule -> rule.getRuleName().equals(ruleName));
        System.out.println("✓ Removed rule: " + ruleName);
    }

    /**
     * Evaluates all enabled rules and executes actions if conditions are met
     */
    public void evaluateRules() {
        int executedCount = 0;

        for (AutomationRule rule : rules) {
            if (rule.executeIfTrue()) {
                executedCount++;
            }
        }

        if (executedCount == 0) {
            System.out.println("ℹ No automation rules triggered");
        } else {
            System.out.println("✓ " + executedCount + " automation rule(s) executed");
        }
    }

    /**
     * Lists all rules
     */
    public void listRules() {
        System.out.println("\n📜 Automation Rules:");
        if (rules.isEmpty()) {
            System.out.println("  No rules defined");
        } else {
            for (int i = 0; i < rules.size(); i++) {
                System.out.println("  " + (i+1) + ". " + rules.get(i));
            }
        }
    }

    /**
     * Enables a rule by name
     * @param ruleName The name of the rule to enable
     */
    public void enableRule(String ruleName) {
        for (AutomationRule rule : rules) {
            if (rule.getRuleName().equals(ruleName)) {
                rule.enable();
                return;
            }
        }
        System.out.println("✗ Rule not found: " + ruleName);
    }

    /**
     * Disables a rule by name
     * @param ruleName The name of the rule to disable
     */
    public void disableRule(String ruleName) {
        for (AutomationRule rule : rules) {
            if (rule.getRuleName().equals(ruleName)) {
                rule.disable();
                return;
            }
        }
        System.out.println("✗ Rule not found: " + ruleName);
    }

    public List<AutomationRule> getRules() {
        return new ArrayList<>(rules);
    }
}