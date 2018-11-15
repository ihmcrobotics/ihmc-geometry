package us.ihmc.geometry.polytope;

import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;

public interface SupportingVertexHolder
{
   /**
    * Get a support vertex in the direction specified
    * 
    * @param supportDirection the direction to search in
    * @return the spatial location of the supporting vertex
    */
   Point3D getSupportingVertex(Vector3DReadOnly supportDirection);
}
