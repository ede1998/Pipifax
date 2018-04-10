package me.erikhennig.pipifax;

public class Main
{
	public static void main(String[] args)
	{
		CompilerManager cm = new CompilerManager(args[0]);
		cm.compile();
	}

}
