package us.ihmc.geometry.polytope.DCELPolytope;

import us.ihmc.geometry.polytope.DCELPolytope.Basics.Vertex3DReadOnly;

public class SimplexVertex extends ExtendedPolytopeVertex
{
   Vertex3DReadOnly polytopeAVertexReference;
   Vertex3DReadOnly polytopeBVertexReference;

   public SimplexVertex()
   {
      super();
   }

   public SimplexVertex(ExtendedPolytopeVertex vertexOnPolytopeA, ExtendedPolytopeVertex vertexOnPolytopeB)
   {
      set(vertexOnPolytopeA, vertexOnPolytopeB);
   }

   public void set(Vertex3DReadOnly vertexOnPolytopeA, Vertex3DReadOnly vertexOnPolytopeB)
   {
      this.polytopeAVertexReference = vertexOnPolytopeA;
      this.polytopeBVertexReference = vertexOnPolytopeB;
      sub(vertexOnPolytopeA, vertexOnPolytopeB);
   }

   public Vertex3DReadOnly getVertexOnPolytopeA()
   {
      return polytopeAVertexReference;
   }

   public Vertex3DReadOnly getVertexOnPolytopeB()
   {
      return polytopeBVertexReference;
   }
}
