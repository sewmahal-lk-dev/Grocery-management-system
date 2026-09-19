package lk.evergreen.grocery.entity;

/**
 * How {@link Product#getPrice()} is interpreted at checkout / display.
 */
public enum PricingMode {
    /** Price per kilogram (e.g. produce sold by weight). */
    WEIGHT_BASED_KG,
    /** Fixed price per discrete unit (e.g. one bottle, one pack). */
    UNIT_BASED
}
