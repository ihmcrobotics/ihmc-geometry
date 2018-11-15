package us.ihmc.geometry.polytope.DCELPolytope.Basics;

import us.ihmc.euclid.geometry.interfaces.LineSegment3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;

public interface HalfEdge3DReadOnly extends LineSegment3DReadOnly
{
   /**
    * Returns a reference to the origin vertex for this half edge
    * 
    * @return a read only reference of the origin vertex for the half edge
    */
   Vertex3DReadOnly getOriginVertex();

   /**
    * Returns a reference to the destination vertex for this half edge
    * 
    * @return a read only reference of the destination vertex for the half edge
    */
   Vertex3DReadOnly getDestinationVertex();

   @Override
   default Point3DReadOnly getFirstEndpoint()
   {
      return getOriginVertex();
   }

   @Override
   default Point3DReadOnly getSecondEndpoint()
   {
      return getDestinationVertex();
   }

   /**
    * Returns a reference to the twin edge of this half edge
    * 
    * @return a read only reference to the twin half edge
    */
   HalfEdge3DReadOnly getTwinHalfEdge();

   /**
    * Returns a reference to the {@code nextHalfEdge} in the same {@code face} as this half edge
    * 
    * @return a read only reference to the next half edge
    */
   HalfEdge3DReadOnly getNextHalfEdge();

   /**
    * Returns a reference to the {@code previousHalfEdge} in the same {@code face} as this half edge
    * 
    * @return a read only reference to the previous half edge
    */
   HalfEdge3DReadOnly getPreviousHalfEdge();

   /**
    * Returns the reference to the face that this half edge is a part of
    * 
    * @return a read only reference to the face
    */
   ConvexPolytopeFaceReadOnly getFace();

   /**
    * Returns a vector that represents the spatial direction that this edge points in (from origin to
    * destination). Length of the returned vector is equal to the length of the half edge
    * 
    * @return a read only 3D vector
    */
   Vector3DReadOnly getEdgeVector();

   /**
    * Returns a unit vector that represents the spatial direction that this half edge points in (from
    * origin to destination)
    * 
    * @return a read only 3D unit vector
    */
   Vector3DReadOnly getNormalizedEdgeVector();

   /**
    * Geometrically checks if the specified half edge is the twin of the current edge.
    * 
    * @param twinEdge the half edge that is to be checked
    * @param the precision required for the check
    * @return {@code true} if the twin edge of the specified half edge is not null and the twin edge's
    *         origin and destination are in an {@code epsilon} vicinity of this half edges origin and
    *         destination respectively
    */
   boolean isTwin(HalfEdge3DReadOnly twinEdge, double epsilon);

   default boolean epsilonEquals(HalfEdge3DReadOnly other, double epsilon)
   {
      return LineSegment3DReadOnly.super.epsilonEquals(other, epsilon);
   }

   /**
    * Returns a string that indicates the spatial location of the object
    * 
    * @return string containing details
    */
   String toString();

   /**
    * Get the shortest distance to the edge
    * 
    * @param point the point from which the distance to the line is to be computed
    * @return shortest distance from the specified point to the edge
    */
   double getShortestDistanceTo(Point3DReadOnly point);

}