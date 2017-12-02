package us.ihmc.geometry.polytope;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import us.ihmc.commons.Epsilons;
import us.ihmc.commons.PrintTools;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.tools.EuclidCoreRandomTools;
import us.ihmc.euclid.transform.RigidBodyTransform;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple4D.Quaternion;

public class ConvexPolytopeConstructor
{
   private static final double EPSILON = Epsilons.ONE_BILLIONTH;

   public static ConvexPolytope constructUnitCube()
   {
      ConvexPolytope polytope = new ConvexPolytope();
      polytope.addVertex(new PolytopeVertex(0.0, 0.0, 0.0), EPSILON);
      polytope.addVertex(new PolytopeVertex(1.0, 0.0, 0.0), EPSILON);
      polytope.addVertex(new PolytopeVertex(0.0, 1.0, 0.0), EPSILON);
      polytope.addVertex(new PolytopeVertex(1.0, 1.0, 0.0), EPSILON);
      polytope.addVertex(new PolytopeVertex(0.0, 0.0, 1.0), EPSILON);
      polytope.addVertex(new PolytopeVertex(1.0, 0.0, 1.0), EPSILON);
      polytope.addVertex(new PolytopeVertex(0.0, 1.0, 1.0), EPSILON);
      polytope.addVertex(new PolytopeVertex(1.0, 1.0, 1.0), EPSILON);
      return polytope;
   }

   public static ConvexPolytope constructCube(double sideLength, Point3D center)
   {
      ConvexPolytope polytope = new ConvexPolytope();

      return polytope;
   }

   public static ConvexPolytope constructBox(Point3D center, Quaternion orientation, double edgeLengthX, double edgeLengthY, double edgeLengthZ)
   {
      ConvexPolytope polytope = new ConvexPolytope();
      RigidBodyTransform transform = new RigidBodyTransform();
      transform.setRotation(orientation);
      polytope.addVertex(center.getX() - edgeLengthX/2, center.getY() - edgeLengthY/2, center.getZ() - edgeLengthZ/2, EPSILON);
      polytope.addVertex(center.getX() + edgeLengthX/2, center.getY() - edgeLengthY/2, center.getZ() - edgeLengthZ/2, EPSILON);
      polytope.addVertex(center.getX() + edgeLengthX/2, center.getY() + edgeLengthY/2, center.getZ() - edgeLengthZ/2, EPSILON);
      polytope.addVertex(center.getX() - edgeLengthX/2, center.getY() + edgeLengthY/2, center.getZ() - edgeLengthZ/2, EPSILON);
      polytope.addVertex(center.getX() - edgeLengthX/2, center.getY() - edgeLengthY/2, center.getZ() + edgeLengthZ/2, EPSILON);
      polytope.addVertex(center.getX() + edgeLengthX/2, center.getY() - edgeLengthY/2, center.getZ() + edgeLengthZ/2, EPSILON);
      polytope.addVertex(center.getX() + edgeLengthX/2, center.getY() + edgeLengthY/2, center.getZ() + edgeLengthZ/2, EPSILON);
      polytope.addVertex(center.getX() - edgeLengthX/2, center.getY() + edgeLengthY/2, center.getZ() + edgeLengthZ/2, EPSILON);
      polytope.applyTransform(transform);
      return polytope;
   }
   
