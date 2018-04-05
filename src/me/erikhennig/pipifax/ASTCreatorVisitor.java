package me.erikhennig.pipifax;

import java.util.ArrayList;

import org.antlr.v4.runtime.tree.TerminalNodeImpl;

import me.erikhennig.pipifax.antlr.PipifaxBaseVisitor;
import me.erikhennig.pipifax.antlr.PipifaxParser;
import me.erikhennig.pipifax.antlr.PipifaxParser.ProgContext;
import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.expressions.*;

public class ASTCreatorVisitor extends PipifaxBaseVisitor<Node>
{

	@Override
	public Node visitProg(PipifaxParser.ProgContext ctx)
	{
		ProgramNode pn = new ProgramNode();

		for (int i = 0; i < ctx.getChildCount(); i++)
		{
			Node n = ctx.getChild(i).accept(this);
			pn.addNode(n);
		}
		return pn;
	}

	@Override
	public Node visitVardecl(PipifaxParser.VardeclContext ctx)
	{
		TypeNode t = (TypeNode) ctx.type().accept(this);
		VariableNode d = new VariableNode(ctx.ID().getText(), t);
		return d;
	}

	@Override
	public Node visitIntType(PipifaxParser.IntTypeContext ctx)
	{
		return new TypeNode(Types.INT);
	}

	@Override
	public Node visitDoubleType(PipifaxParser.DoubleTypeContext ctx)
	{
		return new TypeNode(Types.DOUBLE);
	}

	@Override
	public Node visitStringType(PipifaxParser.StringTypeContext ctx)
	{
		return new TypeNode(Types.STRING);
	}

	@Override
	public Node visitArrayType(PipifaxParser.ArrayTypeContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		tn.addDimension(Integer.parseInt(ctx.INT().getText()));
		return tn;
	}

	@Override
	public Node visitFuncdecl(PipifaxParser.FuncdeclContext ctx)
	{
		TypeNode returnType = (ctx.type() != null) ? (TypeNode) ctx.type().accept(this) : null;
		FunctionNode fn = new FunctionNode(returnType, ctx.ID().getText());
		for (PipifaxParser.ParameterContext pc : ctx.parameterlist().parameter())
		{
			ParameterNode pn = (ParameterNode) pc.accept(this);
			fn.addParameter(pn);
		}
		for (int i = 0; i < ctx.block().getChildCount(); i++)
		{
			Node n = ctx.block().getChild(i).accept(this);
			fn.addStatement(n);
		}
		return fn;
	}

	@Override
	public Node visitParameter(PipifaxParser.ParameterContext ctx)
	{
		ParameterTypeNode ptn = (ParameterTypeNode) ctx.parameter_type().accept(this);
		ParameterNode pn = new ParameterNode(ctx.ID().getText(), ptn);
		return pn;
	}

	@Override
	public Node visitTypeParameter(PipifaxParser.TypeParameterContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		ParameterTypeNode ptn = new ParameterTypeNode(tn, false, false);
		return ptn;
	}

	@Override
	public Node visitReferenceParameter(PipifaxParser.ReferenceParameterContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		ParameterTypeNode ptn = new ParameterTypeNode(tn, true, false);
		return ptn;
	}

	@Override
	public Node visitReferenceArrayParameter(PipifaxParser.ReferenceArrayParameterContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		ParameterTypeNode ptn = new ParameterTypeNode(tn, true, true);
		return ptn;
	}
	
	@Override
	public Node visitAssignmentStatement(PipifaxParser.AssignmentStatementContext ctx1)
	{
		PipifaxParser.AssignmentContext ctx = ctx1.assignment();
		
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode src = (ExpressionNode) ctx.expr().accept(this);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}
	
	@Override
	public Node visitFunctionCallStatement(PipifaxParser.FunctionCallStatementContext ctx)
	{
		return ctx.funccall().accept(this);
	}
	
	@Override
	public Node visitIfStatement(PipifaxParser.IfStatementContext ctx1)
	{
		PipifaxParser.IfstmtContext ctx = ctx1.ifstmt();
		
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		IfNode in = new IfNode(en);

		// If-Part
		if (ctx.block() != null)
			for (int i = 0; i < ctx.block().getChildCount(); i++)
			{
				Node n = ctx.block().getChild(i).accept(this);
				in.addStatement(n);
			}

		// else-Part
		if ((ctx.elsestmt() != null) && (ctx.elsestmt().block() != null))
			for (int i = 0; i < ctx.elsestmt().block().getChildCount(); i++)
			{
				in.addElseStatements(ctx.elsestmt().block().getChild(i).accept(this));
			}
		return in;
	}

