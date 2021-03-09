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
package org.terasology.ShatteredPlanes.FacetProviders;

import org.joml.Vector2ic;
import org.terasology.ShatteredPlanes.Facets.EasterEggFacet;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.WhiteNoise;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;

@Produces(EasterEggFacet.class)
public class EasterEggProvider implements FacetProvider {

    private Noise noise;

    @Override
    public void setSeed(long seed) {
        noise = new WhiteNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(EasterEggFacet.class);
        EasterEggFacet eggFacet = new EasterEggFacet(region.getRegion(), border);
        for (Vector2ic pos : eggFacet.getWorldArea()) {
            if (noise.noise(pos.x(), pos.y()) < (0.0001f) - 1) {
                eggFacet.setWorld(pos.x(), pos.y(), true);
            }
        }

        region.setRegionFacet(EasterEggFacet.class, eggFacet);
    }
}