   /**
    * Constructs a icosahedron that envelops the sphere to be created
    * @param radius
    * @param center
    * @param edgeLengthForDiscretization
    * @return
    */
   public static ConvexPolytope constructSphere(double radius, Point3D center, int recursionLevel)
   {
      ConvexPolytope polytope = new ConvexPolytope();
      int t1 = 1;
      double t2 = 1 + Math.sqrt(5) / 2;
      double scale = radius / Math.sqrt(t1 * t1 + t2 * t2);
      t1 *= scale;
      t2 *= scale;

      polytope.addVertex(center.getX() - t1, center.getY() + t2, center.getZ(), EPSILON);
      polytope.addVertex(center.getX() + t1, center.getY() + t2, center.getZ(), EPSILON);
      polytope.addVertex(center.getX() - t1, center.getY() - t2, center.getZ(), EPSILON);
      polytope.addVertex(center.getX() + t1, center.getY() - t2, center.getZ(), EPSILON);

      polytope.addVertex(center.getX(), center.getY() - t1, center.getZ() + t2, EPSILON);
      polytope.addVertex(center.getX(), center.getY() + t1, center.getZ() + t2, EPSILON);
      polytope.addVertex(center.getX(), center.getY() - t1, center.getZ() - t2, EPSILON);
      polytope.addVertex(center.getX(), center.getY() + t1, center.getZ() - t2, EPSILON);

      polytope.addVertex(center.getX() + t2, center.getY(), center.getZ() - t1, EPSILON);
      polytope.addVertex(center.getX() + t2, center.getY(), center.getZ() + t1, EPSILON);
      polytope.addVertex(center.getX() - t2, center.getY(), center.getZ() - t1, EPSILON);
      polytope.addVertex(center.getX() - t2, center.getY(), center.getZ() + t1, EPSILON);

      // FIXME add the recursion level code here. Mostly need to precompute the points and then add to polytope. But then whats the point of having the polytope class
      //      List<PolytopeHalfEdge> edges = new ArrayList<>((int) (120 * Math.pow(4, recursionLevel)));
      //      for (int i = 0; i < recursionLevel; i++)
      //      {
      //         edges.clear();
      //         edges.addAll(polytope.getEdges());
      //         scale = radius / Math.sqrt(radius * radius - edges.get(0).getEdgeVector().dot(edges.get(0).getEdgeVector())) / 2.0;
      //         for (int j = 0; j < edges.size();)
      //         {
      //            PrintTools.debug(j + "");
      //            PolytopeVertex origin = edges.get(j).getOriginVertex();
      //            PolytopeVertex destination = edges.get(j).getDestinationVertex();
      //            PrintTools.debug(origin == null ? "null" : origin.toString());
      //            PrintTools.debug(destination == null ? "null" : origin.toString());
      //            PolytopeVertex newVertex = new PolytopeVertex((origin.getX() + destination.getX()) * scale, (origin.getY() + destination.getY()) * scale,
      //                                                          (origin.getZ() + destination.getZ()) * scale);
      //            polytope.addVertex(newVertex, EPSILON);
      //            edges.remove(edges.get(j));
      //         }
      //      }
      return polytope;
   }

   public static ConvexPolytope constructSphere(Point3D center, double radius, int cubeDivisions)
   {
      ConvexPolytope polytope = new ConvexPolytope();
      List<PolytopeVertex> vertices = new ArrayList<>();
      for (int i = 0; i < cubeDivisions; i++)
      {
         for (int j = 0; j < cubeDivisions; j++)
         {
            PolytopeVertex vertex = new PolytopeVertex( (2.0 * (float) i / (float)cubeDivisions - 1) * radius, (2.0 * (float) j / (float)cubeDivisions - 1) * radius, -radius);
            vertices.add(vertex);
         }
      }
      
      for (int i = 1; i < cubeDivisions; i++)
      {
         for (int j = 0; j < cubeDivisions; j++)
         {
            PolytopeVertex vertex = new PolytopeVertex( -radius, (2.0 * (float) j / (float)cubeDivisions - 1) * radius, (2.0 * (float) i / (float)cubeDivisions - 1) * radius);
            vertices.add(vertex);
            vertex = new PolytopeVertex( (2.0 * (float) j / (float)cubeDivisions - 1) * radius, -radius, (2.0 * (float) i / (float)cubeDivisions - 1) * radius);
            vertices.add(vertex);
            vertex = new PolytopeVertex( (2.0 * (float) j / (float)cubeDivisions - 1) * radius, radius, (2.0 * (float) i / (float)cubeDivisions - 1) * radius);
            vertices.add(vertex);
            vertex = new PolytopeVertex( radius, (2.0 * (float) j / (float)cubeDivisions - 1) * radius, (2.0 * (float) i / (float)cubeDivisions - 1) * radius);
            vertices.add(vertex);
         }
      }
      
      for (int i = 1; i < cubeDivisions -1; i++)
      {
         for (int j = 1; j < cubeDivisions -1; j++)
         {
            PolytopeVertex vertex = new PolytopeVertex( (2.0 * (float) i / (float)cubeDivisions - 1) * radius, (2.0 * (float) j / (float)cubeDivisions - 1) * radius, radius);
            vertices.add(vertex);
         }
      }
      
      for(int i = 0; i < vertices.size(); i++)
      {
         PrintTools.debug("" + i);
         PolytopeVertex vertex = vertices.get(i);
         double mag = Math.sqrt(vertex.getX() * vertex.getX() + vertex.getY() * vertex.getY() + vertex.getZ() * vertex.getZ());
         vertex.setX(vertex.getX() * radius / mag);
         vertex.setY(vertex.getY() * radius / mag);
         vertex.setZ(vertex.getZ() * radius / mag);
         polytope.addVertex(vertex, EPSILON);
      }
      return polytope;
   }

