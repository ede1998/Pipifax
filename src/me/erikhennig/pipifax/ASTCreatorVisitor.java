package me.erikhennig.pipifax;

import me.erikhennig.pipifax.antlr.PipifaxBaseVisitor;
import me.erikhennig.pipifax.antlr.PipifaxParser;

public class ASTCreatorVisitor extends PipifaxBaseVisitor<Node> {

	@Override public Node visitProg(PipifaxParser.ProgContext ctx)
	{
		//Node t = ctx.
		return null;
	}
	
	@Override public Node visitVardecl(PipifaxParser.VardeclContext ctx)
	{
		TypeNode t = (TypeNode) ctx.type().accept(this);
		VarNode d = new VarNode(ctx.ID().getText(), t);
		return d;
	}
	
	@Override public Node visitType(PipifaxParser.TypeContext ctx)
	{
		types tmp;
		switch (ctx.getText())
		{
		case "int":
			tmp = types.INT;
		case "double":
			tmp = types.DOUBLE;
		case "string":
			tmp = types.STRING;
		}
		TypeNode t = new TypeNode();
		return t;
	}
}
