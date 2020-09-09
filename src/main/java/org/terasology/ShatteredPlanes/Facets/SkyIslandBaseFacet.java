// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.ShatteredPlanes.Facets;

import org.terasology.engine.math.Region3i;
import org.terasology.engine.world.generation.Border3D;
import org.terasology.engine.world.generation.facets.SurfaceHeightFacet;

public class SkyIslandBaseFacet extends SurfaceHeightFacet {

    public SkyIslandBaseFacet(Region3i targetRegion, Border3D border) {
        super(targetRegion, border);
    }
}
