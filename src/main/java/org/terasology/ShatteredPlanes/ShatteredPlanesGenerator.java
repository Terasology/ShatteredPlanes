// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes;

import org.terasology.ShatteredPlanes.FacetProviders.BiomeHeightProvider;
import org.terasology.ShatteredPlanes.FacetProviders.BiomeProvider;
import org.terasology.ShatteredPlanes.FacetProviders.BoulderProvider;
import org.terasology.ShatteredPlanes.FacetProviders.DefaultFloraProvider;
import org.terasology.ShatteredPlanes.FacetProviders.DefaultTreeProvider;
import org.terasology.ShatteredPlanes.FacetProviders.HillsProvider;
import org.terasology.ShatteredPlanes.FacetProviders.MountainsProvider;
import org.terasology.ShatteredPlanes.FacetProviders.OceanProvider;
import org.terasology.ShatteredPlanes.FacetProviders.RiftProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SkyIslandBaseProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SkyIslandBottomHeightProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SkyIslandTopHeightProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SmoothingFilter;
import org.terasology.ShatteredPlanes.FacetProviders.SurfaceProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SurrealScaleProvider;
import org.terasology.ShatteredPlanes.Rasterizer.SkyIslandRasterizer;
import org.terasology.ShatteredPlanes.Rasterizer.SolidRasterizer;
import org.terasology.coreworlds.generator.facetProviders.PerlinHumidityProvider;
import org.terasology.coreworlds.generator.facetProviders.PerlinSurfaceTemperatureProvider;
import org.terasology.coreworlds.generator.facetProviders.SeaLevelProvider;
import org.terasology.coreworlds.generator.facetProviders.SurfaceToDensityProvider;
import org.terasology.coreworlds.generator.rasterizers.FloraRasterizer;
import org.terasology.coreworlds.generator.rasterizers.TreeRasterizer;
import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.generation.BaseFacetedWorldGenerator;
import org.terasology.engine.world.generation.WorldBuilder;
import org.terasology.engine.world.generator.RegisterWorldGenerator;
import org.terasology.engine.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "ShatteredPlanes", displayName = "Shattered Planes")
public class ShatteredPlanesGenerator extends BaseFacetedWorldGenerator {

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    public ShatteredPlanesGenerator(SimpleUri uri) {
        super(uri);
    }

    @Override
    protected WorldBuilder createWorld() {

        WorldBuilder worldBuilder = new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new SurrealScaleProvider())
                .addProvider(new BiomeHeightProvider())
                .addProvider(new PerlinHumidityProvider())
                .addProvider(new PerlinSurfaceTemperatureProvider())
                .addProvider(new SurfaceProvider())
                .addProvider(new SeaLevelProvider(0))
                .addProvider(new OceanProvider())
                .addProvider(new RiftProvider())
                .addProvider(new BoulderProvider())
                .addProvider(new HillsProvider())
                .addProvider(new MountainsProvider())
                .addProvider(new SmoothingFilter(1f, 1, 1))
                .addProvider(new BiomeProvider())
                .addProvider(new SurfaceToDensityProvider())
                .addProvider(new SkyIslandBaseProvider())
                .addProvider(new SkyIslandTopHeightProvider())
                .addProvider(new SkyIslandBottomHeightProvider())
                .addProvider(new DefaultFloraProvider())
                .addProvider(new DefaultTreeProvider())
                //.addProvider(new GaussFilter(2f,0.5f,3,1))
                .addRasterizer(new SkyIslandRasterizer())
                .addRasterizer(new SolidRasterizer())
                .addRasterizer(new FloraRasterizer())
                .addRasterizer(new TreeRasterizer())
                .addPlugins();

        return worldBuilder;

    }
}
