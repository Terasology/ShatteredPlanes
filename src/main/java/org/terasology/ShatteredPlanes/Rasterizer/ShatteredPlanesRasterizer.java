// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.Rasterizer;

import org.terasology.ShatteredPlanes.Facets.BiomeHeightFacet;
import org.terasology.engine.math.ChunkMath;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.CoreChunk;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizer;
import org.terasology.engine.world.generation.facets.SeaLevelFacet;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.math.geom.Vector3i;

public class ShatteredPlanesRasterizer implements WorldRasterizer {

    private Block dirt, grass, water, sand, stone;

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
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        SeaLevelFacet seaLevelFacet = chunkRegion.getFacet(SeaLevelFacet.class);
        BiomeHeightFacet biomeHeightFacet = chunkRegion.getFacet(BiomeHeightFacet.class);
        for (Vector3i position : chunkRegion.getRegion()) {
            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);
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
            } else if (position.y <= surfaceHeight && position.y >= surfaceHeight - 4 && surfaceHeight < -35) {
                chunk.setBlock(ChunkMath.calcRelativeBlockPos(position), sand);
            }


        }

    }
}

