package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.controls.CaseNode;
import me.erikhennig.pipifax.nodes.controls.ForNode;
import me.erikhennig.pipifax.nodes.controls.IfNode;
import me.erikhennig.pipifax.nodes.controls.SwitchNode;
import me.erikhennig.pipifax.nodes.controls.WhileNode;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.lvalues.ArrayAccessNode;
import me.erikhennig.pipifax.nodes.expressions.lvalues.StructAccessNode;
import me.erikhennig.pipifax.nodes.expressions.lvalues.VariableAccessNode;
import me.erikhennig.pipifax.nodes.types.CustomTypeNode;
import me.erikhennig.pipifax.nodes.types.DoubleTypeNode;
import me.erikhennig.pipifax.nodes.types.IntTypeNode;
import me.erikhennig.pipifax.nodes.types.RefTypeNode;
import me.erikhennig.pipifax.nodes.types.SizedArrayTypeNode;
import me.erikhennig.pipifax.nodes.types.StringTypeNode;
import me.erikhennig.pipifax.nodes.types.UnsizedArrayTypeNode;
import me.erikhennig.pipifax.nodes.types.VoidTypeNode;

public class PrintVisitor extends Visitor
{
	private String m_program = "";
	private int m_indentLevel = -1;

	private static final int INDENT_MULTIPLIER = 2;

	private String genSp()
	{
		String s = "";
		for (int i = 0; i < m_indentLevel * INDENT_MULTIPLIER; i++)
			s += " ";
		return s;
	}

	public String getProgram()
	{
		return m_program;
	}

	public void visit(AssignmentNode n)
	{
		m_indentLevel++;
		m_program += genSp();
		n.getDestination().accept(this);
		m_program += " = ";
		n.getSource().accept(this);
		m_program += "\n";
		m_indentLevel--;
	}

	public void visit(FunctionNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "Function " + n.getName() + " : ";
		n.getReturnVariable().getType().accept(this);
		m_program += "\n";

		m_indentLevel++;
		if (!n.getParameterList().isEmpty())
		{
			m_program += genSp() + "Parameters\n";
			for (ParameterNode pn : n.getParameterList())
			{
				pn.accept(this);
			}
		}
		m_program += genSp() + "Statements\n";
		n.getStatements().accept(this);
		m_indentLevel--;
		m_indentLevel--;
	}

	@Override
	public void visit(DoubleTypeNode n)
	{
		m_program += "double";
	}

	@Override
	public void visit(IntTypeNode n)
	{
		m_program += "int";
	}

	@Override
	public void visit(StringTypeNode n)
	{
		m_program += "string";
	}

	@Override
	public void visit(VoidTypeNode n)
	{
		m_program += "void";
	}

	@Override
	public void visit(CustomTypeNode customTypeNode)
	{
		m_program += customTypeNode.getName();
	}

	@Override
	public void visit(RefTypeNode n)
	{
		m_program += "*";
		super.visit(n);
	}

	@Override
	public void visit(SizedArrayTypeNode n)
	{
		m_program += "[";
		m_program += n.getSize();
		m_program += "]";
		super.visit(n);
	}

	@Override
	public void visit(UnsizedArrayTypeNode n)
	{
		m_program += "[]";
		super.visit(n);
	}

	@Override
	public void visit(BlockNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "{\n";
		super.visit(n);
		m_program += genSp() + "}\n";
		m_indentLevel--;
	}

	public void visit(IfNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "If ";
		n.getCondition().accept(this);
		m_program += "\n";
		n.getStatements().accept(this);
		m_program += (!n.getElseStatements().getStatements().isEmpty()) ? genSp() + "Else\n" : "";
		if (!n.getElseStatements().getStatements().isEmpty())
			n.getElseStatements().accept(this);
		m_indentLevel--;
	}

