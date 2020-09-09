// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.ShatteredPlanes.Facets.SkyIslandBaseFacet;
import org.terasology.ShatteredPlanes.Facets.SurrealScaleFacet;
import org.terasology.engine.utilities.procedural.BrownianNoise;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.SimplexNoise;
import org.terasology.engine.utilities.procedural.SubSampledNoise;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.Facet;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Produces;
import org.terasology.engine.world.generation.Requires;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;

@Produces(SkyIslandBaseFacet.class)
@Requires({@Facet(value = BiomeHeightFacet.class),
        @Facet(value = SurrealScaleFacet.class)})
public class SkyIslandBaseProvider implements FacetProvider {

    private final float skyIslandHeight = 60;
    private Noise surfaceNoise1;
    private Noise surfaceNoise2;
    private Noise surfaceNoise3;

    @Override
    public void setSeed(long seed) {
        surfaceNoise1 = new SubSampledNoise(new SimplexNoise(seed - 23214), new Vector2f(0.01f, 0.01f), 1);
        surfaceNoise2 = new SubSampledNoise(new BrownianNoise(new SimplexNoise(seed + 3), 8), new Vector2f(0.022f,
                0.022f), 1);
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
            float bheight = biomeHeightFacet.getWorld(position);
            float height =
                    TeraMath.clamp(surfaceNoise1.noise(position.x(), position.y()) * 10 + surfaceNoise2.noise(position.x(), position.y() * 10), -skyIslandHeight, skyIslandHeight);
            float val =
                    TeraMath.clamp(surfaceNoise1.noise(position.x(), position.y()) / 3 + surfaceNoise2.noise(position.x(), position.y()) / 3 + surfaceNoise3.noise(position.x(), position.y() / 3), 0, 1);
            if (((val > 0.45 - surreal / 10 && val < 0.65 + surreal / 10) ||
                    (val > 0.9 - surreal / 10)) && bheight > 0.8 && bheight < 1.6) {
                facet.setWorld(position, height * bheight * bheight);
            } else {
                facet.setWorld(position, -999);
            }
        }

        region.setRegionFacet(SkyIslandBaseFacet.class, facet);
    }
}
