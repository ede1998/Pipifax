package me.erikhennig.pipifax.nameresolution;

import java.util.Hashtable;

import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.VariableNode;

public class Scope
{
	private Scope m_OuterScope = null;;
	private Hashtable<String, VariableNode> m_variables = new Hashtable<>();
	private Hashtable<String, FunctionNode> m_functions = new Hashtable<>();
	
	public Scope() {}
	public Scope enterScope()
	{
		return new Scope(this);
	}

	public Scope leaveScope()
	{
		return m_OuterScope;
	}

	public boolean registerVariable(VariableNode vn)
	{
		return m_variables.put(vn.getName(), vn) == null;
	}

	public boolean registerFunction(FunctionNode fn)
	{
		return m_functions.put(fn.getName(), fn) == null;
	}

	public Scope getOuterScope()
	{
		return m_OuterScope;
	}

	private Scope(Scope outerScope)
	{
		m_OuterScope = outerScope;
	}

	public VariableNode getVariable(String name)
	{
		VariableNode vn = fetchVariable(name);
		if ((vn == null) && (m_OuterScope != null))
			vn = m_OuterScope.getVariable(name);
		return vn;
	}

	public FunctionNode getFunction(String name)
	{
		FunctionNode fn = fetchFunction(name);
		if ((fn == null) && (m_OuterScope != null))
			fn = m_OuterScope.getFunction(name);
		return fn;
	}

	private VariableNode fetchVariable(String name)
	{
		Node n = m_variables.get(name);
		if (n instanceof VariableNode)
			return (VariableNode) n;
		return null;
	}

	private FunctionNode fetchFunction(String name)
	{
		Node n = m_functions.get(name);
		if (n instanceof FunctionNode)
			return (FunctionNode) n;
		return null;
	}

}
