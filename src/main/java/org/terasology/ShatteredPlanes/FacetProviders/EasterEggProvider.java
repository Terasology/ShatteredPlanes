// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.EasterEggFacet;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.WhiteNoise;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;

@Produces(EasterEggFacet.class)
public class EasterEggProvider implements FacetProvider {

    private Noise noise;

    @Override
    public void setSeed(long seed) {
        noise = new WhiteNoise(seed);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(EasterEggFacet.class);
        EasterEggFacet eggFacet = new EasterEggFacet(region.getRegion(), border);
        Rect2i worldRegion = eggFacet.getWorldRegion();

        for (BaseVector2i pos : worldRegion.contents()) {
            if (noise.noise(pos.x(), pos.y()) > 0.9) {
                eggFacet.setWorld(pos.x(), pos.y(), true);

            }
        }


        region.setRegionFacet(EasterEggFacet.class, eggFacet);
    }
}
