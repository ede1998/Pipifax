package me.erikhennig.pipifax.visitors;

import java.util.Iterator;
import java.util.Map.Entry;

import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.values.*;
import me.erikhennig.pipifax.nodes.types.*;

public class CCodeGenerationVisitor extends Visitor
{

	private String m_code = "";
	private String m_fileName;

	public CCodeGenerationVisitor(String fname)
	{
		m_fileName = fname;
	}

	public String getCode()
	{
		return m_code;
	}

	private void prefill()
	{
		// include guard
		m_code += "#ifndef " + m_fileName.toUpperCase() + "_H\n";
		m_code += "#define " + m_fileName.toUpperCase() + "_H\n";
		// includes
		m_code += "#include <stdlib.h> //for malloc, free\n";
		m_code += "#include <string.h> //for strcmp, strcpy,...\n";
		m_code += "\n";
	}

	private void postfill()
	{
		// include guard end
		m_code += "#endif //INCLUDEGUARD\n";
	}

	@Override
	public void visit(ProgramNode n) throws VisitorException
	{
		prefill();
		// all vars, classes and structs
		// all function prototypes
		for (Node node : n.getNodes())
		{
			if (!(node instanceof FunctionNode))
			{
				node.accept(this);
			}
			else
			{
				FunctionNode fn = (FunctionNode) node;
				// print prototype
				fn.getReturnVariable().getType().accept(this);
				m_code += fn.getName() + "(";
				if (fn.getParameterList().isEmpty())
					m_code += "void";
				else
				{
					Iterator<ParameterNode> iter = fn.getParameterList().iterator();
					iter.next().accept(this);
					while (iter.hasNext())
					{
						m_code += " , ";
						iter.next().accept(this);
					}
				}
				m_code += ");\n";
			}
		}

		// all functions
		for (Node node : n.getNodes())
		{
			if (node instanceof FunctionNode)
			{
				node.accept(this);
			}
		}

		postfill();
	}

	@Override
	public void visit(AssignmentNode n) throws VisitorException
	{
		// strings
		// all other?
		n.getDestination().accept(this);
		m_code += " = ";
		n.getSource().accept(this);
	}

	@Override
	public void visit(BinaryExpressionNode n) throws VisitorException
	{
		switch (n.getOperation())
		{
		case ADDITION:
		case SUBTRACTION:
		case MULTIPLICATION:
		case DIVISION:
		case MODULO:
		case AND:
		case OR:
		case EQUALS:
		case NOTEQUALS:
		case LESS:
		case LESSOREQUALS:
		case GREATER:
		case GREATEROREQUALS:
			n.getLeftSide().accept(this);
			m_code += " " + n.stringify() + " ";
			n.getRightSide().accept(this);
			break;
		case STRINGCOMPARE:
			m_code += "strcmp(";
			n.getLeftSide().accept(this);
			m_code += " , ";
			n.getRightSide().accept(this);
			m_code += ")";
			break;
		case CONCATENATION:
			// TODO
			break;
		}

	}

	@Override
	public void visit(UnaryExpressionNode n) throws VisitorException
	{
		switch (n.getOperation())
		{
		case INTCAST:
		case DOUBLECAST:
		case NEGATION:
		case NOT:
			m_code += n.stringify() + " ";
			n.getOperand().accept(this);
			break;
		default:
			throw new VisitorException(this, n, "Invalid operation cannot be converted in visit(UnaryExpression)");
		}
		// TODO class cast
	}

	@Override
	public void visit(VariableNode n) throws VisitorException
	{
		n.getType().accept(this);
		m_code += " " + n.getName();
		if (n.getType() instanceof SizedArrayTypeNode)
		{
			m_code += " [" + ((SizedArrayTypeNode) n.getType()).getSize() + "] ";
		}
		if (n.getExpression() != null)
		{ // TODO: strings variables? free malloc -ed memory
			m_code += " = ";
			if (n.getExpression().getType().checkType(TypeNode.getString())
					&& !(n.getExpression() instanceof StringLiteralNode))
			{
				m_code += "strcpy(malloc(strlen(";
				n.getExpression().accept(this);
				m_code += ")), ";
				n.getExpression().accept(this);
			}
			else
				n.getExpression().accept(this);
		}
	}

	@Override
	public void visit(ParameterNode n) throws VisitorException
	{
		if (n.getType() instanceof RefTypeNode)
		{
			if (((RefTypeNode) n.getType()).getType() instanceof UnsizedArrayTypeNode)
			{
				((RefTypeNode) n.getType()).getType().accept(this);
			}
		}
		else
			n.getType().accept(this);
		m_code += " " + n.getName();
		if (n.getType() instanceof SizedArrayTypeNode)
		{
			m_code += " [" + ((SizedArrayTypeNode) n.getType()).getSize() + "] ";
		}
	}

