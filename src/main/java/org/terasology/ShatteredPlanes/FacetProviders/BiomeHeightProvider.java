// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.engine.utilities.procedural.BrownianNoise;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.SimplexNoise;
import org.terasology.engine.utilities.procedural.SubSampledNoise;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;

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

        Rect2i processRegion = facet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()) {
            facet.setWorld(position, 10 * (surfaceNoise1.noise(position.x(), position.y()) / 3 +
                    surfaceNoise2.noise(position.x(), position.y()) / 3 + surfaceNoise3.noise(position.x(),
                    position.y()) / 3));
        }

        region.setRegionFacet(BiomeHeightFacet.class, facet);
    }
}
