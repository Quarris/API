/*
 * This file ("PartUniqueId.java") is part of the RockBottomAPI by Ellpeck.
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

package de.ellpeck.rockbottom.api.data.set.part;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.UUID;

public final class PartUniqueId extends BasicDataPart<UUID> {

    public static final IPartFactory<PartUniqueId> FACTORY = new IPartFactory<PartUniqueId>() {
        @Override
        public PartUniqueId parse(JsonElement element) {
            if (element.isJsonPrimitive()) {
                JsonPrimitive prim = element.getAsJsonPrimitive();
                if (prim.isString()) {
                    try {
                        return new PartUniqueId(UUID.fromString(prim.getAsString()));
                    } catch (Exception ignored) {
                    }
                }
            }
            return null;
        }

        @Override
        public PartUniqueId parse(DataInput stream) throws Exception {
            return new PartUniqueId(new UUID(stream.readLong(), stream.readLong()));
        }

        @Override
        public int getPriority() {
            return 50;
        }
    };

    public PartUniqueId(UUID data) {
        super(data);
    }

    @Override
    public void write(DataOutput stream) throws Exception {
        stream.writeLong(this.data.getMostSignificantBits());
        stream.writeLong(this.data.getLeastSignificantBits());
    }

    @Override
    public JsonElement write() {
        return new JsonPrimitive(this.data.toString());
    }

    @Override
    public IPartFactory<? extends DataPart<UUID>> getFactory() {
        return FACTORY;
    }
}
