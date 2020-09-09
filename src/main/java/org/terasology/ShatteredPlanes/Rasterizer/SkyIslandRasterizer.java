// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.Rasterizer;

import org.terasology.ShatteredPlanes.Facets.SkyIslandBaseFacet;
import org.terasology.ShatteredPlanes.Facets.SkyIslandBottomHeightFacet;
import org.terasology.ShatteredPlanes.Facets.SkyIslandTopHeightFacet;
import org.terasology.engine.math.ChunkMath;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.CoreChunk;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizer;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.math.geom.Vector3i;

public class SkyIslandRasterizer implements WorldRasterizer {

    private Block dirt, grass, stone;

    @Override
    public void initialize() {
        dirt = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Dirt");
        grass = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Grass");
        stone = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Stone");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SkyIslandBaseFacet skyIslandBaseFacet = chunkRegion.getFacet(SkyIslandBaseFacet.class);
        SkyIslandTopHeightFacet skyIslandTopHeightFacet = chunkRegion.getFacet(SkyIslandTopHeightFacet.class);
        SkyIslandBottomHeightFacet skyIslandBottomHeightFacet = chunkRegion.getFacet(SkyIslandBottomHeightFacet.class);
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);

        for (Vector3i position : chunkRegion.getRegion()) {

            float baseHeight = skyIslandBaseFacet.getWorld(position.x, position.z);
            float topHeight = skyIslandTopHeightFacet.getWorld(position.x, position.z);
            float bottomHeight = skyIslandBottomHeightFacet.getWorld(position.x, position.z);
            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);

            if (baseHeight > surfaceHeight && position.y < baseHeight + topHeight - 1 && position.y >= baseHeight) {
                chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), dirt);
            } else if (baseHeight > surfaceHeight && position.y <= baseHeight + topHeight && position.y >= baseHeight) {
                chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), grass);
            } else if (baseHeight > surfaceHeight && position.y >= baseHeight - bottomHeight && position.y < baseHeight) {
                chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), stone);
            }

        }
    }
}
