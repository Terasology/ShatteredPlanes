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
package org.terasology.ShatteredPlanes;

import org.terasology.ShatteredPlanes.FacetProviders.BiomeHeightProvider;
import org.terasology.ShatteredPlanes.FacetProviders.BoulderProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SmoothingFilter;
import org.terasology.ShatteredPlanes.FacetProviders.SurfaceProvider;
import org.terasology.ShatteredPlanes.FacetProviders.SurrealScaleProvider;
import org.terasology.ShatteredPlanes.Rasterizer.ShatteredPlanesRasterizer;
import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.engine.core.SimpleUri;
import org.terasology.engine.registry.In;
import org.terasology.engine.world.generation.BaseFacetedWorldGenerator;
import org.terasology.engine.world.generation.WorldBuilder;
import org.terasology.engine.world.generator.RegisterWorldGenerator;
import org.terasology.engine.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "CanyonTest", displayName = "CanyonTest")
public class CanyonTestGenerator extends BaseFacetedWorldGenerator {

    public CanyonTestGenerator(SimpleUri uri) {
        super(uri);
    }

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

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
