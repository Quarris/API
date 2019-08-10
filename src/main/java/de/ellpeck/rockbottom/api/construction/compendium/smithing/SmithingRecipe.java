package de.ellpeck.rockbottom.api.construction.compendium.smithing;

import de.ellpeck.rockbottom.api.construction.compendium.BasicCompendiumRecipe;
import de.ellpeck.rockbottom.api.construction.resource.IUseInfo;
import de.ellpeck.rockbottom.api.entity.player.AbstractEntityPlayer;
import de.ellpeck.rockbottom.api.item.ItemInstance;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SmithingRecipe extends BasicCompendiumRecipe {

	public static final ResourceName ID = ResourceName.intern("recipe");

	protected ResourceName infoName;
	protected List<ItemInstance> outputs;
	protected List<IUseInfo> inputs;
	protected float skillReward;
	protected boolean isKnowledge;

	public SmithingRecipe(ResourceName name, List<IUseInfo> inputs, List<ItemInstance> outputs, float skillReward) {
		super(name);
		this.infoName = name.addPrefix("recipe_");
		this.inputs = inputs;
		this.outputs = outputs;
		this.skillReward = skillReward;
	}

	public SmithingRecipe(ResourceName name, float skillReward, ItemInstance output, IUseInfo... inputs) {
		this(name, Arrays.asList(inputs), Collections.singletonList(output), skillReward);
	}

	public SmithingRecipe(float skillReward, ItemInstance output, IUseInfo... inputs) {
		this(output.getItem().getName(), skillReward, output, inputs);
	}

	@Override
	public boolean isKnown(AbstractEntityPlayer player) {
		return !player.world.isStoryMode() || (isKnowledge && player.getKnowledge().knowsRecipe(this)) ;
	}

	@Override
	public List<IUseInfo> getInputs() {
		return this.inputs;
	}

	@Override
	public List<ItemInstance> getOutputs() {
		return this.outputs;
	}

	@Override
	public ResourceName getKnowledgeInformationName() {
		return this.infoName;
	}
}
