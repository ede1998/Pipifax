package me.erikhennig.pipifax;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;

import me.erikhennig.pipifax.antlr.PipifaxLexer;
import me.erikhennig.pipifax.antlr.PipifaxParser;
import me.erikhennig.pipifax.nodes.Node;
import me.erikhennig.pipifax.nodes.ProgramNode;
import me.erikhennig.pipifax.visitors.PrintVisitor;

public class Main {

	public static void main(String[] args) {
		CharStream stream;
		try {
			stream = CharStreams.fromFileName(args[0]);
			Lexer lexer = new PipifaxLexer(stream);
			TokenStream ts = new CommonTokenStream(lexer);
			PipifaxParser parser = new PipifaxParser(ts);
			PipifaxParser.ProgContext ast = parser.prog();

			//Construct AST
			ASTCreatorVisitor acv = new ASTCreatorVisitor();
			Node n = ast.accept(acv);
			ProgramNode pn = (ProgramNode) n;
			
			//Print AST
			PrintVisitor printer = new PrintVisitor();
			pn.accept(printer);
			System.out.println(printer.getProgram());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RecognitionException e) {
			System.err.println("Syntax error: " + e);
		}

	}

}
