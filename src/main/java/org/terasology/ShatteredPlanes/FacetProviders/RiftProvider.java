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
import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.world.block.BlockAreac;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.ElevationFacet;
import org.terasology.world.generation.facets.SurfaceHumidityFacet;
import org.terasology.world.generation.facets.SurfaceTemperatureFacet;

@Updates({@Facet(ElevationFacet.class),@Facet(SurfaceTemperatureFacet.class),@Facet(SurfaceHumidityFacet.class)})
@Requires(@Facet(BiomeHeightFacet.class))
public class RiftProvider implements FacetProvider {

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {
        ElevationFacet elevationFacet = region.getRegionFacet(ElevationFacet.class);
        SurfaceTemperatureFacet surfaceTemperatureFacet = region.getRegionFacet(SurfaceTemperatureFacet.class);
        SurfaceHumidityFacet surfaceHumidityFacet = region.getRegionFacet(SurfaceHumidityFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);

        for (Vector2ic position : elevationFacet.getWorldArea()) {
            float bheight = biomeHeightFacet.getWorld(position);
            if (bheight > 1 && bheight < 1.4) {
                elevationFacet.setWorld(position, -60f);
                if (surfaceHumidityFacet.getWorldArea().contains(position)) {
                    surfaceHumidityFacet.setWorld(position, 0f);
                }
                if (surfaceTemperatureFacet.getWorldArea().contains(position)) {
                    surfaceTemperatureFacet.setWorld(position, 0f);

                }
            }
        }
    }
}