   public static ConvexPolytope constructUnitSphere(int recursionLevel)
   {
      return constructSphere(1.0, new Point3D(), recursionLevel);
   }

   /**
    * Creates a polytope by discretizing the curved surface of the cylinder
    * @param center location of the polytope centroid
    * @param radius radius of the cylinder to be made
    * @param length lenght of the cylinder to be made
    * @param numberOfDivisionsForCurvedSurface
    * @return
    */
   public static ConvexPolytope constructCylinder(Point3D center, double radius, double length, int numberOfDivisionsForCurvedSurface)
   {
      ConvexPolytope polytope = new ConvexPolytope();
      double vertexAngle = 2 * Math.PI / numberOfDivisionsForCurvedSurface;
      double enclosingRadius = radius / Math.cos(vertexAngle / 2.0);
      for (int i = 0; i < numberOfDivisionsForCurvedSurface; i++)
         polytope.addVertex(new PolytopeVertex(center.getX() + enclosingRadius * Math.cos(i * vertexAngle),
                                               center.getY() + enclosingRadius * Math.sin(i * vertexAngle), center.getZ() - length / 2.0),
                            EPSILON);
      for (int i = 0; i < numberOfDivisionsForCurvedSurface; i++)
      {
         polytope.addVertex(new PolytopeVertex(center.getX() + enclosingRadius * Math.cos(i * vertexAngle),
                                               center.getY() + enclosingRadius * Math.sin(i * vertexAngle), center.getZ() + length / 2.0),
                            EPSILON);
      }
      return polytope;
   }

   public static FrameConvexPolytope constructUnitCube(ReferenceFrame frame)
   {
      FrameConvexPolytope polytope = new FrameConvexPolytope(frame, constructUnitCube());
      return polytope;
   }

   public static ConvexPolytope constructBoxWithCenterAtZero(double halfLengthX, double halfWidthY, double halfHeightZ)
   {
      ConvexPolytope polytope = new ConvexPolytope();

      polytope.addVertex(new PolytopeVertex(-halfLengthX, -halfWidthY, -halfHeightZ), EPSILON);
      polytope.addVertex(new PolytopeVertex(halfLengthX, -halfWidthY, -halfHeightZ), EPSILON);
      polytope.addVertex(new PolytopeVertex(halfLengthX, halfWidthY, -halfHeightZ), EPSILON);
      polytope.addVertex(new PolytopeVertex(-halfLengthX, halfWidthY, -halfHeightZ), EPSILON);
      polytope.addVertex(new PolytopeVertex(-halfLengthX, -halfWidthY, halfHeightZ), EPSILON);
      polytope.addVertex(new PolytopeVertex(halfLengthX, -halfWidthY, halfHeightZ), EPSILON);
      polytope.addVertex(new PolytopeVertex(halfLengthX, halfWidthY, halfHeightZ), EPSILON);
      polytope.addVertex(new PolytopeVertex(-halfLengthX, halfWidthY, halfHeightZ), EPSILON);

      return polytope;
   }

   public static ConvexPolytope constructRandomSphereOutlinedPolytope(Random random, int numberOfPoints, double radius, double xyzBoundary)
   {
      ConvexPolytope polytope = new ConvexPolytope();

      Point3D sphereCenter = EuclidCoreRandomTools.nextPoint3D(random, xyzBoundary, xyzBoundary, xyzBoundary);
      for (int i = 0; i < numberOfPoints; i++)
      {
         Vector3D randomVector = EuclidCoreRandomTools.nextVector3DWithFixedLength(random, radius);
         Point3D point = new Point3D(sphereCenter);
         point.add(randomVector);
         polytope.addVertex(point, EPSILON);
      }

      return polytope;
   }

   public static ConvexPolytope constructSinglePointPolytope(Point3D singlePoint)
   {
      ConvexPolytope polytope = new ConvexPolytope();
      polytope.addVertex(singlePoint, EPSILON);
      return polytope;
   }

   public static ConvexPolytope constructFromVertices(double[][] vertices)
   {
      ConvexPolytope polytope = new ConvexPolytope();
      for (double[] vertex : vertices)
      {
         polytope.addVertex(EPSILON, vertex);
      }
      return polytope;
   }
}
