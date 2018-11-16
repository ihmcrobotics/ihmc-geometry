package us.ihmc.geometry.polytope.DCELPolytope.Basics;

import us.ihmc.euclid.geometry.interfaces.LineSegment3DBasics;
import us.ihmc.euclid.geometry.tools.EuclidGeometryTools;
import us.ihmc.euclid.transform.interfaces.Transform;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.PolytopeHalfEdgeProvider;

/**
 * A template that defines the basic structure of a DCEL half edge. A half edge is composed of
 * <li>{@code originVertex} starting point reference for this directed edge
 * <li>{@code destinatioVertex} ending point reference for this directed edge
 * <li>{@code twinHalfEdge} reference to the twin half edge on an adjacent face, if defined
 * <li>{@code nextHalfEdge} reference to the half edge on {@code face} that succeeds this edge in a
 * counter clockwise sense
 * <li>{@code previousHalfEdge} reference to the half edge on {@code face} that precedes this edge
 * in a counter clockwise sense
 * <li>{@code face} the face that this half edge is a part of
 *
 * @author Apoorv S
 *
 * @param <V> Data structure representing a point in 3D space
 * @param <E> A class that extends this data structure Represents an directed edge formed by joining
 *           two vertices
 * @param <F> A collection of edges that constitute a face of the polytope
 */
public abstract class HalfEdge3DBasics implements HalfEdge3DReadOnly, LineSegment3DBasics, SimplexBasics
{
   /**
    * Specifies the spatial location at which the half edge originates
    */
   private Vertex3DBasics originVertex;
   /**
    * Specifies the spatial location at which the half edge terminates
    */
   private Vertex3DBasics destinationVertex;
   /**
    * The half edge on an adjacent face that originates at the {@code destinatioVertex} and terminates
    * at the {@code originVertex}. Represents the opposite spatial direction
    */
   private HalfEdge3DBasics twinEdge;
   /**
    * The half edge on the same face as this edge that originates at the {@code destinationVertex}
    */
   private HalfEdge3DBasics nextHalfEdge;
   /**
    * The half edge on the same face as this edge that terminates at the {@code originVertex}
    */
   private HalfEdge3DBasics previousHalfEdge;
   /**
    * The face that this edge is a part of
    */
   private ConvexPolytopeFaceBasics face;
   /**
    * A vector that represents the direction and lenght of the half edge. Not recomputed on change of
    * values. Only recomputed when called through its getter
    */
   private Vector3D edgeVector = new Vector3D();
   /**
    * A temporary variable for storing results
    */
   private Point3D tempPoint = new Point3D();

   /**
    * Returns a object of type {@code PolytopeHalfEdgeProvider} that can be used to generate half edges
    * of the same type as this half edge
    *
    * @return an object that can be used to create other half edge objects
    */
   protected abstract PolytopeHalfEdgeProvider getHalfEdgeProvider();

   /**
    * Default constructor
    */
   public HalfEdge3DBasics()
   {

   }

   /**
    * Primary constructor for half edge
    *
    * @param originVertex
    * @param destinationVertex
    */
   public HalfEdge3DBasics(Vertex3DBasics originVertex, Vertex3DBasics destinationVertex)
   {
      setOriginVertex(originVertex);
      setDestinationVertex(destinationVertex);
   }

   /**
    * Copy constructor that copies all associations
    *
    * @param edge
    */
   public HalfEdge3DBasics(HalfEdge3DBasics edge)
   {
      set(edge);
   }

   /**
    * Creates a half edge using the {@code createTwinHalfEdge{} function and stores a reference to the
    * new object in the twin edge field @return a twin edge
    */
   public HalfEdge3DBasics setAndCreateTwinHalfEdge()
   {
      HalfEdge3DBasics twinEdge = createTwinHalfEdge();
      setTwinHalfEdge(twinEdge);
      return twinEdge;
   }

   /**
    * Creates a half edge from a twin edge and the face that the new half edge is to be a part of
    *
    * @param twinEdge the edge that is to be the twin of the new half edgegetShortestDistanceTo
    * @param face the face that the new half edge is to be a part of
    */
   public HalfEdge3DBasics(HalfEdge3DBasics twinEdge, ConvexPolytopeFaceBasics face)
   {
      setTwinHalfEdge(twinEdge);
      setOriginVertex(twinEdge.getDestinationVertex());
      setDestinationVertex(twinEdge.getOriginVertex());
      setFace(face);
   }

