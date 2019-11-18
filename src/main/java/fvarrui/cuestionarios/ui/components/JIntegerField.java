package fvarrui.cuestionarios.ui.components;



@SuppressWarnings("serial")
public class JIntegerField extends JNumberField {

	@Override
	protected Number parseNumber(String text) throws NumberFormatException {
		if ("-".equals(text)) text = "-0";
		return Integer.parseInt(text);
	}
	
	public Integer getInteger() {
		return (Integer) getNumber();
	}
	
	public void setInteger(Integer value) {
		setNumber(value);
	}

}

