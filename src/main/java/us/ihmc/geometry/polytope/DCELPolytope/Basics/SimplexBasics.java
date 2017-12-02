package us.ihmc.geometry.polytope.DCELPolytope.Basics;

import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;

public interface SimplexBasics
{
   /**
    * Return the shortest distance from the point to the simplex
    * @param point the spatial point from which the distance to the simplex is to be computed
    * @return shortest distance from the specified point to the simplex
    */
   double getShortestDistanceTo(Point3DReadOnly point);

   /**
    * Get a vector in the direction of the specified point from its closest point on the simplex
    * @param point the point that the vector should point towards
    * @param supportVectorToPack the vector in which the computed result is to be stored
    */
   void getSupportVectorDirectionTo(Point3DReadOnly point, Vector3D supportVectorToPack);
   
   /**
    * The smallest simplex member on which the projection of the specified point lies. Generally a vertex, edge or face of the simplex
    * @param point the point for which the smallest simplex is needed
    * @return the smallest simplex for said point
    */
   SimplexBasics getSmallestSimplexMemberReference(Point3DReadOnly point);
}
