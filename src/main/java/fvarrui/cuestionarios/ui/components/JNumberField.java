package fvarrui.cuestionarios.ui.components;

import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public abstract class JNumberField extends JTextFieldWithPrompt {
	
	public JNumberField() {
		super();
		setHorizontalAlignment(JNumberField.RIGHT);
	}
	
	@Override
	protected void onKeyTyped(KeyEvent e) {
		char pressedKey = e.getKeyChar();
		if (!Character.isISOControl(pressedKey)) {
			try { 
				int pos = getCaretPosition();
				String start = getText().substring(0, pos);
				String end = getText().substring(pos, getText().length());
				parseNumber(start + pressedKey + end);
				super.onKeyTyped(e);
			} catch (NumberFormatException e1) {
				e.consume();
			}
		} else {
			super.onKeyTyped(e);
		}
	}
	
	public void setNumber(Number value) {
		if (value != null)
			setText("" + value);
		else
			setText("");
	}
	
	public Number getNumber() {
		if (getText().isEmpty()) {
			return null;
		}
		return parseNumber(getText());
	}
	
	@Override
	public void setText(String t) {
		try {
			parseNumber(t);
			super.setText(t);
		} catch (NumberFormatException e) {
			super.setText("");
		}
	}
	
	protected abstract Number parseNumber(String text) throws NumberFormatException;

}

