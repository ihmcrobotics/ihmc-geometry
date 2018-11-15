package us.ihmc.geometry.polytope.DCELPolytope;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.geometry.polytope.ConvexPolytopeConstructor;
import us.ihmc.geometry.polytope.DCELPolytope.CollisionDetection.HybridGJKEPACollisionDetector;

public class HybridGJKEPACollisionDetectorTest
{
   @Test(timeout = 1000)
   public void testSupportVectorDirectionNegative()
   {
      HybridGJKEPACollisionDetector collisionDetector = new HybridGJKEPACollisionDetector();
      collisionDetector.setSupportVectorDirection(new Vector3D(1, -1, 10));
      Vector3D vectorToGet = new Vector3D();
      collisionDetector.getSupportVectorDirectionNegative(vectorToGet);
      assertTrue(vectorToGet.getX() == -1);
      assertTrue(vectorToGet.getY() == 1);
      assertTrue(vectorToGet.getZ() == -10);
   }

   @Test(timeout = 1000)
   public void testCollisionDetection()
   {
      HybridGJKEPACollisionDetector collisionDetector = new HybridGJKEPACollisionDetector();
      ExtendedSimplexPolytope simplex = new ExtendedSimplexPolytope();
      ExtendedConvexPolytope obstacle = ConvexPolytopeConstructor.getCuboidCollisionMesh(new Point3D(-0.15, 0.0, 0.45), 0.15, 0.15, 0.15);
      //      collisionDetector.setPolytopeA(polytopeA);
      collisionDetector.setSimplex(simplex);
      collisionDetector.setPolytopeB(obstacle);
   }
}
