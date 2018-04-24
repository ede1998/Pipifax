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
import me.erikhennig.pipifax.nodes.NamedNode;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ProgramNode;
import me.erikhennig.pipifax.visitors.AccessControlVisitor;
import me.erikhennig.pipifax.visitors.NameResolutionVisitor;
import me.erikhennig.pipifax.visitors.PrintVisitor;
import me.erikhennig.pipifax.visitors.TypeCheckingVisitor;
import me.erikhennig.pipifax.visitors.VisitorException;

public class Program
{
	private ProgramNode m_program;
	private Hashtable<String, NamedNode> m_symbols = new Hashtable<>();
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (RecognitionException e)
		{
			System.err.println("Syntax error: " + e);
		}
		return true;
	}

	private void addPublicSymbols()
	{
		// Only 1 symbol with same name can be stored in hashmap
		// -> no problem when function is declared multiple times
		for (Node n : m_program.getNodes())
		{
			NamedNode nn = (NamedNode) n;
			if (nn.isExported())
				m_symbols.put(nn.getName(), nn);
		}
	}

	public void checkSemantics()
	{
		// Resolve names
		NameResolutionVisitor nrv = new NameResolutionVisitor(constructScope());
		try
		{
			m_program.accept(nrv);
			System.out.println("Name resolution successful.");
		}
		catch (VisitorException e)
		{
			m_checked = true;
			System.err.println(e);
			System.out.println("Name resolution error.");
			return;
		}

		// Type checking
		TypeCheckingVisitor tcv = new TypeCheckingVisitor();
		try
		{
			m_program.accept(tcv);
			System.out.println("Type checking done.");
		}
		catch (VisitorException e)
		{
			System.err.println(e);
			System.out.println("Type checking error.");
			m_checked = true;
		}

		// Visibility checking
		AccessControlVisitor acv = new AccessControlVisitor();
		try
		{
			m_program.accept(acv);
			System.out.println("Visibility checking done.");
		}
		catch (VisitorException e)
		{
			System.err.println(e);
			System.out.println("Visibility checking error.");
		}
		finally
		{
			m_checked = true;
		}
	}

	private Scope constructScope()
	{
		Scope s = new Scope();
		for (Program p : m_readyInclude)
		{
			Collection<NamedNode> nnc = p.m_symbols.values();
			for (Iterator<NamedNode> iter = nnc.iterator(); iter.hasNext();)
			{
				NamedNode nn = iter.next();
				if (!s.register(nn))
					System.err.println("Name already exists in global scope: " + nn.getName());
			}
		}
		return s;
	}

	public void print()
	{
		// Print AST
		PrintVisitor printer = new PrintVisitor();
		try
		{
			m_program.accept(printer);
		}
		catch (VisitorException e)
		{
			System.err.println(e);
		}
		System.out.println(printer.getProgram());
	}

	public String getProgramPath()
	{
		return m_programPath;
	}
}
