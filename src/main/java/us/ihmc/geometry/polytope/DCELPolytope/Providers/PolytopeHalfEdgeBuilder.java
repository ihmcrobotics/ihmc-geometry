package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.Vertex3D;
import us.ihmc.geometry.polytope.DCELPolytope.HalfEdge3D;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DBasics;

public class PolytopeHalfEdgeBuilder implements PolytopeHalfEdgeProvider
{

   @Override
   public HalfEdge3D getHalfEdge(Vertex3DBasics origin, Vertex3DBasics destination)
   {
      return new HalfEdge3D((Vertex3D) origin, (Vertex3D) destination);
   }

   @Override
   public HalfEdge3D getHalfEdge()
   {
      return new HalfEdge3D();
   }

   @Override
   public HalfEdge3D getHalfEdge(HalfEdge3DReadOnly polytopeHalfEdge)
   {
      return new HalfEdge3D(polytopeHalfEdge);
   }
}
