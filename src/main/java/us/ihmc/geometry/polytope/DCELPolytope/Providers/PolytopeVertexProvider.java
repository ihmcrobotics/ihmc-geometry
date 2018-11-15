package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DBasics;

public interface PolytopeVertexProvider
{
   Vertex3DBasics getVertex();

   Vertex3DBasics getVertex(double x, double y, double z);

   Vertex3DBasics getVertex(double coords[]);

   Vertex3DBasics getVertex(Point3DReadOnly vertexToAdd);
}
