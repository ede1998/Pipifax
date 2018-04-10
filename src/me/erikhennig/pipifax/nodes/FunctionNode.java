package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class FunctionNode extends Node
{

	private String m_name;
	private VariableNode m_returnVariable = null;
	private ArrayList<ParameterNode> m_parameterList = new ArrayList<>();
	private BlockNode m_statements;

	public FunctionNode(TypeNode retType, String name, BlockNode bn)
	{
		m_returnVariable = new VariableNode(name, retType, null);
		m_name = name;
		m_statements = bn;
	}

	public void addParameter(ParameterNode param)
	{
		m_parameterList.add(param);
	}

	public void addStatement(Node n)
	{
		m_statements.addStatement(n);
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

	public VariableNode getReturnVariable()
	{
		return m_returnVariable;
	}

	public ArrayList<ParameterNode> getParameterList()
	{
		return m_parameterList;
	}

	public BlockNode getStatements()
	{
		return m_statements;
	}
}
