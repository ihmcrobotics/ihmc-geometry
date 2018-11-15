package us.ihmc.geometry.polytope;

import us.ihmc.euclid.tuple3D.Point3D;
import us.ihmc.euclid.tuple3D.Vector3D;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;

public interface GilbertJohnsonKeerthiCollisionDetectorListener
{
   public abstract void addedVertexToSimplex(SimplexPolytope simplex, Point3D vertexOnSimplex, Point3DReadOnly vertexOnA, Point3DReadOnly vertexOnB);

   public abstract void foundClosestPointOnSimplex(SimplexPolytope simplex, Point3D closestPointToOrigin);

   public abstract void foundCollision(SimplexPolytope simplex, Point3D pointOnAToPack, Point3D pointOnBToPack);

   public abstract void foundSupportPoints(SimplexPolytope simplex, Point3DReadOnly supportingPointOnA, Point3DReadOnly supportingPointOnB, Vector3D supportPointOnSimplex);

   public abstract void computeVDotPAndPercentCloser(double vDotP, double percentCloser);

   public abstract void metStoppingConditionForNoIntersection(double vDotP, double percentCloser, Point3D pointOnAToPack, Point3D pointOnBToPack);

   public abstract void tooManyIterationsStopping(SimplexPolytope simplex, Point3D pointOnAToPack, Point3D pointOnBToPack);

   public abstract void metStoppingConditionForNoIntersection(Point3D pointOnAToPack, Point3D pointOnBToPack);

   public abstract void checkingIfPolytopesAreColliding(SupportingVertexHolder polytopeA, SupportingVertexHolder polytopeB);
}
