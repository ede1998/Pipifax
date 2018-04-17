package me.erikhennig.pipifax;

import me.erikhennig.pipifax.antlr.PipifaxBaseVisitor;
import me.erikhennig.pipifax.antlr.PipifaxParser;
import me.erikhennig.pipifax.nodes.*;
import me.erikhennig.pipifax.nodes.controls.*;
import me.erikhennig.pipifax.nodes.expressions.*;
import me.erikhennig.pipifax.nodes.expressions.lvalues.ArrayAccessNode;
import me.erikhennig.pipifax.nodes.expressions.lvalues.LValueNode;
import me.erikhennig.pipifax.nodes.expressions.lvalues.StructAccessNode;
import me.erikhennig.pipifax.nodes.expressions.lvalues.VariableAccessNode;
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

		for (int i = 0; i < ctx.getChildCount(); i++)
		{
			Node n = ctx.getChild(i).accept(this);
			pn.addNode(n);
		}
		return pn;
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

		return bn;
	}

	@Override
	public Node visitVardecl(PipifaxParser.VardeclContext ctx)
	{
		TypeNode t = (TypeNode) ctx.type().accept(this);
		ExpressionNode en = null;
		if (ctx.expr() != null)
			en = (ExpressionNode) ctx.expr().accept(this);
		VariableNode d = new VariableNode(ctx.ID().getText(), t, en);
		return d;
	}

	@Override
	public Node visitStruct(PipifaxParser.StructContext ctx)
	{
		StructNode sn = new StructNode(ctx.ID().getText());
		for (PipifaxParser.MemberdeclContext pc : ctx.memberdecl())
		{
			sn.add(pc.ID().getText(), (StructComponentNode) pc.accept(this));
		}
		return sn;
	}
	
	@Override
	public Node visitMemberdecl(PipifaxParser.MemberdeclContext ctx) {
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		StructComponentNode scn = new StructComponentNode(ctx.ID().getText(), tn);
		return scn;
	}

	@Override
	public Node visitIntType(PipifaxParser.IntTypeContext ctx)
	{
		return TypeNode.getInt();
	}

	@Override
	public Node visitDoubleType(PipifaxParser.DoubleTypeContext ctx)
	{
		return TypeNode.getDouble();
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
		return satn;
	}

	@Override
	public Node visitCustomType(PipifaxParser.CustomTypeContext ctx)
	{
		CustomTypeNode ctn = new CustomTypeNode(ctx.ID().getText());
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
		return pn;
	}

	@Override
	public Node visitReferenceParameter(PipifaxParser.ReferenceParameterContext ctx)
	{
		String name = ((PipifaxParser.ParameterContext) ctx.getParent()).ID().getText();
		TypeNode tn = (TypeNode) ctx.type().accept(this);
		RefTypeNode rtn = new RefTypeNode(tn);
		ParameterNode pn = new ParameterNode(name, rtn);
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
		return in;
	}

	@Override
	public Node visitWhilestmt(PipifaxParser.WhilestmtContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		BlockNode bn = (BlockNode) ctx.statements().accept(this);
		WhileNode wn = new WhileNode(en, bn);
		return wn;
	}

	@Override
	public Node visitAssign(PipifaxParser.AssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode src = (ExpressionNode) ctx.expr().accept(this);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitAdditionAssign(PipifaxParser.AdditionAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.ADDITION);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitSubtractionAssign(PipifaxParser.SubtractionAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.SUBTRACTION);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitMultiplicationAssign(PipifaxParser.MultiplicationAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.MULTIPLICATION);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitDivisionAssign(PipifaxParser.DivisionAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.DIVISION);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitAndAssign(PipifaxParser.AndAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.AND);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitOrAssign(PipifaxParser.OrAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.OR);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitModuloAssign(PipifaxParser.ModuloAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.MODULO);
		AssignmentNode an = new AssignmentNode(dest, src);
		return an;
	}

	@Override
	public Node visitStringAssign(PipifaxParser.StringAssignContext ctx)
	{
		LValueNode dest = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode left = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode right = (ExpressionNode) ctx.expr().accept(this);
		ExpressionNode src = new BinaryExpressionNode(left, right, BinaryOperation.CONCATENATION);
		AssignmentNode an = new AssignmentNode(dest, src);
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

		return sn;
	}

	@Override
	public Node visitCasestmt(PipifaxParser.CasestmtContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		BlockNode bn = (BlockNode) ctx.statements().accept(this);

		CaseNode cn = new CaseNode(en, bn);

		return cn;
	}
	
	@Override
	public Node visitArrayAccess(PipifaxParser.ArrayAccessContext ctx) {
		LValueNode lvn = (LValueNode) ctx.lvalue().accept(this);
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		ArrayAccessNode aan = new ArrayAccessNode(lvn, en);
		return aan;
	}
	
	@Override
	public Node visitStructAccess(PipifaxParser.StructAccessContext ctx) {
		LValueNode lvn = (LValueNode) ctx.lvalue().accept(this);
		StructAccessNode san = new StructAccessNode(lvn, ctx.ID().getText());
		return san;
	}
	
	@Override
	public Node visitVarAccess(PipifaxParser.VarAccessContext ctx) {
		VariableAccessNode van = new VariableAccessNode(ctx.ID().getText());
		return van;
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
		String str = ctx.STRING().getText();
		return new StringLiteralNode(str.substring(1, str.length() - 1));
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
		return new BinaryExpressionNode(l, r, BinaryOperation.ADDITION);
	}

	@Override
	public Node visitSubtraction(PipifaxParser.SubtractionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.SUBTRACTION);
	}

	@Override
	public Node visitMultiplication(PipifaxParser.MultiplicationContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.MULTIPLICATION);
	}

	@Override
	public Node visitDivision(PipifaxParser.DivisionContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.DIVISION);
	}

	@Override
	public Node visitModulo(PipifaxParser.ModuloContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.MODULO);
	}

	@Override
	public Node visitStringConcat(PipifaxParser.StringConcatContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.CONCATENATION);
	}

	@Override
	public Node visitNegation(PipifaxParser.NegationContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		return new UnaryExpressionNode(op, UnaryOperation.NEGATION);
	}

	@Override
	public Node visitEquals(PipifaxParser.EqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.EQUALS);
	}

	@Override
	public Node visitNotEquals(PipifaxParser.NotEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.NOTEQUALS);
	}

	@Override
	public Node visitNot(PipifaxParser.NotContext ctx)
	{
		ExpressionNode op = (ExpressionNode) ctx.expr().accept(this);
		return new UnaryExpressionNode(op, UnaryOperation.NOT);
	}

	@Override
	public Node visitOr(PipifaxParser.OrContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.OR);
	}

	@Override
	public Node visitAnd(PipifaxParser.AndContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.AND);
	}

	@Override
	public Node visitGreater(PipifaxParser.GreaterContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.GREATER);
	}

	@Override
	public Node visitGreaterOrEquals(PipifaxParser.GreaterOrEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.GREATEROREQUALS);
	}

	@Override
	public Node visitLess(PipifaxParser.LessContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.LESS);
	}

	@Override
	public Node visitLessOrEquals(PipifaxParser.LessOrEqualsContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.LESSOREQUALS);
	}

	@Override
	public Node visitStringCompare(PipifaxParser.StringCompareContext ctx)
	{
		ExpressionNode l = (ExpressionNode) ctx.expr(0).accept(this);
		ExpressionNode r = (ExpressionNode) ctx.expr(1).accept(this);
		return new BinaryExpressionNode(l, r, BinaryOperation.STRINGCOMPARE);
	}

	@Override
	public Node visitIntCast(PipifaxParser.IntCastContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		return new UnaryExpressionNode(en, UnaryOperation.INTCAST);
	}

	@Override
	public Node visitDoubleCast(PipifaxParser.DoubleCastContext ctx)
	{
		ExpressionNode en = (ExpressionNode) ctx.expr().accept(this);
		return new UnaryExpressionNode(en, UnaryOperation.DOUBLECAST);
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
}
