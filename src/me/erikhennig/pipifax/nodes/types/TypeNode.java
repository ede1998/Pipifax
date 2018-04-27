package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.nodes.Node;

public abstract class TypeNode extends Node
{
	private static IntTypeNode m_intType = new IntTypeNode();

	public static IntTypeNode getInt()
	{
		return m_intType;
	}

	public static DoubleTypeNode getDouble()
	{
		return new DoubleTypeNode();
	}

	private static StringTypeNode m_stringType = new StringTypeNode();

	public static StringTypeNode getString()
	{
		return m_stringType;
	}

	private static VoidTypeNode m_voidType = new VoidTypeNode();

	public static VoidTypeNode getVoid()
	{
		return m_voidType;
	}

	public abstract boolean checkType(TypeNode tn);
}
