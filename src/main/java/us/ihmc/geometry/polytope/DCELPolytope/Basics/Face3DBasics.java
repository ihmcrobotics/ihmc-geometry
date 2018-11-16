package us.ihmc.geometry.polytope.DCELPolytope.Basics;

import java.util.ArrayList;
import java.util.List;

import us.ihmc.commons.Epsilons;
import us.ihmc.commons.MathTools;
import us.ihmc.commons.PrintTools;
import us.ihmc.euclid.geometry.tools.EuclidGeometryTools;
import us.ihmc.euclid.interfaces.Clearable;
import us.ihmc.euclid.interfaces.Settable;
import us.ihmc.euclid.interfaces.Transformable;
import us.ihmc.euclid.transform.interfaces.Transform;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;
import us.ihmc.geometry.polytope.SupportingVertexHolder;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.PolytopeHalfEdgeProvider;

/**
 * A template class for a DCEL face. A face is composed of
 * <li>{@code edges} list of half edges that make up this face
 * <li>{@code faceNormal} a vector normal to this face, can point in either direction
 *
 * @author Apoorv S
 *
 * @param <V> Data structure representing a point in 3D space
 * @param <E> A data structure representing an directed edge formed by joining two vertices
 * @param <F> A extension of this class denoting a collection of edges that constitute a face of the
 *           polytope
 */
