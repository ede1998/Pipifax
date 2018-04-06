package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.visitors.Visitor;

public class FunctionNode extends Node
{

	private String m_name;
	private TypeNode m_returnType;
	private ArrayList<ParameterNode> m_parameterList = new ArrayList<>();
	private ArrayList<Node> m_statements = new ArrayList<>();

	public FunctionNode(TypeNode retType, String name)
	{
		m_returnType = retType;
		m_name = name;
	}

	public void addParameter(ParameterNode param)
	{
		m_parameterList.add(param);
	}

	public void addStatement(Node n)
	{
		if (n != null)
			m_statements.add(n);
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public String getName()
	{
		return m_name;
	}

	public TypeNode getReturnType()
	{
		return m_returnType;
	}

	public ArrayList<ParameterNode> getParameterList()
	{
		return m_parameterList;
	}

	public ArrayList<Node> getStatements()
	{
		return m_statements;
	}
}
