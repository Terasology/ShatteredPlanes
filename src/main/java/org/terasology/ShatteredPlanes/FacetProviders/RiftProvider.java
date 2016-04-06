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

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.facets.SurfaceHumidityFacet;
import org.terasology.world.generation.facets.SurfaceTemperatureFacet;

@Updates({@Facet(SurfaceHeightFacet.class),@Facet(SurfaceTemperatureFacet.class),@Facet(SurfaceHumidityFacet.class)})
@Requires(@Facet(BiomeHeightFacet.class))
public class RiftProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {/*
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed+1000), new Vector2f(0.0012f, 0.0012f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 53), 8), new Vector2f(0.005f, 0.005f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 45), new Vector2f(0.001f, 0.001f), 1);*/
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        SurfaceTemperatureFacet surfaceTemperatureFacet = region.getRegionFacet(SurfaceTemperatureFacet.class);
        SurfaceHumidityFacet surfaceHumidityFacet = region.getRegionFacet(SurfaceHumidityFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);

        Rect2i processRegion = surfaceHeightFacet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()) {
            float bheight=biomeHeightFacet.getWorld(position);
            if(bheight > 1 && bheight <1.4) {
                surfaceHeightFacet.setWorld(position, -60f);
                if(surfaceHumidityFacet.getWorldRegion().contains(position)) {
                    surfaceHumidityFacet.setWorld(position, 0f);
                }
                if(surfaceTemperatureFacet.getWorldRegion().contains(position)){
                    surfaceTemperatureFacet.setWorld(position, 0f);

                }
            }
        }
    }
}
