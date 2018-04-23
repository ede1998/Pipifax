package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import me.erikhennig.pipifax.visitors.Visitor;

public class ClassNode extends TypeDefinitionNode
{
	private LinkedHashMap<String, ClassDataComponentNode> m_members = new LinkedHashMap<>();
	private LinkedHashMap<String, ClassFunctionComponentNode> m_functions = new LinkedHashMap<>();
	private ClassNode m_parentClass;
	private String m_parent;

	public ClassNode(String name, String parent)
	{
		super(name);
		m_parent = parent;
	}

	@Override
	public void accept(Visitor v)
	{
		v.visit(this);
	}

	public boolean add(String name, ClassDataComponentNode t)
	{
		t.setParent(this);
		return m_members.put(name, t) != null;
	}

	public boolean add(String name, ClassFunctionComponentNode t)
	{
		t.setParent(this);
		return m_functions.put(name, t) != null;
	}

	public ClassDataComponentNode findMember(String name)
	{
		ClassDataComponentNode cdcn = m_members.get(name);
		if (m_parentClass != null)
			if (cdcn == null)
			{
				return m_parentClass.findMember(name);
			}
		return cdcn;
	}

	public ClassFunctionComponentNode findFunction(String name)
	{
		ClassFunctionComponentNode cfcn = m_functions.get(name);
		if (m_parentClass != null)
			if (cfcn == null)
			{
				return m_parentClass.findFunction(name);
			}
		return cfcn;
	}

	public LinkedHashMap<String, ClassDataComponentNode> getMembers()
	{
		return m_members;
	}

	public LinkedHashMap<String, ClassFunctionComponentNode> getFunctions()
	{
		return m_functions;
	}

	public ArrayList<ClassDataComponentNode> getAllMembers()
	{
		ArrayList<ClassDataComponentNode> al = new ArrayList<>();
		for (Entry<String, ClassDataComponentNode> pair : m_members.entrySet())
		{
			al.add(pair.getValue());
		}
		if (m_parentClass != null)
			al.addAll(m_parentClass.getAllMembers());
		return al;
	}

	public ArrayList<ClassFunctionComponentNode> getAllFunctions()
	{
		ArrayList<ClassFunctionComponentNode> al = new ArrayList<>();
		for (Entry<String, ClassFunctionComponentNode> pair : m_functions.entrySet())
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
}
