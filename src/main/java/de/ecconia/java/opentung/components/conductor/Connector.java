package de.ecconia.java.opentung.components.conductor;

import de.ecconia.java.opentung.components.fragments.CubeFull;
import de.ecconia.java.opentung.components.fragments.CubeOpen;
import de.ecconia.java.opentung.components.fragments.CubeOpenRotated;
import de.ecconia.java.opentung.components.meta.Component;
import de.ecconia.java.opentung.components.meta.ModelHolder;
import de.ecconia.java.opentung.components.meta.Part;
import de.ecconia.java.opentung.meshing.MeshTypeThing;
import de.ecconia.java.opentung.util.math.Quaternion;
import de.ecconia.java.opentung.util.math.Vector3;
import de.ecconia.java.opentung.simulation.Cluster;
import de.ecconia.java.opentung.simulation.Clusterable;
import de.ecconia.java.opentung.simulation.InheritingCluster;
import de.ecconia.java.opentung.simulation.SimulationManager;
import de.ecconia.java.opentung.simulation.SourceCluster;
import de.ecconia.java.opentung.simulation.Wire;
import java.util.ArrayList;
import java.util.List;

public abstract class Connector extends Part implements Clusterable
{
	private final CubeFull model;
	private final List<Wire> wires = new ArrayList<>();
	
	private Cluster cluster;
	
	public Connector(Component parent, CubeFull model)
	{
		super(parent);
		this.model = model;
		
		setRotation(Quaternion.angleAxis(0, Vector3.yp));
	}
	
	@Override
	public void setCluster(Cluster cluster)
	{
		this.cluster = cluster;
	}
	
	@Override
	public boolean hasCluster()
	{
		return cluster != null;
	}
	
	@Override
	public Cluster getCluster()
	{
		return cluster;
	}
	
	public void addWire(Wire wire)
	{
		wires.add(wire);
	}
	
	public List<Wire> getWires()
	{
		return wires;
	}
	
	public CubeFull getModel()
	{
		return model;
	}
	
	public boolean contains(Vector3 probe)
	{
		return model.contains(probe);
	}
	
	@Override
	public int getWholeMeshEntryVCount(MeshTypeThing type)
	{
		if(type == MeshTypeThing.Conductor)
		{
			return model.getFacesCount() * 4 * type.getFloatCount();
		}
		else
		{
			throw new RuntimeException("Wrong meshing type, for this stage of the project. Fix the code here. Type: " + type.name());
		}
	}
	
	@Override
	public int getWholeMeshEntryICount(MeshTypeThing type)
	{
		if(type == MeshTypeThing.Conductor)
		{
			return model.getFacesCount() * 4 * (2 * 3);
		}
		else
		{
			throw new RuntimeException("Wrong meshing type, for this stage of the project. Fix the code here. Type: " + type.name());
		}
	}
	
	@Override
	public void insertMeshData(float[] vertices, ModelHolder.IntHolder verticesOffset, int[] indices, ModelHolder.IntHolder indicesOffset, ModelHolder.IntHolder vertexCounter, MeshTypeThing type)
	{
		if(type == MeshTypeThing.Conductor)
		{
			model.generateMeshEntry(this, vertices, verticesOffset, indices, indicesOffset, vertexCounter, null, position, rotation, getParent().getModelHolder().getPlacementOffset(), type);
		}
		else
		{
			throw new RuntimeException("Wrong meshing type, for this stage of the project. Fix the code here. Type: " + type.name());
		}
	}
	
	public Vector3 getConnectionPoint()
	{
		//TODO: VERY ungeneric, to be fixed!!! Btw also wrong for at least displays.
		Vector3 connectionOffset;
		if(model instanceof CubeOpenRotated)
		{
			Vector3 directionV = ((CubeOpenRotated) model).getDirection().asVector();
			connectionOffset = new Vector3(
					directionV.getX() * model.getSize().getX(),
					directionV.getY() * model.getSize().getY(),
					directionV.getZ() * model.getSize().getZ()).multiply(-0.8);
			
			Quaternion rotation = ((CubeOpenRotated) model).getRotation();
			
			return getPosition()
					.add(getParent().getRotation().inverse().multiply(
							rotation.multiply(
									getModel().getPosition()
											.add(connectionOffset))
									.add(getParent().getModelHolder().getPlacementOffset())));
		}
		else if(model instanceof CubeOpen)
		{
			Vector3 directionV = ((CubeOpen) model).getDirection().asVector();
			connectionOffset = new Vector3(
					directionV.getX() * model.getSize().getX(),
					directionV.getY() * model.getSize().getY(),
					directionV.getZ() * model.getSize().getZ()).multiply(-0.8);
		}
		else
		{
			connectionOffset = new Vector3(0, model.getSize().getY() * 0.8, 0);
		}
		
		return getPosition().add(getParent().getRotation().inverse().multiply(getModel().getPosition().add(connectionOffset).add(getParent().getModelHolder().getPlacementOffset())));
	}
	
	public void remove(Wire wire)
	{
		wires.remove(wire);
	}
	
	@Override
	public void leftClicked(SimulationManager simulation)
	{
		if(cluster instanceof SourceCluster)
		{
			System.out.println("Source#" + cluster.hashCode() + " Drains: " + ((SourceCluster) cluster).getDrains().size() + " Active: " + cluster.isActive());
		}
		else
		{
			System.out.println("Drain#" + cluster.hashCode() + " Sources: " + ((InheritingCluster) cluster).getSources().size() + " Active: " + cluster.isActive());
		}
	}
}
