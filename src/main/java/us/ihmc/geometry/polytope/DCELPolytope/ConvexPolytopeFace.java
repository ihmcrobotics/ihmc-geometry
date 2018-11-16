package us.ihmc.geometry.polytope.DCELPolytope;

import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Face3DBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.PolytopeHalfEdgeBuilder;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.PolytopeHalfEdgeProvider;

/**
 * This class defines a polytope face. A face is defined by the set of edges that bound it.
 * 
 * @author Apoorv S
 *
 */
public class ConvexPolytopeFace extends Face3DBasics
{
   private final PolytopeHalfEdgeBuilder halfEdgeBuilder = new PolytopeHalfEdgeBuilder();

   public ConvexPolytopeFace()
   {
      super();
   }

   public ConvexPolytopeFace(HalfEdge3D[] edges)
   {
      super(edges);
   }

   @Override
   protected PolytopeHalfEdgeProvider getHalfEdgeProvider()
   {
      return halfEdgeBuilder;
   }

   @Override
   public HalfEdge3D getEdge(int index)
   {
      return (HalfEdge3D) super.getEdge(index);
   }

   @Override
   public HalfEdge3D getFirstVisibleEdge(Point3DReadOnly vertex)
   {
      return (HalfEdge3D) super.getFirstVisibleEdge(vertex);
   }
}
