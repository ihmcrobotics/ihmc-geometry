package us.ihmc.geometry.polytope.DCELPolytope.Basics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import us.ihmc.commons.Epsilons;
import us.ihmc.commons.PrintTools;
import us.ihmc.euclid.geometry.BoundingBox3D;
import us.ihmc.euclid.interfaces.Clearable;
import us.ihmc.euclid.interfaces.Settable;
import us.ihmc.euclid.interfaces.Transformable;
import us.ihmc.euclid.transform.interfaces.Transform;
import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Vector3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.CollisionDetection.PolytopeListener;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.ConvexPolytopeFaceProvider;
import us.ihmc.geometry.polytope.DCELPolytope.Providers.PolytopeVertexProvider;

/**
 * 
 * @author Apoorv S
 *
 * @param <V>
 * @param <E>
 * @param <F>
 * 
 */
public abstract class ConvexPolytopeBasics implements ConvexPolytopeReadOnly, SimplexBasics, Clearable, Transformable, Settable<ConvexPolytopeReadOnly>
{
   private final static boolean debug = false;
   private final ArrayList<Vertex3DBasics> vertices = new ArrayList<>();
   private final ArrayList<HalfEdge3DBasics> edges = new ArrayList<>();
   private final ArrayList<Face3DBasics> faces = new ArrayList<>();
   /**
    * Bounding box for the polytope
    */
   private boolean boundingBoxNeedsUpdating = false;
   private final BoundingBox3D boundingBox = new BoundingBox3D(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                                                               Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
   private final ArrayList<Face3DBasics> visibleFaces = new ArrayList<>();
   private final ArrayList<Face3DBasics> silhouetteFaces = new ArrayList<>();
   private final ArrayList<Face3DBasics> nonSilhouetteFaces = new ArrayList<>();
   private final ArrayList<Face3DBasics> onFaceList = new ArrayList<>();
   private final ArrayList<HalfEdge3DBasics> visibleSilhouetteList = new ArrayList<>();

   private Vector3D tempVector = new Vector3D();
   private Point3D centroid = new Point3D();
   private final PolytopeListener listener;

   public ConvexPolytopeBasics()
   {
      listener = null;
   }

   public ConvexPolytopeBasics(PolytopeListener listener)
   {
      this.listener = listener;
      this.listener.attachPolytope(this);
   }

   public ConvexPolytopeBasics(ConvexPolytopeReadOnly polytope)
   {
      set(polytope);
      boundingBoxNeedsUpdating = true;
      listener = null;
   }

   public void getBoundingBox(BoundingBox3D boundingBoxToPack)
   {
      if (boundingBoxNeedsUpdating)
      {
         updateBoundingBox();
         boundingBoxNeedsUpdating = false;
      }
      boundingBoxToPack.set(boundingBox);
   }

   private void updateBoundingBox()
   {
      double xMin = Double.POSITIVE_INFINITY;
      double yMin = Double.POSITIVE_INFINITY;
      double zMin = Double.POSITIVE_INFINITY;

      double xMax = Double.NEGATIVE_INFINITY;
      double yMax = Double.NEGATIVE_INFINITY;
      double zMax = Double.NEGATIVE_INFINITY;

      for (int i = 0; i < faces.size(); i++)
      {
         double x = faces.get(i).getMinX();
         double y = faces.get(i).getMinY();
         double z = faces.get(i).getMinZ();

         if (x < xMin)
            xMin = x;
         if (y < yMin)
            yMin = y;
         if (z < zMin)
            zMin = z;

         x = faces.get(i).getMaxX();
         y = faces.get(i).getMaxY();
         z = faces.get(i).getMaxZ();
         if (x > xMax)
            xMax = x;
         if (y > yMax)
            yMax = y;
         if (z > zMax)
            zMax = z;
      }
      boundingBox.set(xMin, yMin, zMin, xMax, yMax, zMax);
   }

   public int getNumberOfVertices()
   {
      // Polyhedron formula for quick calc
      return getNumberOfEdges() - getNumberOfFaces() + 2;
   }

   public List<Vertex3DBasics> getVertices()
   {
      updateVertices();
      return vertices;
   }

   public void getVertices(List<Point3D> verticesToPack)
   {
      updateVertices();
      for (int i = 0; i < vertices.size(); i++)
         verticesToPack.get(i).set(vertices.get(i));
   }

   private void updateVertices()
   {
      unmarkAllFaces();
      vertices.clear();
      for (int i = 0; i < faces.size(); i++)
      {
         for (int j = 0; j < faces.get(i).getNumberOfEdges(); j++)
         {
            if (!faces.get(i).getEdge(j).getOriginVertex().isAnyFaceMarked())
            {
               vertices.add(faces.get(i).getEdge(j).getOriginVertex());
            }
         }
         faces.get(i).mark();
      }
   }

   public Vertex3DBasics getVertex(int index)
   {
      updateVertices();
      return vertices.get(index);
   }

   public int getNumberOfEdges()
   {
      updateEdges();
      return edges.size() / 2;
   }

   public List<HalfEdge3DBasics> getEdges()
   {
      updateEdges();
      return edges;
   }

   private void updateEdges()
   {
      edges.clear();
      for (int i = 0; i < faces.size(); i++)
      {
         List<HalfEdge3DBasics> faceEdgeList = faces.get(i).getEdgeList();
         for (int j = 0; j < faceEdgeList.size(); j++)
         {
            edges.add(faceEdgeList.get(j));
         }
      }
   }

   public int getNumberOfFaces()
   {
      return faces.size();
   }

   public List<Face3DBasics> getFaces()
   {
      return faces;
   }

   public Face3DBasics getFace(int index)
   {
      return faces.get(index);
   }

   public Vector3DReadOnly getFaceNormalAt(Point3DReadOnly point)
   {
      return getFaceContainingPointClosestTo(point).getFaceNormal();
   }

   @Override
   public void applyTransform(Transform transform)
   {
      // Applying the transform to the vertices is less expensive computationally but getting the vertices is hard
      updateVertices();
      for (int i = 0; i < vertices.size(); i++)
         vertices.get(i).applyTransform(transform);
      boundingBoxNeedsUpdating = true;
   }

   @Override
   public void applyInverseTransform(Transform transform)
   {
      // Applying the transform to the vertices is less expensive computationally but getting the vertices is hard
      updateVertices();
      for (int i = 0; i < vertices.size(); i++)
         vertices.get(i).applyInverseTransform(transform);
      boundingBoxNeedsUpdating = true;
   }

   private void unmarkAllFaces()
   {
      for (int i = 0; i < faces.size(); i++)
         faces.get(i).unmark();
   }

   public void addVertices(double epsilon, Point3D... vertices)
   {
      for (int i = 0; i < vertices.length; i++)
         addVertex(vertices[i], epsilon);
   }

   public void addVertices(double epsilon, List<Point3D> vertices)
   {
      for (int i = 0; i < vertices.size(); i++)
         addVertex(vertices.get(i), epsilon);
   }

   public void addVertices(List<Vertex3DBasics> vertices, double epsilon)
   {
      for (int i = 0; i < vertices.size(); i++)
         addVertex(vertices.get(i), epsilon);
   }

   public void addVertex(double epsilon, double... coordinates)
   {
      addVertex(getVertexProvider().getVertex(coordinates[0], coordinates[1], coordinates[2]), epsilon);
   }

   public void addVertex(double x, double y, double z, double epsilon)
   {
      addVertex(getVertexProvider().getVertex(x, y, z), epsilon);
   }

   public void addVertex(Point3D vertexToAdd, double epsilon)
   {
      addVertex(getVertexProvider().getVertex(vertexToAdd), epsilon);
   }

   /**
    * Adds a polytope vertex to the current polytope. In case needed faces are removed and recreated.
    * This will result in garbage. Fix if possible
    * 
    * @param vertexToAdd
    * @param epsilon
    * @return
    */
   public void addVertex(Vertex3DBasics vertexToAdd, double epsilon)
   {
      vertexToAdd.round(epsilon);
      if (faces.size() == 0)
      {
         // Polytope is empty. Creating face and adding the vertex
         Face3DBasics newFace = getConvexFaceProvider().getFace();
         newFace.addVertex(vertexToAdd, epsilon);
         faces.add(newFace);
         boundingBoxNeedsUpdating = true;
         updateListener();
         return;
      }
      else if (faces.size() == 1)
      {
         if (faces.get(0).isPointInFacePlane(vertexToAdd, epsilon))
         {
            if (!faces.get(0).isInteriorPoint(vertexToAdd, epsilon))
               faces.get(0).addVertex(vertexToAdd, epsilon);
            updateListener();
            return;
         }
         else
         {
            if (faces.get(0).isFaceVisible(vertexToAdd, epsilon))
               faces.get(0).reverseFaceNormal();

            visibleSilhouetteList.clear();
            HalfEdge3DBasics halfEdge = faces.get(0).getEdge(0);
            if (listener != null)
               listener.udpateVisibleEdgeSeed(halfEdge);
            for (int i = 0; i < faces.get(0).getNumberOfEdges(); i++)
            {
               visibleSilhouetteList.add(halfEdge);
               halfEdge = halfEdge.getPreviousHalfEdge();
            }
            if (listener != null)
               listener.updateVisibleSilhouette(visibleSilhouetteList);
            onFaceList.clear();
            createFacesFromVisibleSilhouetteAndOnFaceList(visibleSilhouetteList, onFaceList, vertexToAdd, epsilon);
         }
         boundingBoxNeedsUpdating = true;
         updateListener();
         return;
      }
      Face3DBasics visibleFaceSeed = getVisibleFaces(visibleFaces, vertexToAdd, epsilon);
      if (visibleFaces.isEmpty())
      {
         updateListener();
         return;
      }
      getFacesWhichPointIsOn(vertexToAdd, onFaceList, epsilon);
      if (debug)
         PrintTools.debug("Visible faces: " + visibleFaces.size() + ", On Faces: " + onFaceList.size());
      getSilhouetteFaces(silhouetteFaces, nonSilhouetteFaces, visibleFaces);
      HalfEdge3DBasics firstHalfEdgeForSilhouette = null;
      if (listener != null)
      {
         listener.updateOnFaceList(onFaceList);
         listener.updateVisibleFaceList(Arrays.asList(visibleFaceSeed));
      }
      if (onFaceList.size() > 0)
      {
         if (checkIsInteriorPointOf(onFaceList, vertexToAdd, epsilon))
            return;
         HalfEdge3DBasics firstVisibleEdge = getFirstVisibleEdgeFromOnFaceList(onFaceList, visibleFaces); //onFaceList.get(0).getFirstVisibleEdge(vertexToAdd);
         if (firstVisibleEdge == null)
            return;
         firstHalfEdgeForSilhouette = firstVisibleEdge.getTwinHalfEdge();
      }
      else
      {
         firstHalfEdgeForSilhouette = getSeedEdgeForSilhouetteCalculation(visibleFaces, silhouetteFaces.get(0));
      }
      if (listener != null)
         listener.udpateVisibleEdgeSeed(firstHalfEdgeForSilhouette);
      if (firstHalfEdgeForSilhouette == null)
      {
         if (debug)
            PrintTools.debug("Seed edge was null, aborting. On faces: " + onFaceList.size() + ", visible: " + visibleFaces.size());
         return;
      }
      getVisibleSilhouetteUsingSeed(visibleSilhouetteList, firstHalfEdgeForSilhouette, visibleFaces);
      if (listener != null)
         listener.updateVisibleSilhouette(visibleSilhouetteList);
      if (visibleSilhouetteList.isEmpty())
      {
         if (debug)
            PrintTools.debug("Empty visible silhouette ");
         updateListener();
         return;
      }
      removeFaces(nonSilhouetteFaces);
      removeFaces(silhouetteFaces);
      createFacesFromVisibleSilhouetteAndOnFaceList(visibleSilhouetteList, onFaceList, vertexToAdd, epsilon);
      boundingBoxNeedsUpdating = true;
      updateListener();
   }

   private boolean checkIsInteriorPointOf(ArrayList<Face3DBasics> onFaceList, Point3DReadOnly vertexToAdd, double epsilon)
   {
      for (int i = 0; i < onFaceList.size(); i++)
      {
         if (onFaceList.get(i).getFirstVisibleEdge(vertexToAdd) == null)
            return true;
      }
      return false;
   }

   private HalfEdge3DBasics getFirstVisibleEdgeFromOnFaceList(ArrayList<Face3DBasics> onFaceList, ArrayList<Face3DBasics> visibleFaces)
   {
      Face3DBasics firstFace = onFaceList.get(0);
      HalfEdge3DBasics edgeUnderConsideration = firstFace.getEdge(0);
      for (int i = 0; i < firstFace.getNumberOfEdges(); i++)
      {
         if (!visibleFaces.contains(edgeUnderConsideration.getNextHalfEdge().getTwinHalfEdge().getFace())
               && !onFaceList.contains(edgeUnderConsideration.getNextHalfEdge().getTwinHalfEdge().getFace())
               && visibleFaces.contains(edgeUnderConsideration.getTwinHalfEdge().getFace()))
            return edgeUnderConsideration;
         else
            edgeUnderConsideration = edgeUnderConsideration.getNextHalfEdge();
      }
      return null;
   }

   public void updateListener()
   {
      if (listener != null)
      {
         listener.updateAll();
      }
   }

   public void getSilhouetteFaces(List<Face3DBasics> silhouetteFacesToPack, List<Face3DBasics> nonSilhouetteFacesToPack, List<Face3DBasics> visibleFaceList)
   {
      if (silhouetteFacesToPack != null)
         silhouetteFacesToPack.clear();
      if (nonSilhouetteFacesToPack != null)
         nonSilhouetteFacesToPack.clear();
      for (int i = 0; i < visibleFaceList.size(); i++)
      {
         Face3DBasics candidateFace = visibleFaceList.get(i);

         boolean allNeighbouringFacesVisible = true;
         for (int j = 0; j < candidateFace.getNumberOfEdges(); j++)
            allNeighbouringFacesVisible &= visibleFaceList.contains(candidateFace.getNeighbouringFace(j));

         if (allNeighbouringFacesVisible && nonSilhouetteFacesToPack != null)
            nonSilhouetteFacesToPack.add(candidateFace);
         else if (silhouetteFacesToPack != null)
            silhouetteFacesToPack.add(candidateFace);
      }
   }

   public void getVisibleSilhouette(Point3DReadOnly vertex, List<HalfEdge3DBasics> visibleSilhouetteToPack, double epsilon)
   {
      Face3DBasics leastVisibleFace = getVisibleFaces(visibleFaces, vertex, epsilon);
      if (visibleFaces.isEmpty())
      {
         return;
      }
      getFacesWhichPointIsOn(vertex, onFaceList, epsilon);
      getSilhouetteFaces(silhouetteFaces, nonSilhouetteFaces, visibleFaces);
      HalfEdge3DBasics firstHalfEdgeForSilhouette = onFaceList.size() > 0 ? onFaceList.get(0).getFirstVisibleEdge(vertex).getTwinHalfEdge()
            : getSeedEdgeForSilhouetteCalculation(visibleFaces, leastVisibleFace);
      getVisibleSilhouetteUsingSeed(visibleSilhouetteToPack, firstHalfEdgeForSilhouette, visibleFaces);
   }

   public void getVisibleSilhouetteUsingSeed(List<HalfEdge3DBasics> visibleSilhouetteToPack, HalfEdge3DBasics seedHalfEdge, List<Face3DBasics> silhouetteFaceList)
   {
      HalfEdge3DBasics halfEdgeUnderConsideration = seedHalfEdge;
      visibleSilhouetteToPack.clear();
      int numberOfEdges = getNumberOfEdges();
      int count;
      for (count = 0; count < numberOfEdges; count++)
      {
         if (debug)
         {
            if (halfEdgeUnderConsideration == null)
               PrintTools.debug("Half edge null " + faces.size());
            if (visibleSilhouetteToPack == null)
               PrintTools.debug("visible list null");
            if (halfEdgeUnderConsideration.getTwinHalfEdge() == null)
               PrintTools.debug("Twing half edge null");
         }

         visibleSilhouetteToPack.add(halfEdgeUnderConsideration.getTwinHalfEdge());
         Vertex3DBasics destinationVertex = halfEdgeUnderConsideration.getDestinationVertex();
         for (int i = 0; i < destinationVertex.getNumberOfAssociatedEdges(); i++)
         {
            if (debug)
            {
               if (destinationVertex.getAssociatedEdge(i) == null)
                  PrintTools.debug("Associated edge is null");
               if (destinationVertex.getAssociatedEdge(i).getTwinHalfEdge() == null)
                  PrintTools.debug("Associated edge twin is null\n" + toString());
               if (destinationVertex.getAssociatedEdge(i).getTwinHalfEdge().getFace() == null)
                  PrintTools.debug("Associated edge twin face is null");
            }
            if (silhouetteFaceList.contains(destinationVertex.getAssociatedEdge(i).getFace())
                  && !silhouetteFaceList.contains(destinationVertex.getAssociatedEdge(i).getTwinHalfEdge().getFace()))
            {
               halfEdgeUnderConsideration = destinationVertex.getAssociatedEdge(i);
               break;
            }
         }
         if (halfEdgeUnderConsideration == seedHalfEdge)
            break;
      }
      if (count == numberOfEdges && faces.size() > 1)
      {
         if (debug)
         {
            PrintTools.warn("Could not determine visible silhouette " + onFaceList.size() + ", " + silhouetteFaceList.size() + ", "
                  + visibleSilhouetteToPack.size());
            PrintTools.warn("On face size: " + onFaceList.size());
            for (int i = 0; i < onFaceList.size(); i++)
            {
               PrintTools.debug(onFaceList.get(i).toString());
            }
            PrintTools.warn("Visible face size: " + visibleFaces.size());
            for (int i = 0; i < visibleFaces.size(); i++)
            {
               PrintTools.debug(visibleFaces.get(i).toString());
            }
         }
         if (listener != null)
         {
            listener.updateOnFaceList(onFaceList);
            listener.updateVisibleFaceList(visibleFaces);
            listener.updateVisibleSilhouette(visibleSilhouetteToPack);
         }
         visibleSilhouetteToPack.clear();
      }
   }

   public HalfEdge3DBasics getSeedEdgeForSilhouetteCalculation(List<Face3DBasics> visibleFaceList, Face3DBasics leastVisibleFace)
   {
      if (faces.size() == 1)
         return faces.get(0).getEdge(0);
      HalfEdge3DBasics seedEdge = null;
      HalfEdge3DBasics seedEdgeCandidate = leastVisibleFace.getEdge(0);
      for (int i = 0; seedEdge == null && i < leastVisibleFace.getNumberOfEdges(); i++)
      {
         if (!visibleFaceList.contains(seedEdgeCandidate.getTwinHalfEdge().getFace()))
            seedEdge = seedEdgeCandidate;
         seedEdgeCandidate = seedEdgeCandidate.getNextHalfEdge();
      }
      return seedEdge;
   }

   //   public S getSeedEdgeForSilhouetteCalculation(List<U> silhouetteFaceList)
   //   {
   //      U seedFaceCandidate = silhouetteFaceList.get(0);
   //      S seedEdgeCandidate = seedFaceCandidate.getEdge(0);
   //      for (int i = 0; i < seedFaceCandidate.getNumberOfEdges(); i++)
   //      {
   //         if (silhouetteFaceList.contains(seedEdgeCandidate.getTwinHalfEdge().getFace())
   //               && !silhouetteFaceList.contains(seedEdgeCandidate.getNextHalfEdge().getTwinHalfEdge().getFace()))
   //            break;
   //         seedEdgeCandidate = seedEdgeCandidate.getNextHalfEdge();
   //      }
   //      seedEdgeCandidate = seedEdgeCandidate.getNextHalfEdge();
   //      return seedEdgeCandidate;
   //   }

   //   private void createFacesFromVisibleSilhouette(PolytopeVertex vertexToAdd)
   //   {
   //      U firstNewFace = createFaceFromTwinEdgeAndVertex(vertexToAdd, visibleSilhouetteList.get(0));
   //      twinEdges(visibleSilhouetteList.get(0), firstNewFace.getEdge(0));
   //      for (int i = 1; i < visibleSilhouetteList.size(); i++)
   //      {
   //         U newFace = createFaceFromTwinEdgeAndVertex(vertexToAdd, visibleSilhouetteList.get(i));
   //         twinEdges(visibleSilhouetteList.get(i - 1).getTwinHalfEdge().getNextHalfEdge(), newFace.getEdge(0).getPreviousHalfEdge());
   //      }
   //      twinEdges(visibleSilhouetteList.get(visibleSilhouetteList.size() - 1).getTwinHalfEdge().getNextHalfEdge(), firstNewFace.getEdge(0).getPreviousHalfEdge());
   //   }

   private void createFacesFromVisibleSilhouetteAndOnFaceList(List<HalfEdge3DBasics> silhouetteEdges, List<Face3DBasics> onFaceList, Vertex3DBasics vertexToAdd, double epsilon)
   {
      //for(int i = 0; i < silhouetteEdges.size(); i++)
      //PrintTools.debug("Sil: " + silhouetteEdges.get(i));
      //PrintTools.debug(silhouetteEdges.get(0).getFace().toString());
      HalfEdge3DBasics previousLeadingEdge = null, trailingEdge = null;
      if (onFaceList.contains(silhouetteEdges.get(0).getFace()))
      {
         previousLeadingEdge = silhouetteEdges.get(0).getFace().getFirstVisibleEdge(vertexToAdd);
         if (previousLeadingEdge == null)
         {
            PrintTools.debug("vertex to add: " + vertexToAdd);
            PrintTools.debug("Face: " + silhouetteEdges.get(0).getFace().toString());
            PrintTools.debug("Polytope: " + toString());
         }
         silhouetteEdges.get(0).getFace().addVertex(vertexToAdd, epsilon);
         trailingEdge = previousLeadingEdge.getNextHalfEdge();
      }
      else
      {
         Face3DBasics firstFace = createFaceFromTwinEdgeAndVertex(vertexToAdd, silhouetteEdges.get(0), epsilon);
         previousLeadingEdge = firstFace.getEdge(0).getNextHalfEdge();
         trailingEdge = firstFace.getEdge(0).getPreviousHalfEdge();
      }
      //PrintTools.debug("PrevLeadEdge: "  + ((previousLeadingEdge == null )? "null" : previousLeadingEdge.toString()));
      //PrintTools.debug("TrailEdge: "  + ((trailingEdge == null) ? "null" : trailingEdge.toString() ));
      for (int i = 1; i < silhouetteEdges.size(); i++)
      {
         //PrintTools.debug("Previous leading: " + previousLeadingEdge.toString() + " Visible : " + visibleSilhouetteList.get(i).toString());
         if (onFaceList.contains(silhouetteEdges.get(i).getFace()))
         {
            Face3DBasics faceToExtend = silhouetteEdges.get(i).getFace();
            HalfEdge3DBasics tempEdge = faceToExtend.getFirstVisibleEdge(vertexToAdd);
            faceToExtend.addVertex(vertexToAdd, epsilon);
            if (tempEdge == null)
               PrintTools.debug("This again");
            if (tempEdge.getNextHalfEdge() == null)
               PrintTools.debug("WTF");
            twinEdges(previousLeadingEdge, tempEdge.getNextHalfEdge());
            previousLeadingEdge = tempEdge;
         }
         else
         {
            Face3DBasics newFace = createFaceFromTwinEdgeAndVertex(vertexToAdd, silhouetteEdges.get(i), epsilon);
            twinEdges(previousLeadingEdge, newFace.getEdge(0).getPreviousHalfEdge());
            previousLeadingEdge = newFace.getEdge(0).getNextHalfEdge();
         }
      }
      twinEdges(previousLeadingEdge, trailingEdge);
   }

   private void twinEdges(HalfEdge3DBasics halfEdge1, HalfEdge3DBasics halfEdge2)
   {
      if (halfEdge1.getOriginVertex() != halfEdge2.getDestinationVertex() && halfEdge1.getDestinationVertex() != halfEdge2.getOriginVertex())
         PrintTools.debug("This should print \n\n\n\n");
      halfEdge1.setTwinHalfEdge(halfEdge2);
      halfEdge2.setTwinHalfEdge(halfEdge1);
   }

   private Face3DBasics createFaceFromTwinEdgeAndVertex(Vertex3DBasics vertex, HalfEdge3DBasics twinEdge, double epsilon)
   {
      Face3DBasics newFace = getConvexFaceProvider().getFace();
      faces.add(newFace);
      newFace.addVertex(twinEdge.getDestinationVertex(), epsilon);
      newFace.addVertex(twinEdge.getOriginVertex(), epsilon);
      newFace.addVertex(vertex, epsilon);
      twinEdges(newFace.getEdge(0), twinEdge);
      return newFace;
   }

   private void removeFaces(List<Face3DBasics> facesToRemove)
   {
      for (int i = 0; i < facesToRemove.size(); i++)
      {
         removeFace(facesToRemove.get(i));
      }
   }

   public Face3DBasics getVisibleFaces(List<Face3DBasics> faceReferencesToPack, Point3DReadOnly vertexUnderConsideration, double epsilon)
   {
      Face3DBasics leastVisibleFace = null;
      double minimumVisibilityProduct = Double.POSITIVE_INFINITY;
      faceReferencesToPack.clear();
      for (int i = 0; i < faces.size(); i++)
      {
         double visibilityProduct = faces.get(i).getFaceVisibilityProduct(vertexUnderConsideration);
         if (visibilityProduct > epsilon)
         {
            faceReferencesToPack.add(faces.get(i));
            if (visibilityProduct < minimumVisibilityProduct)
            {
               leastVisibleFace = faces.get(i);
               minimumVisibilityProduct = visibilityProduct;
            }
         }
      }
      return leastVisibleFace;
   }

   public void getFacesWhichPointIsOn(Point3DReadOnly vertexUnderConsideration, List<Face3DBasics> faceReferenceToPack, double epsilon)
   {
      faceReferenceToPack.clear();

      for (int i = 0; i < faces.size(); i++)
      {
         if (faces.get(i).isPointInFacePlane(vertexUnderConsideration, epsilon))
         {
            faceReferenceToPack.add(faces.get(i));
         }
      }
   }

   public void removeFace(Face3DBasics faceToRemove)
   {
      for (int i = 0; i < faceToRemove.getNumberOfEdges(); i++)
      {
         HalfEdge3DBasics twinHalfEdge = faceToRemove.getEdge(i).getTwinHalfEdge();
         if (twinHalfEdge != null)
            twinHalfEdge.setTwinHalfEdge(null);
         faceToRemove.getEdge(i).clear();
      }
      faceToRemove.clearEdgeList();
      faces.remove(faceToRemove);
   }

   private Face3DBasics isInteriorPointInternal(Point3DReadOnly pointToCheck, double epsilon)
   {
      if (faces.size() == 0)
         return null;
      else if (faces.size() == 1)
         return faces.get(0).isInteriorPoint(pointToCheck, epsilon) ? null : faces.get(0);

      for (int i = 0; i < faces.size(); i++)
      {
         tempVector.sub(pointToCheck, faces.get(i).getEdge(0).getOriginVertex());
         double dotProduct = tempVector.dot(faces.get(i).getFaceNormal());
         if (dotProduct >= epsilon || faces.get(i).getNumberOfEdges() < 3)
         {
            return faces.get(i);
         }
      }
      return null;
   }

   public boolean isInteriorPoint(Point3DReadOnly pointToCheck, double epsilon)
   {
      return isInteriorPointInternal(pointToCheck, epsilon) == null;
   }

   @Override
   public Point3DReadOnly getSupportingVertex(Vector3DReadOnly supportDirection)
   {
      Vertex3DBasics bestVertex = faces.get(0).getEdge(0).getOriginVertex();
      tempVector.set(bestVertex);
      double maxDotProduct = supportDirection.dot(tempVector);
      Vertex3DBasics vertexCandidate = bestVertex;

      while (true)
      {
         for (int i = 0; i < bestVertex.getNumberOfAssociatedEdges(); i++)
         {
            tempVector.set(bestVertex.getAssociatedEdge(i).getDestinationVertex());
            double dotProduct = supportDirection.dot(tempVector);
            if (dotProduct > maxDotProduct)
            {
               vertexCandidate = bestVertex.getAssociatedEdge(i).getDestinationVertex();
               maxDotProduct = dotProduct;
            }
         }
         if (bestVertex == vertexCandidate)
            return bestVertex;
         else
            bestVertex = vertexCandidate;
      }
   }

   // TODO Hacking this for the new collision detector. #FIXME fix this and the related interfaces and all that depends on those interfaces so that there dont need to be two versions of the getSupportinVertex function
   @Override
   public Vertex3DBasics getSupportingPolytopeVertex(Vector3DReadOnly supportDirection)
   {
      Vertex3DBasics bestVertex = faces.get(0).getEdge(0).getOriginVertex();
      tempVector.set(bestVertex);
      double maxDotProduct = supportDirection.dot(tempVector);
      Vertex3DBasics vertexCandidate = bestVertex;
      while (true)
      {
         for (int i = 0; i < bestVertex.getNumberOfAssociatedEdges(); i++)
         {
            tempVector.set(bestVertex.getAssociatedEdge(i).getDestinationVertex());
            double dotProduct = supportDirection.dot(tempVector);
            if (dotProduct > maxDotProduct)
            {
               vertexCandidate = bestVertex.getAssociatedEdge(i).getDestinationVertex();
               maxDotProduct = dotProduct;
            }
         }
         if (bestVertex == vertexCandidate)
            return bestVertex;
         else
            bestVertex = vertexCandidate;
      }
   }

   public String toString()
   {
      String string = "\n\nNumber of faces: " + faces.size();
      for (int i = 0; i < faces.size(); i++)
      {
         string = string + "\n" + faces.get(i).toString();
      }
      return string;
   }

   @Override
   public boolean epsilonEquals(ConvexPolytopeReadOnly other, double epsilon)
   {
      // TODO imlepment this
      return false;
   }

   @Override
   public boolean containsNaN()
   {
      boolean result = false;
      for (int i = 0; i < faces.size(); i++)
      {
         result |= faces.get(i).containsNaN();
      }
      return result;
   }

   @Override
   public void setToNaN()
   {
      // This should also set all the edges and vertices to NaN assuming all relationships are intact
      for (int i = 0; i < faces.size(); i++)
      {
         faces.get(i).setToNaN();
      }
   }

   @Override
   public void setToZero()
   {
      // This should also set all the edges and vertices to zero assuming all relationships are intact
      for (int i = 0; i < faces.size(); i++)
      {
         faces.get(i).setToZero();
      }
   }

   @Override
   public void set(ConvexPolytopeReadOnly other)
   {
      copyFaces(other.getFaces());
   }

   private void copyFaces(List<? extends Face3DReadOnly> faces)
   {
      //TODO implement this 
      throw new RuntimeException("Unimplemented feature");
   }

   public void clear()
   {
      vertices.clear();
      edges.clear();
      faces.clear();
      visibleFaces.clear();
      silhouetteFaces.clear();
      nonSilhouetteFaces.clear();
      onFaceList.clear();
      visibleSilhouetteList.clear();
   }

   @Override
   public double distance(Point3DReadOnly point)
   {
      if (isInteriorPoint(point, Epsilons.ONE_TRILLIONTH))
      {
         return getFaceContainingPointClosestTo(point).distance(point);
      }
      else
      {
         return getFaceContainingPointClosestTo(point).distance(point);
      }
   }

   public Face3DBasics getFaceContainingPointClosestTo(Point3DReadOnly point)
   {
      if (faces.size() == 0)
         return null;
      else if (faces.size() == 1)
      {
         return faces.get(0);
      }

      unmarkAllFaces();
      Face3DBasics currentBestFace = faces.get(0);
      Face3DBasics faceUnderConsideration = currentBestFace;
      double minDistance = faceUnderConsideration.distance(point);
      faceUnderConsideration.mark();

      for (int i = 0; i < faces.size(); i++)
      {
         for (int j = 0; j < currentBestFace.getNumberOfEdges(); j++)
         {
            if (currentBestFace.getNeighbouringFace(j) != null && currentBestFace.getNeighbouringFace(j).isNotMarked())
            {
               double distance = currentBestFace.getNeighbouringFace(j).distance(point);
               if (distance < minDistance)
               {
                  minDistance = distance;
                  faceUnderConsideration = currentBestFace.getNeighbouringFace(j);
               }
               currentBestFace.getNeighbouringFace(j).mark();
            }
         }
         if (faceUnderConsideration == currentBestFace)
            break;
         else
            currentBestFace = faceUnderConsideration;
      }
      return currentBestFace;
   }

   private void updateCentroid()
   {
      updateVertices();
      centroid.setToZero();
      for (int i = 0; i < vertices.size(); i++)
         centroid.add(vertices.get(i));
      centroid.scale(1.0 / vertices.size());
   }

   public Point3DReadOnly getCentroid()
   {
      updateCentroid();
      return centroid;
   }

   protected abstract PolytopeVertexProvider getVertexProvider();

   protected abstract ConvexPolytopeFaceProvider getConvexFaceProvider();

   @Override
   public void getSupportVectorDirectionTo(Point3DReadOnly point, Vector3DBasics supportVectorToPack)
   {
      getFaceContainingPointClosestTo(point).getSupportVectorDirectionTo(point, supportVectorToPack);
   }

   @Override
   public SimplexBasics getSmallestSimplexMemberReference(Point3DReadOnly point)
   {
      return getFaceContainingPointClosestTo(point).getSmallestSimplexMemberReference(point);
   }
}
