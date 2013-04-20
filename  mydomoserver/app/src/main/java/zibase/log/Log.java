package zibase.log;

import javax.swing.JDialog;

public class Log {

	static LogDialog logDialog;

	public Log() {

	}

	public static void info(String text) {
		if (logDialog != null) {
			logDialog.addLog(text);
		}
		
		System.out.println(text);
	}

	public static JDialog getJDialog() {
		if (logDialog == null)
			logDialog = new LogDialog();

		return logDialog;

	}

}
