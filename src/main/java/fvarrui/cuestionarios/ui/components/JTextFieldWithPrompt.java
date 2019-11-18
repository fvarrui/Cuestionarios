package fvarrui.cuestionarios.ui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Highlighter;

@SuppressWarnings("serial")
public class JTextFieldWithPrompt extends JTextField {
	
	private static final Color PROMPT_COLOR = Color.LIGHT_GRAY;
	
	private Boolean promptMode = false;
	private String prompt = "";
	
	private Color fontColor; 
	private Font currentFont; 
	private Highlighter highlighter;

	public JTextFieldWithPrompt() {
		super();
		fontColor = getForeground();
		currentFont = getFont();
		highlighter = getHighlighter();
		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) { onKeyTyped(e); }
		});
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { onMouseReleased(e); }
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) { onMouseDragged(e); }
		});
		addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent e) {
				if (promptMode) {
					removeCaretListener(this);
					setCaretPosition(0);
					addCaretListener(this);
				}
			}
		});
	}

	protected void onMouseDragged(MouseEvent e) {
		if (promptMode) {
			select(0, 0);
			setCaretPosition(0);
		}	
	}

	protected void onMouseReleased(MouseEvent e) {
		if (promptMode) {
			select(0, 0);
			setCaretPosition(0);
		}
	}

	protected void onKeyTyped(KeyEvent e) {
		if (promptMode && !Character.isISOControl(e.getKeyChar()) && e.getKeyChar() != KeyEvent.VK_RIGHT) {
			setPromptMode(false);
		} else if (super.getText().isEmpty()) {
			setPromptMode(true);
		}
	}
	
	@Override
	public void setHighlighter(Highlighter h) {
		super.setHighlighter(h);
		highlighter = h;
	}
	
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		currentFont = f;
	}
	
	@Override
	public void setForeground(Color fg) {
		super.setForeground(fg);
		fontColor = fg;
	}
	
	@Override
	public String getText() {
		if (promptMode) return "";
		return super.getText();
	}
	
	@Override
	public void setText(String t) {
		if (t == null || t.isEmpty()) {
			setPromptMode(true);
		} else {
			setPromptMode(false);
			super.setText(t);
		}
	}
	
	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
		if (super.getText().isEmpty()) {
			setText(null);
		}
	}
	
	protected void setPromptMode(Boolean promptMode) {
		if (this.promptMode != promptMode) {
			this.promptMode = promptMode;
			if (promptMode) {
				super.setText(this.prompt);
				this.setCaretPosition(0);
				super.setFont(currentFont.deriveFont(Font.ITALIC));
				super.setForeground(PROMPT_COLOR);
				super.setHighlighter(null);
			} else {
				this.promptMode = promptMode;
				super.setText("");
				super.setFont(currentFont);
				super.setForeground(fontColor);
				super.setHighlighter(highlighter);
			}
		}
	}
	
	protected Boolean isPromptMode() {
		return this.promptMode;
	}

}