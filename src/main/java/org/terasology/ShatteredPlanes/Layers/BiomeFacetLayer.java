// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.ShatteredPlanes.Layers;

import org.terasology.biomesAPI.Biome;
import org.terasology.coreworlds.generator.facets.BiomeFacet;
import org.terasology.engine.world.viewer.layers.NominalFacetLayer;
import org.terasology.engine.world.viewer.layers.Renders;
import org.terasology.engine.world.viewer.layers.ZOrder;

/**
 * Maps {@link org.terasology.ShatteredPlanes.ShatteredPlanesBiome} biomes to corresponding colors.
 */
@Renders(value = BiomeFacet.class, order = ZOrder.BIOME)
public class BiomeFacetLayer extends NominalFacetLayer<Biome> {

    public BiomeFacetLayer() {
        super(BiomeFacet.class, new ShatteredPlanesBiomeColors());
    }
}
