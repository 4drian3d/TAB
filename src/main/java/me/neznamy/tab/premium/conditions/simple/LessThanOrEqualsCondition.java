package me.neznamy.tab.premium.conditions.simple;

import java.util.List;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.Shared;
import me.neznamy.tab.shared.features.PlaceholderManager;
import me.neznamy.tab.shared.placeholders.Placeholder;
/**
 * "leftSide<=rightSide" condition where leftSide supports placeholders
 */
public class LessThanOrEqualsCondition extends SimpleCondition {

	private String leftSide;
	private List<String> leftSidePlaceholders;
	private double rightValue;
	
	public LessThanOrEqualsCondition(String leftSide, String rightSide) {
		this.leftSide = leftSide;
		leftSidePlaceholders = PlaceholderManager.detectAll(leftSide);
		this.rightValue = Shared.errorManager.parseDouble(rightSide, 0, "right side of <= condition");
	}
	
	@Override
	public boolean isMet(TabPlayer p) {
		String leftSide = this.leftSide;
		for (String identifier : leftSidePlaceholders) {
			Placeholder pl = ((PlaceholderManager) Shared.featureManager.getFeature("placeholders")).getPlaceholder(identifier);
			if (pl != null) leftSide = pl.set(leftSide, p);
		}
		double leftValue = Shared.errorManager.parseDouble(leftSide.replace(",", ""), 0, "left side of <= condition");
		return leftValue <= rightValue;
	}
	
	public static LessThanOrEqualsCondition compile(String line) {
		if (line.contains("<=")) {
			String[] arr = line.split("<=");
			return new LessThanOrEqualsCondition(arr[0], arr[1]);
		} else {
			return null;
		}
	}
}
