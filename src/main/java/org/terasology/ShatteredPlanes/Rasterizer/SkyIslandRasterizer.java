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

import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.terasology.ShatteredPlanes.Facets.SkyIslandBaseFacet;
import org.terasology.ShatteredPlanes.Facets.SkyIslandBottomHeightFacet;
import org.terasology.ShatteredPlanes.Facets.SkyIslandTopHeightFacet;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.Chunk;
import org.terasology.engine.world.chunks.Chunks;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizer;
import org.terasology.engine.world.generation.facets.ElevationFacet;

public class SkyIslandRasterizer implements WorldRasterizer {

    private Block dirt;
    private Block grass;
    private Block stone;

    @Override
    public void initialize() {
        dirt = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Dirt");
        grass = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Grass");
        stone = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Stone");
    }

    @Override
    public void generateChunk(Chunk chunk, Region chunkRegion) {
        SkyIslandBaseFacet skyIslandBaseFacet = chunkRegion.getFacet(SkyIslandBaseFacet.class);
        SkyIslandTopHeightFacet skyIslandTopHeightFacet = chunkRegion.getFacet(SkyIslandTopHeightFacet.class);
        SkyIslandBottomHeightFacet skyIslandBottomHeightFacet = chunkRegion.getFacet(SkyIslandBottomHeightFacet.class);
        ElevationFacet elevationFacet = chunkRegion.getFacet(ElevationFacet.class);

        Vector3i tempPos = new Vector3i();
        for (Vector3ic position : chunkRegion.getRegion()) {

            float baseHeight = skyIslandBaseFacet.getWorld(position.x(), position.z());
            float topHeight = skyIslandTopHeightFacet.getWorld(position.x(), position.z());
            float bottomHeight = skyIslandBottomHeightFacet.getWorld(position.x(), position.z());
            float surfaceHeight = elevationFacet.getWorld(position.x(), position.z());

            if (baseHeight > surfaceHeight && position.y() < baseHeight + topHeight - 1 && position.y() >= baseHeight) {
                chunk.setBlock(Chunks.toRelative(position, tempPos), dirt);
            } else if (baseHeight > surfaceHeight && position.y() <= baseHeight + topHeight && position.y() >= baseHeight) {
                chunk.setBlock(Chunks.toRelative(position, tempPos), grass);
            } else if (baseHeight > surfaceHeight && position.y() >= baseHeight - bottomHeight && position.y() < baseHeight) {
                chunk.setBlock(Chunks.toRelative(position, tempPos), stone);
            }

        }
    }
}
