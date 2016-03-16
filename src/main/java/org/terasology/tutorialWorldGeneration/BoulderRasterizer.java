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

import java.util.Map.Entry;
import java.lang.Math;

import org.terasology.math.ChunkMath;
import org.terasology.math.Region3i;
import org.terasology.math.TeraMath;
import org.terasology.math.geom.BaseVector3i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

public class BoulderRasterizer implements WorldRasterizer {
    private Block stone;
    private Block grass;

    @Override
    public void initialize() {
        stone = CoreRegistry.get(BlockManager.class).getBlock("Core:Dirt");
        grass = CoreRegistry.get(BlockManager.class).getBlock("Core:Grass");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        BoulderFacet boulderFacet = chunkRegion.getFacet(BoulderFacet.class);
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);

            for (Vector3i position : chunkRegion.getRegion()) {
                float boulderVal = boulderFacet.getWorld(position.x,position.y,position.z);
                if (boulderVal>0.99) {
                    int surfaceHeight = TeraMath.floorToInt(surfaceHeightFacet.getWorld(position.x,position.z));
                    for(int y=surfaceHeight;y<=position.y-1;y++) {
                        chunk.setBlock(ChunkMath.calcBlockPos(position.x,y,position.z), stone);
                    }
                    chunk.setBlock(ChunkMath.calcBlockPos(position), grass);
                    surfaceHeightFacet.setWorld(position.x,position.z,position.y);
                }

            }
    }
}
