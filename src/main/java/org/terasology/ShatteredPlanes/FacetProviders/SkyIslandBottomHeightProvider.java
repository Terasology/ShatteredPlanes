// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.SkyIslandBottomHeightFacet;
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

@Produces(SkyIslandBottomHeightFacet.class)
public class SkyIslandBottomHeightProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;
    private BrownianNoise PreNoise;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed + 12), new Vector2f(0.01f, 0.01f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 57), 9), new Vector2f(0.01f,
                0.01f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 39), new Vector2f(0.1f, 0.1f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(SkyIslandBottomHeightFacet.class);
        SkyIslandBottomHeightFacet facet = new SkyIslandBottomHeightFacet(region.getRegion(), border);
        // loop through every position on our 2d array
        Rect2i processRegion = facet.getWorldRegion();

        for (BaseVector2i position : processRegion.contents()) {
            float height =
                    Math.abs(surfaceNoise1.noise(position.x(), position.y()) * 8 + surfaceNoise2.noise(position.x(),
                            position.y()) * 7 + surfaceNoise3.noise(position.x(), position.y()) * 20 + 1);
            facet.setWorld(position, height);

        }
        // give our newly created and populated facet to the region
        region.setRegionFacet(SkyIslandBottomHeightFacet.class, facet);
    }
}
