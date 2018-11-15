package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeVertexBasics;

public interface PolytopeVertexProvider
{
   PolytopeVertexBasics getVertex();

   PolytopeVertexBasics getVertex(double x, double y, double z);

   PolytopeVertexBasics getVertex(double coords[]);

   PolytopeVertexBasics getVertex(Point3DReadOnly vertexToAdd);
}
