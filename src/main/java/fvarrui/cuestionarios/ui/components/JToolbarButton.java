package fvarrui.cuestionarios.ui.components;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class JToolbarButton extends JButton {

	public JToolbarButton() {
		super();
		setOpaque(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setMargin(new Insets(0, 0, 0, 0));
		setFocusable(false);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) { onMouseEntered(e); }
			public void mouseExited(MouseEvent e) { onMouseExited(e); }
		});
	}

	protected void onMouseEntered(MouseEvent e) {
		setContentAreaFilled(true);
	}

	protected void onMouseExited(MouseEvent e) {
		setContentAreaFilled(false);
	}
	
}
