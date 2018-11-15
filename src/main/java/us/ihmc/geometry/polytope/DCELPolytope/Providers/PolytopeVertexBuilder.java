package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Vertex3D;

public class PolytopeVertexBuilder implements PolytopeVertexProvider
{

   @Override
   public Vertex3D getVertex()
   {
      return new Vertex3D();
   }

   @Override
   public Vertex3D getVertex(double x, double y, double z)
   {
      return new Vertex3D(x, y, z);
   }

   @Override
   public Vertex3D getVertex(double[] coords)
   {
      return new Vertex3D(coords[0], coords[1], coords[2]);
   }

   @Override
   public Vertex3D getVertex(Point3DReadOnly vertexToAdd)
   {
      return new Vertex3D(vertexToAdd);
   }

}
