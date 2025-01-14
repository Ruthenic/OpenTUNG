package de.ecconia.java.opentung.components.fragments;

import de.ecconia.java.opentung.components.meta.ModelHolder;
import de.ecconia.java.opentung.components.meta.Part;
import de.ecconia.java.opentung.meshing.MeshTypeThing;
import de.ecconia.java.opentung.util.math.Quaternion;
import de.ecconia.java.opentung.util.math.Vector3;

public class CubeOpen extends CubeFull
{
	protected final Direction direction;
	
	public CubeOpen(Vector3 position, Vector3 size, Direction openDirection)
	{
		this(position, size, openDirection, null);
	}
	
	public CubeOpen(Vector3 position, Vector3 size, Direction openDirection, Color color)
	{
		super(position, size, color);
		
		this.direction = openDirection;
	}
	
	public Direction getDirection()
	{
		return direction;
	}
	
	@Override
	public int getFacesCount()
	{
		return 5;
	}
	
	@Override
	public int getVCount()
	{
		//Faces * Vertices * Data
		return 5 * 4 * 9;
	}
	
	@Override
	public int getICount()
	{
		//Faces * Triangles * Indices
		return 5 * 2 * 3;
	}
	
	@Override
	public void generateMeshEntry(Part instance, float[] vertices, ModelHolder.IntHolder offsetV, int[] indices, ModelHolder.IntHolder indicesIndex, ModelHolder.IntHolder vertexCounter, Color colorOverwrite, Vector3 position, Quaternion rotation, Vector3 placementOffset, MeshTypeThing type)
	{
		Vector3 color = this.color;
		if(colorOverwrite != null)
		{
			color = colorOverwrite.asVector();
		}
		
		Vector3 size = new Vector3(this.size.getX(), this.size.getY(), this.size.getZ());
		Vector3 min = this.position.add(placementOffset).subtract(size);
		Vector3 max = this.position.add(placementOffset).add(size);
		
		Vector3 normal;
		//Position Normal Coord Color
		if(direction != Direction.YPos)
		{
			//Up:
			normal = rotation.inverse().multiply(new Vector3(0, 1, 0));
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), max.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), max.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), max.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), max.getY(), max.getZ()), normal, color, type);
			genIndex(indices, indicesIndex.getAndInc(6), vertexCounter.getAndInc(4));
		}
		if(direction != Direction.YNeg)
		{
			//Down
			normal = rotation.inverse().multiply(new Vector3(0, -1, 0));
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), min.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), min.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), min.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), min.getY(), max.getZ()), normal, color, type);
			genIndex(indices, indicesIndex.getAndInc(6), vertexCounter.getAndInc(4));
		}
		if(direction != Direction.XPos)
		{
			//Right:
			normal = rotation.inverse().multiply(new Vector3(1, 0, 0));
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), min.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), min.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), max.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), max.getY(), min.getZ()), normal, color, type);
			genIndex(indices, indicesIndex.getAndInc(6), vertexCounter.getAndInc(4));
		}
		if(direction != Direction.XNeg)
		{
			//Left:
			normal = rotation.inverse().multiply(new Vector3(-1, 0, 0));
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), min.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), min.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), max.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), max.getY(), max.getZ()), normal, color, type);
			genIndex(indices, indicesIndex.getAndInc(6), vertexCounter.getAndInc(4));
		}
		if(direction != Direction.ZNeg)
		{
			//Forward:
			normal = rotation.inverse().multiply(new Vector3(0, 0, 1));
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), min.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), min.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), max.getY(), min.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), max.getY(), min.getZ()), normal, color, type);
			genIndex(indices, indicesIndex.getAndInc(6), vertexCounter.getAndInc(4));
		}
		if(direction != Direction.ZPos)
		{
			//Back:
			normal = rotation.inverse().multiply(new Vector3(0, 0, -1));
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), min.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), min.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(min.getX(), max.getY(), max.getZ()), normal, color, type);
			genVertex(vertices, offsetV, position, rotation, new Vector3(max.getX(), max.getY(), max.getZ()), normal, color, type);
			genIndex(indices, indicesIndex.getAndInc(6), vertexCounter.getAndInc(4));
		}
	}
}
