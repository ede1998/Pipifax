package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

public class TypeNode extends Node
{
	private Types m_baseType;
	private ArrayList<Integer> m_dimensions = new ArrayList<>();

	public TypeNode(Types t)
	{
		m_baseType = t;
	}

	public void addDimension(int size)
	{
		m_dimensions.add(size);
	}
}
