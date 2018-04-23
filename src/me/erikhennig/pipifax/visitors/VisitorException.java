package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.Node;

public class VisitorException extends Exception
{
	private static final long serialVersionUID = 1L;
	
	private Node m_node;
	private Visitor m_visitor;
	private String m_message;
	
    public VisitorException(Visitor v, Node n, String msg)
    {
    	super("Error in " + v.getName() + " (" + n.getLine() + ", " + n.getPositionInLine() + "): " + msg);
    	m_message = msg;
    	m_node = n;
    	m_visitor = v;
    }
    
	public Node getNode()
	{
		return m_node;
	}

	public Visitor getVisitor()
	{
		return m_visitor;
	}	
}
