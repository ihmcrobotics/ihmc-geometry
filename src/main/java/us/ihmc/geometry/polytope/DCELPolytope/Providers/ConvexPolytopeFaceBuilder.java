package us.ihmc.geometry.polytope.DCELPolytope.Providers;

import us.ihmc.geometry.polytope.DCELPolytope.Face3D;

public class ConvexPolytopeFaceBuilder implements ConvexPolytopeFaceProvider
{

   @Override
   public Face3D getFace()
   {
      return new Face3D();
   }
}
