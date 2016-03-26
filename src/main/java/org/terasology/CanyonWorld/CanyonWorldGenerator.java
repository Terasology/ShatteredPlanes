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
package org.terasology.CanyonWorld;

import org.terasology.core.world.generator.facetProviders.SeaLevelProvider;
import org.terasology.engine.SimpleUri;
import org.terasology.registry.In;
import org.terasology.world.generation.BaseFacetedWorldGenerator;
import org.terasology.world.generation.WorldBuilder;
import org.terasology.world.generation.World;
import org.terasology.world.generator.RegisterWorldGenerator;
import org.terasology.world.generator.plugin.WorldGeneratorPluginLibrary;

@RegisterWorldGenerator(id = "CanyonWorld", displayName = "Canyon World")
public class CanyonWorldGenerator extends BaseFacetedWorldGenerator {

    public CanyonWorldGenerator(SimpleUri uri) {
        super(uri);
    }

    @In
    private WorldGeneratorPluginLibrary worldGeneratorPluginLibrary;

    @Override
    protected WorldBuilder createWorld() {
        //Maximal radius for filter is 8
        WorldBuilder worldBuilder=new WorldBuilder(worldGeneratorPluginLibrary)
                .addProvider(new SurfaceProvider())
                .addProvider(new EasterEggProvider())
                .addProvider(new SeaLevelProvider(0))
                .addProvider(new BoulderProvider())
            //  .addProvider(new GaussFilter(2f,0.5f,3,1))
            //  .addProvider(new GaussFilter(1f,1f,3,1, world));
                .addProvider(new SmoothingFilter(1f,2,1))

        //worldBuilder.addProvider(new GaussFilter(1f,0.4f,5,1,world));
        //worldBuilder.addProvider(new SmoothingFilter(1f,0.4f,2,1,world));
                .addRasterizer(new CanyonWorldRasterizer())
                .addRasterizer(new EasterEggRasterizer());
        return worldBuilder;

    }
}
