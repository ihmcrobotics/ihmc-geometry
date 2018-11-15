package us.ihmc.geometry.polytope.DCELPolytope.Basics;

import java.util.ArrayList;
import java.util.List;

import us.ihmc.commons.MathTools;
import us.ihmc.euclid.tools.EuclidCoreIOTools;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;

/**
 * Template data structure that defines a Doubly-connected edge list (DCEL) polytope vertex
 *
 * A DCEL vertex is composed of
 * <li>3D point: A data structure that stores the spatial location of the vertex in 3D space. May /
 * may not have a notion of a {@code ReferenceFrame}
 * <li>Associated edge list: A list of DCEL edges {@code E} that have their origins at this vertex
 *
 * @author Apoorv S
 *
 * @param <V> The class that extends this template. Should represent a point in some 3D space
 * @param <E> Data structure representing edges formed by joining two vertices
 * @param <F> A collection of edges that constitute a face of the polytope
 */
public abstract class Vertex3DBasics implements SimplexBasics, Vertex3DReadOnly, Point3DBasics
{
   /**
    * List of edges that start at this vertex. May be part of different faces
    */
   private final List<PolytopeHalfEdgeBasics> associatedEdges = new ArrayList<>();

   /**
    * Default constructor
    */
   public Vertex3DBasics()
   {
   }

   /**
    * Set the spatial coordinates from another vertex and copy all the edge associations
    */
   public void set(Vertex3DBasics other)
   {
      Point3DBasics.super.set(other);
      clearAssociatedEdgeList();
      addAssociatedEdges(other.getAssociatedEdges());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<PolytopeHalfEdgeBasics> getAssociatedEdges()
   {
      return associatedEdges;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public PolytopeHalfEdgeBasics getAssociatedEdge(int index)
   {
      return associatedEdges.get(index);
   }

   /**
    * Method to remove a particular edge from the associated edge list
    *
    * @param edgeToAdd the associated edge that is to be removed. In case the edge specified is not on
    *           the list, no errors are thrown
    *
    */
   public void removeAssociatedEdge(PolytopeHalfEdgeBasics edgeToAdd)
   {
      associatedEdges.remove(edgeToAdd);
   }

   /**
    * Remove all edges in the associated edge list
    */
   public void clearAssociatedEdgeList()
   {
      associatedEdges.clear();
   }

   /**
    * Add a {@code List<E>} of DCEL edges to the associated edge list. This invokes the
    * {@code addAssociatedEdge()} method and addition to the list follows the same set of rules
    *
    * @param edgeList a list of DCEL edges that must be added
    */
   public void addAssociatedEdges(List<? extends PolytopeHalfEdgeBasics> edgeList)
   {
      for (int i = 0; i < edgeList.size(); i++)
      {
         addAssociatedEdge(edgeList.get(i));
      }
   }

   /**
    * Add a DCEL edge to the associated edge list. In case the edge is already on the associated edge
    * list no action is carried out. The check for whether an edge is already on the list is done by
    * comparing objects. Hence is possible to add a edge that geometrically equals an already existent
    * edge
    *
    * @param edge the DCEL edge to add to the associated edge list
    */
   public void addAssociatedEdge(PolytopeHalfEdgeBasics edge)
   {
      if (!isAssociatedWithEdge(edge))
         associatedEdges.add(edge);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAssociatedWithEdge(HalfEdge3DReadOnly edgeToCheck)
   {
      return associatedEdges.contains(edgeToCheck);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAssociatedWithEdge(HalfEdge3DReadOnly edgeToCheck, double epsilon)
   {
      for (int i = 0; i < associatedEdges.size(); i++)
      {
         if (associatedEdges.get(i).epsilonEquals(edgeToCheck, epsilon))
            return true;
      }
      return false;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int getNumberOfAssociatedEdges()
   {
      return associatedEdges.size();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public double dot(Vector3DReadOnly vector)
   {
      return getX() * vector.getX() + getY() * vector.getY() + getZ() * vector.getZ();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return EuclidCoreIOTools.getTuple3DString(this);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isAnyFaceMarked()
   {
      boolean isMarked = false;
      for (int i = 0; !isMarked && i < associatedEdges.size(); i++)
      {
         isMarked |= associatedEdges.get(i).getFace().isMarked();
      }
      return isMarked;
   }

   @Override
   public double getShortestDistanceTo(Point3DReadOnly point)
   {
      return distance(point);
   }

   @Override
   public void getSupportVectorDirectionTo(Point3DReadOnly point, Vector3D supportVectorToPack)
   {
      supportVectorToPack.sub(point, this);
   }

   @Override
   public SimplexBasics getSmallestSimplexMemberReference(Point3DReadOnly point)
   {
      return this;
   }

   public void round(double epsilon)
   {
      setX(MathTools.roundToPrecision(getX(), epsilon));
      setY(MathTools.roundToPrecision(getY(), epsilon));
      setZ(MathTools.roundToPrecision(getZ(), epsilon));
   }
}
