package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

public class ProgramNode extends Node {
	private ArrayList<Node> m_nodes = new ArrayList<>();
	
	public void addNode(Node n)
	{
		m_nodes.add(n);
	}
}
