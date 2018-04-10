package me.erikhennig.pipifax.visitors;

import java.util.ArrayList;
import java.util.Iterator;

import me.erikhennig.pipifax.nodes.AssignmentNode;
import me.erikhennig.pipifax.nodes.BlockNode;
import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ParameterNode;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.types.TypeNode;

public class TypeCheckingVisitor extends Visitor
{
	@Override
	public void visit(BinaryExpressionNode n)
	{
		super.visit(n);
		
		TypeNode lefttype = n.getLeftSide().getType();
		TypeNode righttype = n.getRightSide().getType();
		
		boolean retVal = lefttype.checkType(righttype);
		
		switch (n.getOperation())
		{
		case ADDITION:case SUBTRACTION:case MULTIPLICATION:case DIVISION:
			retVal &= lefttype.checkType(TypeNode.getDouble())
					|| lefttype.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(lefttype);
			break;
		case MODULO:case AND:case OR:
			retVal &= lefttype.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(lefttype);
			break;
		case EQUALS:case NOTEQUALS:case LESS:case LESSOREQUALS:case GREATER:case GREATEROREQUALS:
			retVal &= lefttype.checkType(TypeNode.getDouble())
					|| lefttype.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(TypeNode.getInt());
			break;
		case STRINGCOMPARE:
			retVal &= lefttype.checkType(TypeNode.getString());
			if (retVal)
				n.setType(TypeNode.getInt());
			break;
		case CONCATENATION:
			retVal &= lefttype.checkType(TypeNode.getString());
			if (retVal)
				n.setType(TypeNode.getString());
			break;
		}
		
		if (!retVal)
			System.err.println("Type Check Error: Binary Expression can't take those types");
	}

	@Override
	public void visit(CallNode n)
	{
		super.visit(n);
		
		FunctionNode func = n.getFunction();
		ArrayList<ParameterNode> parameters = func.getParameterList();
		ArrayList<ExpressionNode> arguments = n.getArguments();
		
		
		n.setType(func.getReturnVariable().getType());

		boolean areValidArgs = parameters.size() == arguments.size();
		
		Iterator<ExpressionNode> exp = arguments.iterator();
		Iterator<ParameterNode> param = parameters.iterator();
        if (areValidArgs)
        	for (; exp.hasNext();)
        	{
        		areValidArgs &= param.next().getType().checkType(exp.next().getType());
        	}

		if (!areValidArgs)
			System.err.println("Type Check Error: Invalid arguments for function call");
	}
	
	

	@Override
	public void visit(AssignmentNode n)
	{
		super.visit(n);
		if (!n.getSource().getType().checkType(n.getDestination().getType()))
			System.err.println("Type Check Error: Conflicting types in Assignment");
	}

	@Override
	public void visit(UnaryExpressionNode n)
	{
		super.visit(n);
		
		boolean retVal = false;
		TypeNode type = n.getOperand().getType();
		
		switch (n.getOperation())
		{
		case INTCAST:
			retVal = type.checkType(TypeNode.getInt())
			   || type.checkType(TypeNode.getDouble());
			if (retVal)
				n.setType(TypeNode.getInt());
		case DOUBLECAST:retVal = type.checkType(TypeNode.getInt())
				   || type.checkType(TypeNode.getDouble());
				if (retVal)
					n.setType(TypeNode.getDouble());
		case NEGATION:
			retVal = type.checkType(TypeNode.getInt())
			   || type.checkType(TypeNode.getDouble());
			if (retVal)
				n.setType(type);
		case NOT:
			retVal = type.checkType(TypeNode.getInt());
			if (retVal)
				n.setType(TypeNode.getInt());
		}
		
		if (!retVal)
			System.err.println("Type Check Error: Unary Expression can't take this type");
	}

	@Override
	public void visit(LValueNode n)
	{
		super.visit(n);
		
		boolean retVal = true;
		VariableNode vn = n.getVariable();
		TypeNode vnt = vn.getType();
		TypeNode tn = n.getType();
		
		TypeNode tn;
		int lvalDimensions = n.getOffsets().size();
		for (int i = 0; i < lvalDimensions; i++)
		{
			
		}
		
		int varDimensions = vn.getDimensions().size();
		if (lvalDimensions <= varDimensions)
		{
			m_type = new TypeNode(m_variable.getType(), varDimensions - lvalDimensions);
		} else
			return false;

		for (ExpressionNode en : n.getOffsets())
		{
			if (!en.getType().checkType(TypeNode.getInt()))
				retVal = false;
		}

		if (!retVal)
			System.err.println("Type Check Error: LValue type error");
	}

	@Override
	public void visit(WhileNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			System.err.println("Type Check Error: While Node needs int type");
	}

	@Override
	public void visit(IfNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			System.err.println("Type Check Error: If Node needs int type");
	}

	@Override
	public void visit(ForNode n)
	{
		super.visit(n);
		TypeNode type = n.getCondition().getType();
		boolean retVal = type.checkType(TypeNode.getInt());
		if (!retVal)
			System.err.println("Type Check Error: For Node needs int type");
	}

	@Override
	public void visit(SwitchNode n)
	{
		super.visit(n);
		
		TypeNode type = n.getCondition().getType();
		ArrayList<Node> nodes = n.getStatements().getStatements();
		boolean retVal = type.checkType(TypeNode.getInt())
				|| type.checkType(TypeNode.getDouble());
		for (Iterator<Node> iter = nodes.iterator(); iter.hasNext();)
		{
			TypeNode casecondtype = ((CaseNode) iter.next()).getCondition().getType();
			retVal &= type.checkType(casecondtype);
		}
		
		if (!retVal)
			System.err.println("Type Check Error: Switch Node needs int type");
	}
}
