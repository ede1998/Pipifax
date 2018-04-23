package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.nodes.types.TypeNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class FunctionNode extends NamedNode
{
	private VariableNode m_returnVariable = null;
	private ArrayList<ParameterNode> m_parameterList = new ArrayList<>();
	private BlockNode m_statements;
	private ClassFunctionComponentNode m_parent = null;

	public FunctionNode(TypeNode retType, String name, BlockNode bn)
	{
		super(name);
		m_returnVariable = new VariableNode(name, retType, null);
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

	public ClassFunctionComponentNode getParent()
	{
		return m_parent;
	}

	void setParent(ClassFunctionComponentNode parent)
	{
		m_parent = parent;
	}
}
