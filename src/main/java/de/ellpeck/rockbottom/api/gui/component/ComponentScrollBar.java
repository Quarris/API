/*
 * This file ("ComponentScrollBar.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.gui.component;

import de.ellpeck.rockbottom.api.IGameInstance;
import de.ellpeck.rockbottom.api.IRenderer;
import de.ellpeck.rockbottom.api.RockBottomAPI;
import de.ellpeck.rockbottom.api.assets.IAssetManager;
import de.ellpeck.rockbottom.api.data.settings.Settings;
import de.ellpeck.rockbottom.api.gui.Gui;
import de.ellpeck.rockbottom.api.util.BoundBox;
import de.ellpeck.rockbottom.api.util.Util;
import de.ellpeck.rockbottom.api.util.reg.IResourceName;

import java.util.function.Consumer;

public class ComponentScrollBar extends GuiComponent{

    protected final Consumer<Integer> scrollConsumer;
    protected final BoundBox hoverArea;
    protected int number;
    protected int max;

    protected boolean wasMouseDown;

    public ComponentScrollBar(Gui gui, int x, int y, int height, BoundBox hoverArea, int max, Consumer<Integer> scrollConsumer){
        super(gui, x, y, 6, height);
        this.scrollConsumer = scrollConsumer;
        this.hoverArea = hoverArea;
        this.max = max;
    }

    @Override
    public IResourceName getName(){
        return RockBottomAPI.createInternalRes("scroll_bar");
    }

    public BoundBox getHoverArea(){
        return this.hoverArea;
    }

    public int getNumber(){
        return this.number;
    }

    public int getMax(){
        return this.max;
    }

    public void setNumber(int number){
        this.number = number;
    }

    public void setMax(int max){
        this.max = max;
    }

    @Override
    public void render(IGameInstance game, IAssetManager manager, IRenderer g, int x, int y){
        int max = this.getMax();
        float percentage = max <= 0 ? 0 : (float)this.number/(float)max;
        float renderY = y+percentage*(this.height-10);
        int color = this.isMouseOverPrioritized(game) || this.hoverArea.contains(g.getMouseInGuiX(), g.getMouseInGuiY()) ? getElementColor() : getUnselectedElementColor();

        g.addFilledRect(x, y, 6F, this.height, color);
        g.addEmptyRect(x, y, 6F, this.height, getElementOutlineColor());

        g.addFilledRect(x, renderY, 6F, 10F, color);
        g.addEmptyRect(x, renderY, 6F, 10F, getElementOutlineColor());
    }

    @Override
    public void update(IGameInstance game){
        if(this.wasMouseDown){
            if(Settings.KEY_GUI_ACTION_1.isDown()){
                int max = this.getMax();
                int y = this.getRenderY();
                float clickPercentage = (game.getRenderer().getMouseInGuiY()-y)/(float)(this.height-y);

                int number = Util.clamp((int)(clickPercentage*(max-1)), 0, max);
                if(number != this.number){
                    this.number = number;
                    this.onScroll();
                }
            }
            else{
                this.wasMouseDown = false;
            }
        }
        else{
            int scroll = game.getInput().getMouseWheelChange();
            if(scroll != 0 && this.hoverArea.contains(game.getRenderer().getMouseInGuiX(), game.getRenderer().getMouseInGuiY())){
                int number = Util.clamp(this.number+(scroll < 0 ? 1 : -1), 0, this.getMax());
                if(number != this.number){
                    this.number = number;
                    this.onScroll();
                }
            }
        }
    }

    @Override
    public boolean onMouseAction(IGameInstance game, int button, float x, float y){
        if(this.isMouseOver(game)){
            if(!this.wasMouseDown){
                this.wasMouseDown = true;
                return true;
            }
        }
        return false;
    }

    protected void onScroll(){
        if(this.scrollConsumer != null){
            this.scrollConsumer.accept(this.number);
        }
    }
}
