package fvarrui.cuestionarios.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import fvarrui.cuestionarios.gen.HtmlGenerator;
import fvarrui.cuestionarios.model.Cuestionario;
import fvarrui.cuestionarios.model.Hoja;
import fvarrui.cuestionarios.model.utils.Persistencia;
import fvarrui.cuestionarios.ui.components.JIntegerField;
import fvarrui.cuestionarios.ui.components.JToolbarButton;
import fvarrui.cuestionarios.ui.components.ListTableModel;
import fvarrui.cuestionarios.ui.images.Icons;
import fvarrui.cuestionarios.utils.DateUtils;

@SuppressWarnings("serial")
public class CuestionarioFrame extends JFrame {
	
	private static final String FORMATO_FECHA = "dd/MM/yyyy";
	
	private File fichero = null;
	private Boolean hayCambios = false;

	private JFileChooser fileDialog;

	private JPanel contentPane;
	private JTextField temaText;
	private JIntegerField penalizacionText;
	private JTextField fechaText;
	private JTextField solucionText;
	private JTable hojasTable;
	private JIntegerField limiteText;
	private ListTableModel<Hoja> hojasTableModel;

	private Cuestionario cuestionario = new Cuestionario();
	private JLabel preguntasLabel;
	private JMenuItem nuevoMenuItem;
	private JMenuItem abrirMenuItem;
	private JMenuItem guardarMenuItem;
	private JMenuItem guardarComoMenuItem;
	private JMenuItem salirMenuItem;

	public CuestionarioFrame() {
		initFrame();
		initComponents();
	}
	
