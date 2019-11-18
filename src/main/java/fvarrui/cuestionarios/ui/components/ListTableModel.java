package fvarrui.cuestionarios.ui.components;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * Modelo de datos genérico para el JTable, que gestiona los datos a presentar como
 * un "List" de objetos. Para añadirle columnas se usa el método "addColumn". 
 * @author Fran Vargas
 * @param <E> Clase de objetos que contendrá el modelo 
 */
@SuppressWarnings("serial")
public class ListTableModel<E> extends AbstractTableModel {

	private List<ColumnDefinition> columnDefinitions;
	private List<E> objects = new ArrayList<E>();

	public ListTableModel() {
		columnDefinitions = new ArrayList<ColumnDefinition>();
	}

	@Override
	public Class<?> getColumnClass(int column) {
		return columnDefinitions.get(column).getClazz();
	}

	@Override
	public int getColumnCount() {
		return columnDefinitions.size();
	}

	@Override
	public String getColumnName(int column) {
		return columnDefinitions.get(column).getTitle();
	}

	@Override
	public int getRowCount() {
		return objects.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		ColumnDefinition cd = columnDefinitions.get(column);
		String propertyName = cd.getPropertyName();
		Object object = objects.get(row);
		return get(object, propertyName);
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return columnDefinitions.get(column).isEditable();
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		ColumnDefinition cd = columnDefinitions.get(column);
		String propertyName = cd.getPropertyName();
		Object object = objects.get(row);
		set(object, propertyName, value);
		fireTableCellUpdated(row, column);
	}

	public List<E> getValues() {
		return objects;
	}

	public void setValues(List<E> objects) {
		this.objects = objects;
		fireTableDataChanged();
	}

	public List<ColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}
	
	public void addColumn(String title, Class<?> clazz, Boolean editable, String propertyName) {
		getColumnDefinitions().add(new ColumnDefinition(title, clazz, editable, propertyName));
	}

	public static boolean set(Object object, String fieldName, Object fieldValue) {
	    Class<?> clazz = object.getClass();
	    while (clazz != null) {
	        try {
	            Field field = clazz.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            field.set(object, fieldValue);
	            return true;
	        } catch (NoSuchFieldException e) {
	            clazz = clazz.getSuperclass();
	        } catch (Exception e) {
	            return false;
	        }
	    }
	    return false;
	}
	
	@SuppressWarnings("unchecked")
	public static <E> E get(Object object, String fieldName) {
	    Class<?> clazz = object.getClass();
	    while (clazz != null) {
	        try {
	            Field field = clazz.getDeclaredField(fieldName);
	            field.setAccessible(true);
	            return (E) field.get(object);
	        } catch (NoSuchFieldException e) {
	            clazz = clazz.getSuperclass();
	        } catch (Exception e) {
	            return null;
	        }
	    }
	    return null;
	}
	
	public void clear() {
		objects.clear();
		fireTableDataChanged();
	}
	
	protected class ColumnDefinition {
		private String title;
		private Class<?> clazz;
		private Boolean editable;
		private String propertyName;

		public ColumnDefinition(String title, Class<?> clazz, Boolean editable, String propertyName) {
			this.title = title;
			this.clazz = clazz;
			this.editable = editable;
			this.propertyName = propertyName;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public Class<?> getClazz() {
			return clazz;
		}

		public void setClazz(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Boolean isEditable() {
			return editable;
		}

		public void setEditable(Boolean editable) {
			this.editable = editable;
		}

		public String getPropertyName() {
			return propertyName;
		}

		public void setPropertyName(String propertyName) {
			this.propertyName = propertyName;
		}

	}

	
}
