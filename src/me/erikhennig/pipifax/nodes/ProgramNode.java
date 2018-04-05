package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

public class ProgramNode extends Node {
	private ArrayList<Node> m_nodes = new ArrayList<>();
	
	public void addNode(Node n)
	{
		if (n != null)
			m_nodes.add(n);
	}
	
	public ArrayList<Node> getNodes()
	{
		return m_nodes;
	}
}
