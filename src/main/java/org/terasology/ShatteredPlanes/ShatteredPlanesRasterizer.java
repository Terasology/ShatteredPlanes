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
package org.terasology.ShatteredPlanes;

import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.SeaLevelFacet;
import org.terasology.world.generation.facets.SurfaceHeightFacet;

public class ShatteredPlanesRasterizer implements WorldRasterizer {

    private Block dirt;
    private Block grass, water, sand, inviswall;

    @Override
    public void initialize() {
        dirt = CoreRegistry.get(BlockManager.class).getBlock("Core:Dirt");
        grass = CoreRegistry.get(BlockManager.class).getBlock("Core:Grass");
        water = CoreRegistry.get(BlockManager.class).getBlock("Core:Water");
        sand = CoreRegistry.get(BlockManager.class).getBlock("Core:Sand");
        inviswall = CoreRegistry.get(BlockManager.class).getBlock("ShatteredPlanes:InvisWall");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        SeaLevelFacet seaLevelFacet = chunkRegion.getFacet(SeaLevelFacet.class);
        BiomeHeightFacet biomeHeightFacet = chunkRegion.getFacet(BiomeHeightFacet.class);
        for (Vector3i position : chunkRegion.getRegion()) {

            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);
            float biomeHeight = biomeHeightFacet.getWorld(position.x,position.z);
            if (position.y >= surfaceHeight-3 && position.y<=surfaceHeight  && biomeHeight < 0 && surfaceHeight < 5) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), sand);
            } else if (position.y < surfaceHeight - 1) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), dirt);
            } else if (position.y < surfaceHeight && surfaceHeight>seaLevelFacet.getSeaLevel()) {
                chunk.setBlock(ChunkMath.calcBlockPos(position), grass);
            } else if (position.y < seaLevelFacet.getSeaLevel() && position.y >= surfaceHeight && surfaceHeight>-250 && biomeHeight<0) {
                chunk.setBlock(ChunkMath.calcBlockPos(position),water);
            } else if (position.y <= surfaceHeight && surfaceHeight<-250) {
                chunk.setBlock(ChunkMath.calcBlockPos(position),inviswall);
            }

        }
    }
}
