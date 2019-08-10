package de.ellpeck.rockbottom.api.tile.entity;

import de.ellpeck.rockbottom.api.construction.ConstructionTool;
import de.ellpeck.rockbottom.api.item.Item;
import de.ellpeck.rockbottom.api.item.ItemInstance;

public interface ICraftingStation {

	default boolean damageTool(ConstructionTool tool, boolean simulate) {
		ItemInstance toolItem;
		if (tool != null && (toolItem = getTool(tool.tool)) != null) {
			if (!simulate) {
				toolItem.getItem().takeDamage(toolItem, tool.usage);
			}
			return true;
		}
		return tool == null || tool.tool == null;
	}

	ItemInstance getTool(Item tool);

}
