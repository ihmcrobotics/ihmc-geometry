package us.ihmc.geometry.polytope.DCELPolytope;

import java.util.ArrayList;
import java.util.List;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;

import us.ihmc.commons.Epsilons;
import us.ihmc.euclid.geometry.tools.EuclidGeometryTools;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.SimplexBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DReadOnly;

public class ExtendedSimplexPolytope implements SimplexBasics
{
   private double epsilon = Epsilons.ONE_TRILLIONTH;
   private ExtendedConvexPolytope polytope = new ExtendedConvexPolytope();
   private List<SimplexVertex> vertices = new ArrayList<>();
   private final Vector3D basisVector1 = new Vector3D();
   private final Vector3D basisVector2 = new Vector3D();
   private final Vector3D pointVector = new Vector3D();
   private final Point3D projection = new Point3D();
   private final DenseMatrix64F basis = new DenseMatrix64F(3, 2);
   private final DenseMatrix64F basisInverse = new DenseMatrix64F(2, 3);
   private final DenseMatrix64F vector = new DenseMatrix64F(3, 1);
   private final DenseMatrix64F coordinates = new DenseMatrix64F(2, 1);

   public ExtendedSimplexPolytope()
   {
      super();
   }

   public void setEpsilon(double epsilon)
   {
      this.epsilon = epsilon;
   }

   public void addVertex(Vertex3DReadOnly vertexOnPolytopeA, Vertex3DReadOnly vertexOnPolytopeB)
   {
      addVertex(vertexOnPolytopeA, vertexOnPolytopeB, epsilon);
   }

   public void addVertex(Vertex3DReadOnly vertexOnPolytopeA, Vertex3DReadOnly vertexOnPolytopeB, double epsilon)
   {
      SimplexVertex newVertex = new SimplexVertex();
      newVertex.set(vertexOnPolytopeA, vertexOnPolytopeB);
      polytope.addVertex(newVertex, epsilon);
   }

   public void clear()
   {
      vertices.clear();
      polytope.clear();
   }

   public boolean isInteriorPoint(Point3DReadOnly pointToCheck, double epsilon)
   {
      return polytope.isInteriorPoint(pointToCheck, epsilon);
   }

   @Override
   public double distance(Point3DReadOnly point)
   {
      return polytope.distance(point);
   }

   @Override
   public void getSupportVectorDirectionTo(Point3DReadOnly point, Vector3DBasics supportVectorToPack)
   {
      polytope.getSupportVectorDirectionTo(point, supportVectorToPack);
   }

   public boolean isEmpty()
   {
      return polytope.isEmpty();
   }

   @Override
   public SimplexBasics getSmallestSimplexMemberReference(Point3DReadOnly point)
   {
      return polytope.getSmallestSimplexMemberReference(point);
   }

   @Override
   public String toString()
   {
      return polytope.toString();
   }

   public ExtendedConvexPolytope getPolytope()
   {
      return polytope;
   }

   public void getCollidingPointsOnSimplex(Point3DReadOnly point, Point3D pointOnA, Point3D pointOnB)
   {
      SimplexBasics member = getSmallestSimplexMemberReference(point);
      // Assuming linearity between the simplex and polytope points
      if (member instanceof Face3D)
      {
         // TODO fix this nasty type casting
         SimplexVertex simplexVertex1 = (SimplexVertex) ((Face3D) member).getEdge(0).getOriginVertex();
         Vertex3DReadOnly polytopeAVertex1 = simplexVertex1.getVertexOnPolytopeA();
         Vertex3DReadOnly polytopeBVertex1 = simplexVertex1.getVertexOnPolytopeB();
         SimplexVertex simplexVertex2 = (SimplexVertex) ((Face3D) member).getEdge(0).getDestinationVertex();
         Vertex3DReadOnly polytopeAVertex2 = simplexVertex2.getVertexOnPolytopeA();
         Vertex3DReadOnly polytopeBVertex2 = simplexVertex2.getVertexOnPolytopeB();
         SimplexVertex simplexVertex3 = (SimplexVertex) ((Face3D) member).getEdge(1).getDestinationVertex();
         Vertex3DReadOnly polytopeAVertex3 = simplexVertex3.getVertexOnPolytopeA();
         Vertex3DReadOnly polytopeBVertex3 = simplexVertex3.getVertexOnPolytopeB();

         // Computing the coordinate vector for the face basis (using the first two edges as the basis)
         EuclidGeometryTools.orthogonalProjectionOnPlane3D(point, simplexVertex2, ((Face3D) member).getFaceNormal(), projection);
         for (int i = 0; i < 3; i++)
         {
            basis.set(i, 0, simplexVertex1.getElement(i) - simplexVertex2.getElement(i));
            basis.set(i, 1, simplexVertex3.getElement(i) - simplexVertex2.getElement(i));
            vector.set(i, 0, projection.getElement(i) - simplexVertex2.getElement(i));
         }
         CommonOps.pinv(basis, basisInverse);
         CommonOps.mult(basisInverse, vector, coordinates);
         setByInterpolation(pointOnA, polytopeAVertex1, polytopeAVertex2, polytopeAVertex3, coordinates.get(0, 0), coordinates.get(1, 0));
         setByInterpolation(pointOnB, polytopeBVertex1, polytopeBVertex2, polytopeBVertex3, coordinates.get(0, 0), coordinates.get(1, 0));
      }
      else if (member instanceof HalfEdge3D)
      {
         // TODO fix this nasty type casting
         SimplexVertex simplexVertex1 = (SimplexVertex) ((HalfEdge3D) member).getOriginVertex();
         Vertex3DReadOnly polytopeAVertex1 = simplexVertex1.getVertexOnPolytopeA();
         Vertex3DReadOnly polytopeBVertex1 = simplexVertex1.getVertexOnPolytopeB();
         SimplexVertex simplexVertex2 = (SimplexVertex) ((HalfEdge3D) member).getDestinationVertex();
         Vertex3DReadOnly polytopeAVertex2 = simplexVertex2.getVertexOnPolytopeA();
         Vertex3DReadOnly polytopeBVertex2 = simplexVertex2.getVertexOnPolytopeB();
         double percentage = EuclidGeometryTools.percentageAlongLineSegment3D(point, simplexVertex1, simplexVertex2);
         pointOnA.interpolate(polytopeAVertex1, polytopeAVertex2, percentage);
         pointOnB.interpolate(polytopeBVertex1, polytopeBVertex2, percentage);
      }
      else if (member instanceof SimplexVertex)
      {
         // TODO fix this nasty type casting
         pointOnA.set(((SimplexVertex) member).getVertexOnPolytopeA());
         pointOnB.set(((SimplexVertex) member).getVertexOnPolytopeB());
      }
      else
      {
         throw new RuntimeException("Unhandled simplex member " + member.getClass());
      }
   }

   private void setByInterpolation(Point3D pointOnA, Vertex3DReadOnly polytopeAVertex1, Vertex3DReadOnly polytopeAVertex2, Vertex3DReadOnly polytopeAVertex3,
                                   double a, double b)
   {
      basisVector1.sub(polytopeAVertex1, polytopeAVertex2);
      basisVector2.sub(polytopeAVertex3, polytopeAVertex2);
      pointVector.setAndScale(a, basisVector1);
      pointVector.scaleAdd(b, basisVector2, pointVector);
      pointOnA.add(pointVector, polytopeAVertex2);
   }
}
