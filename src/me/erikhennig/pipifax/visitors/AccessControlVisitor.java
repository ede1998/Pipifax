package me.erikhennig.pipifax.visitors;

import me.erikhennig.pipifax.nodes.ClassDataComponentNode;
import me.erikhennig.pipifax.nodes.ClassFunctionComponentNode;
import me.erikhennig.pipifax.nodes.ClassNode;
import me.erikhennig.pipifax.nodes.Visibility;
import me.erikhennig.pipifax.nodes.expressions.values.CallNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassDataAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.ClassFunctionAccessNode;
import me.erikhennig.pipifax.nodes.expressions.values.VariableAccessNode;

public class AccessControlVisitor extends Visitor
{
	private ClassNode m_currentClass = new ClassNode("", "");

	@Override
	protected String getName()
	{
		return "access control";
	}

	@Override
	public void visit(ClassFunctionAccessNode n) throws VisitorException
	{
		super.visit(n);
		ClassFunctionComponentNode cfcn = n.getComponent();
		Visibility visibility = cfcn.getAccessModifier();
		if (visibility != Visibility.PUBLIC)
		{
			if (visibility == Visibility.PRIVATE)
			{
				if (!cfcn.getParent().getName().equals(m_currentClass.getName()))
					throw new VisitorException(this, n, "Cannot access private function of class " + cfcn.getParent().getName() + " from here");
			}
			else if (visibility == Visibility.PROTECTED)
			{
				ClassNode curr = m_currentClass;
				do
				{
					if (cfcn.getParent().getName().equals(curr.getName()))
					{
						break;
					}
					curr = curr.getParentClass();
				}
				while (curr != null);
				
				if (curr == null)
				{
					throw new VisitorException(this, n, "Cannot access protected function of class " + cfcn.getParent().getName() + " from here");
				}
			}

		}
	}

	@Override
	public void visit(ClassDataAccessNode n) throws VisitorException
	{
		super.visit(n);
		ClassDataComponentNode cdcn = n.getComponent();
		Visibility visibility = cdcn.getAccessModifier();
		if (visibility != Visibility.PUBLIC)
		{
			if (visibility == Visibility.PRIVATE)
			{
				if (!cdcn.getParent().getName().equals(m_currentClass.getName()))
					throw new VisitorException(this, n, "Cannot access private member of class " + cdcn.getParent().getName() + " from here");
			}
			else if (visibility == Visibility.PROTECTED)
			{
				ClassNode curr = m_currentClass;
				do
				{
					if (cdcn.getParent().getName().equals(curr.getName()))
					{
						break;
					}
					curr = curr.getParentClass();
				}
				while (curr != null);
				
				if (curr == null)
				{
					throw new VisitorException(this, n, "Cannot access protected member of class " + cdcn.getParent().getName() + " from here");
				}
			}

		}
	}

	@Override
	public void visit(VariableAccessNode n) throws VisitorException
	{
		super.visit(n);
		ClassDataComponentNode cdcn = n.getVariable().getParent();
		if (cdcn == null)
			return;
		
		Visibility visibility = cdcn.getAccessModifier();
		if (visibility != Visibility.PUBLIC)
		{
			if (visibility == Visibility.PRIVATE)
			{
				if (!cdcn.getParent().getName().equals(m_currentClass.getName()))
					throw new VisitorException(this, n, "Cannot access private member of class " + cdcn.getParent().getName() + " from here");
			}
			else if (visibility == Visibility.PROTECTED)
			{
				ClassNode curr = m_currentClass;
				do
				{
					if (cdcn.getParent().getName().equals(curr.getName()))
					{
						break;
					}
					curr = curr.getParentClass();
				}
				while (curr != null);
				
				if (curr == null)
				{
					throw new VisitorException(this, n, "Cannot access protected member of class " + cdcn.getParent().getName() + " from here");
				}
			}

		}
	}

	@Override
	public void visit(CallNode n) throws VisitorException
	{
		super.visit(n);
		ClassFunctionComponentNode cdcn = n.getFunction().getParent();
		if (cdcn == null)
			return;
		
		Visibility visibility = cdcn.getAccessModifier();
		if (visibility != Visibility.PUBLIC)
		{
			if (visibility == Visibility.PRIVATE)
			{
				if (!cdcn.getParent().getName().equals(m_currentClass.getName()))
					throw new VisitorException(this, n, "Cannot access private function of class " + cdcn.getParent().getName() + " from here");
			}
			else if (visibility == Visibility.PROTECTED)
			{
				ClassNode curr = m_currentClass;
				do
				{
					if (cdcn.getParent().getName().equals(curr.getName()))
					{
						break;
					}
					curr = curr.getParentClass();
				}
				while (curr != null);
				
				if (curr == null)
				{
					throw new VisitorException(this, n, "Cannot access protected function of class " + cdcn.getParent().getName() + " from here");
				}
			}

		}
	}

	@Override
	public void visit(ClassNode n) throws VisitorException
	{
		m_currentClass = n;
		super.visit(n);
		m_currentClass = new ClassNode("", "");
	}
}
