/*
 * This file ("MortarRecipe.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.construction.compendium.mortar;

import de.ellpeck.rockbottom.api.Registries;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.construction.compendium.BasicCompendiumRecipe;
import de.ellpeck.rockbottom.api.construction.resource.IUseInfo;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.inventory.IInventory;
import de.ellpeck.rockbottom.api.inventory.Inventory;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.List;

public class MortarRecipe extends BasicCompendiumRecipe {

    public static final ResourceName ID = ResourceName.intern("mortar");

    protected ResourceName infoName;
    protected final List<IUseInfo> input;
    protected final List<ItemInstance> output;
    protected final int time;

    public MortarRecipe(ResourceName name, List<IUseInfo> input, List<ItemInstance> output, int time) {
        super(name);
        this.infoName = name.addPrefix("recipe_");
        this.input = input;
        this.output = output;
        this.time = time;
    }

    public static MortarRecipe forName(ResourceName name) {
        return Registries.MORTAR_REGISTRY.get(name);
    }

    public static MortarRecipe getRecipe(IInventory inv) {
        for (MortarRecipe recipe : Registries.MORTAR_REGISTRY.values()) {
            if (recipe.canConstruct(inv, inv)) {
                return recipe;
            }
        }
        return null;
    }

    public int getTime() {
        return this.time;
    }

    public MortarRecipe register() {
        Registries.MORTAR_REGISTRY.register(this.getName(), this);
        return this;
    }

    public void construct(Inventory inventory, int amount) {
        RockBottomAPI.getApiHandler().construct(null, inventory, inventory, this, null, amount, this.getActualInputs(inventory), items -> this.getActualOutputs(inventory, inventory, items), 0F);
    }

    @Override
    public boolean isKnown(AbstractEntityPlayer player) {
        return true;
    }

    @Override
    public List<IUseInfo> getInputs() {
        return this.input;
    }

    @Override
    public List<ItemInstance> getOutputs() {
        return this.output;
    }

	@Override
	public ResourceName getKnowledgeInformationName() {
		return null;
	}
}
