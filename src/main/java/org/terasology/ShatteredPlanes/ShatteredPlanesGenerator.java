/*
 * Copyright 2014 MovingBlocks
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
package org.terasology.ShatteredPlanes;

import org.terasology.ShatteredPlanes.FacetProviders.*;
import org.terasology.ShatteredPlanes.Rasterizer.SkyIslandRasterizer;
import org.terasology.ShatteredPlanes.Rasterizer.SolidRasterizer;
import org.terasology.core.world.generator.facetProviders.PerlinHumidityProvider;
import org.terasology.core.world.generator.facetProviders.PerlinSurfaceTemperatureProvider;
import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.core.world.generator.facetProviders.SurfaceToDensityProvider;
import org.terasology.core.world.generator.rasterizers.FloraRasterizer;
import org.terasology.core.world.generator.rasterizers.TreeRasterizer;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "ShatteredPlanes", displayName = "Shattered Planes")
public class ShatteredPlanesGenerator extends BaseFacetedWorldGenerator {

    public ShatteredPlanesGenerator(SimpleUri uri) {
        super(uri);
    }

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

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
                .addProvider(new BiomeProvider())
                .addProvider(new SmoothingFilter(1f, 2, 1))
                .addProvider(new SurfaceToDensityProvider())
                .addProvider(new SkyIslandBaseProvider())
                .addProvider(new SkyIslandTopHeightProvider())
                .addProvider(new SkyIslandBottomHeightProvider())
                .addProvider(new DefaultFloraProvider())
                .addProvider(new DefaultTreeProvider())
                //.addProvider(new GaussFilter(2f,0.5f,3,1))
                //.addRasterizer(new ShatteredPlanesRasterizer())
                .addRasterizer(new SkyIslandRasterizer())
                .addRasterizer(new SolidRasterizer())
                .addRasterizer(new FloraRasterizer())
                .addRasterizer(new TreeRasterizer());
        return worldBuilder;

    }
}
