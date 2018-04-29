package me.erikhennig.pipifax;

import me.erikhennig.pipifax.antlr.PipifaxBaseVisitor;
import me.erikhennig.pipifax.antlr.PipifaxParser;
import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.values.*;
import me.erikhennig.pipifax.nodes.types.*;

public class ASTCreatorVisitor extends PipifaxBaseVisitor<Node>
{
	private Program m_program;

	public ASTCreatorVisitor(Program prog)
	{
		m_program = prog;
	}

	@Override
	public Node visitProg(PipifaxParser.ProgContext ctx)
	{
		ProgramNode pn = new ProgramNode();

		for (int i = 0; i < ctx.includedecl().size(); i++)
		{
			Node n = ctx.includedecl(i).accept(this);
			pn.addNode(n);
		}

		for (int i = 0; i < ctx.declaration().size(); i++)
		{
			Node n = ctx.declaration(i).accept(this);
			pn.addNode(n);
		}
		return pn;
	}

	@Override
	public Node visitDeclaration(PipifaxParser.DeclarationContext ctx)
	{
		boolean export = ctx.EXPORT() != null;

		NamedNode nn = (NamedNode) ctx.getChild((export) ? 1 : 0).accept(this);
		nn.setExported(export);

		return nn;
	}

	@Override
	public Node visitIncludedecl(PipifaxParser.IncludedeclContext ctx)
	{
		String str = ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1);
		if (!m_program.add(str))
			System.err.println("Invalid include: " + str);
		return null;
	}

	@Override
	public Node visitStatementBlock(PipifaxParser.StatementBlockContext ctx)
	{
		return ctx.block().accept(this);
	}

	@Override
	public Node visitStatementSingle(PipifaxParser.StatementSingleContext ctx)
	{
		BlockNode bn = new BlockNode();
		Node n = ctx.statement().accept(this);
		bn.addStatement(n);

		bn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return bn;
	}

	@Override
	public Node visitBlock(PipifaxParser.BlockContext ctx)
	{
		BlockNode bn = new BlockNode();
		for (int i = 0; i < ctx.getChildCount(); i++)
		{
			Node n = ctx.getChild(i).accept(this);
			bn.addStatement(n);
		}

		bn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return bn;
	}

	@Override
	public Node visitVardecl(PipifaxParser.VardeclContext ctx)
	{
		TypeNode t = (TypeNode) ctx.type().accept(this);
		ExpressionNode en = null;
		if (ctx.expr() != null)
			en = (ExpressionNode) ctx.expr().accept(this);
		VariableNode vn = new VariableNode(ctx.ID().getText(), t, en);

		vn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return vn;
	}

	@Override
	public Node visitStruct(PipifaxParser.StructContext ctx)
	{
		StructNode sn = new StructNode(ctx.ID().getText());
		for (PipifaxParser.MemberdeclContext pc : ctx.memberdecl())
		{
			sn.add(pc.ID().getText(), (StructComponentNode) pc.accept(this));
		}

		sn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return sn;
	}

	@Override
	public Node visitMemberdecl(PipifaxParser.MemberdeclContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		StructComponentNode scn = new StructComponentNode(ctx.ID().getText(), tn);

		scn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return scn;
	}

	@Override
	public Node visitClassdecl(PipifaxParser.ClassdeclContext ctx)
	{
		ClassNode cn = new ClassNode(ctx.ID(0).getText(), (ctx.PARENT != null) ? ctx.PARENT.getText() : "");
		for (PipifaxParser.ClassmemberdeclContext cmdc : ctx.classmemberdecl())
		{
			Node n = cmdc.accept(this);

			if (n instanceof ClassFunctionNode)
				cn.add(((ClassFunctionNode) n).getName(), (ClassFunctionNode) n);
			else if (n instanceof ClassFieldNode)
				cn.add(((ClassFieldNode) n).getName(), (ClassFieldNode) n);
		}
		return cn;
	}

	@Override
	public Node visitClassfunction(PipifaxParser.ClassfunctionContext ctx)
	{
		FunctionNode fn = (FunctionNode) ctx.funcdecl().accept(this);
		ClassFunctionNode cfcn = new ClassFunctionNode(fn);
		switch (ctx.ACCESS_MODIFIER().getText())
		{
		case "private":
			cfcn.setVisibility(Visibility.PRIVATE);
			break;
		case "protected":
			cfcn.setVisibility(Visibility.PROTECTED);
			break;
		case "public":
			cfcn.setVisibility(Visibility.PUBLIC);
			break;
		}
		cfcn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
		return cfcn;
	}

	@Override
	public Node visitClassvar(PipifaxParser.ClassvarContext ctx)
	{
		ClassFieldNode cdcn = (ClassFieldNode) ctx.classvardecl().accept(this);
		switch (ctx.ACCESS_MODIFIER().getText())
		{
		case "private":
			cdcn.setVisibility(Visibility.PRIVATE);
			break;
		case "protected":
			cdcn.setVisibility(Visibility.PROTECTED);
			break;
		case "public":
			cdcn.setVisibility(Visibility.PUBLIC);
			break;
		default:
			throw new NullPointerException();
		}
		cdcn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
		return cdcn;
	}

	@Override
	public Node visitClassvardecl(PipifaxParser.ClassvardeclContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		ClassFieldNode cdcn = new ClassFieldNode(ctx.ID().getText(), tn);
		cdcn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
		return cdcn;
	}

	@Override
	public Node visitIntType(PipifaxParser.IntTypeContext ctx)
	{
		return TypeNode.getInt();
	}

	@Override
	public Node visitDoubleType(PipifaxParser.DoubleTypeContext ctx)
	{
		DoubleTypeNode dtn = TypeNode.getDouble();
		if (ctx.unit() != null)
			dtn.setUnitNode((UnitNode) ctx.unit().accept(this));
		dtn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
		return dtn;
	}

	@Override
	public Node visitStringType(PipifaxParser.StringTypeContext ctx)
	{
		return TypeNode.getString();
	}

	@Override
	public Node visitArrayType(PipifaxParser.ArrayTypeContext ctx)
	{
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		int size = Integer.parseInt(ctx.INT().getText());
		SizedArrayTypeNode satn = new SizedArrayTypeNode(tn, size);

		satn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return satn;
	}

	@Override
	public Node visitCustomType(PipifaxParser.CustomTypeContext ctx)
	{
		CustomTypeNode ctn = new CustomTypeNode(ctx.ID().getText());

		ctn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ctn;
	}

	@Override
	public Node visitFuncdecl(PipifaxParser.FuncdeclContext ctx)
	{
		TypeNode returnType = (ctx.type() != null) ? (TypeNode) ctx.type().accept(this) : TypeNode.getVoid();
		BlockNode bn = (BlockNode) ctx.block().accept(this);
		FunctionNode fn = new FunctionNode(returnType, ctx.ID().getText(), bn);
		for (PipifaxParser.ParameterContext pc : ctx.parameterlist().parameter())
		{
			ParameterNode pn = (ParameterNode) pc.accept(this);
			fn.addParameter(pn);
		}

		fn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return fn;
	}

	@Override
	public Node visitParameter(PipifaxParser.ParameterContext ctx)
	{
		return ctx.parameter_type().accept(this);
	}

	@Override
	public Node visitTypeParameter(PipifaxParser.TypeParameterContext ctx)
	{
		String name = ((PipifaxParser.ParameterContext) ctx.getParent()).ID().getText();
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		ParameterNode pn = new ParameterNode(name, tn);

		pn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return pn;
	}

	@Override
	public Node visitReferenceParameter(PipifaxParser.ReferenceParameterContext ctx)
	{
		String name = ((PipifaxParser.ParameterContext) ctx.getParent()).ID().getText();
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		RefTypeNode rtn = new RefTypeNode(tn);
		ParameterNode pn = new ParameterNode(name, rtn);

		pn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return pn;
	}

	@Override
	public Node visitReferenceArrayParameter(PipifaxParser.ReferenceArrayParameterContext ctx)
	{
		String name = ((PipifaxParser.ParameterContext) ctx.getParent()).ID().getText();
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		UnsizedArrayTypeNode uatn = new UnsizedArrayTypeNode(tn);
		RefTypeNode rtn = new RefTypeNode(uatn);
		ParameterNode pn = new ParameterNode(name, rtn);

		pn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return pn;
	}

	@Override
	public Node visitIfstmt(PipifaxParser.IfstmtContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		BlockNode bn = (BlockNode) ctx.statements().accept(this);
		BlockNode bn1 = null;
		if (ctx.elsestmt() != null)
		{
			bn1 = (BlockNode) ctx.elsestmt().statements().accept(this);
		}
		else
		{
			bn1 = new BlockNode();
		}
		IfNode in = new IfNode(en, bn, bn1);

		in.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return in;
	}

	@Override
	public Node visitWhilestmt(PipifaxParser.WhilestmtContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		BlockNode bn = (BlockNode) ctx.statements().accept(this);
		WhileNode wn = new WhileNode(en, bn);

		wn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return wn;
	}

	@Override
	public Node visitDowhilestmt(PipifaxParser.DowhilestmtContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		BlockNode bn = (BlockNode) ctx.statements().accept(this);
		DoWhileNode dwn = new DoWhileNode(en, bn);

		dwn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return dwn;
	}

	@Override
	public Node visitAssign(PipifaxParser.AssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode src = (ExpressionNode) ctx.expr().accept(this);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitAdditionAssign(PipifaxParser.AdditionAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.ADDITION);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitSubtractionAssign(PipifaxParser.SubtractionAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.SUBTRACTION);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitMultiplicationAssign(PipifaxParser.MultiplicationAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.MULTIPLICATION);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitDivisionAssign(PipifaxParser.DivisionAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.DIVISION);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitAndAssign(PipifaxParser.AndAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.AND);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitOrAssign(PipifaxParser.OrAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.OR);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitModuloAssign(PipifaxParser.ModuloAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.MODULO);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitStringAssign(PipifaxParser.StringAssignContext ctx)
	{
		ValueNode dest = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.CONCATENATION);
		AssignmentNode an = new AssignmentNode(dest, src);

		an.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return an;
	}

	@Override
	public Node visitForstmt(PipifaxParser.ForstmtContext ctx)
	{
		AssignmentNode an1 = null;
		AssignmentNode an2 = null;
		if (ctx.initassign != null)
			an1 = (AssignmentNode) ctx.initassign.accept(this);
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		if (ctx.loopedassign != null)
			an2 = (AssignmentNode) ctx.loopedassign.accept(this);
		BlockNode bn = (BlockNode) ctx.statements().accept(this);
		ForNode fn = new ForNode(an1, en, an2, bn);

		fn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return fn;
	}

	@Override
	public Node visitSwitchstmt(PipifaxParser.SwitchstmtContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);

		// case-Part
		BlockNode bn2 = new BlockNode();
		for (int i = 0; i < ctx.casestmt().size(); i++)
		{
			CaseNode n = (CaseNode) ctx.casestmt(i).accept(this);
			bn2.addStatement(n);
		}
		// default-Part
		BlockNode bn = null;
		if (ctx.defaultstmt() != null)
			bn = (BlockNode) ctx.defaultstmt().accept(this);
		else
			bn = new BlockNode();

		SwitchNode sn = new SwitchNode(en, bn2, bn);

		sn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return sn;
	}

	@Override
	public Node visitCasestmt(PipifaxParser.CasestmtContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		BlockNode bn = (BlockNode) ctx.statements().accept(this);

		CaseNode cn = new CaseNode(en, bn);

		cn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return cn;
	}

	@Override
	public Node visitArrayAccess(PipifaxParser.ArrayAccessContext ctx)
	{
		ValueNode lvn = (ValueNode) ctx.lvalue().accept(this);
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		ArrayAccessNode aan = new ArrayAccessNode(lvn, en);

		aan.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return aan;
	}

	@Override
	public Node visitStructAccess(PipifaxParser.StructAccessContext ctx)
	{
		ValueNode lvn = (ValueNode) ctx.lvalue().accept(this);
		StructAccessNode san = new StructAccessNode(lvn, ctx.ID().getText());

		san.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return san;
	}

	@Override
	public Node visitVarAccess(PipifaxParser.VarAccessContext ctx)
	{
		VariableAccessNode van = new VariableAccessNode(ctx.ID().getText());

		van.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return van;
	}

	@Override
	public Node visitClassFuncAccess(PipifaxParser.ClassFuncAccessContext ctx)
	{
		ValueNode vn = (ValueNode) ctx.lvalue().accept(this);
		CallNode cn = (CallNode) ctx.funccall().accept(this);
		ClassFunctionAccessNode cfan = new ClassFunctionAccessNode(vn, cn);
		cfan.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
		return cfan;
	}

	@Override
	public Node visitClassVarAccess(PipifaxParser.ClassVarAccessContext ctx)
	{
		ValueNode vn = (ValueNode) ctx.lvalue().accept(this);
		ClassDataAccessNode cdan = new ClassDataAccessNode(vn, ctx.ID().getText());
		cdan.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
		return cdan;
	}

	@Override
	public Node visitFunctionAccess(PipifaxParser.FunctionAccessContext ctx)
	{
		return ctx.funccall().accept(this);
	}

	@Override
	public Node visitIntLiteral(PipifaxParser.IntLiteralContext ctx)
	{
		IntegerLiteralNode iln = new IntegerLiteralNode(Integer.parseInt(ctx.INT().getText()));

		iln.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return iln;
	}

	@Override
	public Node visitDoubleLiteral(PipifaxParser.DoubleLiteralContext ctx)
	{
		UnitNode un = null;
		if (ctx.unit() != null)
			un = (UnitNode) ctx.unit().accept(this);
		DoubleLiteralNode dln = new DoubleLiteralNode(Double.parseDouble(ctx.DOUBLE().getText()), un);

		dln.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return dln;
	}

	@Override
	public Node visitUnit(PipifaxParser.UnitContext ctx)
	{
		UnitNode un = new UnitNode();

		if (ctx.factor != null)
			un.setCoefficient(Double.parseDouble(ctx.factor.getText()));

		ctx.top.forEach((token) ->
		{
			un.expand(token.getText());
		});
		ctx.bottom.forEach((token) ->
		{
			un.reduce(token.getText());
		});

		un.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
		return un;
	}

	@Override
	public Node visitUnitdecl(PipifaxParser.UnitdeclContext ctx)
	{
		UnitNode un = (UnitNode) ctx.unit().accept(this);
		UnitDefinitionNode udn = new UnitDefinitionNode(ctx.ID().getText(), un);

		udn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return udn;
	}

	@Override
	public Node visitStringLiteral(PipifaxParser.StringLiteralContext ctx)
	{
		String str = ctx.STRING().getText();
		StringLiteralNode sln = new StringLiteralNode(str.substring(1, str.length() - 1));

		sln.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return sln;
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
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.ADDITION);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitSubtraction(PipifaxParser.SubtractionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.SUBTRACTION);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitMultiplication(PipifaxParser.MultiplicationContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.MULTIPLICATION);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitDivision(PipifaxParser.DivisionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.DIVISION);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitModulo(PipifaxParser.ModuloContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.MODULO);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitStringConcat(PipifaxParser.StringConcatContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.CONCATENATION);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitNegation(PipifaxParser.NegationContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		UnaryExpressionNode uen = new UnaryExpressionNode(op, UnaryOperation.NEGATION);

		uen.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return uen;
	}

	@Override
	public Node visitEquals(PipifaxParser.EqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.EQUALS);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitNotEquals(PipifaxParser.NotEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.NOTEQUALS);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitNot(PipifaxParser.NotContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		UnaryExpressionNode uen = new UnaryExpressionNode(op, UnaryOperation.NOT);

		uen.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return uen;
	}

	@Override
	public Node visitOr(PipifaxParser.OrContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.OR);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitAnd(PipifaxParser.AndContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.AND);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitGreater(PipifaxParser.GreaterContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.GREATER);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitGreaterOrEquals(PipifaxParser.GreaterOrEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.GREATEROREQUALS);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitLess(PipifaxParser.LessContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.LESS);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitLessOrEquals(PipifaxParser.LessOrEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.LESSOREQUALS);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitStringCompare(PipifaxParser.StringCompareContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		BinaryExpressionNode ben = new BinaryExpressionNode(l, r, BinaryOperation.STRINGCOMPARE);

		ben.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ben;
	}

	@Override
	public Node visitIntCast(PipifaxParser.IntCastContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		UnaryExpressionNode uen = new UnaryExpressionNode(op, UnaryOperation.INTCAST);

		uen.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return uen;
	}

	@Override
	public Node visitDoubleCast(PipifaxParser.DoubleCastContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		UnaryExpressionNode uen = new UnaryExpressionNode(op, UnaryOperation.DOUBLECAST);

		uen.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return uen;
	}

	@Override
	public Node visitClassCast(PipifaxParser.ClassCastContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		ClassCastNode ccn = new ClassCastNode(op, ctx.ID().getText());

		ccn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return ccn;
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

		cn.setPosition(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());

		return cn;
	}

	@Override
	public Node visitStatementAssignment(PipifaxParser.StatementAssignmentContext ctx)
	{
		return ctx.assignment().accept(this);
	}

	@Override
	public Node visitStatementCall(PipifaxParser.StatementCallContext ctx)
	{
		return ctx.funccall().accept(this);
	}

	@Override
	public Node visitStatementFor(PipifaxParser.StatementForContext ctx)
	{
		return ctx.forstmt().accept(this);
	}

	@Override
	public Node visitStatementIf(PipifaxParser.StatementIfContext ctx)
	{
		return ctx.ifstmt().accept(this);
	}

	@Override
	public Node visitStatementSwitch(PipifaxParser.StatementSwitchContext ctx)
	{
		return ctx.switchstmt().accept(this);
	}

	@Override
	public Node visitStatementWhile(PipifaxParser.StatementWhileContext ctx)
	{
		return ctx.whilestmt().accept(this);
	}

	@Override
	public Node visitStatementDoWhile(PipifaxParser.StatementDoWhileContext ctx)
	{
		return ctx.dowhilestmt().accept(this);
	}
}
