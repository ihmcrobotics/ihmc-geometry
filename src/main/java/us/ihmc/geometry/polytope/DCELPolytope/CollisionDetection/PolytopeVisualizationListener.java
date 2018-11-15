package us.ihmc.geometry.polytope.DCELPolytope.CollisionDetection;

import java.awt.Color;
import java.util.List;

import us.ihmc.geometry.polytope.DCELPolytope.Basics.ConvexPolytopeFaceReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DReadOnly;

public interface PolytopeVisualizationListener extends PolytopeListener
{
   void setColor(Color color);

   void setHighlightColor(Color color);

   void highlightOnFaces(List<? extends ConvexPolytopeFaceReadOnly> faces);

   void highlightVisibleFaces(List<? extends ConvexPolytopeFaceReadOnly> faces);

   void highlightEdges(List<? extends HalfEdge3DReadOnly> edges);

   void highlightVertices(List<? extends Vertex3DReadOnly> vertices);

   @Override
   default void updateVisibleSilhouette(List<? extends HalfEdge3DReadOnly> visibleEdges)
   {
      highlightEdges(visibleEdges);
   }

   void highlightEdge(HalfEdge3DReadOnly edgeToHighlight);

   @Override
   default void udpateVisibleEdgeSeed(HalfEdge3DReadOnly visibleEdgeSeed)
   {
      highlightEdge(visibleEdgeSeed);
   }

   @Override
   default void updateOnFaceList(List<? extends ConvexPolytopeFaceReadOnly> onFaceList)
   {
      highlightOnFaces(onFaceList);
   }

   @Override
   default void updateVisibleFaceList(List<? extends ConvexPolytopeFaceReadOnly> visibleFaceList)
   {
      highlightVisibleFaces(visibleFaceList);
   }

}
