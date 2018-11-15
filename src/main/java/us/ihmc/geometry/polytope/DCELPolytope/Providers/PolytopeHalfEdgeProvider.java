package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeHalfEdgeBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeHalfEdgeReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeVertexBasics;

public interface PolytopeHalfEdgeProvider//<A extends PolytopeVertexBasics<A, B, C>, B extends PolytopeHalfEdgeBasics<A, B, C>, C extends ConvexPolytopeFaceBasics<A, B, C>>
{
   PolytopeHalfEdgeBasics getHalfEdge(PolytopeVertexBasics origin, PolytopeVertexBasics destination);

   PolytopeHalfEdgeBasics getHalfEdge();

   PolytopeHalfEdgeBasics getHalfEdge(PolytopeHalfEdgeReadOnly polytopeHalfEdge);
}
