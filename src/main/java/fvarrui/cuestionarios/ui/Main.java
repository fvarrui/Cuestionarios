package fvarrui.cuestionarios.ui;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {		
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					new CuestionarioFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error al iniciar la aplicación:\n\n" + e.getMessage(), "Cuestionarios", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

}
