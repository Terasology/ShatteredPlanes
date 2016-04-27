/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.ShatteredPlanes.Layers;

import com.google.common.collect.Maps;
import org.terasology.ShatteredPlanes.ShatteredPlanesBiome;
import org.terasology.rendering.nui.Color;
import org.terasology.world.biomes.Biome;

import java.util.Map;
import java.util.function.Function;

/**
 * Maps the core biomes to colors
 */
public class ShatteredPlanesBiomeColors implements Function<Biome, Color> {

    private final Map<Biome, Color> biomeColors = Maps.newHashMap();

    public ShatteredPlanesBiomeColors() {
        biomeColors.put(ShatteredPlanesBiome.RIFT, Color.BLUE);
    }

    @Override
    public Color apply(Biome biome) {
        Color color = biomeColors.get(biome);
        return color;
    }
}
