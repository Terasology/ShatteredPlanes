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

import org.joml.Vector2f;
import org.joml.Vector2ic;
import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.math.TeraMath;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.ElevationFacet;

@Requires(@Facet(BiomeHeightFacet.class))
@Updates(@Facet(ElevationFacet.class))
public class MountainsProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;
    private int mountainHeight = 100;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed + 1234), new Vector2f(0.01f, 0.01f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 4312), 8), new Vector2f(0.005f, 0.005f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 2134), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        ElevationFacet facet = region.getRegionFacet(ElevationFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);
        for (Vector2ic position : facet.getWorldArea()) {
            float biomeHeight = biomeHeightFacet.getWorld(position);
            //Mountains:
            if (biomeHeight > 0 && !(biomeHeight > 1 && biomeHeight < 1.4)) {
                facet.setWorld(position, facet.getWorld(position) + biomeHeight * TeraMath.clamp((float)
                    Math.exp(surfaceNoise1.noise(position.x(), position.y()) * 2 + surfaceNoise2.noise(position.x(), position.y())
                        * 3 + surfaceNoise3.noise(position.x(), position.y()) * 6 - 2), 0, mountainHeight));
            }
        }
    }
}
