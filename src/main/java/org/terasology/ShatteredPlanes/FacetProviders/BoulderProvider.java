// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.engine.utilities.procedural.BrownianNoise;
import org.terasology.engine.utilities.procedural.Noise;
import org.terasology.engine.utilities.procedural.PerlinNoise;
import org.terasology.engine.utilities.procedural.SimplexNoise;
import org.terasology.engine.utilities.procedural.SubSampledNoise;
import org.terasology.engine.world.generation.Facet;
import org.terasology.engine.world.generation.FacetBorder;
import org.terasology.engine.world.generation.FacetProvider;
import org.terasology.engine.world.generation.GeneratingRegion;
import org.terasology.engine.world.generation.Requires;
import org.terasology.engine.world.generation.Updates;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2f;

@Requires(@Facet(BiomeHeightFacet.class))
@Updates(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 4)))
public class BoulderProvider implements FacetProvider {

    private final float k = 0.05f;
    private BrownianNoise PreNoise;
    private Noise mountainNoise1;
    private Noise mountainNoise2;
    private Noise noise;

    @Override
    public void setSeed(long seed) {
        PreNoise = new BrownianNoise(new PerlinNoise(seed + 25), 12);
        //PreNoise.setPersistence(0.001);
        mountainNoise1 = new SubSampledNoise(new SimplexNoise(seed - 50), new Vector2f(0.01f, 0.01f), 1);
        mountainNoise2 = new SubSampledNoise(PreNoise, new Vector2f(0.01f, 0.01f), 1);
        noise = new SubSampledNoise(new SimplexNoise(seed + 50), new Vector2f(0.001f, 0.001f), 1);
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);
        BiomeHeightFacet biomeHeightFacet = region.getRegionFacet(BiomeHeightFacet.class);
        Rect2i worldRegion = surfaceHeightFacet.getWorldRegion();

        for (BaseVector2i pos : worldRegion.contents()) {
            int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(pos));
            float biomeHeight = biomeHeightFacet.getWorld(pos);
            float CanyonBaseHeight = 10 * (biomeHeight * biomeHeight - 1f);
            float CanyonHeight = 35;
            float sigma = CanyonHeight / 4;
            // check if height is within this region
            float maxCanyonHeight = CanyonBaseHeight + CanyonHeight;
            if ((surfaceHeight >= region.getRegion().minY() && surfaceHeight + maxCanyonHeight <= region.getRegion().maxY())) {

                for (int wy = surfaceHeight; (wy <= region.getRegion().maxY() && wy <= surfaceHeight + maxCanyonHeight &&
                        biomeHeight > 0.6 && biomeHeight < 4); wy++) {


                    // TODO: check for overlap
                    float noiseVal = Math.abs(noise.noise(pos.x(), pos.y(), wy) / 3 + mountainNoise1.noise(pos.x(),
                            pos.y(), wy) / 3 + mountainNoise2.noise(pos.x(), pos.y(), wy) / 3);
                    float probability = gauss(CanyonHeight / 2 - wy, sigma) / 1.5f;
                    if (noiseVal > (1 - probability)) {
                        surfaceHeightFacet.setWorld(pos, (float) wy + CanyonBaseHeight);
                    }
                }
            }
        }
    }


    private float gauss(float x, float sigma) {
        return (float) /*1/(float)Math.sqrt(2*Math.PI*sigma * sigma)**/(float) Math.exp(-x * x / (2 * sigma * sigma));
    }

}

