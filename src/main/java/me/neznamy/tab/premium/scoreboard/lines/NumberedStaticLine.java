package me.neznamy.tab.premium.scoreboard.lines;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.premium.scoreboard.Scoreboard;
import me.neznamy.tab.shared.PacketAPI;

/**
 * A line with static text (no placeholders) with numbers 1-15
 * Limitations:
 *   1.5.x - 1.7.x: 48 characters
 *   1.8.x - 1.12.x: 72 characters
 */
public class NumberedStaticLine extends StaticLine {

	public NumberedStaticLine(Scoreboard parent, int lineNumber, String text) {
		super(parent, lineNumber, text, "");
	}

	@Override
	public void register(TabPlayer p) {
		p.setProperty(teamName, text);
		if (p.getVersion().getMinorVersion() >= 8) {
			PacketAPI.registerScoreboardScore(p, teamName, name, prefix, suffix, ObjectiveName, parent.lines.size() + 1 - lineNumber);
		} else {
			PacketAPI.registerScoreboardScore(p, teamName, name1_7, prefix1_7, suffix1_7, ObjectiveName, parent.lines.size() + 1 - lineNumber);
		}
	}

	@Override
	public void unregister(TabPlayer p) {
		if (p.getProperty(teamName).get().length() > 0) {
			removeLine(p, p.getVersion().getMinorVersion() >= 8 ? name: name1_7, teamName);
		}
	}
}