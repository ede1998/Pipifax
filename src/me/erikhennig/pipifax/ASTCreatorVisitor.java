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
		DeclNode d = new DeclNode(ctx.ID().getText());
		
		return d;
	}
}
