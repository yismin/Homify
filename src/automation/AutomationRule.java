package automation;

import java.util.function.BooleanSupplier;

/**
 * Represents an automation rule with a condition and action
 */
public class AutomationRule {
    private String ruleName;
    private BooleanSupplier condition;  // The IF part
    private Runnable action;            // The THEN part
    private boolean isEnabled;

    /**
     * Constructor for AutomationRule
     * @param ruleName Name of the rule
     * @param condition The condition to check (IF)
     * @param action The action to execute (THEN)
     */
    public AutomationRule(String ruleName, BooleanSupplier condition, Runnable action) {
        this.ruleName = ruleName;
        this.condition = condition;
        this.action = action;
        this.isEnabled = true;
    }

    /**
     * Evaluates the condition
     * @return true if condition is met, false otherwise
     */
    public boolean evaluate() {
        return isEnabled && condition.getAsBoolean();
    }

    /**
     * Executes the action if condition is met
     * @return true if action was executed, false otherwise
     */
    public boolean executeIfTrue() {
        if (evaluate()) {
            action.run();
            return true;
        }
        return false;
    }

    /**
     * Enables the rule
     */
    public void enable() {
        isEnabled = true;
        System.out.println("✓ Rule '" + ruleName + "' enabled");
    }

    /**
     * Disables the rule
     */
    public void disable() {
        isEnabled = false;
        System.out.println("✓ Rule '" + ruleName + "' disabled");
    }

    // Getters
    public String getRuleName() {
        return ruleName;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public String toString() {
        return String.format("Rule: %s [%s]", ruleName, isEnabled ? "ENABLED" : "DISABLED");
    }
}