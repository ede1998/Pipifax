package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

public class FunctionNode extends Node {

	private String m_name;
	private TypeNode m_returnType;
	private ArrayList<ParameterNode> m_parameterList = new ArrayList<>();
	private ArrayList<Node> m_statements = new ArrayList<>();
	public FunctionNode()
	{
	}
	
	public void addParameter(ParameterNode param)
	{
		m_parameterList.add(param);
	}
	
	public void addStatement(Node n)
	{
		m_statements.add(n);
	}

}
