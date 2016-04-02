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

import org.terasology.ShatteredPlanes.Facets.BiomeFacet;
import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.ShatteredPlanes.ShatteredPlanesBiome;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.world.generation.*;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.facets.SurfaceHumidityFacet;
import org.terasology.world.generation.facets.SurfaceTemperatureFacet;

/**
 * Determines the biome based on temperature and humidity
 */
@Produces(BiomeFacet.class)
@Requires({
    @Facet(SeaLevelFacet.class),
    @Facet(SurfaceHeightFacet.class),
    @Facet(SurfaceTemperatureFacet.class),
    @Facet(BiomeHeightFacet.class),
    @Facet(SurfaceHumidityFacet.class)})
public class BiomeProvider implements FacetProvider {

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {
        SeaLevelFacet seaLevelFacet = region.getRegionFacet(SeaLevelFacet.class);
        SurfaceHeightFacet heightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);
        SurfaceTemperatureFacet temperatureFacet = region.getRegionFacet(SurfaceTemperatureFacet.class);
        SurfaceHumidityFacet humidityFacet = region.getRegionFacet(SurfaceHumidityFacet.class);

        Border3D border = region.getBorderForFacet(BiomeFacet.class);
        BiomeFacet biomeFacet = new BiomeFacet(region.getRegion(), border);

        int seaLevel = seaLevelFacet.getSeaLevel();

        for (BaseVector2i pos : biomeFacet.getRelativeRegion().contents()) {
            float temp = temperatureFacet.get(pos);
            float hum = temp * humidityFacet.get(pos);
            float height = heightFacet.get(pos);
            float bheight = biomeHeightFacet.get(pos);

            if (hum == 0.0f && temp == 0.0f) {
                biomeFacet.set(pos, ShatteredPlanesBiome.RIFT);
            } else if (height <= seaLevel && bheight < 0) {
                 biomeFacet.set(pos, ShatteredPlanesBiome.OCEAN);
            } else if (height <= seaLevel + 2 && bheight <0.1) {
                biomeFacet.set(pos, ShatteredPlanesBiome.BEACH);
            } else if (temp >= 0.5f && hum < 0.3f) {
                biomeFacet.set(pos, ShatteredPlanesBiome.DESERT);
            } else if (hum >= 0.3f && hum <= 0.6f && temp >= 0.5f) {
                biomeFacet.set(pos, ShatteredPlanesBiome.PLAINS);
            } else if (temp <= 0.3f && hum > 0.5f) {
                biomeFacet.set(pos, ShatteredPlanesBiome.SNOW);
            } else if (hum >= 0.2f && hum <= 0.5f && temp < 0.5f && bheight>2 && bheight<5) {
                biomeFacet.set(pos, ShatteredPlanesBiome.MOUNTAINS);
            } else {
                biomeFacet.set(pos, ShatteredPlanesBiome.FOREST);
            }
        }
        region.setRegionFacet(BiomeFacet.class, biomeFacet);
    }
}
