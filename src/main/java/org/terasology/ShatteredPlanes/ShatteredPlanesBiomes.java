// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes;


import org.terasology.biomesAPI.BiomeRegistry;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;

/**
 * Registers biomes.
 */
@RegisterSystem
public class ShatteredPlanesBiomes extends BaseComponentSystem {
    @In
    private BiomeRegistry biomeRegistry;

    @Override
    public void preBegin() {
        biomeRegistry.registerBiome(ShatteredPlanesBiome.RIFT);
    }
}