public abstract class Face3DBasics
      implements SimplexBasics, SupportingVertexHolder, Face3DReadOnly, Clearable, Settable<Face3DReadOnly>, Transformable
{
   private static final boolean debug = false;

   /**
    * Unordered list of half edges that bound the face
    */
   private final ArrayList<HalfEdge3DBasics> edges = new ArrayList<>();

   /**
    * A vector normal to the plane that this face lies on. Do not access directly since this is updated
    * only when the getter is called
    */
   private final Vector3D faceNormal = new Vector3D();
   /**
    * The variable used to store the centroid of the polytope whenever updated Do not access directly
    * since this is updated only when the getter is called
    */
   private final Point3D faceCentroid = new Point3D();

   // Temporary variables for calculations
   private final Vector3D tempVector = new Vector3D();
   private final Point3D tempPoint = new Point3D();
   private final ArrayList<HalfEdge3DBasics> visibleEdgeList = new ArrayList<>();
   private boolean marked = false;

   /**
    * Default constructor. Does not initialize anything
    */
   public Face3DBasics()
   {

   }

   /**
    * Copy constructor. Makes and stores copies of the specified face
    *
    * @param other the polytope that is to be copied Note: while the edges are copied the association
    *           between the edges is not. This will make the polytope inconsistent
    */
   public Face3DBasics(Face3DBasics other)
   {
      this(other.getEdgeList());
   }

   /**
    * Similar to the copy constructor. Creates and stores copies of the specified edges Note: while the
    * edges are copied the association between the edges is not
    *
    * @param edgeList list of edges to be copied
    */
   public Face3DBasics(List<HalfEdge3DBasics> edgeList)
   {
      this.copyEdgeList(edgeList);
   }

   /**
    * Forms a polytope based on the list of edges specified
    *
    * @param edgeList array of edges that will form the boundary of the specified polytope
    */
   public Face3DBasics(HalfEdge3DBasics[] edgeList)
   {
      setEdgeList(edgeList);
   }

   /**
    * Similar to the copy constructor. Creates and stores copies of the specified edges Note: while the
    * edges are copied the association between the edges is not
    *
    * @param edgeList array of edges to be copied
    */
   public Face3DBasics(HalfEdge3DReadOnly[] edgeListArray)
   {
      this.copyEdgeList(edgeListArray);
   }

   /**
    * Sets the face edge list to a copy of the edges in the list provided However, association of the
    * edges is lost
    *
    * @param edgeList
    */
   public void copyEdgeList(List<? extends HalfEdge3DReadOnly> edgeList)
   {
      edges.clear();
      for (int i = 0; i < edgeList.size(); i++)
         edges.add(getHalfEdgeProvider().getHalfEdge(edgeList.get(i)));
   }

   public void copyEdgeList(HalfEdge3DReadOnly[] edgeListArray)
   {
      edges.clear();
      for (int i = 0; i < edgeListArray.length; i++)
         edges.add(getHalfEdgeProvider().getHalfEdge(edgeListArray[i]));
   }

   public void setEdgeList(HalfEdge3DBasics[] edgeListArray)
   {
      edges.clear();
      for (int i = 0; i < edgeListArray.length; i++)
         edges.add(edgeListArray[i]);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<HalfEdge3DBasics> getEdgeList()
   {
      return edges;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HalfEdge3DBasics getEdge(int index)
   {
      return edges.get(index);
   }

   /**
    * Adds a vertex to the face and updates all the associations accordingly
    *
    * @param vertexToAdd the vertex that must be added to the face
    * @param epsilon
    */
   public void addVertex(Vertex3DBasics vertexToAdd, double epsilon)
   {
      vertexToAdd.round(epsilon);
      switch (edges.size())
      {
      case 0:
      {
         HalfEdge3DBasics newEdge = getHalfEdgeProvider().getHalfEdge(vertexToAdd, vertexToAdd);
         newEdge.setFace(this);
         newEdge.setNextHalfEdge(newEdge);
         newEdge.setPreviousHalfEdge(newEdge);
         edges.add(newEdge);
         break;
      }
      case 1:
      {
         // Set the edge for the two points and then create its twin
         if (edges.get(0).getOriginVertex().epsilonEquals(vertexToAdd, epsilon))
            return;
         edges.get(0).setDestinationVertex(vertexToAdd);
         HalfEdge3DBasics newEdge = getHalfEdgeProvider().getHalfEdge(vertexToAdd, edges.get(0).getOriginVertex());
         newEdge.setFace(this);
         newEdge.setNextHalfEdge(edges.get(0));
         newEdge.setPreviousHalfEdge(edges.get(0));
         edges.get(0).setNextHalfEdge(newEdge);
         edges.get(0).setPreviousHalfEdge(newEdge);
         edges.add(newEdge);
         break;
      }
      case 2:
      {
         if (edges.get(0).getOriginVertex().epsilonEquals(vertexToAdd, epsilon) || edges.get(0).getDestinationVertex().epsilonEquals(vertexToAdd, epsilon))
            return;
         // Create a new edge and assign an arbitrary configuration since there is no way to tell up and down in 3D space
         edges.get(1).setDestinationVertex(vertexToAdd);
         HalfEdge3DBasics newEdge = getHalfEdgeProvider().getHalfEdge(vertexToAdd, edges.get(0).getOriginVertex());
         newEdge.setFace(this);
         edges.add(newEdge);
         newEdge.setNextHalfEdge(edges.get(0));
         edges.get(0).setPreviousHalfEdge(newEdge);
         newEdge.setPreviousHalfEdge(edges.get(1));
         edges.get(1).setNextHalfEdge(newEdge);
         break;
      }
      default:
      {
         // Now a ordering is available and all new vertices to add must be done accordingly. Also points must lie in the same plane
         if (!isPointInFacePlane(vertexToAdd, epsilon))
            return;

         getVisibleEdgeList(vertexToAdd, visibleEdgeList);
         switch (visibleEdgeList.size())
         {
         case 0:
            return; // Case where the point is internal
         case 1:
            if (visibleEdgeList.get(0).getOriginVertex().epsilonEquals(vertexToAdd, epsilon)
                  || visibleEdgeList.get(0).getDestinationVertex().epsilonEquals(vertexToAdd, epsilon))
               return;
            HalfEdge3DBasics additionalEdge = getHalfEdgeProvider().getHalfEdge(vertexToAdd, visibleEdgeList.get(0).getDestinationVertex());
            additionalEdge.setFace(this);
            visibleEdgeList.get(0).setDestinationVertex(vertexToAdd);
            additionalEdge.setNextHalfEdge(visibleEdgeList.get(0).getNextHalfEdge());
            visibleEdgeList.get(0).getNextHalfEdge().setPreviousHalfEdge(additionalEdge);
            visibleEdgeList.get(0).setNextHalfEdge(additionalEdge);
            additionalEdge.setPreviousHalfEdge(visibleEdgeList.get(0));
            edges.add(additionalEdge);
            break;
         default:
            visibleEdgeList.get(0).setDestinationVertex(vertexToAdd);
            visibleEdgeList.get(visibleEdgeList.size() - 1).setOriginVertex(vertexToAdd);
            visibleEdgeList.get(0).setNextHalfEdge(visibleEdgeList.get(visibleEdgeList.size() - 1));
            visibleEdgeList.get(visibleEdgeList.size() - 1).setPreviousHalfEdge(visibleEdgeList.get(0));
            for (int i = 1; i < visibleEdgeList.size() - 1; i++)
               edges.remove(visibleEdgeList.get(i));
            break;
         }
         break;
      }
      }
   }

   /**
    *
    * @param newEdge
    */
   public void addEdge(HalfEdge3DBasics newEdge)
   {
      edges.add(newEdge);
   }

   public void getVisibleEdgeList(Point3DReadOnly vertex, List<HalfEdge3DBasics> edgeList)
   {
      edgeList.clear();
      HalfEdge3DBasics edgeUnderConsideration = getFirstVisibleEdge(vertex);
      for (int i = 0; edgeUnderConsideration != null && i < edges.size(); i++)
      {
         edgeList.add(edgeUnderConsideration);
         edgeUnderConsideration = edgeUnderConsideration.getNextHalfEdge();
         if (isPointOnInteriorSideOfEdgeInternal(vertex, edgeUnderConsideration))
            break;
      }
   }

   @Override
   public HalfEdge3DBasics getFirstVisibleEdge(Point3DReadOnly vertex)
   {
      if (edges.size() == 0)
         return null;
      else if (edges.size() == 1 || edges.size() == 2)
         return edges.get(0);

      HalfEdge3DBasics edgeUnderConsideration = edges.get(0);
      double previousDotProduct = getEdgeVisibilityProduct(vertex, edgeUnderConsideration);
      edgeUnderConsideration = edgeUnderConsideration.getNextHalfEdge();
      for (int i = 0; i < getNumberOfEdges(); i++)
      {
         double dotProduct = getEdgeVisibilityProduct(vertex, edgeUnderConsideration);
         if (dotProduct >= 0.0 && previousDotProduct < 0.0)
         {
            return edgeUnderConsideration;
         }
         else if (dotProduct >= 0.0 && previousDotProduct == 0.0)
         {
            return edgeUnderConsideration.getPreviousHalfEdge();
         }
         else
         {
            edgeUnderConsideration = edgeUnderConsideration.getNextHalfEdge();
            previousDotProduct = dotProduct;
         }
      }
      return null;
   }

   @Override
   public boolean isPointOnInteriorSideOfEdgeInternal(Point3DBasics point, int index)
   {
      updateFaceNormal();
      return isPointOnInteriorSideOfEdgeInternal(point, edges.get(index));
   }

   private boolean isPointOnInteriorSideOfEdgeInternal(Point3DReadOnly point, HalfEdge3DBasics halfEdge)
   {
      return getEdgeVisibilityProduct(point, halfEdge) < 0;
   }

   @Override
   public double getFaceVisibilityProduct(Point3DReadOnly point)
   {
      tempVector.sub(point, getEdge(0).getOriginVertex());
      return dotFaceNormal(tempVector);
   }

   private double getEdgeVisibilityProduct(Point3DReadOnly point, HalfEdge3DBasics halfEdge)
   {
      tempVector.sub(point, halfEdge.getOriginVertex());
      tempVector.cross(halfEdge.getEdgeVector());
      return tempVector.dot(getFaceNormal());
   }

   @Override
   public boolean isPointInFacePlane(Point3DReadOnly vertexToCheck, double epsilon)
   {
      boolean isInFacePlane;
      tempVector.sub(vertexToCheck, edges.get(0).getOriginVertex());
      if (edges.size() < 3)
      {
         isInFacePlane = !MathTools.epsilonEquals(Math.abs(edges.get(0).getEdgeVector().dot(tempVector))
               / (edges.get(0).getEdgeVector().length() * tempVector.length()), 1.0, epsilon);
      }
      else
         isInFacePlane = MathTools.epsilonEquals(tempVector.dot(getFaceNormal()), 0.0, epsilon);
      return isInFacePlane;
   }

   @Override
   public boolean isInteriorPoint(Point3DReadOnly vertexToCheck, double epsilon)
   {
      return isPointInFacePlane(vertexToCheck, epsilon) && isInteriorPointInternal(vertexToCheck);
   }

   private boolean isInteriorPointInternal(Point3DReadOnly vertexToCheck)
   {
      if (edges.size() < 3)
         return false;

      boolean result = true;
      HalfEdge3DBasics halfEdge = edges.get(0);
      for (int i = 0; result && i < edges.size(); i++)
      {
         result &= isPointOnInteriorSideOfEdgeInternal(vertexToCheck, halfEdge);
         halfEdge = halfEdge.getNextHalfEdge();
      }
      return result;
   }

   @Override
   public Point3D getFaceCentroid()
   {
      updateFaceCentroid();
      return faceCentroid;
   }

   private void updateFaceCentroid()
   {
      faceCentroid.setToZero();
      for (int i = 0; i < edges.size(); i++)
         faceCentroid.add(edges.get(i).getOriginVertex());
      faceCentroid.scale(1.0 / edges.size());
   }

   @Override
   public Vector3D getFaceNormal()
   {
      updateFaceNormal();
      return faceNormal;
   }

   private void updateFaceNormal()
   {
      if (edges.size() < 3)
         faceNormal.setToZero();
      else
      {
         faceNormal.cross(edges.get(0).getEdgeVector(), edges.get(0).getNextHalfEdge().getEdgeVector());
         if (faceNormal.dot(faceNormal) > Epsilons.ONE_TEN_THOUSANDTH)
            faceNormal.normalize();
      }
   }

   @Override
   public int getNumberOfEdges()
   {
      return edges.size();
   }

   @Override
   public void applyTransform(Transform transform)
   {
      for (int i = 0; i < getNumberOfEdges(); i++)
         edges.get(i).getOriginVertex().applyTransform(transform);
   }

   @Override
   public void applyInverseTransform(Transform transform)
   {
      for (int i = 0; i < getNumberOfEdges(); i++)
         edges.get(i).getOriginVertex().applyInverseTransform(transform);
   }

   @Override
   public boolean epsilonEquals(Face3DReadOnly other, double epsilon)
   {
      if (other.getNumberOfEdges() == getNumberOfEdges())
      {
         int index = findMatchingEdgeIndex(other.getEdge(0), epsilon);
         if (index != -1)
         {
            boolean result = true;
            HalfEdge3DBasics matchedEdge = edges.get(index);
            HalfEdge3DReadOnly candidateEdge = other.getEdge(0);
            for (int i = 0; result && i < edges.size() - 1; i++)
            {
               matchedEdge = matchedEdge.getNextHalfEdge();
               candidateEdge = candidateEdge.getNextHalfEdge();
               result &= matchedEdge.epsilonEquals(candidateEdge, epsilon);
            }
            return result;
         }
         else
            return false;
      }
      else
         return false;
   }

   public int findMatchingEdgeIndex(HalfEdge3DReadOnly edgeToSearch, double epsilon)
   {
      for (int i = 0; i < edges.size(); i++)
      {
         if (edges.get(i).epsilonEquals(edgeToSearch, epsilon))
            return i;
      }
      return -1;
   }

   public HalfEdge3DReadOnly findMatchingEdge(HalfEdge3DReadOnly edgeToSearch, double epsilon)
   {
      return edges.get(findMatchingEdgeIndex(edgeToSearch, epsilon));
   }

   public void reverseFaceNormal()
   {
      for (int i = 0; i < edges.size(); i++)
      {
         edges.get(i).reverseEdge();
      }
      updateFaceNormal();
   }

   @Override
   public void set(Face3DReadOnly other)
   {
      clearEdgeList();
      copyEdgeList(other.getEdgeList());
   }

   public void clearEdgeList()
   {
      edges.clear();
   }

   @Override
   public boolean containsNaN()
   {
      boolean result = edges.size() > 0 && edges.get(0).containsNaN();
      for (int i = 1; !result && i < edges.size(); i++)
         result |= edges.get(i).getDestinationVertex().containsNaN();
      return result;
   }

   @Override
   public void setToNaN()
   {
      for (int i = 0; i < edges.size(); i++)
         edges.get(i).setToNaN();
   }

   @Override
   public double dotFaceNormal(Vector3DReadOnly direction)
   {
      updateFaceNormal();
      return direction.dot(faceNormal);
   }

   @Override
   public boolean isFaceVisible(Point3DReadOnly point, double epsilon)
   {
      return getFaceVisibilityProduct(point) > epsilon;
   }

   @Override
   public void setToZero()
   {
      for (int i = 0; i < edges.size(); i++)
         edges.get(i).setToZero();
   }

   @Override
   public double getMaxElement(int index)
   {
      HalfEdge3DBasics edgeReference = edges.get(0);
      double maxElement = edgeReference.getOriginVertex().getElement(index);
      for (int i = 0; i < edges.size(); i++)
      {
         if (maxElement < edgeReference.getDestinationVertex().getElement(index))
            maxElement = edgeReference.getDestinationVertex().getElement(index);
         edgeReference = edgeReference.getNextHalfEdge();
      }
      return maxElement;
   }

   @Override
   public double getMinElement(int index)
   {
      HalfEdge3DBasics edgeReference = edges.get(0);
      double minElement = edgeReference.getOriginVertex().getElement(index);
      for (int i = 0; i < edges.size(); i++)
      {
         if (minElement > edgeReference.getDestinationVertex().getElement(index))
            minElement = edgeReference.getDestinationVertex().getElement(index);
         edgeReference = edgeReference.getNextHalfEdge();
      }
      return minElement;
   }

   @Override
   public double getMaxX()
   {
      return getMaxElement(0);
   }

   @Override
   public double getMaxY()
   {
      return getMaxElement(1);
   }

   @Override
   public double getMaxZ()
   {
      return getMaxElement(2);
   }

   @Override
   public double getMinX()
   {
      return getMinElement(0);
   }

   @Override
   public double getMinY()
   {
      return getMinElement(1);
   }

   @Override
   public double getMinZ()
   {
      return getMinElement(2);
   }

   @Override
   public Face3DBasics getNeighbouringFace(int index)
   {
      if (index > edges.size() || edges.get(index).getTwinHalfEdge() == null)
         return null;
      else
         return edges.get(index).getTwinHalfEdge().getFace();

   }

   @Override
   public Point3DReadOnly getSupportingVertex(Vector3DReadOnly supportVector)
   {
      Vertex3DBasics bestVertex = edges.get(0).getOriginVertex();
      double maxDot = bestVertex.dot(supportVector);
      Vertex3DBasics bestVertexCandidate = bestVertex;
      while (true)
      {
         bestVertexCandidate = bestVertex;
         for (int i = 0; i < bestVertex.getNumberOfAssociatedEdges(); i++)
         {
            double dotCandidate = bestVertex.getAssociatedEdges().get(i).getDestinationVertex().dot(supportVector);
            if (maxDot < dotCandidate)
            {
               maxDot = dotCandidate;
               bestVertexCandidate = bestVertex.getAssociatedEdge(i).getDestinationVertex();
            }
         }
         if (bestVertexCandidate == bestVertex)
            return bestVertex;
         else
            bestVertex = bestVertexCandidate;
      }
   }

   public void mark()
   {
      marked = true;
   }

   public void unmark()
   {
      marked = false;
   }

   @Override
   public boolean isMarked()
   {
      return marked;
   }

   @Override
   public boolean isNotMarked()
   {
      return !marked;
   }

   @Override
   public String toString()
   {
      String string = "";
      HalfEdge3DBasics edge = edges.get(0);
      for (int i = 0; i < edges.size(); i++)
      {
         string += "\n" + edge.toString() + " Twin: " + (edge.getTwinHalfEdge() == null ? "null" : edge.getTwinHalfEdge().toString());
         edge = edge.getNextHalfEdge();
      }
      return string;
   }

   @Override
   public double distance(Point3DReadOnly point)
   {
      EuclidGeometryTools.orthogonalProjectionOnPlane3D(point, edges.get(0).getOriginVertex(), getFaceNormal(), tempPoint);
      if (isInteriorPointInternal(tempPoint))
         return point.distance(tempPoint);
      else
         return getEdgeClosestTo(tempPoint).distance(point);
   }

   @Override
   public HalfEdge3DBasics getEdgeClosestTo(Point3DReadOnly point)
   {
      HalfEdge3DBasics edge = getFirstVisibleEdge(tempPoint);
      double shortestDistance = edge.distance(tempPoint);
      double shortestDistanceCandidate = Double.NEGATIVE_INFINITY;
      while (shortestDistanceCandidate < shortestDistance)
      {
         edge = edge.getNextHalfEdge();
         shortestDistanceCandidate = edge.distance(tempPoint);
      }
      return edge.getPreviousHalfEdge();
   }

   @Override
   public void getSupportVectorDirectionTo(Point3DReadOnly point, Vector3DBasics supportVectorToPack)
   {
      EuclidGeometryTools.orthogonalProjectionOnPlane3D(point, edges.get(0).getOriginVertex(), getFaceNormal(), tempPoint);
      if (isInteriorPointInternal(tempPoint))
         supportVectorToPack.set(getFaceNormal());
      else
         getEdgeClosestTo(tempPoint).getSupportVectorDirectionTo(point, supportVectorToPack);
   }

   protected abstract PolytopeHalfEdgeProvider getHalfEdgeProvider();

   @Override
   public SimplexBasics getSmallestSimplexMemberReference(Point3DReadOnly point)
   {
      EuclidGeometryTools.orthogonalProjectionOnPlane3D(point, edges.get(0).getOriginVertex(), getFaceNormal(), tempPoint);
      if (isInteriorPointInternal(tempPoint))
         return this;
      else
         return getEdgeClosestTo(tempPoint).getSmallestSimplexMemberReference(point);
   }
}