	@Override
	public void visit(BlockNode n) throws VisitorException
	{
		m_code += "{\n";
		for (Node entry : n.getStatements())
		{
			entry.accept(this);
			m_code += ";\n";
		}
		m_code += "}\n";
	}

	public void visit(IntTypeNode n) throws VisitorException
	{
		m_code += " int ";
	}

	public void visit(StringTypeNode n) throws VisitorException
	{
		m_code += " char * ";
	}

	public void visit(DoubleTypeNode n) throws VisitorException
	{
		m_code += " double ";
	}

	public void visit(VoidTypeNode n) throws VisitorException
	{
		m_code += " void ";
	}

	public void visit(CustomTypeNode n) throws VisitorException
	{
		m_code += " struct " + n.getName() + " * ";
	}

	public void visit(UnsizedArrayTypeNode n) throws VisitorException
	{
		n.getType().accept(this);
		m_code += " * ";
	}

	public void visit(RefTypeNode n) throws VisitorException
	{
		n.getType().accept(this);
		m_code += " * ";
	}

	public void visit(FunctionNode n) throws VisitorException
	{
		n.getReturnVariable().getType().accept(this);
		m_code += n.getName() + "(";
		if (n.getParameterList().isEmpty())
			m_code += "void";
		else
		{
			Iterator<ParameterNode> iter = n.getParameterList().iterator();
			iter.next().accept(this);
			while (iter.hasNext())
			{
				m_code += " , ";
				iter.next().accept(this);
			}
		}

		m_code += ")\n";
		if (!n.getReturnVariable().getType().checkType(TypeNode.getVoid()))
		{
			m_code += "{\n";
			n.getReturnVariable().accept(this);
			n.getStatements().accept(this);
			m_code += "return " + n.getReturnVariable().getName() + ";\n";
			m_code += "}\n";
		}
		else 
			n.getStatements().accept(this);
	}

	public void visit(IfNode n) throws VisitorException
	{
		m_code += "if (";
		n.getCondition().accept(this);
		m_code += ")\n";
		n.getStatements().accept(this);
		m_code += "else\n";
		n.getElseStatements().accept(this);
	}

	public void visit(WhileNode n) throws VisitorException
	{
		m_code += "while (";
		n.getCondition().accept(this);
		m_code += ")\n";
		n.getStatements().accept(this);
	}

	public void visit(DoWhileNode n) throws VisitorException
	{
		m_code += "do\n";
		n.getStatements().accept(this);
		m_code += "while (";
		n.getCondition().accept(this);
		m_code += ");";
	}

	public void visit(ForNode n) throws VisitorException
	{
		m_code += "for (";
		if (n.getInitialAssignment() != null)
			n.getInitialAssignment().accept(this);
		m_code += ";";
		n.getCondition().accept(this);
		m_code += ";";
		if (n.getLoopedAssignment() != null)
			n.getLoopedAssignment().accept(this);
		m_code += ")\n";
		n.getStatements().accept(this);
	}

	public void visit(SwitchNode n) throws VisitorException
	{
		boolean isString = n.getCondition().getType().checkType(TypeNode.getString());

		for (Node node : n.getStatements().getStatements())
		{
			CaseNode cn = (CaseNode) node;
			m_code += "if (";
			if (isString)
			{
				m_code += "!strcmp(";
				n.getCondition().accept(this);
				m_code += " , ";
				cn.getCondition().accept(this);
				m_code += ")\n";
			}
			else
			{
				n.getCondition().accept(this);
				m_code += " == ";
				cn.getCondition().accept(this);
				m_code += ")\n";
			}
			cn.getStatements().accept(this);
			m_code += "else\n";
		}
		n.getDefaultStatements().accept(this);
	}

	public void visit(CallNode n) throws VisitorException
	{
		m_code += n.getName() + "(";
		if (!n.getArguments().isEmpty())
		{
			Iterator<ExpressionNode> iter = n.getArguments().iterator();
			iter.next().accept(this);
			while (iter.hasNext())
			{
				m_code += " , ";
				iter.next().accept(this);
			}
		}
		m_code += ")";
	}

	public void visit(DoubleLiteralNode n) throws VisitorException
	{
		m_code += n.getValue();
	}

	public void visit(IntegerLiteralNode n) throws VisitorException
	{
		m_code += n.getValue();
	}

