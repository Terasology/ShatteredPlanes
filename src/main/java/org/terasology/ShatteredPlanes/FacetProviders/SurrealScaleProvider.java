// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.SurrealScaleFacet;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;

@Produces(SurrealScaleFacet.class)
public class SurrealScaleProvider implements FacetProvider {

    float k = 0.005f;

    @Override
    public void setSeed(long seed) {

    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(SurrealScaleFacet.class);
        SurrealScaleFacet facet = new SurrealScaleFacet(region.getRegion(), border);
        Rect2i processRegion = facet.getWorldRegion();

        for (BaseVector2i position : processRegion.contents()) {
            float val = 1 / (1 + (float) Math.exp(-k * position.length()) * (10 - 1));
            facet.setWorld(position, val);

        }

        region.setRegionFacet(SurrealScaleFacet.class, facet);
    }
}