	private void initFrame() {
		setTitle("Cuestionarios");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 478);
		setLocationRelativeTo(null);
		setIconImage(Icons.QUIZ.getImage());
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				onWindowOpened(e);
			}
		});
	}
	
	private void initComponents() {
		initMenuBar();
		
		JPanel panelCentral = initPanelCentral();
		
		JToolBar toolBar = initToolBar();
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.add(toolBar, BorderLayout.NORTH);
		contentPane.add(panelCentral, BorderLayout.CENTER);
		
		setContentPane(contentPane);

		fileDialog = new JFileChooser();
		fileDialog.setFileFilter(new FileNameExtensionFilter("Cuestionario (*.test)", "test"));
		fileDialog.setCurrentDirectory(new File(System.getProperty("user.home")));
	}

	private JToolBar initToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		JToolbarButton nuevoButton = new JToolbarButton();
		nuevoButton.setIcon(Icons.NEW);
		nuevoButton.setToolTipText("Crear un nuevo cuestionario");
		nuevoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onNuevoButtonActionPerformed(e); }
		});
		toolBar.add(nuevoButton);
		
		JToolbarButton abrirButton = new JToolbarButton();
		abrirButton.setIcon(Icons.OPEN);
		abrirButton.setToolTipText("Abrir un cuestionario");
		abrirButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onAbrirButtonActionPerformed(e); }
		});
		toolBar.add(abrirButton);
		
		JToolbarButton guardarButton = new JToolbarButton();
		guardarButton.setIcon(Icons.SAVE);
		guardarButton.setToolTipText("Guardar el cuestionario");
		guardarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onGuardarButtonActionPerformed(e); }
		});
		toolBar.add(guardarButton);
		
		return toolBar;
	}

	private JPanel initPanelCentral() {
		JPanel panelCentral = new JPanel(new BorderLayout());
		
		JPanel datosPanel = initDatosPanel();
		panelCentral.add(datosPanel, BorderLayout.NORTH);

		JPanel hojasRespuestasPanel = initHojasRespuestasPanel();
		panelCentral.add(hojasRespuestasPanel, BorderLayout.CENTER);

		JPanel botonesPanel = initBotonesPanel();
		panelCentral.add(botonesPanel, BorderLayout.SOUTH);		
		
		return panelCentral;
	}

	private JPanel initBotonesPanel() {
		JPanel panel = new JPanel();

		JButton calcularButton = new JButton("Calcular notas");
		calcularButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onCalcularButtonActionPerformed(e);
			}
		});
		panel.add(calcularButton);
		
		return panel;
	}

	private JPanel initHojasRespuestasPanel() {
		JPanel centroPanel = new JPanel();
		centroPanel.setBorder(BorderFactory.createTitledBorder("Hojas de respuestas"));
		centroPanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		centroPanel.add(scrollPane, BorderLayout.CENTER);

		hojasTableModel = new ListTableModel<Hoja>();
		hojasTableModel.addColumn("Nombre", String.class, false, "nombre");
		hojasTableModel.addColumn("Respuestas", String.class, false, "respuestas");
		hojasTableModel.setValues(cuestionario.getHojas());

		hojasTable = new JTable();
		hojasTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) { onHojasTableMouseClicked(e); }
		});
		hojasTable.setModel(hojasTableModel);
		hojasTable.setFillsViewportHeight(true);
		scrollPane.setViewportView(hojasTable);

		JPanel botonesPanel = new JPanel();
		centroPanel.add(botonesPanel, BorderLayout.EAST);

		JPanel botonesAlineadosPanel = new JPanel();
		botonesPanel.add(botonesAlineadosPanel);
		botonesAlineadosPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton nuevaHojaButton = new JToolbarButton();
		nuevaHojaButton.setIcon(Icons.ADD);
		nuevaHojaButton.setToolTipText("AÒadir una hoja de respuestas");
		nuevaHojaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onNuevaHojaButtonActionPerformed(e); }
		});
		botonesAlineadosPanel.add(nuevaHojaButton);

		JButton eliminarHojaButton = new JToolbarButton();
		eliminarHojaButton.setIcon(Icons.REMOVE);
		eliminarHojaButton.setToolTipText("Eliminar las hojas de respuestas seleccionadas");
		eliminarHojaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { onEliminarHojaButtonActionPerformed(e); }
		});
		botonesAlineadosPanel.add(eliminarHojaButton);

		return centroPanel;
	}

	private JPanel initDatosPanel() {
		
		JPanel datosPanel = new JPanel();
		datosPanel.setBorder(BorderFactory.createTitledBorder("Datos del cuestionario"));

		GridBagLayout gbl_superiorPanel = new GridBagLayout();
		gbl_superiorPanel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_superiorPanel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_superiorPanel.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_superiorPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		datosPanel.setLayout(gbl_superiorPanel);

		JLabel temaLabel = new JLabel("Tema:");
		GridBagConstraints gbc_temaLabel = new GridBagConstraints();
		gbc_temaLabel.anchor = GridBagConstraints.WEST;
		gbc_temaLabel.insets = new Insets(0, 0, 5, 5);
		gbc_temaLabel.gridx = 0;
		gbc_temaLabel.gridy = 0;
		datosPanel.add(temaLabel, gbc_temaLabel);

		temaText = new JTextField();
		temaText.setColumns(10);
		temaText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) { onTextKeyReleased(e); }
		});		
		GridBagConstraints gbc_temaText = new GridBagConstraints();
		gbc_temaText.gridwidth = 3;
		gbc_temaText.insets = new Insets(0, 0, 5, 0);
		gbc_temaText.fill = GridBagConstraints.HORIZONTAL;
		gbc_temaText.gridx = 1;
		gbc_temaText.gridy = 0;
		datosPanel.add(temaText, gbc_temaText);

		JLabel fechaLabel = new JLabel("Fecha:");
		GridBagConstraints gbc_fechaLabel = new GridBagConstraints();
		gbc_fechaLabel.anchor = GridBagConstraints.WEST;
		gbc_fechaLabel.insets = new Insets(0, 0, 5, 5);
		gbc_fechaLabel.gridx = 0;
		gbc_fechaLabel.gridy = 1;
		datosPanel.add(fechaLabel, gbc_fechaLabel);

		fechaText = new JTextField();
		fechaText.setColumns(10);
		fechaText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) { onTextKeyReleased(e); }
		});
		GridBagConstraints gbc_fechaText = new GridBagConstraints();
		gbc_fechaText.anchor = GridBagConstraints.WEST;
		gbc_fechaText.insets = new Insets(0, 0, 5, 5);
		gbc_fechaText.gridx = 1;
		gbc_fechaText.gridy = 1;
		datosPanel.add(fechaText, gbc_fechaText);
		
		JLabel formatoFechaLabel = new JLabel("dd/mm/aaaa");
		GridBagConstraints gbc_formatoFechaLabel = new GridBagConstraints();
		gbc_formatoFechaLabel.anchor = GridBagConstraints.WEST;
		gbc_formatoFechaLabel.insets = new Insets(0, 0, 5, 5);
		gbc_formatoFechaLabel.gridx = 2;
		gbc_formatoFechaLabel.gridy = 1;
		datosPanel.add(formatoFechaLabel, gbc_formatoFechaLabel);

		JLabel limiteLabel = new JLabel("L\u00EDmite:");
		GridBagConstraints gbc_limiteLabel = new GridBagConstraints();
		gbc_limiteLabel.anchor = GridBagConstraints.WEST;
		gbc_limiteLabel.insets = new Insets(0, 0, 5, 5);
		gbc_limiteLabel.gridx = 0;
		gbc_limiteLabel.gridy = 2;
		datosPanel.add(limiteLabel, gbc_limiteLabel);

		limiteText = new JIntegerField();
		limiteText.setColumns(10);
		limiteText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) { onTextKeyReleased(e); }
		});
		GridBagConstraints gbc_limiteText = new GridBagConstraints();
		gbc_limiteText.anchor = GridBagConstraints.WEST;
		gbc_limiteText.insets = new Insets(0, 0, 5, 5);
		gbc_limiteText.gridx = 1;
		gbc_limiteText.gridy = 2;
		datosPanel.add(limiteText, gbc_limiteText);

		JLabel aclaracionLimiteLabel = new JLabel("puntos para aprobar");
		GridBagConstraints gbc_aclaracionLimiteLabel = new GridBagConstraints();
		gbc_aclaracionLimiteLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_aclaracionLimiteLabel.insets = new Insets(0, 0, 5, 5);
		gbc_aclaracionLimiteLabel.gridx = 2;
		gbc_aclaracionLimiteLabel.gridy = 2;
		datosPanel.add(aclaracionLimiteLabel, gbc_aclaracionLimiteLabel);

		JLabel solucionLabel = new JLabel("Soluci\u00F3n:");
		GridBagConstraints gbc_solucionLabel = new GridBagConstraints();
		gbc_solucionLabel.anchor = GridBagConstraints.WEST;
		gbc_solucionLabel.insets = new Insets(0, 0, 5, 5);
		gbc_solucionLabel.gridx = 0;
		gbc_solucionLabel.gridy = 3;
		datosPanel.add(solucionLabel, gbc_solucionLabel);

		solucionText = new JTextField();
		solucionText.setColumns(10);
		solucionText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				onSolucionTextKeyReleased(e);
			}
		});
		GridBagConstraints gbc_solucionText = new GridBagConstraints();
		gbc_solucionText.gridwidth = 2;
		gbc_solucionText.insets = new Insets(0, 0, 5, 5);
		gbc_solucionText.fill = GridBagConstraints.HORIZONTAL;
		gbc_solucionText.gridx = 1;
		gbc_solucionText.gridy = 3;
		datosPanel.add(solucionText, gbc_solucionText);

		preguntasLabel = new JLabel("0");
		preguntasLabel.setPreferredSize(new Dimension(50, 16));
		GridBagConstraints gbc_preguntasLabel = new GridBagConstraints();
		gbc_preguntasLabel.anchor = GridBagConstraints.EAST;
		gbc_preguntasLabel.insets = new Insets(0, 0, 5, 0);
		gbc_preguntasLabel.gridx = 3;
		gbc_preguntasLabel.gridy = 3;
		datosPanel.add(preguntasLabel, gbc_preguntasLabel);

		JLabel penalizacionLabel = new JLabel("Penalizaci\u00F3n:");
		GridBagConstraints gbc_penalizacionLabel = new GridBagConstraints();
		gbc_penalizacionLabel.anchor = GridBagConstraints.WEST;
		gbc_penalizacionLabel.insets = new Insets(0, 0, 0, 5);
		gbc_penalizacionLabel.gridx = 0;
		gbc_penalizacionLabel.gridy = 4;
		datosPanel.add(penalizacionLabel, gbc_penalizacionLabel);

		penalizacionText = new JIntegerField();
		penalizacionText.setColumns(10);
		penalizacionText.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) { onTextKeyReleased(e); }
		});
		GridBagConstraints gbc_anuladasText = new GridBagConstraints();
		gbc_anuladasText.insets = new Insets(0, 0, 0, 5);
		gbc_anuladasText.anchor = GridBagConstraints.WEST;
		gbc_anuladasText.gridx = 1;
		gbc_anuladasText.gridy = 4;
		datosPanel.add(penalizacionText, gbc_anuladasText);

		JLabel aclaracionPenalizacionLabel = new JLabel("preguntas incorrectas descuentan 1 punto");
		GridBagConstraints gbc_aclaracionPenalizacionLabel = new GridBagConstraints();
		gbc_aclaracionPenalizacionLabel.insets = new Insets(0, 0, 0, 5);
		gbc_aclaracionPenalizacionLabel.anchor = GridBagConstraints.WEST;
		gbc_aclaracionPenalizacionLabel.gridx = 2;
		gbc_aclaracionPenalizacionLabel.gridy = 4;
		datosPanel.add(aclaracionPenalizacionLabel, gbc_aclaracionPenalizacionLabel);
		
		return datosPanel;
	}

	private void initMenuBar() {

		nuevoMenuItem = new JMenuItem("Nuevo");
		nuevoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
		nuevoMenuItem.setIcon(Icons.NEW_SMALL);
		nuevoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onArchivoMenuItemActionPerformed(e);
			}
		});

		abrirMenuItem = new JMenuItem("Abrir...");
		abrirMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));
		abrirMenuItem.setIcon(Icons.OPEN_SMALL);
		abrirMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onArchivoMenuItemActionPerformed(e);
			}
		});

		guardarMenuItem = new JMenuItem("Guardar");
		guardarMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));
		guardarMenuItem.setIcon(Icons.SAVE_SMALL);
		guardarMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onArchivoMenuItemActionPerformed(e);
			}
		});

		guardarComoMenuItem = new JMenuItem("Guardar como...");
		guardarComoMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onArchivoMenuItemActionPerformed(e);
			}
		});

		salirMenuItem = new JMenuItem("Salir");
		salirMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
		salirMenuItem.setIcon(Icons.EXIT_SMALL);
		salirMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				onArchivoMenuItemActionPerformed(e);
			}
		});

		JMenu archivoMenu = new JMenu("Archivo");
		archivoMenu.add(nuevoMenuItem);
		archivoMenu.add(abrirMenuItem);
		archivoMenu.add(guardarMenuItem);
		archivoMenu.add(guardarComoMenuItem);
		archivoMenu.addSeparator();
		archivoMenu.add(salirMenuItem);

		JMenuBar menuBar = new JMenuBar();
		menuBar.add(archivoMenu);
		
		setJMenuBar(menuBar);
	}

	protected void onHojasTableMouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int seleccionado = hojasTable.getSelectedRow();
			Hoja hoja = hojasTableModel.getValues().get(seleccionado);
			
			HojaRespuestasDialog hrd = new HojaRespuestasDialog(this, hoja);
			hrd.setVisible(true);
			
			hojasTable.repaint();
			hojasTable.getSelectionModel().setSelectionInterval(seleccionado, seleccionado);

		}
	}

	protected void onEliminarHojaButtonActionPerformed(ActionEvent e) {
		if (hojasTable.getSelectedRowCount() > 0) {
			int respuesta = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar las " + hojasTable.getSelectedRowCount() + " hoja(s) seleccionadas?", "Eliminar hojas", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if (respuesta == JOptionPane.YES_OPTION) {
				int [] seleccionados = hojasTable.getSelectedRows();
				for (int seleccionado = seleccionados.length - 1; seleccionado >= 0; seleccionado--) {
					cuestionario.getHojas().remove(seleccionado);
				}
				hojasTable.repaint();
			}
		}
	}

	protected void onTextKeyReleased(KeyEvent e) {
		hayCambios = true;
	}

	protected void onArchivoMenuItemActionPerformed(ActionEvent e) {
		if (e.getSource() == nuevoMenuItem) 
			nuevo();
		else if (e.getSource() == abrirMenuItem)
			abrir();
		else if (e.getSource() == guardarMenuItem)
			guardar();
		else if (e.getSource() == guardarComoMenuItem)
			guardarComo();
		else if (e.getSource() == salirMenuItem)
			salir();
	}

	private void salir() {
		int respuesta = JOptionPane.NO_OPTION;
		if (hayCambios) {
			respuesta = JOptionPane.showConfirmDialog(this, "Hay cambios en el cuestionario actual sin guardar.\n\n¿Desea guardar los cambios antes de salir?", "Salir", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		}
		if (respuesta == JOptionPane.NO_OPTION) {
			dispose();
		} else if (respuesta == JOptionPane.YES_OPTION) {
			guardar();
			salir();
		}
	}

	private void guardarComo() {
		int respuesta = fileDialog.showSaveDialog(this);
		if (respuesta == JFileChooser.APPROVE_OPTION) {
			fichero = fileDialog.getSelectedFile();
			guardar();
		}
	}

	private void guardar() {
		if (fichero == null) {
			guardarComo();
		} else {
			try {
				saveCuestionario();
				Persistencia.guardar(cuestionario, fichero);
				hayCambios = false;
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void abrir() {
		int respuesta = fileDialog.showOpenDialog(this);
		if (respuesta == JFileChooser.APPROVE_OPTION) {
			if (hayCambios) {
				respuesta = JOptionPane.showConfirmDialog(this, "Hay cambios en el cuestionario actual sin guardar.\n\n¿Desea guardar los cambios antes de abrir un nuevo cuestionario?", "Abrir cuestionario", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (respuesta == JOptionPane.YES_OPTION) {
					guardar();
				}
			}
			if (respuesta != JOptionPane.CANCEL_OPTION) {
				fichero = fileDialog.getSelectedFile();
				hayCambios = false;
				try {
					cuestionario = Persistencia.leer(fichero);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
				loadCuestionario();
			}
		}
	}

	private void nuevo() {
		int respuesta = JOptionPane.NO_OPTION;
		if (hayCambios) {
			respuesta = JOptionPane.showConfirmDialog(this, "Hay cambios en el cuestionario actual sin guardar.\n\n¿Desea guardar los cambios?", "Nuevo cuestionario", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
		}
		if (respuesta == JOptionPane.NO_OPTION) {
			fichero = null;
			hayCambios = false;
			cuestionario = new Cuestionario();
			loadCuestionario();
		} else if (respuesta == JOptionPane.YES_OPTION) {
			guardar();
			nuevo();
		}
	}

	protected void onWindowOpened(WindowEvent e) {
		loadCuestionario();
	}

	protected void onNuevaHojaButtonActionPerformed(ActionEvent e) {
		HojaRespuestasDialog hrd = new HojaRespuestasDialog(this, null);
		hrd.setTotalRespuestas(solucionText.getText().length());
		hrd.setVisible(true);
	}

	protected void onSolucionTextKeyReleased(KeyEvent e) {
		preguntasLabel.setText("" + solucionText.getText().length());
	}

	private void loadCuestionario() {
		try {
			temaText.setText(cuestionario.getTema());
			fechaText.setText(DateUtils.formatDate(cuestionario.getFecha(), FORMATO_FECHA));
			penalizacionText.setInteger(cuestionario.getPenalizacion());
			limiteText.setInteger(cuestionario.getLimite());
			solucionText.setText(cuestionario.getSolucion());
			hojasTableModel.setValues(cuestionario.getHojas());
			preguntasLabel.setText("" + solucionText.getText().length());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void saveCuestionario() {
		try {
			cuestionario.setTema(temaText.getText());
			cuestionario.setFecha(DateUtils.parseDate(fechaText.getText(), FORMATO_FECHA));
			cuestionario.setPenalizacion(penalizacionText.getInteger());
			cuestionario.setLimite(limiteText.getInteger());
			cuestionario.setSolucion(solucionText.getText());
			cuestionario.setHojas(hojasTableModel.getValues());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	protected void onCalcularButtonActionPerformed(ActionEvent e) {
		try {
			saveCuestionario();
			cuestionario.corregir();
			File html = File.createTempFile("cuestionarios-", ".html");
			HtmlGenerator.generate(html, cuestionario);
			Desktop.getDesktop().browse(html.toURI());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void nuevaHoja(Hoja hoja) {
		hayCambios = true;
		cuestionario.getHojas().add(hoja);
		hojasTable.repaint();
	}

	protected void onNuevoButtonActionPerformed(ActionEvent e) {
		nuevo();		
	}

	protected void onAbrirButtonActionPerformed(ActionEvent e) {
		abrir();		
	}
	
	protected void onGuardarButtonActionPerformed(ActionEvent e) {
		guardar();		
	}

}
