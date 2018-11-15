package us.ihmc.geometry.polytope.DCELPolytope;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import us.ihmc.commons.Epsilons;
import us.ihmc.commons.MathTools;
import us.ihmc.commons.MutationTestFacilitator;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DBasics;

public class PolytopeVertexTest
{
   @Test(timeout = 1000)
   public void testConstructors()
   {
      Vertex3D vertex1 = new Vertex3D(0.1, 0.2, 0.4);
      assertTrue(vertex1.getX() == 0.1);
      assertTrue(vertex1.getY() == 0.2);
      assertTrue(vertex1.getZ() == 0.4);

      Vertex3D vertex2 = new Vertex3D(new Point3D(1.2, 3.14, 1.519));
      assertTrue(vertex2.getX() == 1.2);
      assertTrue(vertex2.getY() == 3.14);
      assertTrue(vertex2.getZ() == 1.519);

      Vertex3D vertex3 = new Vertex3D(vertex1);
      assertTrue(vertex1.getX() == vertex3.getX());
      assertTrue(vertex1.getY() == vertex3.getY());
      assertTrue(vertex1.getZ() == vertex3.getZ());
   }

   @Test(timeout = 1000)
   public void testEdgeAssociation()
   {
      Vertex3D vertex1 = new Vertex3D(0.1, 0.2, 0.3);
      Vertex3D vertex2 = new Vertex3D(0.2, 3.1, 5.7);
      Vertex3D vertex3 = new Vertex3D(1.2, 8.1, 0.0);
      HalfEdge3D edge1 = new HalfEdge3D(vertex1, vertex2);
      HalfEdge3D edge2 = new HalfEdge3D(vertex1, vertex3);
      assertTrue(vertex1.getAssociatedEdges().size() == 2);
      assertTrue(vertex1.getAssociatedEdges().get(0) == edge1);
      assertTrue(vertex1.getAssociatedEdges().get(1) == edge2);
      assertTrue(vertex1.getAssociatedEdge(0) == edge1);
      assertTrue(vertex1.getAssociatedEdge(1) == edge2);
      assertTrue(vertex2.getAssociatedEdges().size() == 0);
      assertTrue(vertex3.getAssociatedEdges().size() == 0);

      HalfEdge3DBasics twinEdge1 = edge1.createTwinHalfEdge();
      assertTrue(vertex2.getAssociatedEdges().size() == 1);
      assertTrue(vertex2.getAssociatedEdges().get(0) == twinEdge1);

   }

   @Test(timeout = 1000)
   public void testDotProduct()
   {
      Vertex3D vertex = new Vertex3D(1.1241252, -15.12415, 86.12536);
      assertTrue(vertex.dot(new Vector3D(1.0, 0.5, 1.0)) == 1.1241252 - 0.5 * 15.12415 + 86.12536);
   }

   @Test(timeout = 1000)
   public void testShortestDistanceCalculation()
   {
      Random random = new Random(123l);
      Vertex3D vertex = new Vertex3D(1.1241252, -15.12415, 86.12536);
      Point3D point = EuclidCoreRandomTools.nextPoint3D(random);
      assertTrue(vertex.getShortestDistanceTo(point) == point.distance(vertex));
   }

   @Test(timeout = 1000)
   public void testGetSupportVectorDirectionTo()
   {
      Random random = new Random(1254l);
      Point3D somePoint = EuclidCoreRandomTools.nextPoint3D(random);
      Vertex3D vertex = new Vertex3D(random.nextDouble(), random.nextDouble(), random.nextDouble());
      Vector3D supportVectorToPack = new Vector3D();
      vertex.getSupportVectorDirectionTo(somePoint, supportVectorToPack);
      assertTrue(supportVectorToPack.getX() == somePoint.getX() - vertex.getX());
      assertTrue(supportVectorToPack.getY() == somePoint.getY() - vertex.getY());
      assertTrue(supportVectorToPack.getZ() == somePoint.getZ() - vertex.getZ());
   }

   @Test(timeout = 1000)
   public void testRounding()
   {
      Random random = new Random(1254l);
      Vertex3D vertex = new Vertex3D(random.nextDouble(), random.nextDouble(), random.nextDouble());
      Point3D someCoordinate = new Point3D(vertex);
      vertex.round(Epsilons.ONE_HUNDREDTH);
      assertTrue(MathTools.roundToPrecision(someCoordinate.getX(), Epsilons.ONE_HUNDREDTH) + " " + vertex.getX(),
                 MathTools.roundToPrecision(vertex.getX(), Epsilons.ONE_HUNDREDTH) == vertex.getX());
      assertTrue(MathTools.roundToPrecision(someCoordinate.getY(), Epsilons.ONE_HUNDREDTH) == vertex.getY());
      assertTrue(MathTools.roundToPrecision(someCoordinate.getZ(), Epsilons.ONE_HUNDREDTH) == vertex.getZ());
   }

   @Test(timeout = 1000)
   public void testSubSimplex()
   {
      Vertex3D vertex = new Vertex3D();
      assertTrue(vertex.getSmallestSimplexMemberReference(null) == vertex);
   }

