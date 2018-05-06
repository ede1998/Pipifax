package me.erikhennig.pipifax;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

public class CompilerManager
{
	private Hashtable<String, Program> m_programs = new Hashtable<>();

	public CompilerManager(String startingFile)
	{
		File f = new File(startingFile);

		boolean isValidFile = f.isFile();
		if (isValidFile)
		{
			String fpath = f.toPath().toAbsolutePath().toString();
			m_programs.put(fpath, new Program(fpath));
		}
	}

	public void compile()
	{
		buildASTs();
		printPrograms();
		checkSemantics();
		if (checkSuccess())
			compilePrograms();
	}

	private void compilePrograms()
	{
		m_programs.forEach((path, p) -> p.generateCode());
	}

	public void printPrograms()
	{
		m_programs.forEach((path, prog) ->
		{
			System.out.println(path + "\n");
			prog.print();
		});
	}

	private void buildASTs()
	{
		Collection<Program> progs = m_programs.values();
		// Repeat until all files are in list
		boolean notdone;
		do
		{
			notdone = false;

			ArrayList<String> filesToAdd = new ArrayList<>();
			// try to compile each file
			for (Iterator<Program> iter = progs.iterator(); iter.hasNext();)
			{
				Program p = iter.next();
				boolean b = p.buildAST();
				notdone |= b;

				// don't add includes if file is already compiled
				if (!b)
					continue;

				// add its includes to all files
				for (Iterator<String> iter1 = p.getIncludes().iterator(); iter1.hasNext();)
				{
					String s = iter1.next();
					if (!m_programs.containsKey(s))
					{
						filesToAdd.add(s);
					}
				}
			}
			for (String s : filesToAdd)
				m_programs.put(s, new Program(s));
		} while (notdone);
	}

	private void checkSemantics()
	{
		Program p;
		while ((p = findSuitableProgram()) != null)
		{
			p.checkSemantics();
			removeFromIncludes(p);
		}
	}

	private boolean checkSuccess()
	{
		boolean result = true;
		for (Iterator<Program> iter = m_programs.values().iterator(); iter.hasNext();)
		{
			Program p = iter.next();
			if (!p.isChecked())
			{
				System.err.println("Could not compile program " + p.getProgramPath() + "\nInvalid includes:"
						+ p.getIncludes().toString());
				result = false;
			}
		}
		return result;
	}

	private void removeFromIncludes(Program prog)
	{
		Collection<Program> progs = m_programs.values();
		for (Iterator<Program> iter = progs.iterator(); iter.hasNext();)
		{
			Program p = iter.next();
			p.notifyIncludeIsReady(prog);
		}
	}

	private Program findSuitableProgram()
	{
		Collection<Program> progs = m_programs.values();
		for (Iterator<Program> iter = progs.iterator(); iter.hasNext();)
		{
			Program p = iter.next();
			if (p.getIncludes().isEmpty() && !p.isChecked())
				return p;
		}
		return null;
	}

}
