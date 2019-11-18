package fvarrui.cuestionarios.ui.components;



@SuppressWarnings("serial")
public class JDoubleField extends JNumberField {

	@Override
	protected Number parseNumber(String text) throws NumberFormatException {
		if (text == null) return null;
		if ("-".equals(text)) text = "-0";
		if (text.endsWith("e")) text = text + "0";
		if (text.startsWith("e")) text = 1 + text;
		return Double.parseDouble(text);
	}
	
	public Double getDouble() {
		return (Double) getNumber();
	}
	
	public void setDouble(Double value) {
		setNumber(value);
	}

}

