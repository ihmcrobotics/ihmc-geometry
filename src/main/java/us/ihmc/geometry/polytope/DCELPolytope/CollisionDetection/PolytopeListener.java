package us.ihmc.geometry.polytope.DCELPolytope.CollisionDetection;

import java.util.List;

import us.ihmc.geometry.polytope.DCELPolytope.Basics.Face3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.ConvexPolytopeReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DReadOnly;

public interface PolytopeListener
{
   void attachPolytope(ConvexPolytopeReadOnly polytopeToAttach);

   void updateAll();

   void updateEdges();

   void updateVertices();

   void updateFaces();

   void updateVisibleSilhouette(List<? extends HalfEdge3DReadOnly> visibleEdges);

   void udpateVisibleEdgeSeed(HalfEdge3DReadOnly visibleEdgeSeed);

   void updateOnFaceList(List<? extends Face3DReadOnly> onFaceList);

   void updateVisibleFaceList(List<? extends Face3DReadOnly> visibleFaceList);
}
