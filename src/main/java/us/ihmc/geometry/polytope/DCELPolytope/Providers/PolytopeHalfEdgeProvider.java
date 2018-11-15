package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DBasics;

public interface PolytopeHalfEdgeProvider//<A extends PolytopeVertexBasics<A, B, C>, B extends PolytopeHalfEdgeBasics<A, B, C>, C extends ConvexPolytopeFaceBasics<A, B, C>>
{
   HalfEdge3DBasics getHalfEdge(Vertex3DBasics origin, Vertex3DBasics destination);

   HalfEdge3DBasics getHalfEdge();

   HalfEdge3DBasics getHalfEdge(HalfEdge3DReadOnly polytopeHalfEdge);
}
