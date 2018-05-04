package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.controls.CaseNode;
import me.erikhennig.pipifax.nodes.controls.DoWhileNode;
import me.erikhennig.pipifax.nodes.controls.ForNode;
import me.erikhennig.pipifax.nodes.controls.IfNode;
import me.erikhennig.pipifax.nodes.controls.SwitchNode;
import me.erikhennig.pipifax.nodes.controls.WhileNode;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.values.ArrayAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.CallNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassDataAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassFunctionAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.NewNode;
import me.erikhennig.pipifax.nodes.expressions.values.StructAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.VariableAccessNode;
import me.erikhennig.pipifax.nodes.types.BaseUnits;
import me.erikhennig.pipifax.nodes.types.CustomTypeNode;
import me.erikhennig.pipifax.nodes.types.DoubleTypeNode;
import me.erikhennig.pipifax.nodes.types.IntTypeNode;
import me.erikhennig.pipifax.nodes.types.RefTypeNode;
import me.erikhennig.pipifax.nodes.types.SizedArrayTypeNode;
import me.erikhennig.pipifax.nodes.types.StringTypeNode;
import me.erikhennig.pipifax.nodes.types.UnitNode;
import me.erikhennig.pipifax.nodes.types.UnsizedArrayTypeNode;
import me.erikhennig.pipifax.nodes.types.VoidTypeNode;

public class PrintVisitor extends Visitor
{
	@Override
	protected String getName()
	{
		return "printing";
	}

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

	public void visit(AssignmentNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp();
		n.getDestination().accept(this);
		m_program += " = ";
		n.getSource().accept(this);
		m_program += "\n";
		m_indentLevel--;
	}

