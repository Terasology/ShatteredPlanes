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
import org.terasology.engine.utilities.procedural.BrownianNoise;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.SimplexNoise;
import org.terasology.engine.utilities.procedural.SubSampledNoise;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.engine.world.generation.facets.ElevationFacet;

@Produces(ElevationFacet.class)
public class SurfaceProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed), new Vector2f(0.002f, 0.002f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 30), 8), new Vector2f(0.005f,0.005f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 30), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        // Create our surface height facet (we will get into borders later)
        Border3D border = region.getBorderForFacet(ElevationFacet.class);
        ElevationFacet facet = new ElevationFacet(region.getRegion(), border);
        // loop through every position on our 2d array
        for (Vector2ic position : facet.getWorldArea()) {
            facet.setWorld(position,
                Math.abs(surfaceNoise1.noise(position.x(), position.y()) * 1 + surfaceNoise2.noise(position.x(),
                    position.y()) * 8 + surfaceNoise3.noise(position.x(), position.y()) * 20));
        }
        // give our newly created and populated facet to the region
        region.setRegionFacet(ElevationFacet.class, facet);
    }
}
