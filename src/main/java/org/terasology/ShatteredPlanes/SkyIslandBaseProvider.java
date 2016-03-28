/*
 * Copyright 2015 MovingBlocks
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
package org.terasology.ShatteredPlanes;

import java.lang.Math;

import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;
import org.terasology.math.TeraMath;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.WhiteNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Produces;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

@Produces(SkyIslandBaseFacet.class)
@Requires({ @Facet(value = BiomeHeightFacet.class),
            @Facet(value = SurrealScaleFacet.class)})
public class SkyIslandBaseProvider implements FacetProvider {

    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed - 23214), new Vector2f(0.01f, 0.01f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 3), 8), new Vector2f(0.022f, 0.022f), 1);
        surfaceNoise3 = new SubSampledNoise(new SimplexNoise(seed - 13), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {

        Border3D border = region.getBorderForFacet(SkyIslandBaseFacet.class);
        SurrealScaleFacet surrealScaleFacet = region.getRegionFacet(SurrealScaleFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);
        SkyIslandBaseFacet facet = new SkyIslandBaseFacet(region.getRegion(), border);

        Rect2i processRegion = facet.getWorldRegion();


        for (BaseVector2i position : processRegion.contents()) {
            float surreal = surrealScaleFacet.getWorld(position);
            float bheight = Math.abs(biomeHeightFacet.getWorld(position));
            float height = TeraMath.clamp(surfaceNoise1.noise(position.x(), position.y()) * 30 + 110 + surfaceNoise2.noise(position.x(), position.y()) * 30, 120, 300);
            float val = TeraMath.clamp(surfaceNoise1.noise(position.x(), position.y()) / 3 + surfaceNoise2.noise(position.x(), position.y()) / 3 + surfaceNoise3.noise(position.x(), position.y() / 3), 0, 1);
            if (((val > 0.5-surreal/10-bheight/20 && val < 0.6+surreal/10 + bheight/20) ||
                    (val > 0.9-surreal/10-bheight/20)) && surreal<0.3) {
                facet.setWorld(position, height*(bheight*bheight));
            } else {
                facet.setWorld(position, -999);
            }
        }

        region.setRegionFacet(SkyIslandBaseFacet.class, facet);
    }
}
