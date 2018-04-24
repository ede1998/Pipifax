package me.erikhennig.pipifax.nodes;

import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public abstract class Node
{
	private int m_line = -1;
	private int m_charInLine = -1;
	public abstract void accept(Visitor v)  throws VisitorException;
	
	public void setPosition(int line, int character)
	{
		m_line = line;
		m_charInLine = character;
	}
	
	public int getLine()
	{
		return m_line;
	}
	
	public int getPositionInLine()
	{
		return m_charInLine;
	}
}
