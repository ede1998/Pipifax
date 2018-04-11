package me.erikhennig.pipifax;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;

import me.erikhennig.pipifax.antlr.PipifaxLexer;
import me.erikhennig.pipifax.antlr.PipifaxParser;
import me.erikhennig.pipifax.nameresolution.Scope;
import me.erikhennig.pipifax.nodes.FunctionNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ProgramNode;
import me.erikhennig.pipifax.nodes.VariableNode;
import me.erikhennig.pipifax.visitors.NameResolutionVisitor;
import me.erikhennig.pipifax.visitors.PrintVisitor;
import me.erikhennig.pipifax.visitors.TypeCheckingVisitor;

public class Program
{
	private ProgramNode m_program;
	private Hashtable<String, VariableNode> m_variables = new Hashtable<>();
	private Hashtable<String, FunctionNode> m_functions = new Hashtable<>();
	private String m_programPath;
	private ArrayList<String> m_includes = new ArrayList<>();
	private ArrayList<Program> m_readyInclude = new ArrayList<>();
	private boolean m_checked = false;

	public Program(String path)
	{
		m_programPath = path;
	}

	public ArrayList<String> getIncludes()
	{
		return m_includes;
	}

	public boolean add(String file)
	{
		Path p = Paths.get(m_programPath).toAbsolutePath();
		p = p.getParent().resolve(file);
		boolean isValidFile = p.toFile().isFile();
		if (isValidFile)
		{
			m_includes.add(p.toString());
			return true;
		}
		return false;
	}

	public void notifyIncludeIsReady(Program p)
	{
		if (m_includes.remove(p.m_programPath))
			m_readyInclude.add(p);
	}

	public boolean isChecked()
	{
		return m_checked;
	}

	public boolean buildAST()
	{
		if (m_program != null)
			return false;
		System.out.println("Compiling " + m_programPath);
		CharStream stream;
		try
		{
			stream = CharStreams.fromFileName(m_programPath);
			Lexer lexer = new PipifaxLexer(stream);
			TokenStream ts = new CommonTokenStream(lexer);
			PipifaxParser parser = new PipifaxParser(ts);
			PipifaxParser.ProgContext ast = parser.prog();

			// Construct AST
			ASTCreatorVisitor acv = new ASTCreatorVisitor(this);
			Node n = ast.accept(acv);
			m_program = (ProgramNode) n;

			addPublicSymbols();
		} catch (IOException e)
		{
			e.printStackTrace();
		} catch (RecognitionException e)
		{
			System.err.println("Syntax error: " + e);
		}
		return true;
	}

	private void addPublicSymbols()
	{
		// Only 1 variable and 1 function with same name can be stored in hashmaps
		// -> no problem when function is declared multiple times
		for (Node n : m_program.getNodes())
		{
			if (n instanceof VariableNode)
			{
				VariableNode vn = (VariableNode) n;
				m_variables.put(vn.getName(), vn);
			} else if (n instanceof FunctionNode)
			{
				FunctionNode fn = (FunctionNode) n;
				m_functions.put(fn.getName(), fn);
			}
		}
	}

	public void checkSemantics()
	{
		// Resolve names
		NameResolutionVisitor nrv = new NameResolutionVisitor(constructScope());
		m_program.accept(nrv);
		if (nrv.wasSuccessful())
			System.out.println("Name resolution successful.");
		else
		{
			m_checked = true;
			System.out.println("Name resolution error.");
			return;
		}

		// Type checking
		TypeCheckingVisitor tcv = new TypeCheckingVisitor();
		m_program.accept(tcv);
		if (tcv.wasSuccessful())
			System.out.println("Type checking done.");
		else
		{
			System.out.println("Type checking error.");
		}
		m_checked = true;
	}

	private Scope constructScope()
	{
		Scope s = new Scope();
		for (Program p : m_readyInclude)
		{
			Collection<FunctionNode> fns = p.m_functions.values();
			for (Iterator<FunctionNode> iter = fns.iterator(); iter.hasNext();)
			{
				FunctionNode fn = iter.next();
				if (!s.registerFunction(fn))
					System.err.println("Function already exists in global scope: " + fn.getName());
			}
			Collection<VariableNode> vars = p.m_variables.values();
			for (Iterator<VariableNode> iter = vars.iterator(); iter.hasNext();)
			{
				VariableNode vn = iter.next();
				if (!s.registerVariable(vn))
					System.err.println("Variable already exists in global scope: " + vn.getName());
			}
		}
		return s;
	}

	public void print()
	{
		// Print AST
		PrintVisitor printer = new PrintVisitor();
		m_program.accept(printer);
		System.out.println(printer.getProgram());
	}

	public String getProgramPath()
	{
		return m_programPath;
	}
}
