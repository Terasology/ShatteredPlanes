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
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.block.BlockAreac;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;

/*
    This will determine the magnitude of changes made by updateprovider on surfaceheightfacet.
 */
@Produces(BiomeHeightFacet.class)
public class BiomeHeightProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed - 12), new Vector2f(0.0001f, 0.0001f), 1);
        surfaceNoise2 = new SubSampledNoise(
            new BrownianNoise(new SimplexNoise(seed + 89), 8), new Vector2f(0.0002f, 0.0002f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 107), new Vector2f(0.0001f, 0.0001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(BiomeHeightFacet.class);
        BiomeHeightFacet facet = new BiomeHeightFacet(region.getRegion(), border);

        for (Vector2ic position : facet.getWorldArea()) {
            facet.setWorld(position, 10 * (surfaceNoise1.noise(position.x(), position.y()) / 3 +
                surfaceNoise2.noise(position.x(), position.y()) / 3 + surfaceNoise3.noise(position.x(), position.y()) / 3));
        }

        region.setRegionFacet(BiomeHeightFacet.class, facet);
    }
}
