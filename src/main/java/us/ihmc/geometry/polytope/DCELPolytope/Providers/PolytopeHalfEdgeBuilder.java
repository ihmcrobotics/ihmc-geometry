package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.Vertex3D;
import us.ihmc.geometry.polytope.DCELPolytope.PolytopeHalfEdge;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeHalfEdgeReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DBasics;

public class PolytopeHalfEdgeBuilder implements PolytopeHalfEdgeProvider
{

   @Override
   public PolytopeHalfEdge getHalfEdge(Vertex3DBasics origin, Vertex3DBasics destination)
   {
      return new PolytopeHalfEdge((Vertex3D) origin, (Vertex3D) destination);
   }

   @Override
   public PolytopeHalfEdge getHalfEdge()
   {
      return new PolytopeHalfEdge();
   }

   @Override
   public PolytopeHalfEdge getHalfEdge(PolytopeHalfEdgeReadOnly polytopeHalfEdge)
   {
      return new PolytopeHalfEdge(polytopeHalfEdge);
   }
}
