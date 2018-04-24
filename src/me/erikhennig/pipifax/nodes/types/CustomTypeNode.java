package me.erikhennig.pipifax.nodes.types;

import me.erikhennig.pipifax.nodes.ClassNode;
import me.erikhennig.pipifax.nodes.StructNode;
import me.erikhennig.pipifax.nodes.TypeDefinitionNode;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class CustomTypeNode extends TypeNode
{
	private TypeDefinitionNode m_typeDefinition = null;
	private String m_name;

	public CustomTypeNode(String name)
	{
		m_name = name;
	}

	@Override
	public boolean checkType(TypeNode tn)
	{
		if ((m_typeDefinition == null) || !(tn instanceof CustomTypeNode))
			return false;
		
		CustomTypeNode other = (CustomTypeNode) tn;
		
		if (m_typeDefinition instanceof StructNode)
			return checkStructType(other);
		if (m_typeDefinition instanceof ClassNode)
			return checkClassType(other);
		
		return false;
	}
		
   private boolean checkClassType(CustomTypeNode n)
   {
	   ClassNode cn = (ClassNode) m_typeDefinition;
	   if (n.m_typeDefinition instanceof ClassNode)
		   return cn.isPolymorphable((ClassNode) n.m_typeDefinition);
	   return false;
   }
   
   private boolean checkStructType(CustomTypeNode n)
   {
	   return n.m_typeDefinition == m_typeDefinition;
   }

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public String getName()
	{
		return m_name;
	}

	public TypeDefinitionNode getTypeDefinition()
	{
		return m_typeDefinition;
	}

	public void setTypeDefinition(TypeDefinitionNode typeDefinition)
	{
		m_typeDefinition = typeDefinition;
	}

}
