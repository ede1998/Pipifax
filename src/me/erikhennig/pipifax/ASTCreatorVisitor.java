package me.erikhennig.pipifax;

import me.erikhennig.pipifax.antlr.PipifaxBaseVisitor;
import me.erikhennig.pipifax.antlr.PipifaxParser;
import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ProgramNode;
import me.erikhennig.pipifax.nodes.TypeNode;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.nodes.Types;

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
		return fn;	
	}
}