   @Test(timeout = 1000)
   public void testAssociatedEdgeRemoval()
   {
      Vertex3D vertex1 = new Vertex3D(1.0, 2.0, 3.0);
      Vertex3D vertex2 = new Vertex3D(4.0, 5.0, 6.0);
      Vertex3D vertex3 = new Vertex3D(10.0, 15.0, 6.0);
      HalfEdge3D halfEdge1 = new HalfEdge3D(vertex1, vertex2);
      HalfEdge3D halfEdge2 = new HalfEdge3D(vertex1, vertex2);
      HalfEdge3D halfEdge3 = new HalfEdge3D(vertex1, vertex3);
      assertTrue(vertex1.getNumberOfAssociatedEdges() == 3);
      assertTrue(vertex1.getAssociatedEdge(0) == halfEdge1);
      assertTrue(vertex1.getAssociatedEdge(1) == halfEdge2);
      assertTrue(vertex1.getAssociatedEdge(2) == halfEdge3);
      vertex1.addAssociatedEdge(halfEdge1);
      assertTrue(vertex1.getNumberOfAssociatedEdges() == 3);
      assertTrue(vertex1.getAssociatedEdge(0) == halfEdge1);
      assertTrue(vertex1.getAssociatedEdge(1) == halfEdge2);
      assertTrue(vertex1.getAssociatedEdge(2) == halfEdge3);
      vertex1.removeAssociatedEdge(halfEdge1);
      assertTrue(vertex1.getNumberOfAssociatedEdges() == 2);
      assertTrue(vertex1.getAssociatedEdge(0) == halfEdge2);
      assertTrue(vertex1.getAssociatedEdge(1) == halfEdge3);
      boolean fail = false;
      try
      {
         vertex1.getAssociatedEdge(2);
      }
      catch (Exception e)
      {
         fail = true;
      }
      assertTrue(fail);
      assertTrue(vertex1.isAssociatedWithEdge(halfEdge1, Epsilons.ONE_BILLIONTH));
      Vertex3D vertex4 = new Vertex3D(12.0, 1.0, 9.0);
      HalfEdge3D halfEdge4 = new HalfEdge3D(vertex3, vertex4);
      assertFalse(vertex1.isAssociatedWithEdge(halfEdge4));
      assertFalse(vertex1.isAssociatedWithEdge(halfEdge4, Epsilons.ONE));
   }

   @Test(timeout = 1000)
   public void testTransform()
   {
      Vertex3D vertex = new Vertex3D(1.0, 2.0, 3.0);
      RigidBodyTransform transform = new RigidBodyTransform();
      transform.setTranslation(10.0, 11.0, 12.0);
      transform.setRotationRoll(Math.PI / 2.0d);
      vertex.applyTransform(transform);
      assertTrue(vertex.getX() == 11.0);
      assertTrue(vertex.getY() + "", MathTools.epsilonCompare(vertex.getY(), 8.0, Epsilons.ONE_TEN_THOUSANDTH));
      assertTrue(vertex.getZ() + "", MathTools.epsilonCompare(vertex.getZ(), 14.0, Epsilons.ONE_BILLIONTH));
   }
   
   @Test(timeout = 1000)
   public void testMultipleAssociationAdd()
   {
      Vertex3D vertex = new Vertex3D(1.0, 2.0, 3.0);
      List<HalfEdge3D> halfEdgesToAdd = new ArrayList<>();
      halfEdgesToAdd.add(new HalfEdge3D());
      halfEdgesToAdd.add(new HalfEdge3D(vertex, null));
      halfEdgesToAdd.add(new HalfEdge3D());
      halfEdgesToAdd.add(new HalfEdge3D(vertex, null));
      halfEdgesToAdd.add(new HalfEdge3D());
      assertTrue(vertex.getNumberOfAssociatedEdges() == 2);
      vertex.addAssociatedEdges(halfEdgesToAdd);
      assertTrue(vertex.getNumberOfAssociatedEdges() == 5);
   }

   @Test(timeout = 1000)
   public void testInverseTransform()
   {
      Vertex3D vertex = new Vertex3D(1.0, 2.0, 3.0);
      RigidBodyTransform transform = new RigidBodyTransform();
      transform.setTranslation(10.0, 11.0, 12.0);
      transform.setRotationRoll(Math.PI / 2.0d);
      vertex.applyInverseTransform(transform);
      assertTrue(vertex.getX() == -9.0);
      assertTrue(vertex.getY() + "", MathTools.epsilonCompare(vertex.getY(), -9.0, Epsilons.ONE_TEN_THOUSANDTH));
      assertTrue(vertex.getZ() + "", MathTools.epsilonCompare(vertex.getZ(), 9.0, Epsilons.ONE_BILLIONTH));
   }

   @Test(timeout = 1000)
   public void testSetToNaN()
   {
      Vertex3D vertex = new Vertex3D(10.0, 1.0, 12.0);
      vertex.setToNaN();
      assertTrue(Double.isNaN(vertex.getElement(0)));
      assertTrue(Double.isNaN(vertex.getElement(1)));
      assertTrue(Double.isNaN(vertex.getElement(2)));
      assertTrue(vertex.containsNaN());
   }
   
   @Test(timeout = 1000)
   public void testSetToZero()
   {
      Vertex3D vertex = new Vertex3D(10.0, 1.0, 12.0);
      vertex.setToZero();
      assertTrue(vertex.getElement(0) == 0.0);
      assertTrue(vertex.getElement(1) == 0.0);
      assertTrue(vertex.getElement(2) == 0.0);
   }
   
   public static void main(String args[])
   {
      MutationTestFacilitator.facilitateMutationTestForClass(Vertex3DBasics.class, PolytopeVertexTest.class);
   }
}