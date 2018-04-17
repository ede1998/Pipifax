package me.erikhennig.pipifax.nodes.expressions.lvalues;

import me.erikhennig.pipifax.nodes.StructComponentNode;
import me.erikhennig.pipifax.visitors.Visitor;

public class StructAccessNode extends LValueNode {
	private LValueNode m_base;
	private StructComponentNode m_component;
	private String m_name;
	
	public StructAccessNode(LValueNode base, String name)
	{		
		m_base = base;
		m_name = name;
	}
	
	public void setComponent(StructComponentNode comp)
	{
		m_component = comp;
	}

	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
	
	public LValueNode getBase()
	{
		return m_base;
	}
	
	public String getName()
	{
		return m_name;
	}
}
