package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nameresolution.Scope;
import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.IfNode;
import me.erikhennig.pipifax.nodes.ParameterNode;
import me.erikhennig.pipifax.nodes.ProgramNode;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.nodes.WhileNode;
import me.erikhennig.pipifax.nodes.expressions.CallNode;
import me.erikhennig.pipifax.nodes.expressions.LValueNode;

public class NameResolutionVisitor extends Visitor
{
	private Scope m_currentScope = new Scope();
	private boolean m_isGlobalIteration = true; // used for duplicate traversal to lookup global names before they are
												// defined

	@Override
	public void visit(ProgramNode n)
	{
		m_isGlobalIteration = true;
		super.visit(n);
		m_isGlobalIteration = false;
		super.visit(n);
	}

	@Override
	public void visit(CallNode n)
	{
		FunctionNode fn = m_currentScope.getFunction(n.getName());
		if (fn == null)
			System.err.println("Name lookup failed for function " + n.getName());
		n.setFunction(fn);
		super.visit(n);
	}

	@Override
	public void visit(FunctionNode n)
	{
		if (m_isGlobalIteration)
		{
			if (!m_currentScope.registerFunction(n))
				System.err.println("Name already defined for function " + n.getName());
		} else
		{
			m_currentScope = m_currentScope.enterScope();
			super.visit(n);
			m_currentScope = m_currentScope.leaveScope();
		}
	}

	@Override
	public void visit(LValueNode n)
	{
		VariableNode vn = m_currentScope.getVariable(n.getName());
		if (vn == null)
			System.err.println("Name lookup failed for variable " + n.getName());
		n.setVariable(vn);
		super.visit(n);
	}

	@Override
	public void visit(ParameterNode n)
	{
		if (!m_currentScope.registerVariable(n))
			System.err.println("Name already defined for parameter " + n.getName());
	}

	@Override
	public void visit(VariableNode n)
	{
		if (m_isGlobalIteration)
			return;
		if (!m_currentScope.registerVariable(n))
			System.err.println("Name already defined for variable " + n.getName());
	}

	@Override
	public void visit(IfNode n)
	{
		m_currentScope = m_currentScope.enterScope();
		super.visit(n);
		m_currentScope = m_currentScope.leaveScope();
	}

	@Override
	public void visit(WhileNode n)
	{
		m_currentScope = m_currentScope.enterScope();
		super.visit(n);
		m_currentScope = m_currentScope.leaveScope();
	}
}
