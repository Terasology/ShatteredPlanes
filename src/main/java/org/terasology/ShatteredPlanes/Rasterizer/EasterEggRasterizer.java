// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.Rasterizer;

import org.terasology.ShatteredPlanes.Facets.EasterEggFacet;
import org.terasology.engine.math.ChunkMath;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.world.block.Block;
import org.terasology.engine.world.block.BlockManager;
import org.terasology.engine.world.chunks.CoreChunk;
import org.terasology.engine.world.generation.Region;
import org.terasology.engine.world.generation.WorldRasterizer;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;
import org.terasology.math.geom.BaseVector2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.math.geom.Vector3i;

import java.util.ArrayList;

/**
 * Creates snow-made easter eggs all over the world.
 * <p>
 * Eggs will never intersect chunk boundaries.
 */
public class EasterEggRasterizer implements WorldRasterizer {

    private final int eggHeight = 6;
    private final int eggRadius = 4;
    private Block snow;

    @Override
    public void initialize() {
        snow = CoreRegistry.get(BlockManager.class).getBlock("CoreAssets:Snow");
    }

    @Override
    public void generateChunk(CoreChunk chunk, Region chunkRegion) {
        SurfaceHeightFacet surfaceHeightFacet = chunkRegion.getFacet(SurfaceHeightFacet.class);
        EasterEggFacet eggFacet = chunkRegion.getFacet(EasterEggFacet.class);
        for (Vector3i position : chunkRegion.getRegion().expand(-eggRadius - 1)) {

            float surfaceHeight = surfaceHeightFacet.getWorld(position.x, position.z);
            if (position.y == surfaceHeight && eggFacet.getWorld(position.x, position.z)) {
                for (int h = -eggHeight; h <= eggHeight; h++) {
                    int radius =
                            (int) Math.round(Math.sqrt((eggHeight * eggHeight - h * h)) * eggRadius / (eggHeight * Math.sqrt(Math.exp(0.2 * h))));

                    Vector2i[] selection = selector(new Vector2i(position.x, position.z), radius);
                    for (int i = 0; i < selection.length; i++) {
                        chunk.setBlock(ChunkMath.calcRelativeBlockPos(selection[i].x,
                                (int) surfaceHeight + eggHeight + h, selection[i].y), snow);
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
                Vector2i temp = new Vector2i(o.x() + Math.round((float) Math.cos(i) * r),
                        o.y() + Math.round((float) Math.sin(i) * r));
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
