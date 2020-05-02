package de.ecconia.java.opentung.models;

public class SnappingPegModel extends GenericModel
{
	//936 bytes
	public SnappingPegModel()
	{
		//Create:
		IntHolder offset = new IntHolder(); //Vertices array offset
		vertices = new float[1 * 6 * 4 * 9];
		placeCube(0, 0.15f, 0,
				0.045f, 0.15f, 0.045f,
				0.0f, 150f / 255f, 141f / 255f, offset, null);
		
		offset = new IntHolder(); //Indices array offset
		indices = new short[1 * 6 * 6];
		placeCubeIndices(offset, 0 * 24, null);
		
		upload();
	}
}