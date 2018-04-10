package me.erikhennig.pipifax.nodes;

import java.util.ArrayList;

import me.erikhennig.pipifax.visitors.Visitor;

public class BlockNode extends Node {

	private ArrayList<Node> m_statements = new ArrayList<>();
	
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}

	public ArrayList<Node> getStatements() {
		return m_statements;
	}
	
	public void addStatement(Node n)
	{
		if (n != null)
			m_statements.add(n);
	}

}
