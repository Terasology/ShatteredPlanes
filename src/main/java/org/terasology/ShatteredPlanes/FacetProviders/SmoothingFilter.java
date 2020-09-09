// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.FacetProviders;

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
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
import org.terasology.math.geom.Vector2i;

import java.util.ArrayList;
//TODO: Differentiate between a messy gaussian filter (copy into temp facet and back) and smooth filter (apply 
// directly to surface)

@Requires(@Facet(BiomeHeightFacet.class))
@Updates(@Facet(value = SurfaceHeightFacet.class, border = @FacetBorder(sides = 4)))
public class SmoothingFilter implements FacetProvider {

    private final int radius;
    private final int mode;
    private float amplitude;


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

        SurfaceHeightFacet facet = region.getRegionFacet(SurfaceHeightFacet.class);
        BiomeHeightFacet bfacet = region.getRegionFacet(BiomeHeightFacet.class);
        Rect2i worldRegionExtended = facet.getWorldRegion();
        Rect2i worldRegion = worldRegionExtended.expand(-4, -4);


        for (BaseVector2i position : worldRegion.contents()) {
            int yOrigin = TeraMath.floorToInt(facet.getWorld(position));
            float biomeHeight = bfacet.getWorld(position);

            if (biomeHeight >= 0) {
                float change = 0;
                ArrayList<Vector2i> selectedPositions = selector(position, worldRegionExtended);
                for (Vector2i selection : selectedPositions) {

                    float dis = (float) position.distance(selection);
                    float ySelection = facet.getWorld(selection);

                    change += ySelection;

                }

                change = yOrigin + amplitude * (change / selectedPositions.size() - yOrigin)/*TeraMath.clamp((float) 
                Math.log(yOrigin+1),0,1)*/;
                TeraMath.clamp(change, region.getRegion().minY(), region.getRegion().maxY());
                facet.setWorld(position, change);

            }
        }
    }


    //select all relevant neighbor positions
    private ArrayList<Vector2i> selector(BaseVector2i o, Rect2i worldRegionExtended) {

        ArrayList<Vector2i> positions = new ArrayList<>();

        //circular selector
        for (int r = 1; r <= radius; r++) {
            for (int i = 0; i <= 360; i = i + 90) {
                Vector2i temp = new Vector2i(o.x() + Math.round((float) Math.cos(i)) * r,
                        o.y() + Math.round((float) Math.sin(i)) * r);
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