	@Override
	public Node visitWhileStatement(PipifaxParser.WhileStatementContext ctx1)
	{
		PipifaxParser.WhilestmtContext ctx = ctx1.whilestmt();
		
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		WhileNode wn = new WhileNode(en);
		if (ctx.block() != null)
			for (int i = 0; i < ctx.block().getChildCount(); i++)
			{
				Node n = ctx.block().getChild(i).accept(this);
				wn.addStatement(n);
			}
		return wn;
	}

	@Override
	public Node visitLvalue(PipifaxParser.LvalueContext ctx)
	{
		ArrayList<ExpressionNode> alen = new ArrayList<>();
		if (ctx.expr() != null)
			for (int i = 0; i < ctx.expr().size(); i++)
			{
				alen.add((ExpressionNode) ctx.expr(i).accept(this));
			}
		LValueNode lvn = new LValueNode(ctx.ID().getText(), alen);
		return lvn;
	}

	@Override
	public Node visitIntLiteral(PipifaxParser.IntLiteralContext ctx)
	{
		return new IntegerLiteralNode(Integer.parseInt(ctx.INT().getText()));
	}

	@Override
	public Node visitDoubleLiteral(PipifaxParser.DoubleLiteralContext ctx)
	{
		return new DoubleLiteralNode(Double.parseDouble(ctx.DOUBLE().getText()));
	}

	@Override
	public Node visitStringLiteral(PipifaxParser.StringLiteralContext ctx)
	{
		return new StringLiteralNode(ctx.STRING().getText());
	}

	@Override
	public Node visitCall(PipifaxParser.CallContext ctx)
	{
		return ctx.funccall().accept(this);
	}
	
	@Override
	public Node visitLValueExpression(PipifaxParser.LValueExpressionContext ctx)
	{
		return ctx.lvalue().accept(this);
	}
	
	@Override
	public Node visitParentheses(PipifaxParser.ParenthesesContext ctx)
	{
		return ctx.expr().accept(this);
	}
	@Override
	public Node visitAddition(PipifaxParser.AdditionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new AdditionNode(l, r);
	}

	@Override
	public Node visitSubtraction(PipifaxParser.SubtractionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new SubtractionNode(l, r);
	}

	@Override
	public Node visitMultiplication(PipifaxParser.MultiplicationContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new MultiplicationNode(l, r);
	}

	@Override
	public Node visitDivision(PipifaxParser.DivisionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new DivisionNode(l, r);
	}

	@Override
	public Node visitNegation(PipifaxParser.NegationContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		return new NegationNode(op);
	}

	@Override
	public Node visitEquals(PipifaxParser.EqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new EqualsNode(l, r);
	}

	@Override
	public Node visitNotEquals(PipifaxParser.NotEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new NotEqualsNode(l, r);
	}

	@Override
	public Node visitNot(PipifaxParser.NotContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		return new NotNode(op);
	}

	@Override
	public Node visitOr(PipifaxParser.OrContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new OrNode(l, r);
	}

	@Override
	public Node visitAnd(PipifaxParser.AndContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new AndNode(l, r);
	}

	@Override
	public Node visitGreater(PipifaxParser.GreaterContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new GreaterNode(l, r);
	}

	@Override
	public Node visitGreaterOrEquals(PipifaxParser.GreaterOrEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new GreaterOrEqualsNode(l, r);
	}

	@Override
	public Node visitLess(PipifaxParser.LessContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new LessNode(l, r);
	}

	@Override
	public Node visitLessOrEquals(PipifaxParser.LessOrEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new LessOrEqualsNode(l, r);
	}

	@Override
	public Node visitStringCompare(PipifaxParser.StringCompareContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new StringCompareNode(l, r);
	}

	@Override
	public Node visitFunccall(PipifaxParser.FunccallContext ctx)
	{
		CallNode cn = new CallNode(ctx.ID().getText());
		if (ctx.expr() != null)
			for (int i = 0; i < ctx.expr().size(); i++)
			{
				cn.addArgument((ExpressionNode) ctx.expr(i).accept(this));
			}

		return cn;
	}
}
