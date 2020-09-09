// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes;

import org.terasology.ShatteredPlanes.FacetProviders.BiomeHeightProvider;
import org.terasology.ShatteredPlanes.FacetProviders.BoulderProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SmoothingFilter;
import org.terasology.ShatteredPlanes.FacetProviders.SurfaceProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SurrealScaleProvider;
import org.terasology.ShatteredPlanes.Rasterizer.ShatteredPlanesRasterizer;
import org.terasology.coreworlds.generator.facetProviders.SeaLevelProvider;
import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.generation.BaseFacetedWorldGenerator;
import org.terasology.engine.world.generation.WorldBuilder;
import org.terasology.engine.world.generator.RegisterWorldGenerator;
import org.terasology.engine.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "CanyonTest", displayName = "CanyonTest")
public class CanyonTestGenerator extends BaseFacetedWorldGenerator {

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public CanyonTestGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {

        WorldBuilder worldBuilder = new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new SurrealScaleProvider())
                .addProvider(new BiomeHeightProvider())
                .addProvider(new SurfaceProvider())
                .addProvider(new SeaLevelProvider(-2))
                .addProvider(new BoulderProvider())
                //.addProvider(new GaussFilter(2f,0.5f,3,1))
                .addProvider(new SmoothingFilter(1f, 2, 1))
                .addRasterizer(new ShatteredPlanesRasterizer());
        return worldBuilder;

    }
}
