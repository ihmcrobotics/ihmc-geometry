package us.ihmc.geometry.polytope.DCELPolytope.Frame;

import us.ihmc.euclid.referenceFrame.FramePoint3D;
import us.ihmc.euclid.referenceFrame.ReferenceFrame;
import us.ihmc.euclid.referenceFrame.interfaces.FramePoint3DBasics;
import us.ihmc.euclid.tuple3D.interfaces.Point3DReadOnly;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeVertexBasics;
import us.ihmc.geometry.polytope.DCELPolytope.Basics.PolytopeVertexReadOnly;

public class FramePolytopeVertex extends PolytopeVertexBasics<FramePolytopeVertex, FramePolytopeHalfEdge, FrameConvexPolytopeFace>
      implements FrameSimplex, FramePoint3DBasics
{
   private ReferenceFrame referenceFrame;
   private double x, y, z;

   public FramePolytopeVertex()
   {
      setToZero(ReferenceFrame.getWorldFrame());
   }

   public FramePolytopeVertex(ReferenceFrame frame)
   {
      setToZero(frame);
   }

   public FramePolytopeVertex(ReferenceFrame frame, double x, double y, double z)
   {
      setIncludingFrame(frame, x, y, z);
   }

   public FramePolytopeVertex(ReferenceFrame frame, Point3DReadOnly vertex)
   {
      setIncludingFrame(frame, vertex);
   }

   public FramePolytopeVertex(FramePoint3D vertex)
   {
      setIncludingFrame(vertex);
   }

   @Override
   public void setReferenceFrame(ReferenceFrame referenceFrame)
   {
      this.referenceFrame = referenceFrame;
   }

   @Override
   public void setX(double x)
   {
      this.x = x;
   }

   @Override
   public void setY(double y)
   {
      this.y = y;
   }

   @Override
   public void setZ(double z)
   {
      this.z = z;
   }

   @Override
   public ReferenceFrame getReferenceFrame()
   {
      return referenceFrame;
   }

   @Override
   public double getX()
   {
      return x;
   }

   @Override
   public double getY()
   {
      return y;
   }

   @Override
   public double getZ()
   {
      return z;
   }

   public boolean epsilonEquals(FramePolytopeVertex other, double epsilon)
   {
      return super.epsilonEquals(other, epsilon);
   }
}
