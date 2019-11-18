package fvarrui.cuestionarios.ui.images;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Icons {
	
	public static final ImageIcon ADD = loadIcon("add.png");
	public static final ImageIcon EDIT = loadIcon("edit.png");
	public static final ImageIcon NEW = loadIcon("new.png");
	public static final ImageIcon OPEN = loadIcon("open.png");
	public static final ImageIcon QUIZ = loadIcon("quiz.png");
	public static final ImageIcon REMOVE = loadIcon("remove.png");
	public static final ImageIcon SAVE = loadIcon("save.png");

	public static final ImageIcon NEW_SMALL = loadIcon("new.png", 16);
	public static final ImageIcon OPEN_SMALL = loadIcon("open.png", 16);
	public static final ImageIcon SAVE_SMALL = loadIcon("save.png", 16);
	public static final ImageIcon EXIT_SMALL = loadIcon("exit.png", 16);

	public static ImageIcon loadIcon(String name) {
		return new ImageIcon(Icons.class.getResource(name));
	}

	public static ImageIcon loadIcon(String name, int size) {
		ImageIcon icon = new ImageIcon(Icons.class.getResource(name));
		return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
	}

}
