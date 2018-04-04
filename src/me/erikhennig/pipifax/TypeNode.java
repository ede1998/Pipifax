package me.erikhennig.pipifax;

enum types { DOUBLE, INT, STRING };
public class TypeNode extends Node {
	private types m_baseType;
	public TypeNode(types t)
	{
		m_baseType = t;
	}
}
