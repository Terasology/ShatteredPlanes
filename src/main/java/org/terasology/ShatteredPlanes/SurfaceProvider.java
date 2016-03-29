/*
 * Copyright 2015 MovingBlocks
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
package org.terasology.ShatteredPlanes;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(SurfaceHeightFacet.class)
public class SurfaceProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;
    private BrownianNoise PreNoise;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed), new Vector2f(0.002f, 0.002f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 30), 8), new Vector2f(0.005f, 0.005f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 30), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        // Create our surface height facet (we will get into borders later)
        Border3D border = region.getBorderForFacet(SurfaceHeightFacet.class);
        SurfaceHeightFacet facet = new SurfaceHeightFacet(region.getRegion(), border);
        // loop through every position on our 2d array
        Rect2i processRegion = facet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()) {
            facet.setWorld(position, Math.abs(surfaceNoise1.noise(position.x(), position.y()) * 3 + surfaceNoise2.noise(position.x(), position.y()) * 8 + surfaceNoise3.noise(position.x(), position.y()) * 20));
            //facet.setWorld(position, 20);
        }
        // give our newly created and populated facet to the region
        region.setRegionFacet(SurfaceHeightFacet.class, facet);
    }
}
