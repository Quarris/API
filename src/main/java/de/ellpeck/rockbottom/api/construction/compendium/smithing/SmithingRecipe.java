package de.ellpeck.rockbottom.api.construction.compendium.smithing;

import de.ellpeck.rockbottom.api.GameContent;
import de.ellpeck.rockbottom.api.Registries;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.construction.ConstructionTool;
import de.ellpeck.rockbottom.api.construction.compendium.BasicCompendiumRecipe;
import de.ellpeck.rockbottom.api.construction.compendium.construction.ConstructionRecipe;
import de.ellpeck.rockbottom.api.construction.resource.IUseInfo;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentConstruct;
import de.ellpeck.rockbottom.api.gui.component.construction.ComponentPolaroid;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.tile.entity.ICraftingStation;
import de.ellpeck.rockbottom.api.tile.entity.TileEntity;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SmithingRecipe extends BasicCompendiumRecipe {

	public static final ResourceName TEX = ResourceName.intern("gui.smithing_table.item_background");
	public static final ResourceName TEX_HIGHLIGHTED = ResourceName.intern("gui.smithing_table.item_background_highlighted");
	public static final ResourceName TEX_SELECTED = ResourceName.intern("gui.smithing_table.item_background_selected");

	protected List<ItemInstance> outputs;
	protected List<IUseInfo> inputs;
	protected ConstructionTool tool;
	protected float skillReward;
	protected boolean isKnowledge;

	public SmithingRecipe(ResourceName name, List<IUseInfo> inputs, List<ItemInstance> outputs, float skillReward, boolean isKnowledge) {
		super(name);
		this.inputs = inputs;
		this.outputs = outputs;
		this.tool = new ConstructionTool(GameContent.ITEM_HAMMER, 5); // TODO Add usage to RecipeLoader for Smithing Recipe
		this.skillReward = skillReward;
		this.isKnowledge = isKnowledge;
	}

	public SmithingRecipe(ResourceName name, float skillReward, boolean isKnowledge, ItemInstance output, IUseInfo... inputs) {
		this(name, Arrays.asList(inputs), Collections.singletonList(output), skillReward, isKnowledge);
	}

	public SmithingRecipe(float skillReward, boolean isKnowledge, ItemInstance output, IUseInfo... inputs) {
		this(output.getItem().getName(), skillReward, isKnowledge, output, inputs);
	}

	public SmithingRecipe register() {
		Registries.SMITHING_RECIPES.register(this.getName(), this);
		return this;
	}

	@Override
	public boolean isKnown(AbstractEntityPlayer player) {
		return !player.world.isStoryMode() || (this.isKnowledge && player.getKnowledge().knowsRecipe(this)) ;
	}

	@Override
	public List<IUseInfo> getInputs() {
		return this.inputs;
	}

	@Override
	public List<ItemInstance> getOutputs() {
		return this.outputs;
	}

	public boolean canUseTools(TileEntity machine) {
		if (!RockBottomAPI.getInternalHooks().useCraftingTool(machine, new ConstructionTool(GameContent.ITEM_HAMMER, 5), true)) {
			return false;
		}
		return true;
	}

	@Override
	public ComponentPolaroid getPolaroidButton(Gui gui, AbstractEntityPlayer player, boolean canConstruct, boolean constructionTable) {
		return new ComponentPolaroid(gui, this, canConstruct, TEX, TEX_HIGHLIGHTED, TEX_SELECTED);
	}

	@Override
	public ComponentConstruct getConstructButton(Gui gui, AbstractEntityPlayer player, TileEntity machine, boolean canConstruct) {
		boolean hasTool = false;
		if (machine instanceof ICraftingStation) {
			hasTool = ((ICraftingStation) machine).getTool(GameContent.ITEM_HAMMER) != null;
		}
		return new ComponentConstruct(gui, this, hasTool, canConstruct, () -> {
			RockBottomAPI.getInternalHooks().defaultConstruct(player, this, machine);
			return true;
		});
	}
}
