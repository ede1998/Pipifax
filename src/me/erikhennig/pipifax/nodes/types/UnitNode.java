package me.erikhennig.pipifax.nodes.types;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.visitors.Visitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class UnitNode extends Node
{
	private List<BaseUnits> m_top = new LinkedList<>();
	private List<BaseUnits> m_bottom = new LinkedList<>();
	private double m_coefficient = 1.0;

	private List<String> m_topStr = new LinkedList<>();
	private List<String> m_bottomStr = new LinkedList<>();

	public UnitNode()
	{
	}
	
	public UnitNode(UnitNode un)
	{
		m_top.addAll(un.m_top);
		m_bottom.addAll(un.m_bottom);
		m_coefficient = un.m_coefficient;
	}
	
	public boolean check(UnitNode un)
	{
		if (m_top.size() != un.m_top.size() || m_bottom.size() != un.m_bottom.size())
			return false;
		
		Iterator<BaseUnits> iter1 = m_top.iterator();
		Iterator<BaseUnits> iter2 = un.m_top.iterator();
		while (iter1.hasNext())
			if (iter1.next() != iter2.next())
				return false;
		
		iter1 = m_bottom.iterator();
		iter2 = un.m_bottom.iterator();
		while (iter1.hasNext())
			if (iter1.next() != iter2.next())
				return false;
		
		return true;
	}

	public void expand(String str)
	{
		BaseUnits bu = parse(str);
		if (bu == null)
			m_topStr.add(str);
		else
			expand(bu);
	}

	public void reduce(String str)
	{
		BaseUnits bu = parse(str);
		if (bu == null)
			m_bottomStr.add(str);
		else
			reduce(bu);
	}

	public void expand(UnitNode un)
	{
		un.m_top.forEach((bu) -> expand(bu));
	}

	public void reduce(UnitNode un)
	{
		un.m_bottom.forEach((bu) -> reduce(bu));
	}

	public void expand(BaseUnits u)
	{
		modify(u, m_top, m_bottom);
	}

	public void reduce(BaseUnits u)
	{
		modify(u, m_bottom, m_top);
	}

	private static BaseUnits parse(String str)
	{
		switch (str)
		{
		case "m":
			return BaseUnits.m;
		case "s":
			return BaseUnits.s;
		case "kg":
			return BaseUnits.kg;
		case "A":
			return BaseUnits.A;
		case "cd":
			return BaseUnits.cd;
		case "K":
			return BaseUnits.K;
		case "mol":
			return BaseUnits.mol;
		default:
			return null;
		}
	}
	
	private void modify(BaseUnits item, List<BaseUnits> addTo, List<BaseUnits> removeFrom)
	{
		int position = removeFrom.indexOf(item);
		if (position == -1)
			addTo.add(item);
		else
			removeFrom.remove(position);
	}

	@Override
	public void accept(Visitor v) throws VisitorException
	{
		v.visit(this);
	}

	public double getCoefficient()
	{
		return m_coefficient;
	}

	public void setCoefficient(double coefficient)
	{
		m_coefficient = coefficient;
	}

	public List<String> getTopStr()
	{
		return m_topStr;
	}

	public List<String> getBottomStr()
	{
		return m_bottomStr;
	}
}
