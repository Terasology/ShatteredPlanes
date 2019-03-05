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
package org.terasology.ShatteredPlanes.Rasterizer;

import org.terasology.ShatteredPlanes.Facets.EasterEggFacet;
import org.terasology.math.ChunkMath;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

import java.util.ArrayList;

/**
 * Creates snow-made easter eggs all over the world.
 *
 * Eggs will never intersect chunk boundaries.
 */
public class EasterEggRasterizer implements WorldRasterizer {

    private Block snow;
    private int eggHeight = 6;
    private int eggRadius = 4;

    @Override
    public void initialize() {
        snow = CoreRegistry.get(BlockManager.class).getBlock("Core:Snow");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        EasterEggFacet eggFacet = chunkRegion.getFacet(EasterEggFacet.class);
        for (Vector3i position : chunkRegion.getRegion().expand(-eggRadius - 1)) {

            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);
            if (position.y == surfaceHeight && eggFacet.getWorld(position.x, position.z)) {
                for (int h = -eggHeight; h <= eggHeight; h++) {
                    int radius = (int) Math.round(Math.sqrt((eggHeight * eggHeight - h * h)) * eggRadius / (eggHeight * Math.sqrt(Math.exp(0.2 * h))));

                    Vector2i[] selection = selector(new Vector2i(position.x, position.z), radius);
                    for (int i = 0; i < selection.length; i++) {
                        chunk.setBlock(ChunkMath.calcBlockPos(selection[i].x, (int) surfaceHeight + eggHeight + h, selection[i].y), snow);
                    }
                }

            }

        }
    }

    private Vector2i[] selector(BaseVector2i o, int radius) {

        ArrayList<Vector2i> positions = new ArrayList<Vector2i>();

        //circular selector
        for (int r = 0; r <= radius; r++) {
            for (int i = 0; i < 360; i = i + 2) {
                Vector2i temp = new Vector2i(o.x() + Math.round((float) Math.cos(i) * r), o.y() + Math.round((float) Math.sin(i) * r));
                if (!positions.contains(temp)) {
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
}
