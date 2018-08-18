/**
 * This project objective is Server Less Machine Learning Platform.
 * This project is developing for ancode developers and it provides PAAS.
 */
package com.assistanz.slmlp.container.util;


import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.pgm.Main;
import org.eclipse.jgit.pgm.TextBuiltin;
import org.eclipse.jgit.pgm.TextBuiltin.TerminatedByHelpException;
import org.eclipse.jgit.util.IO;

public class CLIGitCommand extends Main {

	private final Result result;

	private final Repository db;
	
	PrintWriter writer;
	
	private String gitdir;

	public CLIGitCommand(Repository db) {
		super();
		this.db = db;
		this.gitdir = db.getDirectory().getAbsolutePath();
		result = new Result();
	}

	/**
	 * Executes git commands (with arguments) specified on the command line. The
	 * git repository (same for all commands) can be specified via system
	 * property "-Dgit_work_tree=path_to_work_tree". If the property is not set,
	 * current directory is used.
	 *
	 * @param args
	 *            each element in the array must be a valid git command line,
	 *            e.g. "git branch -h"
	 * @throws Exception
	 */
	public void mains(String[] args) throws Exception {
		String workDir = "/home/testuser/repos/";
		if (workDir == null) {
			workDir = ".";
			System.out.println(
					"System property 'git_work_tree' not specified, using current directory: "
							+ new File(workDir).getAbsolutePath());
		}
		try (Repository db = new FileRepository(workDir + "/.git")) {
			for (String cmd : args) {
				List<String> result = execute(cmd, db);
				for (String line : result) {
					System.out.println(line);
				}
			}
		}
	}

	public static List<String> execute(String str, Repository db)
			throws Exception {
		Result result = executeRaw(str, db);
		return getOutput(result);
	}

	public static Result executeRaw(String str, Repository db)
			throws Exception {
		CLIGitCommand cmd = new CLIGitCommand(db);
		cmd.run(str);
		return cmd.result;
	}

	public static List<String> executeUnchecked(String str, Repository db)
			throws Exception {
		CLIGitCommand cmd = new CLIGitCommand(db);
		try {
			cmd.run(str);
			return getOutput(cmd.result);
		} catch (Throwable e) {
			return cmd.result.errLines();
		}
	}

	private static List<String> getOutput(Result result) {
		if (result.ex instanceof TerminatedByHelpException) {
			return result.errLines();
		}
		return result.outLines();
	}

	private void run(String commandLine) throws Exception {
		String[] argv = convertToMainArgs(commandLine);
		try {
			super.run(argv);
			writer = createErrorWriter();
		} catch (TerminatedByHelpException e) {
			// this is not a failure, super called exit() on help
		} finally {
			writer.flush();
		}
	}

	private static String[] convertToMainArgs(String str)
			throws Exception {
		String[] args = split(str);
		if ((!args[0].equalsIgnoreCase("git") && !args[0].equalsIgnoreCase("/usr/local/bin/git-lfs")) || args.length < 2) {
			throw new IllegalArgumentException(
					"Expected 'git <command> [<args>]', was:" + str);
		}
		String[] argv = new String[args.length - 1];
		System.arraycopy(args, 1, argv, 0, args.length - 1);
		return argv;
	}

	PrintWriter createErrorWriter() {
		return new PrintWriter(new OutputStreamWriter(System.err, UTF_8));
	}

	void init(final TextBuiltin cmd) throws IOException {
	
	}

	@Override
	protected Repository openGitDir(String aGitdir) throws IOException {
		return db;
	}

	void exit(int status, Exception t) throws Exception {
		if (t == null) {
			t = new IllegalStateException(Integer.toString(status));
		}
		result.ex = t;
		throw t;
	}

	/**
	 * Split a command line into a string array.
	 *
	 * A copy of Gerrit's
	 * com.google.gerrit.sshd.CommandFactoryProvider#split(String)
	 *
	 * @param commandLine
	 *            a command line
	 * @return the array
	 */
	static String[] split(String commandLine) {
		final List<String> list = new ArrayList<>();
		boolean inquote = false;
		boolean inDblQuote = false;
		StringBuilder r = new StringBuilder();
		for (int ip = 0; ip < commandLine.length();) {
			final char b = commandLine.charAt(ip++);
			switch (b) {
			case '\t':
			case ' ':
				if (inquote || inDblQuote)
					r.append(b);
				else if (r.length() > 0) {
					list.add(r.toString());
					r = new StringBuilder();
				}
				continue;
			case '\"':
				if (inquote)
					r.append(b);
				else
					inDblQuote = !inDblQuote;
				continue;
			case '\'':
				if (inDblQuote)
					r.append(b);
				else
					inquote = !inquote;
				continue;
			case '\\':
				if (inDblQuote || inquote || ip == commandLine.length())
					r.append(b); // literal within a quote
				else
					r.append(commandLine.charAt(ip++));
				continue;
			default:
				r.append(b);
				continue;
			}
		}
		if (r.length() > 0)
			list.add(r.toString());
		return list.toArray(new String[list.size()]);
	}

	public static class Result {
		public final ByteArrayOutputStream out = new ByteArrayOutputStream();

		public final ByteArrayOutputStream err = new ByteArrayOutputStream();

		public Exception ex;

		public byte[] outBytes() {
			return out.toByteArray();
		}

		public byte[] errBytes() {
			return err.toByteArray();
		}

		public String outString() {
			return out.toString();
		}

		public List<String> outLines() {
			return IO.readLines(out.toString());
		}

		public String errString() {
			return err.toString();
		}

		public List<String> errLines() {
			return IO.readLines(err.toString());
		}
	}

}