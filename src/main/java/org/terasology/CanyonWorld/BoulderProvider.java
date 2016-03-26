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
package org.terasology.ShatteredPlanes;

import org.terasology.math.geom.Rect2i;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector2f;
import org.terasology.math.geom.Vector3i;
import org.terasology.utilities.procedural.*;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Updates(@Facet(SurfaceHeightFacet.class))
public class BoulderProvider implements FacetProvider {

    private BrownianNoise PreNoise;
    private Noise mountainNoise1;
    private Noise mountainNoise2;
    private Noise noise;

    @Override
    public void setSeed(long seed) {
        PreNoise=new BrownianNoise(new PerlinNoise(seed + 25), 12);
        //PreNoise.setPersistence(0.001);
        mountainNoise1 = new SubSampledNoise(new SimplexNoise(seed-50), new Vector2f(0.01f, 0.01f), 1);
        mountainNoise2 = new SubSampledNoise(PreNoise, new Vector2f(0.01f, 0.01f), 1);
        noise= new SubSampledNoise(new SimplexNoise(seed+50), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {

        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        Rect2i worldRegion = surfaceHeightFacet.getWorldRegion();

        for (int wz = region.getRegion().minZ(); wz <= region.getRegion().maxZ(); wz++) {
            for (int wx = region.getRegion().minX(); wx <= region.getRegion().maxX(); wx++) {
                int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(wx, wz));
                // check if height is within this region
                if (surfaceHeight >= region.getRegion().minY() &&
                        surfaceHeight <= region.getRegion().maxY()-20) {

                    for (int wy = surfaceHeight; (wy <= region.getRegion().maxY() && wy <= surfaceHeight+20 && surfaceHeight>0); wy++) {


                        // TODO: check for overlap
                        float noiseVal = TeraMath.clamp(noise.noise(wx, wz, wy)+mountainNoise1.noise(wx,wz,wy)*4+mountainNoise2.noise(wx,wz,wy)*2, 0, 1);

                        if (noiseVal > 0.45) {
                            surfaceHeightFacet.setWorld(wx, wz, (float) wy);
                        }
                    }
                }
            }
        }

    }


}

