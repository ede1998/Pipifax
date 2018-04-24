package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class ClassNode extends TypeDefinitionNode
{
	private LinkedHashMap<String, ClassFieldNode> m_members = new LinkedHashMap<>();
	private LinkedHashMap<String, ClassFunctionNode> m_functions = new LinkedHashMap<>();
	private ClassNode m_parentClass;
	private String m_parent;

	public ClassNode(String name, String parent)
	{
		super(name);
		m_parent = parent;
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public boolean add(String name, ClassFieldNode t)
	{
		t.setParent(this);
		return m_members.put(name, t) != null;
	}

	public boolean add(String name, ClassFunctionNode t)
	{
		t.setParent(this);
		return m_functions.put(name, t) != null;
	}

	public ClassFieldNode findMember(String name)
	{
		ClassFieldNode cdcn = m_members.get(name);
		if (m_parentClass != null)
			if (cdcn == null)
			{
				return m_parentClass.findMember(name);
			}
		return cdcn;
	}

	public ClassFunctionNode findFunction(String name)
	{
		ClassFunctionNode cfcn = m_functions.get(name);
		if (m_parentClass != null)
			if (cfcn == null)
			{
				return m_parentClass.findFunction(name);
			}
		return cfcn;
	}

	public LinkedHashMap<String, ClassFieldNode> getMembers()
	{
		return m_members;
	}

	public LinkedHashMap<String, ClassFunctionNode> getFunctions()
	{
		return m_functions;
	}

	public ArrayList<ClassFieldNode> getAllMembers()
	{
		ArrayList<ClassFieldNode> al = new ArrayList<>();
		for (Entry<String, ClassFieldNode> pair : m_members.entrySet())
		{
			al.add(pair.getValue());
		}
		if (m_parentClass != null)
			al.addAll(m_parentClass.getAllMembers());
		return al;
	}

	public ArrayList<ClassFunctionNode> getAllFunctions()
	{
		ArrayList<ClassFunctionNode> al = new ArrayList<>();
		for (Entry<String, ClassFunctionNode> pair : m_functions.entrySet())
		{
			al.add(pair.getValue());
		}
		if (m_parentClass != null)
			al.addAll(m_parentClass.getAllFunctions());
		return al;
	}

	public ClassNode getParentClass()
	{
		return m_parentClass;
	}

	public void setParent(ClassNode parent)
	{
		m_parentClass = parent;
	}

	public String getParent()
	{
		return m_parent;
	}

	public boolean isPolymorphable(ClassNode cn)
	{
		boolean retVal = this == cn;
		if (m_parentClass != null)
			retVal |= (m_parentClass.isPolymorphable(cn));
		return retVal;
	}
}
