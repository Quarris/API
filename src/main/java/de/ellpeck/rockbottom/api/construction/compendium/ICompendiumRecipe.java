/*
 * This file ("ICompendiumRecipe.java") is part of the RockBottomAPI by Ellpeck.
 * View the source code at <https://github.com/RockBottomGame/>.
 * View information on the project at <https://rockbottom.ellpeck.de/>.
 *
 * The RockBottomAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The RockBottomAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the RockBottomAPI. If not, see <http://www.gnu.org/licenses/>.
 *
 * © 2018 Ellpeck
 */

package de.ellpeck.rockbottom.api.construction.compendium;

import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.construction.resource.IUseInfo;
import de.ellpeck.rockbottom.api.content.IContent;
import de.ellpeck.rockbottom.api.entity.AbstractEntityItem;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentConstruct;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentIngredient;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentPolaroid;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.entity.ICraftingStation;
import de.ellpeck.rockbottom.api.tile.entity.TileEntity;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface ICompendiumRecipe extends IContent {

    ResourceName getName();

    boolean isKnown(AbstractEntityPlayer player);

    List<IUseInfo> getInputs();

    List<ItemInstance> getOutputs();

    default boolean canConstruct(IInventory inputInventory, IInventory outputInventory) {
        for (IUseInfo info : this.getActualInputs(inputInventory)) {
            if (!inputInventory.containsResource(info)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Called during construction with the machine used to construct the recipe.
     * Provides the same parameters as the ConstructEvent directly to the recipe.
     * @return True if the construction should continue
     */
    default boolean handleMachine(AbstractEntityPlayer player, Inventory inputInventory, Inventory outputInventory, TileEntity machine, int amount, List<IUseInfo> inputs, Function<List<ItemInstance>, List<ItemInstance>> outputGetter, float skillReward) {
        return true;
    }

    default void playerConstruct(AbstractEntityPlayer player, TileEntity machine, int amount) {
		Inventory inv = player.getInv();
		List<ItemInstance> remains = RockBottomAPI.getApiHandler().construct(player, inv, inv, this, machine, amount, this.getActualInputs(inv), items -> this.getActualOutputs(inv, inv, items), this.getSkillReward());
		for (ItemInstance instance : remains) {
			AbstractEntityItem.spawn(player.world, instance, player.getX(), player.getY(), 0F, 0F);
		}
	}

    default List<IUseInfo> getActualInputs(IInventory inventory) {
        return this.getInputs();
    }

    default List<ItemInstance> getActualOutputs(IInventory inputInventory, IInventory outputInventory, List<ItemInstance> inputs) {
        return this.getOutputs();
    }

    default float getSkillReward() {
    	return 0;
	}

    default List<ComponentIngredient> getIngredientButtons(Gui gui, AbstractEntityPlayer player, boolean constructionTable) {
        List<ComponentIngredient> ingredients = new ArrayList<>();
        for (IUseInfo info : this.getInputs()) {
            ingredients.add(new ComponentIngredient(gui, player.getInv().containsResource(info), info.getItems(),
                    constructionTable ? ComponentIngredient.CONSTRUCTION_TEX : ComponentIngredient.DEFAULT_TEX,
                    constructionTable ? ComponentIngredient.CONSTRUCTION_TEX_NONE : ComponentIngredient.DEFAULT_TEX_NONE));
        }
        return ingredients;
    }

    default ComponentConstruct getConstructButton(Gui gui, AbstractEntityPlayer player, TileEntity machine, boolean canConstruct) {
        return new ComponentConstruct(gui, this, true, canConstruct, null);
    }

    default ComponentPolaroid getPolaroidButton(Gui gui, AbstractEntityPlayer player, boolean canConstruct, boolean constructionTable) {
        return new ComponentPolaroid(gui, this, canConstruct,
                constructionTable ? ComponentPolaroid.CONSTRUCTION_TEX : ComponentPolaroid.DEFAULT_TEX,
                constructionTable ? ComponentPolaroid.CONSTRUCTION_TEX_HIGHLIGHTED : ComponentPolaroid.DEFAULT_TEX_HIGHLIGHTED,
                constructionTable ? ComponentPolaroid.CONSTRUCTION_TEX_SELECTED : ComponentPolaroid.DEFAULT_TEX_SELECTED);
    }

    ResourceName getKnowledgeInformationName();
}
