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
import org.terasology.math.geom.Vector2i;
import org.terasology.utilities.procedural.Noise;
import org.terasology.utilities.procedural.SimplexNoise;
import org.terasology.utilities.procedural.SubSampledNoise;
import org.terasology.world.generation.Facet;
import org.terasology.world.generation.Border3D;
import org.terasology.world.generation.FacetProvider;
import org.terasology.world.generation.GeneratingRegion;
import org.terasology.world.generation.Updates;
import org.terasology.world.generation.Requires;
import org.terasology.world.generation.facets.SurfaceHeightFacet;
//TODO: Differentiate between a messy gaussian filter (copy into temp facet and back) and smooth filter (apply directly to surface)

@Updates({  @Facet(SurfaceHeightFacet.class)})
public class GaussFilter1 implements FacetProvider {

    private float sigma;
    private float amplitude;
    private int radius;
    private int mode;
    private Region3i region;
    private Rect2i worldRegion;
    private SurfaceHeightFacet facet;
    //smooth mode=1, messy mode=2
    public GaussFilter1(){
        sigma=1f;
        amplitude=1f;
        radius=1;
        mode=1;
    }

    public GaussFilter1(float sigma, float amplitude, int radius, int mode){
        this.sigma=sigma;
        this.amplitude=amplitude;
        this.radius=radius;
        this.mode=mode;
    }

    @Override
    public void setSeed(long seed) {
    }

    @Override
    public void process(GeneratingRegion region) {
        this.region=region.getRegion();

        facet = region.getRegionFacet(SurfaceHeightFacet.class);
        SurfaceHeightFacet temp;
        SurfaceHeightFacet selectedFacet=facet;
        worldRegion=facet.getWorldRegion();
        if(mode==2){
            Border3D border = region.getBorderForFacet(SurfaceHeightFacet.class);
            border.extendBy(0,0,radius);
            temp = new SurfaceHeightFacet(region.getRegion(), border);
            temp.set(facet.getInternal());
            selectedFacet=temp;
        }
        // loop through every position on our 2d array
        for (int wz = region.getRegion().minZ(); wz <= region.getRegion().maxZ(); wz++) {
            for (int wx = region.getRegion().minX(); wx <= region.getRegion().maxX(); wx++) {
                Vector2i position=new Vector2i(wx,wz);
                float yOrigin = facet.getWorld(position);

                if(yOrigin>0 && yOrigin>=this.region.minY() && yOrigin<=this.region.maxY()) {
                    Vector2i[] selection = selector(position);
                    /*for (int i = 0; i < selection.length; i++) {
                        float dis = (float) position.distance(selection[i]);

                        float ySelection = facet.getWorld(selection[i]);
                        float change=0;
                        if(Math.round(yOrigin - ySelection)>0) {
                            change = (1-gauss(dis)) / (float)Math.pow(Math.round(yOrigin - ySelection),1) * amplitude * (float) Math.log(yOrigin+1);
                        }
                        selectedFacet.setWorld(position, yOrigin + change);
                    }*/
                }
            }
        }
        if(mode==2){
            facet.set(selectedFacet.getInternal());
        }


    }

    //select all relevant neighbor positions
    private Vector2i[] selector(BaseVector2i o){

        ArrayList<Vector2i> positions = new ArrayList<Vector2i>();

        //circular selector
        for(int r=1;r<=radius;r++) {
            for (int i = 0; i < 360; i=i+5) {
                Vector2i temp=new Vector2i(o.x() + Math.round((float) Math.cos(i)*r), o.y() + Math.round((float) Math.sin(i)*r));
                if(!positions.contains(temp) && worldRegion.contains(temp.x,temp.y)){
                    float height=facet.getWorld(temp);
                    if(height>=region.minY() && height<=region.maxY()){
                        positions.add(temp);
                    }
                }


            }
        }

        Vector2i[] selection = new Vector2i[positions.size()];
        for(int i=0;i<selection.length;i++){
            selection[i]=positions.get(i);
        }
        return selection;
    }
    private float gauss(float x){
        return (float) /*1/Math.sqrt(2*Math.PI*sigma*sigma)**/Math.exp(-x*x/(2*sigma*sigma));
    }



    public void setSigma(float sig){
        sigma=sig;
    }

    public void setAmplitude(float ampl){
        amplitude=ampl;
    }
}
