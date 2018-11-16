package us.ihmc.geometry.polytope.DCELPolytope;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import us.ihmc.commons.Epsilons;
import us.ihmc.continuousIntegration.ContinuousIntegrationAnnotations.ContinuousIntegrationTest;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.HalfEdge3DBasics;

public class ConvexPolytopeFaceTest
{
   private static final double epsilon = Epsilons.ONE_TEN_THOUSANDTH;

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testConstructorAndAddVertex()
   {
      Face3D face = new Face3D();
      Vertex3D vertex1 = new Vertex3D(0.0, 0.0, 0.0);
      Vertex3D vertex2 = new Vertex3D(0.0, 1.0, 0.0);
      Vertex3D vertex3 = new Vertex3D(1.0, 0.0, 0.0);
      face.addVertex(vertex1, epsilon);
      assertTrue(face.getNumberOfEdges() == 1);
      assertTrue(face.getEdge(0).getOriginVertex() == vertex1);
      assertTrue(face.getEdge(0).getDestinationVertex() == vertex1);
      face.addVertex(vertex2, epsilon);
      assertTrue(face.getNumberOfEdges() == 2);
      assertTrue(face.getEdge(0).getOriginVertex() == vertex1);
      assertTrue(face.getEdge(0).getDestinationVertex() == vertex2);
      assertTrue(face.getEdge(1).getOriginVertex() == vertex2);
      assertTrue(face.getEdge(1).getDestinationVertex() == vertex1);
      face.addVertex(vertex3, epsilon);
      assertTrue(face.getNumberOfEdges() == 3);
      assertTrue(face.getEdge(0).getOriginVertex() == vertex1);
      assertTrue(face.getEdge(0).getDestinationVertex() == vertex2);
      assertTrue(face.getEdge(1).getOriginVertex() == vertex2);
      assertTrue(face.getEdge(1).getDestinationVertex() == vertex3);
      assertTrue(face.getEdge(2).getOriginVertex() == vertex3);
      assertTrue(face.getEdge(2).getDestinationVertex() == vertex1);

      Vertex3D vertex4 = new Vertex3D(1.0, 1.0, 0.0);
      face.addVertex(vertex4, epsilon);
      assertTrue("Got: " + face.getNumberOfEdges() + " , should have been 4", face.getNumberOfEdges() == 4);
      HalfEdge3D edge = face.getEdge(0);
      assertTrue(edge.getOriginVertex() == vertex1);
      assertTrue(edge.getDestinationVertex() == vertex2);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex2);
      assertTrue(edge.getDestinationVertex() == vertex4);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex4);
      assertTrue(edge.getDestinationVertex() == vertex3);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex3);
      assertTrue(edge.getDestinationVertex() == vertex1);

      Vertex3D vertex5 = new Vertex3D(2.0, 2.0, 0.0);
      face.addVertex(vertex5, epsilon);
      assertTrue("Number of edges: " + face.getNumberOfEdges() + ", needed: " + 4, face.getNumberOfEdges() == 4);
      edge = face.getEdge(0);
      assertTrue(edge.getOriginVertex() == vertex1);
      assertTrue(edge.getDestinationVertex() == vertex2);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex2);
      assertTrue(edge.getDestinationVertex() == vertex5);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex5);
      assertTrue(edge.getDestinationVertex() == vertex3);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex3);
      assertTrue(edge.getDestinationVertex() == vertex1);

      Vertex3D vertex6 = new Vertex3D(3.0, 3.0, 0.0);
      face.addVertex(vertex6, epsilon);
      assertTrue("Number of edges: " + face.getNumberOfEdges() + ", needed: " + 4, face.getNumberOfEdges() == 4);
      edge = face.getEdge(0);
      assertTrue(edge.getOriginVertex() == vertex1);
      assertTrue(edge.getDestinationVertex() == vertex2);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex2);
      assertTrue(edge.getDestinationVertex() == vertex6);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex6);
      assertTrue(edge.getDestinationVertex() == vertex3);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex3);
      assertTrue(edge.getDestinationVertex() == vertex1);

      Vertex3D vertex7 = new Vertex3D(2.0, 3.0, 0.0);
      face.addVertex(vertex7, epsilon);
      assertTrue("Number of edges: " + face.getNumberOfEdges() + ", needed: " + 5, face.getNumberOfEdges() == 5);
      edge = face.getEdge(0);
      assertTrue(edge.getOriginVertex() == vertex1);
      assertTrue(edge.getDestinationVertex() == vertex2);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex2);
      assertTrue(edge.getDestinationVertex() == vertex7);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex7);
      assertTrue(edge.getDestinationVertex() == vertex6);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex6);
      assertTrue(edge.getDestinationVertex() == vertex3);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex3);
      assertTrue(edge.getDestinationVertex() == vertex1);

      Vertex3D vertex8 = new Vertex3D(0.0, 0.0, 1.0);
      assertFalse(face.isPointInFacePlane(vertex8, Epsilons.ONE_BILLIONTH));
      face.addVertex(vertex8, epsilon);
      assertTrue("Number of edges: " + face.getNumberOfEdges() + ", needed: " + 5, face.getNumberOfEdges() == 5);
      edge = face.getEdge(0);
      assertTrue(edge.getOriginVertex() == vertex1);
      assertTrue(edge.getDestinationVertex() == vertex2);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex2);
      assertTrue(edge.getDestinationVertex() == vertex7);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex7);
      assertTrue(edge.getDestinationVertex() == vertex6);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex6);
      assertTrue(edge.getDestinationVertex() == vertex3);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex3);
      assertTrue(edge.getDestinationVertex() == vertex1);

      Vertex3D vertex9 = new Vertex3D(1.0, 1.0, 0.0);
      assertTrue(face.isPointInFacePlane(vertex9, Epsilons.ONE_MILLIONTH));
      assertTrue(face.isInteriorPoint(vertex9, epsilon));
      face.addVertex(vertex9, epsilon);
      assertTrue(face.toString() + "\nNumber of edges: " + face.getNumberOfEdges() + ", needed: " + 5, face.getNumberOfEdges() == 5);
      edge = face.getEdge(0);
      assertTrue(edge.getOriginVertex() == vertex1);
      assertTrue(edge.getDestinationVertex() == vertex2);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex2);
      assertTrue(edge.getDestinationVertex() == vertex7);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex7);
      assertTrue(edge.getDestinationVertex() == vertex6);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex6);
      assertTrue(edge.getDestinationVertex() == vertex3);
      edge = edge.getNextHalfEdge();
      assertTrue(edge.getOriginVertex() == vertex3);
      assertTrue(edge.getDestinationVertex() == vertex1);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testGetFirstVisibleEdge()
   {
      Vertex3D vertex1 = new Vertex3D(0.0, 0.0, 0.0);
      Vertex3D vertex2 = new Vertex3D(1.0, 0.0, 0.0);
      Vertex3D vertex3 = new Vertex3D(1.0, 1.0, 0.0);
      Vertex3D vertex4 = new Vertex3D(0.0, 1.0, 0.0);
      Vertex3D vertex5 = new Vertex3D(-1.0, 0.5, 0.0);
      HalfEdge3D halfEdge1 = new HalfEdge3D(vertex1, vertex2);
      HalfEdge3D halfEdge2 = new HalfEdge3D(vertex2, vertex3);
      HalfEdge3D halfEdge3 = new HalfEdge3D(vertex3, vertex4);
      HalfEdge3D halfEdge4 = new HalfEdge3D(vertex4, vertex5);
      HalfEdge3D halfEdge5 = new HalfEdge3D(vertex5, vertex1);
      halfEdge1.setNextHalfEdge(halfEdge2);
      halfEdge2.setNextHalfEdge(halfEdge3);
      halfEdge3.setNextHalfEdge(halfEdge4);
      halfEdge4.setNextHalfEdge(halfEdge5);
      halfEdge5.setNextHalfEdge(halfEdge1);
      halfEdge1.setPreviousHalfEdge(halfEdge5);
      halfEdge2.setPreviousHalfEdge(halfEdge1);
      halfEdge3.setPreviousHalfEdge(halfEdge2);
      halfEdge4.setPreviousHalfEdge(halfEdge3);
      halfEdge5.setPreviousHalfEdge(halfEdge4);
      Face3D face = new Face3D(new HalfEdge3D[] {halfEdge1, halfEdge2, halfEdge3, halfEdge4, halfEdge5});
      Vertex3D vertex6 = new Vertex3D(-1.0, -1.0, 0.0);
      HalfEdge3D firstVisibleEdge = face.getFirstVisibleEdge(vertex6);
      assertTrue(firstVisibleEdge == halfEdge5);

      Vertex3D vertex7 = new Vertex3D(2.0, -1.0, 0.0);
      firstVisibleEdge = face.getFirstVisibleEdge(vertex7);
      assertTrue(firstVisibleEdge == halfEdge5);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testGetVisibleEdgeList()
   {
      Vertex3D vertex1 = new Vertex3D(0.0, 0.0, 0.0);
      Vertex3D vertex2 = new Vertex3D(1.0, 0.0, 0.0);
      Vertex3D vertex3 = new Vertex3D(1.0, 1.0, 0.0);
      Vertex3D vertex4 = new Vertex3D(0.0, 1.0, 0.0);
      Vertex3D vertex5 = new Vertex3D(-1.0, 0.5, 0.0);
      HalfEdge3D halfEdge1 = new HalfEdge3D(vertex1, vertex2);
      HalfEdge3D halfEdge2 = new HalfEdge3D(vertex2, vertex3);
      HalfEdge3D halfEdge3 = new HalfEdge3D(vertex3, vertex4);
      HalfEdge3D halfEdge4 = new HalfEdge3D(vertex4, vertex5);
      HalfEdge3D halfEdge5 = new HalfEdge3D(vertex5, vertex1);
      halfEdge1.setNextHalfEdge(halfEdge2);
      halfEdge2.setNextHalfEdge(halfEdge3);
      halfEdge3.setNextHalfEdge(halfEdge4);
      halfEdge4.setNextHalfEdge(halfEdge5);
      halfEdge5.setNextHalfEdge(halfEdge1);
      halfEdge1.setPreviousHalfEdge(halfEdge5);
      halfEdge2.setPreviousHalfEdge(halfEdge1);
      halfEdge3.setPreviousHalfEdge(halfEdge2);
      halfEdge4.setPreviousHalfEdge(halfEdge3);
      halfEdge5.setPreviousHalfEdge(halfEdge4);
      Face3D face = new Face3D(new HalfEdge3D[] {halfEdge1, halfEdge2, halfEdge3, halfEdge4, halfEdge5});

      List<HalfEdge3DBasics> visibleEdgeList = new ArrayList<>();
      Vertex3D vertex6 = new Vertex3D(-1.0, -1.0, 0.0);
      face.getVisibleEdgeList(vertex6, visibleEdgeList);
      assertTrue(visibleEdgeList.size() == 2);
      assertTrue(visibleEdgeList.get(0) == halfEdge5);
      assertTrue(visibleEdgeList.get(1) == halfEdge1);

      Vertex3D vertex7 = new Vertex3D(2.0, -1.0, 0.0);
      face.getVisibleEdgeList(vertex7, visibleEdgeList);
      assertTrue(visibleEdgeList.size() == 3);
      assertTrue(visibleEdgeList.get(0) == halfEdge5);
      assertTrue(visibleEdgeList.get(1) == halfEdge1);
      assertTrue(visibleEdgeList.get(2) == halfEdge2);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testRepeatedPointAddition()
   {
      Face3D face = new Face3D();
      Vertex3D vertex1 = new Vertex3D(0.0, 0.0, 0.0);
      Vertex3D vertex2 = new Vertex3D(0.0, 1.0, 0.0);
      Vertex3D vertex3 = new Vertex3D(0.0, 1.0, 0.0);
      face.addVertex(vertex1, epsilon);
      face.addVertex(vertex2, epsilon);
      face.addVertex(vertex3, epsilon);
      assertTrue("Got: " + face.getNumberOfEdges() + ", should have been 2", face.getNumberOfEdges() == 2);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testAdditionPrecision()
   {
      Face3D face = new Face3D();
      face.addVertex(new Vertex3D(0.0001111, 0.0002222, 0.0003333), epsilon);
      face.addVertex(new Vertex3D(1.0001111, 0.0002222, 0.0003333), epsilon);
      face.addVertex(new Vertex3D(1.0001111, 1.0002222, 0.0003333), epsilon);
      face.addVertex(new Vertex3D(0.0001111, 1.0002222, 0.0003333), epsilon);
      face.addVertex(new Vertex3D(1.0001111, 0.0002222, 0.0003333), epsilon);
      assertTrue(face.toString(), face.getNumberOfEdges() == 4);
   }

   @ContinuousIntegrationTest(estimatedDuration = 0.0)
   @Test(timeout = 1000)
   public void testAFailingCase()
   {
      Face3D face = new Face3D();

      face.addVertex(new Vertex3D( -0.15000000000000002, 0.04200000000000001, 0.11500000000000002), epsilon);
      face.addVertex(new Vertex3D( -0.15000000000000002, 0.07500000000000001, -0.12000000000000002), epsilon);
      face.addVertex(new Vertex3D( -0.15000000000000002, -0.07500000000000001, -0.12000000000000002), epsilon);
      face.addVertex(new Vertex3D( -0.15000000000000002, -0.04200000000000001, 0.11500000000000002), epsilon);
      face.addVertex(new Vertex3D( -0.15000000000000002, 0.07500000000000001, -0.12000000000000002), epsilon);
      assertTrue(face.toString(), face.getNumberOfEdges() == 4);
   }

}
