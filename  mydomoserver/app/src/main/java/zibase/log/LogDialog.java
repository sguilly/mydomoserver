package zibase.log;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class LogDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	private JTextArea textArea = new JTextArea();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			LogDialog dialog = new LogDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public LogDialog() {
		
		setBounds(100, 100, 600, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JScrollPane zoneScrolable = new JScrollPane(textArea);
			zoneScrolable.setPreferredSize(new Dimension(560, 200));
			textArea.setAutoscrolls(true);

			contentPanel.add(zoneScrolable);

		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton clearButton = new JButton("Clear");
				clearButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clearLog();
					}
				});
				clearButton.setActionCommand("Clear");
				buttonPane.add(clearButton);
				getRootPane().setDefaultButton(clearButton);
			}
		}
	}

	void addLog(String logString) {
		if (this.isShowing() == true) {
			textArea.append(logString + "\n\r");
		}
	}
	
	void clearLog()
	{
		textArea.setText("");
	}

}
