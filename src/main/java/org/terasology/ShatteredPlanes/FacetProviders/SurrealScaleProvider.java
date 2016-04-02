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

import org.terasology.ShatteredPlanes.Facets.SurrealScaleFacet;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;

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
            float val =  1/(1+(float) Math.exp(-k*position.length())*(10-1));
            facet.setWorld(position, val);

        }

        region.setRegionFacet(SurrealScaleFacet.class, facet);
    }
}
