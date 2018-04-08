package me.erikhennig.pipifax.nodes.expressions;

public abstract class LiteralNode extends ExpressionNode
{
	@Override
	public boolean checkType()
	{
		return true;
	}
}
