package us.ihmc.geometry.polytope;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import us.ihmc.euclid.tuple3D.Point3D;

public class ConvexPolytopeFromExpandingPolytopeEntryGeneratorTest
{

   @Test(timeout = 30000)
   public void testGeneratorUsingIcoSpheres()
   {
      IcoSphereCreator creator = new IcoSphereCreator();
      int recursionLevel = 0;
      SimpleTriangleMesh icoSphere = creator.createIcoSphere(recursionLevel);

      ArrayList<Point3D> vertexPoints = icoSphere.positions;
      ArrayList<Integer> triangleIndices = icoSphere.triangleIndices;
      assertTrue(triangleIndices.size() % 3 == 0);

      int numberOfVertices = vertexPoints.size();
      int numberOfTriangles = triangleIndices.size() / 3;

      assertEquals(12, numberOfVertices);
      assertEquals(20, numberOfTriangles);

      ExpandingPolytopeEntryFromSimpleMeshGenerator generatorOne = new ExpandingPolytopeEntryFromSimpleMeshGenerator();
      ExpandingPolytopeEntry expandingPolytope = generatorOne.generateExpandingPolytope(icoSphere);

      ArrayList<ExpandingPolytopeEntry> triangles = new ArrayList<>();
      expandingPolytope.getAllConnectedTriangles(triangles);
      assertEquals(numberOfTriangles, triangles.size());

      ConvexPolytopeFromExpandingPolytopeEntryGenerator generatorTwo = new ConvexPolytopeFromExpandingPolytopeEntryGenerator();
      ConvexPolytope convexPolytope = generatorTwo.generateConvexPolytope(expandingPolytope);

      List<PolytopeVertex[]> edges = convexPolytope.getEdges();
      List<PolytopeVertex> vertices = convexPolytope.getVertices();

      assertEquals(numberOfVertices, vertices.size());
      assertEquals(60, edges.size());

      // Bigger One:
      recursionLevel = 1;
      icoSphere = creator.createIcoSphere(recursionLevel);

      vertexPoints = icoSphere.positions;
      triangleIndices = icoSphere.triangleIndices;
      assertTrue(triangleIndices.size() % 3 == 0);

      numberOfVertices = vertexPoints.size();
      numberOfTriangles = triangleIndices.size() / 3;

      assertEquals(42, numberOfVertices);
      assertEquals(80, numberOfTriangles);

      expandingPolytope = generatorOne.generateExpandingPolytope(icoSphere);

      triangles.clear();
      expandingPolytope.getAllConnectedTriangles(triangles);
      assertEquals(numberOfTriangles, triangles.size());

      convexPolytope = generatorTwo.generateConvexPolytope(expandingPolytope);

      edges = convexPolytope.getEdges();
      vertices = convexPolytope.getVertices();

      assertEquals(numberOfVertices, vertices.size());
      assertEquals(240, edges.size());
   }

}