	public void visit(ParameterNode n)
	{
		m_indentLevel++;
		m_program += genSp() + n.getName() + " : ";
		n.getType().accept(this);
		m_program += "\n";
		m_indentLevel--;
	}

	public void visit(ProgramNode n)
	{
		for (Node tmp : n.getNodes())
		{
			tmp.accept(this);
		}
	}

	public void visit(VariableNode n)
	{
		m_indentLevel++;
		m_program += genSp() + n.getName() + " : ";
		n.getType().accept(this);
		if (n.getExpression() != null)
		{
			m_program += " = ";
			n.getExpression().accept(this);
		}
		m_program += "\n";
		m_indentLevel--;
	}

	public void visit(WhileNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "While ";
		n.getCondition().accept(this);
		m_program += "\n";
		n.getStatements().accept(this);
		m_indentLevel--;
	}

	public void visit(ForNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "For ";
		n.getCondition().accept(this);
		m_program += "\n";
		if (n.getInitialAssignment() != null)
			n.getInitialAssignment().accept(this);
		if (n.getLoopedAssignment() != null)
			n.getLoopedAssignment().accept(this);
		m_indentLevel++;
		m_program += genSp() + "Statements:\n";
		n.getStatements().accept(this);
		m_indentLevel--;
		m_indentLevel--;
	}

	public void visit(SwitchNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "Switch ";
		n.getCondition().accept(this);
		m_program += "\n";

		n.getStatements().accept(this);

		m_indentLevel++;
		m_program += (!n.getDefaultStatements().getStatements().isEmpty()) ? genSp() + "Default\n" : "";
		if (!n.getDefaultStatements().getStatements().isEmpty())
			n.getDefaultStatements().accept(this);
		m_indentLevel--;
		m_indentLevel--;
	}

	public void visit(CaseNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "Case: ";
		n.getCondition().accept(this);
		m_program += "\n";
		n.getStatements().accept(this);
		m_indentLevel--;
	}

	public void visit(CallNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "FunctionCall " + n.getName() + "\n";
		m_indentLevel++;
		for (ExpressionNode en : n.getArguments())
		{
			m_program += genSp();
			en.accept(this);
			m_program += "\n";
		}

		m_indentLevel--;
		m_indentLevel--;
	}

	@Override
	public void visit(BinaryExpressionNode n)
	{
		String symbol = " " + n.getOperationAsString() + " ";
		n.getLeftSide().accept(this);
		m_program += symbol;
		n.getRightSide().accept(this);
	}

	@Override
	public void visit(UnaryExpressionNode n)
	{
		switch (n.getOperation())
		{
		case INTCAST:
			m_program += " (int) ";
			break;
		case DOUBLECAST:
			m_program += " (double) ";
			break;
		case NEGATION:
			m_program += " - ";
			break;
		case NOT:
			m_program += " ! ";
			break;
		}
		super.visit(n);
	}

	public void visit(DoubleLiteralNode n)
	{
		m_program += n.getValue();
	}

	public void visit(IntegerLiteralNode n)
	{
		m_program += n.getValue();
	}

	@Override
	public void visit(VariableAccessNode n) {
		m_program += n.getName();
	}
	
	@Override
	public void visit(ArrayAccessNode n) {
		super.visit(n);
		m_program += "[";
		n.getOffset().accept(this);
		m_program += "]";
	}
	
	@Override
	public void visit(StructAccessNode n) {
		super.visit(n);
		m_program += "." + n.getName();
	}

	public void visit(StringLiteralNode n)
	{
		m_program += n.getValue();
	}

	@Override
	public void visit(StructNode n)
	{
		m_indentLevel++;
		m_program += "Struct: " + n.getName() + "\n";
		super.visit(n);
		m_indentLevel--;
	}
	
	@Override
	public void visit(StructComponentNode n) {
		m_indentLevel++;
		m_program += genSp() + n.getName() + " : ";
		super.visit(n);
		m_program += "\n";
		m_indentLevel--;
	}
}
