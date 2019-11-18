package fvarrui.cuestionarios.ui.components;

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;

@SuppressWarnings("serial")
public class JLimitedTextField extends JTextField {
	
	public JLimitedTextField(final int limit) {
		super();
		this.setDocument(new PlainDocument() {
			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {   					
				if (!(getLength() + str.length() > limit)) {   
					super.insertString(offset, str, a);   
				}
			}
		});
	}
	
}