   /**
    * Creates a half edge using all specified values
    *
    * @param originVertex the vertex that the new half edge will start at. Stored as a reference. Can
    *           be {@code null}
    * @param destinationVertex the vertex that the new half edge will end at. Stored as a reference.
    *           Can be {@code null}
    * @param twinEdge the half edge that is the DCEL twin of the new edge . Stored as a reference. Can
    *           be {@code null}
    * @param nextHalfEdge the half edge that is originates at the destination vertex and comes after
    *           the current edge when the face is traversed in a counter clockwise manner w.r.t. its
    *           face normal. Can be {@code null}
    * @param previousHalfEdge the half edge that is terminates at the origin vertex and comes before
    *           the current edge when the face is traversed in a counter clockwise manner w.r.t. its
    *           face normal. Can be {@code null}
    * @param face the face that this half edge is a part of. Can be {@code null}
    */
   public HalfEdge3DBasics(Vertex3DBasics originVertex, Vertex3DBasics destinationVertex, HalfEdge3DBasics twinEdge, HalfEdge3DBasics nextHalfEdge,
                           HalfEdge3DBasics previousHalfEdge, ConvexPolytopeFaceBasics face)
   {
      setOriginVertex(originVertex);
      setDestinationVertex(destinationVertex);
      setTwinHalfEdge(twinEdge);
      setNextHalfEdge(nextHalfEdge);
      setPreviousHalfEdge(previousHalfEdge);
      setFace(face);
   }

   /**
    * Takes a edge, clears all its fields and assigns it all the values that a twin edge for this half
    * edge would have i.e. {@code originVertex}, {@code destinationVertex}, {@code twinEdge = this}
    *
    * @param twinEdge
    */
   public void setToTwin(HalfEdge3DBasics twinEdge)
   {
      twinEdge.clear();
      twinEdge.setOriginVertex(destinationVertex);
      twinEdge.setDestinationVertex(originVertex);
      twinEdge.setTwinHalfEdge(this);
   }

   /**
    *
    * @return a twin edge that can be used to generate a adjacent face. The twin edge generated stores
    *         references to the {@code originVertex} and {@code destinationVertex}. Half edge generated
    *         stores this edge as its twin but this half edge does not store the generated half edge as
    *         its twin
    */
   public HalfEdge3DBasics createTwinHalfEdge()
   {
      HalfEdge3DBasics twinEdge = getHalfEdgeProvider().getHalfEdge(getDestinationVertex(), getOriginVertex());
      twinEdge.setTwinHalfEdge(this);
      return twinEdge;
   }

   /**
    * Update the reference to the {@code originVertex} field to the specified value. Also updates the
    * associated edges of the previously held and newly specified {@code originVertex} and the
    * {@code twinEdge} of this edge
    *
    * @param originVertex the new vertex that the half edge originates at. Can be null. Is modified
    */
   public void setOriginVertex(Vertex3DBasics originVertex)
   {
      if (this.originVertex != null)
         this.originVertex.removeAssociatedEdge(this);
      setOriginVertexUnsafe(originVertex);
      if (this.originVertex != null)
         this.originVertex.addAssociatedEdge(this);
      updateTwinDestination();
   }

   /**
    * Update the reference to the {@code originVertex} to the specified value. Associations are not
    * updated
    *
    * @param originVertex the new vertex that the half edge originates at. Can be null. Is not modified
    *           in this function
    */
   public void setOriginVertexUnsafe(Vertex3DBasics originVertex)
   {
      this.originVertex = originVertex;
   }

   /**
    * Internal function to update the origin of the twin edge only if the twin is not null. Is needed
    * since the public versions that can set the origin would lead to a cyclic non-terminating call.
    * Also updates the requisite references.
    */
   private void updateTwinOrigin()
   {
      if (twinEdge != null)
      {
         if (twinEdge.getOriginVertex() != null)
            twinEdge.getOriginVertex().removeAssociatedEdge(twinEdge);
         twinEdge.setOriginVertexUnsafe(destinationVertex);
         if (twinEdge.getOriginVertex() != null)
            twinEdge.getOriginVertex().addAssociatedEdge(twinEdge);
      }
   }

   /**
    * Internal function to update the destination vertex of the twin edge only if the twin is not null.
    * Is needed since the public versions that can set the destination would lead to a cyclic
    * non-terminating call.
    */
   private void updateTwinDestination()
   {
      if (twinEdge != null)
         twinEdge.setDestinationVertexUnsafe(originVertex);
   }