	public void visit(FunctionNode n) throws VisitorException
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
		m_indentLevel--;
		n.getStatements().accept(this);
		m_indentLevel--;
	}

	@Override
	public void visit(DoubleTypeNode n) throws VisitorException
	{
		m_program += "double";
		super.visit(n);
	}

	@Override
	public void visit(UnitDefinitionNode n) throws VisitorException {
		m_program += "unit " + n.getName() + " :->";
		super.visit(n);
		m_program += "\n";
	}
	
	@Override
	public void visit(UnitNode n) throws VisitorException {
		m_program += " [" + n.getCoefficient();
		for (BaseUnits bu: n.getTop())
		{
			m_program += " * " + bu.name();
		}
		for (String u: n.getTopStr())
			m_program += " * " + u;
		for (BaseUnits bu : n.getBottom())
		{
			m_program += " / " + bu.name();
		}
		for (String u: n.getBottomStr())
			m_program += " / " + u;
		m_program += "]";
	}
	
	@Override
	public void visit(IntTypeNode n) throws VisitorException
	{
		m_program += "int";
	}

	@Override
	public void visit(StringTypeNode n) throws VisitorException
	{
		m_program += "string";
	}

	@Override
	public void visit(VoidTypeNode n) throws VisitorException
	{
		m_program += "void";
	}

	@Override
	public void visit(CustomTypeNode customTypeNode)
	{
		m_program += customTypeNode.getName();
	}

	@Override
	public void visit(RefTypeNode n) throws VisitorException
	{
		m_program += "*";
		super.visit(n);
	}

	@Override
	public void visit(SizedArrayTypeNode n) throws VisitorException
	{
		m_program += "[";
		m_program += n.getSize();
		m_program += "]";
		super.visit(n);
	}

	@Override
	public void visit(UnsizedArrayTypeNode n) throws VisitorException
	{
		m_program += "[]";
		super.visit(n);
	}

	@Override
	public void visit(BlockNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + "{\n";
		super.visit(n);
		m_program += genSp() + "}\n";
		m_indentLevel--;
	}

	public void visit(IfNode n) throws VisitorException
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

	public void visit(ParameterNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + n.getName() + " : ";
		n.getType().accept(this);
		m_program += "\n";
		m_indentLevel--;
	}

	public void visit(ProgramNode n) throws VisitorException
	{
		for (Node tmp : n.getNodes())
		{
			tmp.accept(this);
		}
	}

	public void visit(VariableNode n) throws VisitorException
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

	public void visit(WhileNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + "While ";
		n.getCondition().accept(this);
		m_program += "\n";
		n.getStatements().accept(this);
		m_indentLevel--;
	}

	@Override
	public void visit(DoWhileNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + "DoWhile\n";
		n.getStatements().accept(this);
		m_program += genSp() + "Condition: ";
		n.getCondition().accept(this);
		m_program += "\n";
		m_indentLevel--;
	}

	public void visit(ForNode n) throws VisitorException
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

	public void visit(SwitchNode n) throws VisitorException
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

	public void visit(CaseNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + "Case: ";
		n.getCondition().accept(this);
		m_program += "\n";
		n.getStatements().accept(this);
		m_indentLevel--;
	}

	public void visit(CallNode n) throws VisitorException
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
	public void visit(BinaryExpressionNode n) throws VisitorException
	{
		String symbol = " " + n.stringify() + " ";
		n.getLeftSide().accept(this);
		m_program += symbol;
		n.getRightSide().accept(this);
	}

	@Override
	public void visit(UnaryExpressionNode n) throws VisitorException
	{
		m_program += " " + n.stringify() + " ";
		super.visit(n);
	}

	public void visit(DoubleLiteralNode n) throws VisitorException
	{
		m_program += n.getValue();
		super.visit(n);
	}

	public void visit(IntegerLiteralNode n) throws VisitorException
	{
		m_program += n.getValue();
	}

	@Override
	public void visit(VariableAccessNode n) throws VisitorException
	{
		m_program += n.getName();
	}

	@Override
	public void visit(ArrayAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
		m_program += "[";
		n.getOffset().accept(this);
		m_program += "]";
	}

	@Override
	public void visit(StructAccessNode n) throws VisitorException
	{
		super.visit(n);
		m_program += "." + n.getName();
	}

	@Override
	public void visit(ClassDataAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
		m_program += "->" + n.getName();
	}

	@Override
	public void visit(ClassFunctionAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
		m_program += "->";
		n.getCall().accept(this);
	}

	public void visit(StringLiteralNode n) throws VisitorException
	{
		m_program += n.getValue();
	}

	@Override
	public void visit(StructNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += "Struct: " + n.getName() + "\n";
		super.visit(n);
		m_indentLevel--;
	}

	@Override
	public void visit(StructComponentNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + n.getName() + " : ";
		super.visit(n);
		m_program += "\n";
		m_indentLevel--;
	}

	@Override
	public void visit(ClassNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += "Class: " + n.getName();
		if (!n.getParent().isEmpty())
			m_program += " : " + n.getParent();
		m_program += "\n";
		super.visit(n);
		m_indentLevel--;
	}

	private static String convert(Visibility v)
	{
		switch (v)
		{
		case PRIVATE:
			return "private";
		case PUBLIC:
			return "public";
		case PROTECTED:
			return "protected";
		}
		return "";
	}

	@Override
	public void visit(ClassFieldNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + convert(n.getVisibility()) + " " + n.getName() + " : ";
		super.visit(n);
		m_program += "\n";
		m_indentLevel--;
	}

	@Override
	public void visit(ClassFunctionNode n) throws VisitorException
	{
		m_indentLevel++;
		m_program += genSp() + convert(n.getVisibility()) + " ";
		super.visit(n);
		m_program += "\n";
		m_indentLevel--;
	}
	
	@Override
	public void visit(NewNode n) throws VisitorException
	{
		
		m_program += "New<<<";
		super.visit(n);
		m_program += ">>>";
	}
	
	@Override
	public void visit(DeleteNode n) throws VisitorException
	{
		m_program += genSp() + "Delete: ";
		n.getDestructorAccess().getBase().accept(this);
		m_program += "\n";
	}
}
