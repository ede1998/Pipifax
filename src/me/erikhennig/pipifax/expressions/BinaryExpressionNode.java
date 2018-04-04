package me.erikhennig.pipifax.expressions;

public class BinaryExpressionNode extends ExpressionNode {
  protected ExpressionNode m_leftSide;
  protected ExpressionNode m_rightSide;
  
  public BinaryExpressionNode(ExpressionNode left, ExpressionNode right)
  {
	  m_leftSide = left;
	  m_rightSide = right;
  }
}
