package us.ihmc.geometry.polytope.DCELPolytope;

import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.PolytopeHalfEdgeBuilder;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.PolytopeHalfEdgeProvider;

/**
 * This class implements a doubly connected edge list
 * (https://en.wikipedia.org/wiki/Doubly_connected_edge_list) for storing polytope information A
 * half edge is completely described by its origin, destination and twin edge The face, previous
 * half edge and next half edge are stored for readability of code and should not be used for any
 * geometrical operations An attempt is made to update the twin in case the edge is modified to
 * ensure that the relation remains consistent
 * 
 * @author Apoorv S
 */
public class PolytopeHalfEdge extends HalfEdge3DBasics implements Simplex
{
   private final PolytopeHalfEdgeBuilder halfEdgeBuilder = new PolytopeHalfEdgeBuilder();

   public PolytopeHalfEdge()
   {
      super();
   }

   /**
    * Creates a new edge at the same location. References to origin / destination vertices, twin / next
    * / previous edges and associated is not preserved
    * 
    * @param edge
    */
   public PolytopeHalfEdge(HalfEdge3DReadOnly edge)
   {
      super(new Vertex3D(edge.getOriginVertex()), new Vertex3D(edge.getDestinationVertex()));
   }

   public PolytopeHalfEdge(Vertex3D origin, Vertex3D destination)
   {
      super(origin, destination);
   }

   public PolytopeHalfEdge(Vertex3D originVertex, Vertex3D destinationVertex, PolytopeHalfEdge twinEdge,
                           PolytopeHalfEdge nextHalfEdge, PolytopeHalfEdge previousHalfEdge, ConvexPolytopeFace face)
   {
      super(originVertex, destinationVertex, twinEdge, nextHalfEdge, previousHalfEdge, face);
   }

   public PolytopeHalfEdge(PolytopeHalfEdge twinEdge, ConvexPolytopeFace face)
   {
      super(twinEdge, face);
   }

   @Override
   public PolytopeHalfEdge getNextHalfEdge()
   {
      return (PolytopeHalfEdge) super.getNextHalfEdge();
   }

   @Override
   protected PolytopeHalfEdgeProvider getHalfEdgeProvider()
   {
      return halfEdgeBuilder;
   }
}
