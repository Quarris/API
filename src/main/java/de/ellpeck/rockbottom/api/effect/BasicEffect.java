/*
 * This file ("BasicEffect.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.effect;

import de.ellpeck.rockbottom.api.entity.Entity;
import de.ellpeck.rockbottom.api.util.reg.ResourceName;

public class BasicEffect implements IEffect {

    private final ResourceName name;
    private final ResourceName iconName;
    private final ResourceName unlocName;

    private final boolean isBad;
    private final boolean isInstant;
    private final int maxDuration;
    private final int maxLevel;

    public BasicEffect(ResourceName name, boolean isBad, boolean isInstant, int maxDuration, int maxLevel) {
        this.name = name;
        this.unlocName = this.name.addPrefix("effect.");
        this.iconName = this.unlocName.addPrefix("gui.");
        this.isBad = isBad;
        this.isInstant = isInstant;
        this.maxDuration = maxDuration;
        this.maxLevel = maxLevel;
    }

    public BasicEffect(ResourceName name, boolean isBad, boolean isInstant, int maxDuration) {
        this(name, isBad, isInstant, maxDuration, 1);
    }

    @Override
    public boolean isBad(Entity entity) {
        return this.isBad;
    }

    @Override
    public boolean isInstant(Entity entity) {
        return this.isInstant;
    }

    @Override
    public ResourceName getName() {
        return this.name;
    }

    @Override
    public ResourceName getUnlocalizedName(ActiveEffect effect, Entity entity) {
        return this.unlocName;
    }

    @Override
    public ResourceName getIcon(ActiveEffect effect, Entity entity) {
        return this.iconName;
    }

    @Override
    public void updateLasting(ActiveEffect effect, Entity entity) {

    }

    @Override
    public void activateInstant(ActiveEffect effect, Entity entity) {

    }

    @Override
    public void onAddedOrLoaded(ActiveEffect effect, Entity entity, boolean loaded) {

    }

    @Override
    public void onRemovedOrEnded(ActiveEffect effect, Entity entity, boolean ended) {

    }

    @Override
    public int getMaxDuration(Entity entity) {
        return this.maxDuration;
    }

    @Override
    public int getMaxLevel(Entity entity) {
        return this.maxLevel;
    }
}
