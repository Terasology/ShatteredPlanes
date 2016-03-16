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
package org.terasology.CanyonWorld;

import java.lang.Math;

import org.terasology.math.geom.Rect2i;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Vector2f;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.PerlinNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.utilities.procedural.BrownianNoise;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.Border3D;

@Updates(@Facet(SurfaceHeightFacet.class))
public class SpireProvider implements FacetProvider {

    private BrownianNoise PreNoise;
    private Noise mountainNoise1;
    private Noise mountainNoise2;

    @Override
    public void setSeed(long seed) {
        PreNoise=new BrownianNoise(new PerlinNoise(seed + 10), 8);
        PreNoise.setPersistence(0.0001);
        mountainNoise1 = new SubSampledNoise(new SimplexNoise(seed), new Vector2f(0.01f, 0.001f), 1);
        mountainNoise2 = new SubSampledNoise(PreNoise, new Vector2f(0.001f, 0.01f), 1);

    }

    @Override
    public void process(GeneratingRegion region) {

        SurfaceHeightFacet surfaceHeightFacet = region.getRegionFacet(SurfaceHeightFacet.class);

        float SpireHeight=7;

        Rect2i processRegion = surfaceHeightFacet.getWorldRegion();
        for (BaseVector2i position : processRegion.contents()) {
            float height = TeraMath.clamp(mountainNoise2.noise(position.x(),position.y()),0,1);
            float sheight=surfaceHeightFacet.getWorld(position);

            if(height>0.2 && sheight < 10){
                surfaceHeightFacet.setWorld(position,sheight+(float)Math.exp(height*SpireHeight));
            }
        }
    }
}