	public void visit(ArrayAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
		m_code += "[";
		n.getOffset().accept(this);
		m_code += "]";
	}

	public void visit(StructAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
		m_code += "." + n.getName();
	}

	public void visit(VariableAccessNode n) throws VisitorException
	{
		m_code += n.getName();
	}

	public void visit(StringLiteralNode n) throws VisitorException
	{
		m_code += " \" " + n.getValue() + " \" ";
	}

	public void visit(StructNode n) throws VisitorException
	{
		m_code += "struct " + n.getName() + " {\n";
		super.visit(n);
		m_code += "};\n";
	}

	public void visit(StructComponentNode n) throws VisitorException
	{
		n.getType().accept(this);
		m_code += n.getName();
		if (n.getType() instanceof SizedArrayTypeNode)
		{
			m_code += " [" + ((SizedArrayTypeNode) n.getType()).getSize() + "] ";
		}
		m_code += ";\n";
	}

	public void visit(ClassFunctionNode n) throws VisitorException
	{
		n.getReturnVariable().getType().accept(this);
		m_code += n.getName() + "(";
		m_code += n.getParent().getName() + " * this";
		if (!n.getParameterList().isEmpty())
		{
			Iterator<ParameterNode> iter = n.getParameterList().iterator();
			m_code += " , ";
			iter.next().accept(this);
			while (iter.hasNext())
			{
				m_code += " , ";
				iter.next().accept(this);
			}
		}

		m_code += ")\n";

		if (!n.getReturnVariable().getType().checkType(TypeNode.getVoid()))
		{
			m_code += "{\n";
			n.getReturnVariable().accept(this);
			n.getStatements().accept(this);
			m_code += "return " + n.getReturnVariable().getName() + ";\n";
			m_code += "}\n";
		}
		else 
			n.getStatements().accept(this);
	}

	public void visit(ClassNode n) throws VisitorException
	{
		m_code += "struct " + n.getName() + " {\n";
		for (Entry<String, ClassFieldNode> entry : n.getMembers().entrySet())
			entry.getValue().accept(this);
		m_code += "};\n";
		for (Entry<String, ClassFunctionNode> entry : n.getFunctions().entrySet())
			entry.getValue().accept(this);
		// initfunction
		m_code += "struct " + n.getName() + " * new" + n.getName() + "(";
		ClassFunctionNode constructor = n.getFunctions().get(n.getName());
		if (constructor != null)
			if (!constructor.getParameterList().isEmpty())
			{
				Iterator<ParameterNode> iter = constructor.getParameterList().iterator();
				iter.next().accept(this);
				while (iter.hasNext())
				{
					m_code += " , ";
					iter.next().accept(this);
				}
			}
		m_code += ") {\n";
		m_code += "struct " + n.getName() + " * ret = malloc(sizeof(struct " + n.getName() + "));\n";
		if (constructor != null)
		{
			m_code += constructor.getName() + "(";
			if (!constructor.getParameterList().isEmpty())
			{
				Iterator<ParameterNode> iter = constructor.getParameterList().iterator();
				m_code += iter.next().getName();
				while (iter.hasNext())
				{
					m_code += " , ";
					m_code += iter.next().getName();
				}
			}
			m_code += ");";
		}
		m_code += "return ret;\n";
		m_code += "}\n";
	}

	public void visit(ClassDataAccessNode n) throws VisitorException
	{
		n.getBase().accept(this);
		m_code += "." + n.getName();
	}

	public void visit(ClassFunctionAccessNode n) throws VisitorException
	{
		m_code += n.getName() + "(";
		n.getBase().accept(this);
		if (!n.getCall().getArguments().isEmpty())
		{
			Iterator<ExpressionNode> iter = n.getCall().getArguments().iterator();
			iter.next().accept(this);
			while (iter.hasNext())
			{
				m_code += " , ";
				iter.next().accept(this);
			}
		}
		m_code += ")";
	}

	public void visit(NewNode n) throws VisitorException
	{
		// malloc call
		m_code += "new" + n.getName() + "(";
		if (!n.getArguments().isEmpty())
		{
			Iterator<ExpressionNode> iter = n.getArguments().iterator();
			iter.next().accept(this);
			while (iter.hasNext())
			{
				m_code += " , ";
				iter.next().accept(this);
			}
		}
		m_code += ")";
	}

	public void visit(DeleteNode n) throws VisitorException
	{
		n.getDestructorAccess().accept(this);
		// dealloc call
		m_code += ";\nfree(" + n.getVariableName() + ")";
	}
}
