package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nameresolution.Scope;
import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.expressions.CallNode;
import me.erikhennig.pipifax.nodes.expressions.lvalues.VariableAccessNode;
import me.erikhennig.pipifax.nodes.types.CustomTypeNode;

public class NameResolutionVisitor extends Visitor
{
	private Scope m_currentScope;
	private boolean m_onlyAddFunctions = true; // used for duplicate traversal to lookup global function names before
												// they are defined

	public NameResolutionVisitor(Scope s)
	{
		m_currentScope = s;
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
			printErrorAndFail("Name lookup failed for function " + n.getName());
		n.setFunction(fn);
		super.visit(n);
	}

	@Override
	public void visit(FunctionNode n)
	{
		if (m_onlyAddFunctions)
		{
			if (!m_currentScope.register(n))
				printErrorAndFail("Name already defined for function " + n.getName());
		} else
		{
			m_currentScope = m_currentScope.enterScope();
			super.visit(n);
			m_currentScope = m_currentScope.leaveScope();
		}
	}
	
	@Override
	public void visit(VariableAccessNode n) {
		VariableNode vn = m_currentScope.getVariable(n.getName());
		if (vn == null)
			printErrorAndFail("Name lookup failed for variable " + n.getName());
		n.setVariable(vn);
	}
	
	@Override
	public void visit(ParameterNode n)
	{
		if (!m_currentScope.register(n))
			printErrorAndFail("Name already defined for parameter " + n.getName());
	}

	@Override
	public void visit(VariableNode n)
	{
		if (m_onlyAddFunctions)
			return;
		super.visit(n);
		if (!m_currentScope.register(n))
			printErrorAndFail("Name already defined for variable " + n.getName());
	}

	public void visit(BlockNode n)
	{
		m_currentScope = m_currentScope.enterScope();
		super.visit(n);
		m_currentScope = m_currentScope.leaveScope();
	}

	@Override
	public void visit(StructNode sn)
	{
		if (m_onlyAddFunctions)
			return;
		if (!m_currentScope.register(sn))
			printErrorAndFail("Name already defined for struct " + sn.getName());
		super.visit(sn);
	}

	@Override
	public void visit(CustomTypeNode ctn)
	{
		StructNode sn = m_currentScope.getStruct(ctn.getName());
		if (sn == null)
			printErrorAndFail("Name lookup failed for struct " + ctn.getName());
		ctn.setTypeDefinition(sn);
	}
}
