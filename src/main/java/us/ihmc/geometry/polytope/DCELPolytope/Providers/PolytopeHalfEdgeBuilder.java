package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.ExtendedPolytopeVertex;
import us.ihmc.geometry.polytope.DCELPolytope.PolytopeHalfEdge;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeHalfEdgeReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeVertexBasics;

public class PolytopeHalfEdgeBuilder implements PolytopeHalfEdgeProvider
{

   @Override
   public PolytopeHalfEdge getHalfEdge(PolytopeVertexBasics origin, PolytopeVertexBasics destination)
   {
      return new PolytopeHalfEdge((ExtendedPolytopeVertex) origin, (ExtendedPolytopeVertex) destination);
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
