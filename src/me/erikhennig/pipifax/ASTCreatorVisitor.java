package me.erikhennig.pipifax;

import java.util.ArrayList;

import me.erikhennig.pipifax.antlr.PipifaxBaseVisitor;
import me.erikhennig.pipifax.antlr.PipifaxParser;
import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.expressions.*;

public class ASTCreatorVisitor extends PipifaxBaseVisitor<Node> {

	@Override public Node visitProg(PipifaxParser.ProgContext ctx)
	{
		ProgramNode pn = null;
		
		for (PipifaxParser.ProgContext pc: ctx.prog())
		{
			pn = (ProgramNode) pc.accept(this);
		}
		
		if (pn == null)
		{
			pn = new ProgramNode();
		}
		
		
		for(int i = 0; i < ctx.getChildCount(); i++)
		{
			Node n = ctx.getChild(i).accept(this);
			pn.addNode(n);
		}
		return pn;
	}
	
	@Override public Node visitVardecl(PipifaxParser.VardeclContext ctx)
	{
		TypeNode t = (TypeNode) ctx.type().accept(this);
		VariableNode d = new VariableNode(ctx.ID().getText(), t);
		return d;
	}
	
	@Override public Node visitType(PipifaxParser.TypeContext ctx)
	{
		TypeNode t;
		switch (ctx.getText())
		{
		case "int":
			t = new TypeNode(Types.INT);
		case "double":
			t = new TypeNode(Types.DOUBLE);
		case "string":
			t = new TypeNode(Types.STRING);
		default:
			t = (TypeNode) ctx.type().accept(this);
			t.addDimension(Integer.parseInt(ctx.INT().getText()));
		}
		return t;
	}
	
	@Override public Node visitFuncdecl(PipifaxParser.FuncdeclContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		FunctionNode fn = new FunctionNode(tn, ctx.ID().getText());
		for (int i = 0; i < ctx.parameter().getChildCount(); i++)
		{
			fn.addParameter((ParameterNode) ctx.parameter().getChild(i).accept(this)); 
		}
		for (int i = 0; i < ctx.block().getChildCount(); i++)
		{
		  fn.addStatement(ctx.block().getChild(i).accept(this));
		}
		return fn;	
	}
	
	@Override public Node visitParameter(PipifaxParser.ParameterContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.parameter_type().type().accept(this);
		//TODO Add reference types
//		ctx.parameter_type().
		ParameterNode pn = new ParameterNode(ctx.ID().getText(), false, tn);
		return pn;
	}
	
	@Override public Node visitIfstmt(PipifaxParser.IfstmtContext ctx)
	{
		IfNode in = new IfNode((ExpressionNode) ctx.expr().accept(this));
		for (int i = 0; i < ctx.block().getChildCount(); i++)
		{
			in.addStatement(ctx.block().getChild(i).accept(this));
		}
		for (int i = 0; i < ctx.elsestmt().block().getChildCount(); i++)
		{
			in.addElseStatements(ctx.elsestmt().block().getChild(i).accept(this));
		}
		return in;
	}
	
	@Override public Node visitWhilestmt(PipifaxParser.WhilestmtContext ctx)
	{
		WhileNode wn = new WhileNode((ExpressionNode) ctx.expr().accept(this));
		for (int i = 0; i < ctx.block().getChildCount(); i++)
		{
			wn.addStatement(ctx.block().getChild(i).accept(this));
		}
		return wn;
	}
	
	@Override public Node visitAssignment(PipifaxParser.AssignmentContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode src = (ExpressionNode) ctx.expr().accept(this);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override public Node visitLvalue(PipifaxParser.LvalueContext ctx)
	{
		ArrayList<ExpressionNode> alen = new ArrayList<>();
		for (int i = 0; i < ctx.expr().size(); i++)
		{
			alen.add((ExpressionNode) ctx.expr(i).accept(this));
		}
		LValueNode lvn = new LValueNode(ctx.ID().getText(), alen);
		return lvn;
	}
	
	@Override public Node visitIntLiteral(PipifaxParser.IntLiteralContext ctx)
	{
		return new IntegerLiteralNode(Integer.parseInt(ctx.INT().getText()));
	}
	
	@Override public Node visitDoubleLiteral(PipifaxParser.DoubleLiteralContext ctx)
	{
		return new DoubleLiteralNode(Double.parseDouble(ctx.DOUBLE().getText()));
	}
	
	@Override public Node visitStringLiteral(PipifaxParser.StringLiteralContext ctx)
	{
		return new StringLiteralNode(ctx.STRING().getText());
	}
	
	@Override public Node visitAddition(PipifaxParser.AdditionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new AdditionNode(l, r);
	}
	
	@Override public Node visitSubtraction(PipifaxParser.SubtractionContext ctx)
	{
		//TODO
		return null;
	}
	@Override public Node visitFunccall(PipifaxParser.FunccallContext ctx)
	{
		//TODO
		return null;
	}
}

