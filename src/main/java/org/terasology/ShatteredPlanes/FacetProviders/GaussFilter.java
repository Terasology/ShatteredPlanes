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

import org.joml.Math;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.terasology.world.block.BlockArea;
import org.terasology.world.block.BlockAreac;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.FacetBorder;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.facets.ElevationFacet;

import java.util.ArrayList;
//TODO: Differentiate between a messy gaussian filter (copy into temp facet and back) and smooth filter (apply directly to surface)

@Updates(@Facet(value = ElevationFacet.class, border = @FacetBorder(sides = 8)))
public class GaussFilter implements FacetProvider {

    private float sigma;
    private float amplitude;
    private int radius;
    private int mode;
    //smooth mode=1, messy mode=2

    public GaussFilter() {
        sigma = 1f;
        amplitude = 1f;
        radius = 1;
        mode = 1;
    }

    public GaussFilter(float sigma, float amplitude, int radius, int mode) {
        this.sigma = sigma;
        this.amplitude = amplitude;
        this.radius = Math.min(radius, 8);
        this.mode = mode;
    }

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {
        ElevationFacet facet = region.getRegionFacet(ElevationFacet.class);
        BlockAreac worldRegion = facet.getWorldArea().expand(-8, -8, new BlockArea(BlockArea.INVALID));
        // loop through every position on our 2d array
        for (Vector2ic position : worldRegion) {
            float yOrigin = facet.getWorld(position);
            Vector2i[] selection = selector(position, facet.getWorldArea());
            for (Vector2i s1 : selection) {
                float dis = (float) position.distance(s1);
                float ySelection = facet.getWorld(s1);
                float change = 0;
                if (Math.round(yOrigin - ySelection) > 0 && yOrigin > 0) {

                    change =
                        (1 - gauss(dis)) / (float) java.lang.Math.pow(Math.round(yOrigin - ySelection), 1) * amplitude * (float) java.lang.Math.log(yOrigin + 1);
                }
                facet.setWorld(position, yOrigin + change);
            }
        }
    }

    //select all relevant neighbor positions
    private Vector2i[] selector(Vector2ic o, BlockAreac worldRegion) {

        ArrayList<Vector2i> positions = new ArrayList<Vector2i>();

        //circular selector
        for (int r = 1; r <= radius; r++) {
            for (int i = 0; i < 360; i = i + 5) {
                Vector2i temp = new Vector2i(o.x() + Math.round((float) Math.cos(i) * r),
                    o.y() + Math.round((float) Math.sin(i) * r));
                if (!positions.contains(temp) && worldRegion.contains(temp.x, temp.y) && !(temp.x == o.x() && temp.y == o.y())) {
                    positions.add(temp);
                }
            }
        }

        Vector2i[] selection = new Vector2i[positions.size()];
        for (int i = 0; i < selection.length; i++) {
            selection[i] = positions.get(i);
        }
        return selection;
    }

    private float gauss(float x) {
        return (float) /*1/Math.sqrt(2*Math.PI*sigma*sigma)**/Math.exp(-x * x / (2 * sigma * sigma));
    }


    public void setSigma(float sig) {
        sigma = sig;
    }

    public void setAmplitude(float ampl) {
        amplitude = ampl;
    }
}
