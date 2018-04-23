package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nameresolution.Scope;
import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.expressions.values.CallNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassFunctionAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.VariableAccessNode;
import me.erikhennig.pipifax.nodes.types.CustomTypeNode;

public class NameResolutionVisitor extends Visitor
{
	@Override
	protected String getName()
	{
		return "name resolution";
	}

	private Scope m_currentScope;
	private boolean m_onlyAddFunctions = true; // used for duplicate traversal to lookup global function names before
												// they are defined

	public NameResolutionVisitor(Scope s)
	{
		// internal declarations can override global declarations
		m_currentScope = s.enterScope();
	}

	@Override
	public void visit(ProgramNode n)
	{
		m_onlyAddFunctions = true;
		super.visit(n);
		m_onlyAddFunctions = false;
		super.visit(n);
	}

	@Override
	public void visit(CallNode n)
	{
		FunctionNode fn = m_currentScope.getFunction(n.getName());
		if (fn == null)
			printErrorAndFail(n, "Name lookup failed for function " + n.getName());
		n.setFunction(fn);
		super.visit(n);
	}

	@Override
	public void visit(FunctionNode n)
	{
		if (m_onlyAddFunctions)
		{
			if (!m_currentScope.register(n))
				printErrorAndFail(n, "Name already defined for function " + n.getName());
		}
		else
		{
			m_currentScope = m_currentScope.enterScope();
			super.visit(n);
			m_currentScope = m_currentScope.leaveScope();
		}
	}

	@Override
	public void visit(VariableAccessNode n)
	{
		VariableNode vn = m_currentScope.getVariable(n.getName());
		if (vn == null)
			printErrorAndFail(n, "Name lookup failed for variable " + n.getName());
		n.setVariable(vn);
	}

	@Override
	public void visit(ClassFunctionAccessNode n)
	{
		n.getBase().accept(this);
	}

	@Override
	public void visit(ParameterNode n)
	{
		if (!m_currentScope.register(n))
			printErrorAndFail(n, "Name already defined for parameter " + n.getName());
	}

	@Override
	public void visit(VariableNode n)
	{
		if (m_onlyAddFunctions)
			return;
		super.visit(n);
		if (!m_currentScope.register(n))
			printErrorAndFail(n, "Name already defined for variable " + n.getName());
	}

	public void visit(BlockNode n)
	{
		m_currentScope = m_currentScope.enterScope();
		super.visit(n);
		m_currentScope = m_currentScope.leaveScope();
	}

	@Override
	public void visit(StructNode n)
	{
		if (m_onlyAddFunctions)
			return;
		super.visit(n);
		if (!m_currentScope.register(n))
			printErrorAndFail(n, "Name already defined for struct " + n.getName());
	}

	@Override
	public void visit(CustomTypeNode n)
	{
		TypeDefinitionNode tdn = m_currentScope.getTypeDefinition(n.getName());
		if (tdn == null)
			printErrorAndFail(n, "Name lookup failed for custom type " + n.getName());
		n.setTypeDefinition(tdn);
	}

	@Override
	public void visit(ClassNode n)
	{
		if (m_onlyAddFunctions)
			return;
		ClassNode parent = m_currentScope.getClass(n.getParent());
		if (!n.getParent().isEmpty() && parent == null)
			printErrorAndFail(n, "Parent class " + n.getParent() + " does  not exist.");
		n.setParent(parent);
		if (!m_currentScope.register(n))
			printErrorAndFail(n, "Name already defined for class " + n.getName());
		if (parent != null)
		{
			for (ClassDataComponentNode mem : parent.getAllMembers())
			{
				if (!m_currentScope.register(mem.getVariable()))
					printErrorAndFail(n, "Member " + mem.getName() + " already defined in parent");
			}

			for (ClassFunctionComponentNode func : parent.getAllFunctions())
			{
				if (!m_currentScope.register(func.getFunction()))
					printErrorAndFail(n, "Function " + func.getName() + " already defined in parent");
			}
		}

		m_currentScope = m_currentScope.enterScope();
		super.visit(n);
		m_currentScope = m_currentScope.leaveScope();

	}

	@Override
	public void visit(ClassDataComponentNode n)
	{
		super.visit(n);
		if (!m_currentScope.register(n.getVariable()))
			printErrorAndFail(n, "Member " + n.getName() + " already defined in class");
	}

	@Override
	public void visit(ClassFunctionComponentNode n)
	{
		if (!m_currentScope.register(n.getFunction()))
			printErrorAndFail(n, "Function " + n.getName() + " already defined in class");
		super.visit(n);
	}
}
