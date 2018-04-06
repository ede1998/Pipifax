package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.visitors.Visitor;

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

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public Types getBaseType()
	{
		return m_baseType;
	}

	public ArrayList<Integer> getDimensions()
	{
		return m_dimensions;
	}
}
