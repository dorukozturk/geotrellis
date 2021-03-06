/*
 * Copyright 2016 Azavea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package geotrellis.spark.stitch

import geotrellis.raster._
import geotrellis.raster.stitch.Stitcher
import geotrellis.vector.Extent
import geotrellis.spark._
import geotrellis.spark.tiling._
import geotrellis.util._

import org.apache.spark.rdd.RDD

abstract class SpatialTileLayoutCollectionStitchMethods[V <: CellGrid: Stitcher, M: GetComponent[?, LayoutDefinition]]
  extends MethodExtensions[Seq[(SpatialKey, V)] with Metadata[M]] {

  def stitch(): Raster[V] = {
    val (tile, bounds) = TileLayoutStitcher.stitch(self)
    val mapTransform = self.metadata.getComponent[LayoutDefinition].mapTransform
    Raster(tile, mapTransform(bounds))
  }
}

abstract class SpatialTileCollectionStitchMethods[V <: CellGrid: Stitcher]
  extends MethodExtensions[Seq[(SpatialKey, V)]] {

  def stitch(): V = TileLayoutStitcher.stitch(self)._1
}
