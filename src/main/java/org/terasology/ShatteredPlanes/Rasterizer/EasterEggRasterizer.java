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

import org.joml.Vector2ic;
import org.joml.Vector3i;
import org.terasology.ShatteredPlanes.Facets.EasterEggFacet;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.Chunk;
import org.terasology.engine.world.chunks.Chunks;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizer;
import org.terasology.engine.world.generation.facets.SurfacesFacet;

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
        snow = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Snow");
    }

    @Override
    public void generateChunk(Chunk chunk, Region chunkRegion) {
        SurfacesFacet surfacesFacet = chunkRegion.getFacet(SurfacesFacet.class);
        EasterEggFacet eggFacet = chunkRegion.getFacet(EasterEggFacet.class);
        Vector3i temp = new Vector3i();

        for (Vector2ic p : eggFacet.getWorldArea()) {
            if (eggFacet.getWorld(p)) {
                for (int z : surfacesFacet.getWorldColumn(p.x(), p.y())) {
                    for (int h = -eggHeight; h <= eggHeight; h++) {
                        int radius = (int) Math.round(Math.sqrt((eggHeight * eggHeight - h * h)) * eggRadius / (eggHeight * Math.sqrt(Math.exp(0.2 * h))));
                        for (int r = 0; r <= radius; r++) {
                            for (int i = 0; i < 360; i = i + 2) {
                                chunk.setBlock(Chunks.toRelative(p.x() + Math.round((float) Math.cos(i) * r), z + eggHeight + h, p.y() + Math.round((float) Math.sin(i) * r), temp), snow);
                            }
                        }
                    }
                    break;
                }
            }
        }
    }
}