   /**
    * Returns a reference to the origin vertex for this half edge
    */
   @Override
   public Vertex3DBasics getOriginVertex()
   {
      return originVertex;
   }

   /**
    * Update the reference to the {@code destinationVertex} to the specified value. Also updates the
    * associated twin edge
    *
    * @param destinationVertex the new vertex that the half edge originates at. Can be null. Is not
    *           modified in this function
    */
   public void setDestinationVertex(Vertex3DBasics destinationVertex)
   {
      this.destinationVertex = destinationVertex;
      updateTwinOrigin();
   }

   /**
    * Update the reference to the {@code destinationVertex} to the specified value. Associations of the
    * specified vertex and of this object are not updated
    *
    * @param destinationVertex the new vertex that the half edge originates at. Can be null. Is not
    *           modified in this function
    * @param destinationVertex
    */
   public void setDestinationVertexUnsafe(Vertex3DBasics destinationVertex)
   {
      this.destinationVertex = destinationVertex;
   }

   /**
    * Returns a reference to the {@code destinationVertex} of this half edge
    */
   @Override
   public Vertex3DBasics getDestinationVertex()
   {
      return destinationVertex;
   }

   /**
    * Store a reference to the specified half edge as a twin of this half edge. No checks are performed
    * while updating the twin edge.
    *
    * @param twinEdge the half edge to be stored as a twin edge of this half edge.
    */
   public void setTwinHalfEdge(HalfEdge3DBasics twinEdge)
   {
      this.twinEdge = twinEdge;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HalfEdge3DBasics getTwinHalfEdge()
   {
      return twinEdge;
   }

   /**
    * Update the reference to the {@code nextHalfEdge}. Checks to ensure that the origin of the
    * specified edge and destination of the this half edge are the same
    *
    * @param nextHalfEdge the new next half edge for the current half edge. Can be null
    * @throws RuntimeException in case the origin of this specified next half edge is not the same as
    *            the destination of the this edge
    */
   public void setNextHalfEdge(HalfEdge3DBasics nextHalfEdge)
   {
      if (nextHalfEdge == null || nextHalfEdge.getOriginVertex() == getDestinationVertex() && nextHalfEdge.getFace() == getFace())
         setNextHalfEdgeUnsafe(nextHalfEdge);
      else
         throw new RuntimeException("Mismatch between vertices, destination vertex: " + getDestinationVertex().toString() + " , next edge origin vertex: "
               + nextHalfEdge.getOriginVertex().toString());
   }

   /**
    * Internal method to update the next half edge without any checks
    *
    * @param nextHalfEdge the half edge whose reference is to be stored in the next half edge field
    */
   private void setNextHalfEdgeUnsafe(HalfEdge3DBasics nextHalfEdge)
   {
      this.nextHalfEdge = nextHalfEdge;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HalfEdge3DBasics getNextHalfEdge()
   {
      return nextHalfEdge;
   }

   /**
    * Update the reference to the {@code previous HalfEdge}. Checks to ensure that the destination of
    * the specified edge and origin of the this half edge are the same
    *
    * @param previousHalfEdge the new previous half edge for the current half edge. Can be null
    * @throws RuntimeException in case the destination of this specified next half edge is not the same
    *            as the origin of the this edge
    */
   public void setPreviousHalfEdge(HalfEdge3DBasics previousHalfEdge)
   {
      if (previousHalfEdge == null || previousHalfEdge.getDestinationVertex() == getOriginVertex() && previousHalfEdge.getFace() == getFace())
         setPreviousHalfEdgeUnsafe(previousHalfEdge);
      else
         throw new RuntimeException("Mismatch between vertices, origin vertex: " + getOriginVertex().toString() + " , previous edge destination vertex: "
               + previousHalfEdge.getDestinationVertex().toString());
   }

   /**
    * Internal method to update the next half edge without any checks
    *
    * @param previousHalfEdge the half edge whose reference is to be stored in the previous half edge
    *           field
    */
   private void setPreviousHalfEdgeUnsafe(HalfEdge3DBasics previousHalfEdge)
   {
      this.previousHalfEdge = previousHalfEdge;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public HalfEdge3DBasics getPreviousHalfEdge()
   {
      return previousHalfEdge;
   }

   /**
    * Update the reference to the face that this half edge is a part of
    *
    * @param face the face reference to be stored. Can be null
    */
   public void setFace(ConvexPolytopeFaceBasics face)
   {
      this.face = face;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ConvexPolytopeFaceBasics getFace()
   {
      return face;
   }

   @Override
   public Point3DBasics getFirstEndpoint()
   {
      return getOriginVertex();
   }

   @Override
   public Point3DBasics getSecondEndpoint()
   {
      return getDestinationVertex();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Vector3DReadOnly getEdgeVector()
   {
      edgeVector.sub(destinationVertex, originVertex);
      return edgeVector;
   }

   @Override
   public void applyTransform(Transform transform)
   {
      originVertex.applyTransform(transform);
      destinationVertex.applyTransform(transform);
   }

   @Override
   public void applyInverseTransform(Transform transform)
   {
      originVertex.applyInverseTransform(transform);
      destinationVertex.applyInverseTransform(transform);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isTwin(HalfEdge3DReadOnly twinEdge, double epsilon)
   {
      return epsilonEquals(twinEdge.getTwinHalfEdge(), epsilon);
   }

   /**
    * Copies all the references from the specified half edge to this half edge while updating the
    * associated objects
    */
   public void set(HalfEdge3DBasics other)
   {
      setOriginVertex(other.getOriginVertex());
      setDestinationVertex(other.getDestinationVertex());
      setTwinHalfEdge(other.getTwinHalfEdge());
      setNextHalfEdge(other.getNextHalfEdge());
      setPreviousHalfEdge(other.getPreviousHalfEdge());
      setFace(other.getFace());
   }

   @Override
   public boolean containsNaN()
   {
      return originVertex.containsNaN() || destinationVertex.containsNaN();
   }

   /**
    * {@inheritDoc} Also sets the {@code originVertex} and {@code destinationVertex} to NaN
    */
   @Override
   public void setToNaN()
   {
      originVertex.setToNaN();
      destinationVertex.setToNaN();
   }

   /**
    * {@inheritDoc} Also sets the {@code originVertex} and {@code destinationVertex} to zero
    */
   @Override
   public void setToZero()
   {
      originVertex.setToZero();
      destinationVertex.setToZero();
   }

   /**
    * Sets all the references that are held by this half edge to null and also updates the previously
    * associated objects
    */
   public void clear()
   {
      setTwinHalfEdge(null);
      setOriginVertex(null);
      setDestinationVertex(null);
      setNextHalfEdge(null);
      setPreviousHalfEdge(null);
      setFace(null);
   }

   /**
    * Changes the direction of this edge so that it starts at the previous {@code destinationVertex}
    * and ends at the {@code originVertex} The references to the {@code nextHalfEdge} and
    * {@code previousHalfEdge} are also updated as its the twin edge. Reference to the {@code face} is
    * not changed
    */
   public void reverseEdge()
   {
      Vertex3DBasics newDestinationVertex = originVertex;
      setOriginVertex(destinationVertex);
      setDestinationVertex(newDestinationVertex);
      HalfEdge3DBasics newNextHalfEdge = previousHalfEdge;
      setPreviousHalfEdgeUnsafe(nextHalfEdge);
      setNextHalfEdgeUnsafe(newNextHalfEdge);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "From: " + (originVertex == null ? "null" : originVertex.toString()) + ", To: "
            + (destinationVertex == null ? "null" : destinationVertex.toString());
   }

   @Override
   public void getSupportVectorDirectionTo(Point3DReadOnly point, Vector3D supportVectorToPack)
   {
      double percentage = EuclidGeometryTools.percentageAlongLineSegment3D(point, originVertex, destinationVertex);
      if (percentage <= 0.0)
         originVertex.getSupportVectorDirectionTo(point, supportVectorToPack);
      else if (percentage >= 1.0)
         destinationVertex.getSupportVectorDirectionTo(point, supportVectorToPack);
      else
      {
         tempPoint.interpolate(originVertex, destinationVertex, percentage);
         supportVectorToPack.sub(point, tempPoint);
      }
   }

   @Override
   public SimplexBasics getSmallestSimplexMemberReference(Point3DReadOnly point)
   {
      double percentage = EuclidGeometryTools.percentageAlongLineSegment3D(point, originVertex, destinationVertex);
      if (percentage <= 0.0)
         return originVertex;
      else if (percentage >= 1.0)
         return destinationVertex;
      else
         return this;
   }

   @Override
   public double distance(Point3DReadOnly point)
   {
      return HalfEdge3DReadOnly.super.distance(point);
   }
}
