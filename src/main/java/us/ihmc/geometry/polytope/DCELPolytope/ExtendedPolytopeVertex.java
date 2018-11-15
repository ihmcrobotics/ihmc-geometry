package us.ihmc.geometry.polytope.DCELPolytope;

import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeVertexBasics;

/**
 * This class stores the location of a point which is the vertex of a polytope A list of polytope
 * edges originating from this vertex is also stored for ease of algorithm design Faces to which
 * this vertex belongs can be accessed by iterating through the list of edges
 *
 * @author Apoorv S
 *
 */
public class ExtendedPolytopeVertex extends PolytopeVertexBasics implements Simplex
{
   private double x, y, z;

   public ExtendedPolytopeVertex()
   {
      setToZero();
   }

   public ExtendedPolytopeVertex(double x, double y, double z)
   {
      set(x, y, z);
   }

   public ExtendedPolytopeVertex(Point3DReadOnly position)
   {
      set(position);
   }

   public ExtendedPolytopeVertex(ExtendedPolytopeVertex vertex)
   {
      set(vertex);
   }

   @Override
   public void setX(double x)
   {
      this.x = x;
   }

   @Override
   public void setY(double y)
   {
      this.y = y;
   }

   @Override
   public void setZ(double z)
   {
      this.z = z;
   }

   @Override
   public double getX()
   {
      return x;
   }

   @Override
   public double getY()
   {
      return y;
   }

   @Override
   public double getZ()
   {
      return z;
   }
}
