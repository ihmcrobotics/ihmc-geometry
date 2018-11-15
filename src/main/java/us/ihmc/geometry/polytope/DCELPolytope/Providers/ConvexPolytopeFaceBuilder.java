package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.ConvexPolytopeFace;

public class ConvexPolytopeFaceBuilder implements ConvexPolytopeFaceProvider
{

   @Override
   public ConvexPolytopeFace getFace()
   {
      return new ConvexPolytopeFace();
   }
}
