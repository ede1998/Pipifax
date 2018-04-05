package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.expressions.*;

public class PrintVisitor extends Visitor
{
	private String m_program = "";
	private int m_indentLevel = 0;
	
	public String getProgram()
	{
		return m_program;
	}
	
	public void visit(AssignmentNode n)
	{
		n.accept(this);
	}
	
	public void visit(FunctionNode n)
	{
		n.accept(this);
	}
	
	public void visit(IfNode n)
	{
		n.accept(this);
	}
	
	public void visit(ParameterNode n)
	{
		n.accept(this);
	}
	
	public void visit(ParameterTypeNode n)
	{
		n.accept(this);
	}
	
	public void visit(ProgramNode n)
	{
		for (Node tmp: n.getNodes())
		{
			tmp.accept(this);
		}
	}
	
	public void visit(VariableNode n)
	{
		n.accept(this);
	}
	
	public void visit(WhileNode n)
	{
		n.accept(this);
	}
	
	public void visit(AdditionNode n)
	{
		n.accept(this);
	}
	
	public void visit(AndNode n)
	{
		n.accept(this);
	}
	
	public void visit(CallNode n)
	{
		n.accept(this);
	}
	
	public void visit(ComparisonExpressionNode n)
	{
		n.accept(this);
	}
	
	public void visit(DivisionNode n)
	{
		n.accept(this);
	}
	
	public void visit(DoubleLiteralNode n)
	{
		n.accept(this);
	}
	
	public void visit(EqualsNode n)
	{
		n.accept(this);
	}
	
	public void visit(GreaterNode n)
	{
		n.accept(this);
	}

	public void visit(GreaterOrEqualsNode n)
	{
		n.accept(this);
	}

	public void visit(IntegerLiteralNode n)
	{
		n.accept(this);
	}
	
	public void visit(LessNode n)
	{
		n.accept(this);
	}
	
	public void visit(LessOrEqualsNode n)
	{
		n.accept(this);
	}
	
	public void visit(LValueNode n)
	{
		n.accept(this);
	}
	
	public void visit(MultiplicationNode n)
	{
		n.accept(this);
	}
	
	public void visit(NegationNode n)
	{
		n.accept(this);
	}
	
	public void visit(NotEqualsNode n)
	{
		n.accept(this);
	}
	
	public void visit(NotNode n)
	{
		n.accept(this);
	}
	
	public void visit(OrNode n)
	{
		n.accept(this);
	}
	
	public void visit(StringCompareNode n)
	{
		n.accept(this);
	}
	
	public void visit(StringLiteralNode n)
	{
		n.accept(this);
	}
	
	public void visit(SubtractionNode n)
	{
		n.accept(this);
	}
}
