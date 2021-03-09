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
import org.terasology.engine.utilities.procedural.BrownianNoise;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.SimplexNoise;
import org.terasology.engine.utilities.procedural.SubSampledNoise;
import org.terasology.engine.world.generation.Facet;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Requires;
import org.terasology.engine.world.generation.Updates;
import org.terasology.engine.world.generation.facets.ElevationFacet;

@Requires(@Facet(BiomeHeightFacet.class))
@Updates(@Facet(ElevationFacet.class))
public class OceanProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed + 1000), new Vector2f(0.0012f, 0.0012f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 53), 8), new Vector2f(0.005f, 0.005f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 45), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {

        ElevationFacet elevationFacet = region.getRegionFacet(ElevationFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);

        for (Vector2ic position : elevationFacet.getWorldArea()) {
            float bheight = biomeHeightFacet.getWorld(position);
            if (bheight < 0) {
                float change = (float) -Math.exp(-(bheight + 0.3)) * Math.abs(surfaceNoise1.noise(position.x(), position.y()) * 2 +
                    surfaceNoise2.noise(position.x(), position.y()) * 16 + surfaceNoise3.noise(position.x(), position.y()) * 30);
                float sheight = elevationFacet.getWorld(position);
                elevationFacet.setWorld(position, sheight + change);
            }
        }
    }
}
