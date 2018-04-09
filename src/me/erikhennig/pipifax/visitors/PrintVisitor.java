package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.expressions.*;

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

	private static String typesToString(Types t)
	{
		switch (t)
		{
		case INT:
			return "INT";
		case DOUBLE:
			return "DOUBLE";
		case STRING:
			return "STRING";
		default:
			return "";
		}
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
		if (n.getReturnVariable() != null)
		{
			n.getReturnVariable().getType().accept(this);
		} else
		{
			m_program += "void";
		}
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
		for (Node tmp : n.getStatements())
		{
			tmp.accept(this);
		}
		m_indentLevel--;
		m_indentLevel--;
	}

	public void visit(TypeNode n)
	{
		m_program += typesToString(n.getBaseType());
		for (int i : n.getDimensions())
		{
			m_program += "[" + i + "]";
		}
	}

	public void visit(IfNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "If ";
		n.getCondition().accept(this);
		m_program += "\n";
		for (Node tmp : n.getStatements())
		{
			tmp.accept(this);
		}
		m_program += (!n.getElseStatements().isEmpty()) ? genSp() + "Else\n" : "";
		for (Node tmp : n.getElseStatements())
		{
			tmp.accept(this);
		}
		m_indentLevel--;
	}

	public void visit(ParameterNode n)
	{
		m_indentLevel++;
		m_program += genSp() + n.getName() + " : ";
		m_program += (n.isReference()) ? "*" : "";
		m_program += (n.isArrayOfUnknownSize()) ? "[]" : "";
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
		m_program += "\n";
		m_indentLevel--;
	}

	public void visit(WhileNode n)
	{
		m_indentLevel++;
		m_program += genSp() + "While ";
		n.getCondition().accept(this);
		m_program += "\n";
		for (Node tmp : n.getStatements())
		{
			tmp.accept(this);
		}
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
		for (Node tmp : n.getStatements())
		{
			tmp.accept(this);
		}
		m_indentLevel--;
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
		String symbol = "";
		switch (n.getOperation())
		{
		case ADDITION:
			symbol = " + ";
			break;
		case SUBTRACTION:
			symbol = " - ";
			break;
		case MULTIPLICATION:
			symbol = " * ";
			break;
		case DIVISION:
			symbol = " / ";
			break;
		case MODULO:
			symbol = " % ";
			break;
		case EQUALS:
			symbol = " == ";
			break;
		case NOTEQUALS:
			symbol = " != ";
			break;
		case LESS:
			symbol = " < ";
			break;
		case LESSOREQUALS:
			symbol = " <= ";
			break;
		case GREATER:
			symbol = " > ";
			break;
		case GREATEROREQUALS:
			symbol = " >= ";
			break;
		case AND:
			symbol = " && ";
			break;
		case OR:
			symbol = " || ";
			break;
		case STRINGCOMPARE:
			symbol = " <=> ";
			break;
		case CONCATENATION:
			symbol = " ... ";
			break;
		}
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

	public void visit(LValueNode n)
	{
		m_program += n.getName();
		for (ExpressionNode en : n.getOffsets())
		{
			m_program += "[";
			en.accept(this);
			m_program += "]";
		}
	}

	public void visit(StringLiteralNode n)
	{
		m_program += n.getValue();
	}
}
