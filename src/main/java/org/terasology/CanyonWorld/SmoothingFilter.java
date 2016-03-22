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
package org.terasology.CanyonWorld;

import java.lang.Math;
import java.util.ArrayList;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.Region3i;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.Vector2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.World;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
//TODO: Differentiate between a messy gaussian filter (copy into temp facet and back) and smooth filter (apply directly to surface)

@Updates({  @Facet(SurfaceHeightFacet.class)})
public class SmoothingFilter implements FacetProvider {

    private float sigma;
    private float amplitude;
    private int radius;
    private int mode;
    private Region3i region;
    private World world;
    /*
    //smooth mode=1, messy mode=2
    public SmoothingFilter(){
        sigma=1f;
        //amplitude has to be between 0 and 1
        amplitude=1f;
        radius=1;
        mode=1;
    }
    */

    public SmoothingFilter(float sigma, float amplitude, int radius, int mode, World world){
        this.sigma=sigma;
        this.amplitude=amplitude;
        this.radius=radius;
        this.mode=mode;
        this.world=world;
    }

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {
        SurfaceHeightFacet facet = region.getRegionFacet(SurfaceHeightFacet.class);
        Rect2i worldRegion=facet.getWorldRegion();
        Region3i region3iExtended=region.getRegion().expand(radius);
        Region regionExtended = world.getWorldData(region3iExtended);
        SurfaceHeightFacet facetExtended = regionExtended.getFacet(SurfaceHeightFacet.class);
        Rect2i worldRegionExtended=facetExtended.getWorldRegion();


        for (BaseVector2i position : worldRegion.contents()){

            float yOrigin = facet.getWorld(position);


            if(facet.getWorld(position)>0) {
                Vector2i[] selection = selector(position, worldRegionExtended);
                float change=0;
                for (int i = 0; i < selection.length; i++) {

                    float dis = (float) position.distance(selection[i]);
                    float ySelection = facetExtended.getWorld(selection[i]);

                    change += ySelection;

                    }

                    change=amplitude*(change/selection.length-yOrigin)*TeraMath.clamp((float) Math.log(yOrigin+1),0,1);
                    facet.setWorld(position, facet.getWorld(position) + change);

                }
        }
    }

    private float gauss(float x){
        return (float) /*1/Math.sqrt(2*Math.PI*sigma*sigma)**/Math.exp(-x*x/(2*sigma*sigma));
    }

    //select all relevant neighbor positions
    private Vector2i[] selector(BaseVector2i o, Rect2i worldRegionExtended){

        ArrayList<Vector2i> positions = new ArrayList<Vector2i>();

        //circular selector
        for(int r=1;r<=radius;r++) {
            for (int i = 0; i < 360; i=i+5) {
                Vector2i temp=new Vector2i(o.x() + Math.round((float) Math.cos(i))*r, o.y() + Math.round((float) Math.sin(i))*r);
                if(!positions.contains(temp) && worldRegionExtended.contains(temp.x,temp.y)){
                    positions.add(temp);

                }


            }
        }

        Vector2i[] selection = new Vector2i[positions.size()];
        for(int i=0;i<selection.length;i++){
            selection[i]=positions.get(i);
        }
        return selection;
    }

    public void setSigma(float sig){
        sigma=sig;
    }

    public void setAmplitude(float ampl){
        amplitude=ampl;
    }
}
