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

package geotrellis.raster.io.geotiff

import geotrellis.raster._
import geotrellis.raster.io.geotiff.compression._
import spire.syntax.cfor._

trait Int16GeoTiffSegmentCollection extends GeoTiffSegmentCollection {
  type T = Int16GeoTiffSegment

  val bandType = Int16BandType
  val noDataValue: Option[Short]

  lazy val createSegment: Int => Int16GeoTiffSegment = noDataValue match {
    case None =>
      { i: Int => new Int16RawGeoTiffSegment(getDecompressedBytes(i)) }
    case Some(nd) if (nd == Short.MinValue) =>
      { i: Int => new Int16ConstantNoDataGeoTiffSegment(getDecompressedBytes(i)) }
    case Some(nd) =>
      { i: Int => new Int16UserDefinedNoDataGeoTiffSegment(getDecompressedBytes(i), nd) }
  }
}
