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

import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.math.TeraMath;
import org.terasology.world.block.BlockArea;
import org.terasology.world.block.BlockAreac;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.ElevationFacet;

import java.util.ArrayList;
//TODO: Differentiate between a messy gaussian filter (copy into temp facet and back) and smooth filter (apply directly to surface)

@Requires(@Facet(BiomeHeightFacet.class))
@Updates(@Facet(value = ElevationFacet.class, border = @FacetBorder(sides = 4)))
public class SmoothingFilter implements FacetProvider {

    private float amplitude;
    private int radius;
    private int mode;


    //smooth mode=1, messy mode=2
    public SmoothingFilter() {
        //amplitude has to be between 0 and 1
        amplitude = 1f;
        radius = 1;
        mode = 1;
    }


    public SmoothingFilter(float amplitude, int radius, int mode) {

        this.amplitude = amplitude;
        if (radius <= 2) {
            this.radius = radius;
        } else {
            this.radius = 2;
        }
        this.mode = mode;
    }

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {

        ElevationFacet facet = region.getRegionFacet(ElevationFacet.class);
        BiomeHeightFacet bfacet = region.getRegionFacet(BiomeHeightFacet.class);
        BlockAreac worldRegion = new BlockArea(facet.getWorldArea()).expand(-4, -4);

        for (Vector2ic position : worldRegion) {
            int yOrigin = TeraMath.floorToInt(facet.getWorld(position));
            float biomeHeight = bfacet.getWorld(position);

            if (biomeHeight >= 0) {
                float change = 0;
                ArrayList<Vector2i> selectedPositions = selector(position, facet.getWorldArea());
                for (Vector2i selection : selectedPositions) {
                    float ySelection = facet.getWorld(selection);
                    change += ySelection;
                }

                change = yOrigin + amplitude * (change / selectedPositions.size() - yOrigin)/*TeraMath.clamp((float) Math.log(yOrigin+1),0,1)*/;
                facet.setWorld(position, change);
            }
        }
    }

    //select all relevant neighbor positions
    private ArrayList<Vector2i> selector(Vector2ic o, BlockAreac worldRegionExtended) {
        ArrayList<Vector2i> positions = new ArrayList<>();
        //circular selector
        for (int r = 1; r <= radius; r++) {
            for (int i = 0; i <= 360; i = i + 90) {
                Vector2i temp = new Vector2i(o.x() + Math.round((float) Math.cos(i)) * r, o.y() + Math.round((float) Math.sin(i)) * r);
                if (!positions.contains(temp) /*&& worldRegionExtended.contains(temp.x, temp.y)*/) {
                    positions.add(temp);

                }
            }
        }
        return positions;
    }

    public void setAmplitude(float ampl) {
        amplitude = ampl;
    }
}
