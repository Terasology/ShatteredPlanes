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

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.math.ChunkMath;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.chunks.CoreChunk;
import org.terasology.world.generation.Region;
import org.terasology.world.generation.WorldRasterizer;
import org.terasology.world.generation.facets.ElevationFacet;
import org.terasology.world.generation.facets.SeaLevelFacet;

public class ShatteredPlanesRasterizer implements WorldRasterizer {

    private Block dirt, grass, water, sand,stone;

    @Override
    public void initialize() {
        dirt = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Dirt");
        grass = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Grass");
        water = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Water");
        stone = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Stone");
        sand = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Sand");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        ElevationFacet elevationFacet = chunkRegion.getFacet(ElevationFacet.class);
        SeaLevelFacet seaLevelFacet = chunkRegion.getFacet(SeaLevelFacet.class);
        BiomeHeightFacet biomeHeightFacet = chunkRegion.getFacet(BiomeHeightFacet.class);
        for (Vector3i position : chunkRegion.getRegion()) {
            float surfaceHeight = elevationFacet.getWorld(position.x, position.z);
            float biomeHeight = biomeHeightFacet.getWorld(position.x, position.z);

                if (position.y >= surfaceHeight - 3 && position.y <= surfaceHeight && biomeHeight < 0 && surfaceHeight < 5) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), sand);
                } else if (position.y < surfaceHeight - 5) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), stone);
                } else if (position.y < surfaceHeight - 1 && surfaceHeight > -55) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), dirt);
                } else if (position.y < surfaceHeight && surfaceHeight > seaLevelFacet.getSeaLevel()) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), grass);
                } else if (position.y < seaLevelFacet.getSeaLevel() && position.y >= surfaceHeight && surfaceHeight > -250 && biomeHeight < 0) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), water);
                } else if (position.y <= -40) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), water);
                } else if (position.y <= surfaceHeight && position.y >= surfaceHeight - 4 && surfaceHeight< -35) {
                    chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), sand);
                }


        }

    }
}

