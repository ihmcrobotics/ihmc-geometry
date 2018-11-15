package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.ConvexPolytopeFace;
import us.ihmc.geometry.polytope.DCELPolytope.ExtendedPolytopeVertex;
import us.ihmc.geometry.polytope.DCELPolytope.PolytopeHalfEdge;

public class ConvexPolytopeFaceBuilder implements ConvexPolytopeFaceProvider<ExtendedPolytopeVertex, PolytopeHalfEdge, ConvexPolytopeFace>
{

   @Override
   public ConvexPolytopeFace getFace()
   {
      return new ConvexPolytopeFace();
   }
}
