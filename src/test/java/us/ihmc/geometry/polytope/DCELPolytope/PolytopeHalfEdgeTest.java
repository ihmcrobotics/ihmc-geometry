package us.ihmc.geometry.polytope.DCELPolytope;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import us.ihmc.commons.Epsilons;
import us.ihmc.continuousIntegration.ContinuousIntegrationAnnotations.ContinuousIntegrationTest;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DBasics;

public class PolytopeHalfEdgeTest
{
   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testConstructor()
   {
      Vertex3D vertex1 = getRandomPolytopeVertex();
      Vertex3D vertex2 = getRandomPolytopeVertex();
      PolytopeHalfEdge halfEdge1 = new PolytopeHalfEdge(vertex1, vertex2);
      assertTrue(halfEdge1.getOriginVertex() == vertex1);
      assertTrue(halfEdge1.getDestinationVertex() == vertex2);
      assertTrue(halfEdge1.getFace() == null);
      assertTrue(halfEdge1.getPreviousHalfEdge() == null);
      assertTrue(halfEdge1.getNextHalfEdge() == null);
      assertTrue(halfEdge1.getTwinHalfEdge() == null);
      
      PolytopeHalfEdge halfEdge2 = new PolytopeHalfEdge(halfEdge1, null);
      assertTrue(halfEdge2.getOriginVertex() == vertex2);
      assertTrue(halfEdge2.getDestinationVertex() == vertex1);
      assertTrue(halfEdge2.getFace() == null);
      assertTrue(halfEdge2.getPreviousHalfEdge() == null);
      assertTrue(halfEdge2.getNextHalfEdge() == null);
      assertTrue(halfEdge2.getTwinHalfEdge() == halfEdge1);
      
      Vertex3D vertex3 = getRandomPolytopeVertex();
      PolytopeHalfEdge halfEdge3 = new PolytopeHalfEdge(vertex2, vertex3, null, null, halfEdge1, null);
      assertTrue(halfEdge3.getOriginVertex() == vertex2);
      assertTrue(halfEdge3.getDestinationVertex() == vertex3);
      assertTrue(halfEdge3.getFace() == null);
      assertTrue(halfEdge3.getPreviousHalfEdge() == halfEdge1);
      assertTrue(halfEdge3.getNextHalfEdge() == null);
      assertTrue(halfEdge3.getTwinHalfEdge() == null);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testEdgeReverse()
   {
      Vertex3D vertex1 = getRandomPolytopeVertex();
      Vertex3D vertex2 = getRandomPolytopeVertex();
      Vertex3D vertex3 = getRandomPolytopeVertex();
      
      PolytopeHalfEdge halfEdge1 = new PolytopeHalfEdge(vertex1, vertex2);
      PolytopeHalfEdge halfEdge2 = new PolytopeHalfEdge(vertex2, vertex3, null, null, halfEdge1, null);
      PolytopeHalfEdge halfEdge3 = new PolytopeHalfEdge(vertex3, vertex1, null, halfEdge1, halfEdge2, null);
      halfEdge1.setPreviousHalfEdge(halfEdge3);
      halfEdge1.setNextHalfEdge(halfEdge2);
      halfEdge2.setNextHalfEdge(halfEdge3);
      
      halfEdge2.reverseEdge();
      assertTrue(halfEdge2.getOriginVertex() == vertex3);
      assertTrue(halfEdge2.getDestinationVertex() == vertex2);
      assertTrue(halfEdge2.getNextHalfEdge() == halfEdge1);
      assertTrue(halfEdge2.getPreviousHalfEdge() == halfEdge3);
   }

   private Vertex3D getRandomPolytopeVertex()
   {
      return new Vertex3D(Math.random(), Math.random(), Math.random());
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testCreateTwinEdge()
   {
      Vertex3D vertex1 = getRandomPolytopeVertex();
      Vertex3D vertex2 = getRandomPolytopeVertex();
      PolytopeHalfEdge halfEdge1 = new PolytopeHalfEdge(vertex1, vertex2);
      HalfEdge3DBasics twinOfHalfEdge1 = halfEdge1.createTwinHalfEdge();
      assertTrue(twinOfHalfEdge1.getTwinHalfEdge() == halfEdge1);
      assertTrue(twinOfHalfEdge1.getOriginVertex() == vertex2);
      assertTrue(twinOfHalfEdge1.getDestinationVertex() == vertex1);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testSetAndCreateTwinEdge()
   {
      Vertex3D vertex1 = getRandomPolytopeVertex();
      Vertex3D vertex2 = getRandomPolytopeVertex();
      PolytopeHalfEdge halfEdge1 = new PolytopeHalfEdge(vertex1, vertex2);
      HalfEdge3DBasics twinOfHalfEdge1 = halfEdge1.setAndCreateTwinHalfEdge();
      assertTrue(twinOfHalfEdge1.getTwinHalfEdge() == halfEdge1);
      assertTrue(halfEdge1.getTwinHalfEdge() == twinOfHalfEdge1);
      assertTrue(twinOfHalfEdge1.getOriginVertex() == vertex2);
      assertTrue(twinOfHalfEdge1.getDestinationVertex() == vertex1);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testEpsilonEquals()
   {
      Vertex3D vertex1 = getRandomPolytopeVertex();
      Vertex3D vertex2 = getRandomPolytopeVertex();
      PolytopeHalfEdge halfEdge1 = new PolytopeHalfEdge(vertex1, vertex2);
      assertTrue(halfEdge1.epsilonEquals(halfEdge1, 0.0));
      Vertex3D vertex3 = new Vertex3D(vertex1.getX() + Epsilons.ONE_THOUSANDTH, vertex1.getY() - Epsilons.ONE_THOUSANDTH, vertex1.getZ() + Epsilons.ONE_THOUSANDTH);
      Vertex3D vertex4 = new Vertex3D(vertex2.getX() - Epsilons.ONE_THOUSANDTH, vertex2.getY() - Epsilons.ONE_THOUSANDTH, vertex2.getZ() + Epsilons.ONE_THOUSANDTH);
      PolytopeHalfEdge halfEdge2 = new PolytopeHalfEdge(vertex3, vertex4);
      assertTrue(halfEdge1.epsilonEquals(halfEdge2, Epsilons.ONE_THOUSANDTH*2));
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testEdgeVector()
   {
      Vertex3D vertex1 = getRandomPolytopeVertex();
      Vertex3D vertex2 = getRandomPolytopeVertex();
      
      PolytopeHalfEdge edge = new PolytopeHalfEdge(vertex1, vertex2);
      Vector3DReadOnly edgeVector = edge.getEdgeVector();
      assertTrue(edgeVector.getX() == vertex2.getX() - vertex1.getX());
      assertTrue(edgeVector.getY() == vertex2.getY() - vertex1.getY());
      assertTrue(edgeVector.getZ() == vertex2.getZ() - vertex1.getZ());
      
   }
}
