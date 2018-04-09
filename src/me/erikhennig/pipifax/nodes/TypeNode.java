package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;
import java.util.Iterator;

import me.erikhennig.pipifax.visitors.Visitor;

public class TypeNode extends Node
{
	private Types m_baseType;
	private ArrayList<Integer> m_dimensions = new ArrayList<>();

	public TypeNode(Types t)
	{
		m_baseType = t;
	}

	public TypeNode(TypeNode tn, int dimensionsToCopy)
	{
		m_baseType = tn.m_baseType;
		for (int i : tn.m_dimensions)
		{
			if (dimensionsToCopy-- <= 0)
				return;
			m_dimensions.add(i);
		}
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

	public static boolean isSameType(TypeNode tn1, TypeNode tn2)
	{
		if ((tn1 == null) || (tn2 == null))
			return false;

		boolean isSame = tn1.m_baseType == tn2.m_baseType;
		isSame &= tn1.m_dimensions.size() == tn2.m_dimensions.size();
		if (!isSame)
			return false;
		for (Iterator<Integer> iter1 = tn1.m_dimensions.iterator(), iter2 = tn2.m_dimensions.iterator(); iter1
				.hasNext();)
		{
			int i1 = iter1.next(), i2 = iter2.next();
			if ((i1 == 0) || (i2 == 0))
				continue;
			isSame &= iter1.next() == iter2.next();
		}
		return isSame;
	}
	
	private static TypeNode m_basicTypeINT = new TypeNode(Types.INT);
	private static TypeNode m_basicTypeSTRING = new TypeNode(Types.STRING);
	private	static TypeNode m_basicTypeDOUBLE = new TypeNode(Types.DOUBLE);
	
	public static boolean isSameType(TypeNode tn, Types basicType)
	{
		switch (basicType)
		{
		case INT:
			return isSameType(tn, m_basicTypeINT);
		case STRING:
			return isSameType(tn, m_basicTypeSTRING);
		case DOUBLE:
			return isSameType(tn, m_basicTypeDOUBLE);
		}
		return false;
	}
}
