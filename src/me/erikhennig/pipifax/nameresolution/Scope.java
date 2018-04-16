package me.erikhennig.pipifax.nameresolution;

import java.util.Hashtable;

import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.NamedNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.StructNode;
import me.erikhennig.pipifax.nodes.VariableNode;

public class Scope
{
	private Scope m_OuterScope = null;;
	private Hashtable<String, NamedNode> m_symbols = new Hashtable<>();
	
	public Scope() {}
	public Scope enterScope()
	{
		return new Scope(this);
	}

	public Scope leaveScope()
	{
		return m_OuterScope;
	}

	public boolean register(NamedNode nn)
	{
		return m_symbols.put(nn.getName(), nn) == null;
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
	
	public StructNode getStruct(String name)
	{
		StructNode sn = fetchStruct(name);
		if ((sn == null) && (m_OuterScope != null))
			sn = m_OuterScope.getStruct(name);
		return sn;
	}

	public FunctionNode getFunction(String name)
	{
		FunctionNode fn = fetchFunction(name);
		if ((fn == null) && (m_OuterScope != null))
			fn = m_OuterScope.getFunction(name);
		return fn;
	}

	private StructNode fetchStruct(String name)
	{
		Node n = m_symbols.get(name);
		if (n instanceof StructNode)
			return (StructNode) n;
		return null;
	}
	
	private VariableNode fetchVariable(String name)
	{
		Node n = m_symbols.get(name);
		if (n instanceof VariableNode)
			return (VariableNode) n;
		return null;
	}

	private FunctionNode fetchFunction(String name)
	{
		Node n = m_symbols.get(name);
		if (n instanceof FunctionNode)
			return (FunctionNode) n;
		return null;
	}

}
