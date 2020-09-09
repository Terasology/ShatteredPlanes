// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.world.generation.Facet;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Requires;
import org.terasology.engine.world.generation.Updates;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.engine.world.generation.facets.SurfaceHumidityFacet;
import org.terasology.engine.world.generation.facets.SurfaceTemperatureFacet;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;

@Updates({@Facet(SurfaceHeightFacet.class), @Facet(SurfaceTemperatureFacet.class), @Facet(SurfaceHumidityFacet.class)})
@Requires(@Facet(BiomeHeightFacet.class))
public class RiftProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {/*
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed+1000), new Vector2f(0.0012f, 0.0012f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 53), 8), new Vector2f(0.005f, 0
        .005f), 1);
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
            float bheight = biomeHeightFacet.getWorld(position);
            if (bheight > 1 && bheight < 1.4) {
                surfaceHeightFacet.setWorld(position, -60f);
                if (surfaceHumidityFacet.getWorldRegion().contains(position)) {
                    surfaceHumidityFacet.setWorld(position, 0f);
                }
                if (surfaceTemperatureFacet.getWorldRegion().contains(position)) {
                    surfaceTemperatureFacet.setWorld(position, 0f);

                }
            }
        }
    }
}
