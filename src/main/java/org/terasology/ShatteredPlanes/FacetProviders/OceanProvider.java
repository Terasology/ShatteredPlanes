// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

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
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;

@Requires(@Facet(BiomeHeightFacet.class))
@Updates(@Facet(SurfaceHeightFacet.class))
public class OceanProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed + 1000), new Vector2f(0.0012f, 0.0012f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 53), 8), new Vector2f(0.005f,
                0.005f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 45), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {

        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);

        Rect2i processRegion = surfaceHeightFacet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()) {
            float bheight = biomeHeightFacet.getWorld(position);
            if (bheight < 0) {
                float change = (float) -Math.exp(-(bheight + 0.3)) * Math.abs(surfaceNoise1.noise(position.x(),
                        position.y()) * 2 +
                        surfaceNoise2.noise(position.x(), position.y()) * 16 + surfaceNoise3.noise(position.x(),
                        position.y()) * 30);
                float sheight = surfaceHeightFacet.getWorld(position);
                surfaceHeightFacet.setWorld(position, sheight + change);
            }
        }
    }
}
