package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.euclid.referenceFrame.interfaces.ReferenceFrameHolder;
import us.ihmc.geometry.polytope.DCELPolytope.Frame.FrameConvexPolytopeFace;
import us.ihmc.geometry.polytope.DCELPolytope.Frame.FramePolytopeHalfEdge;
import us.ihmc.geometry.polytope.DCELPolytope.Frame.FramePolytopeVertex;
import us.ihmc.geometry.polytope.DCELPolytope.Frame.FrameSimplex;

public class FrameConvexPolytopeFaceBuilder implements ConvexPolytopeFaceProvider<FramePolytopeVertex, FramePolytopeHalfEdge, FrameConvexPolytopeFace, FrameSimplex>
{
   private final ReferenceFrameHolder referenceFrameHolder;
   
   public FrameConvexPolytopeFaceBuilder(ReferenceFrameHolder referenceFrameHolder)
   {
      this.referenceFrameHolder = referenceFrameHolder;
   }
   
   @Override
   public FrameConvexPolytopeFace getFace()
   {
      return new FrameConvexPolytopeFace(referenceFrameHolder.getReferenceFrame());
   }

}
