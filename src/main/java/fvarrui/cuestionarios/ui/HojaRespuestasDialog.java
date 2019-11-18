package fvarrui.cuestionarios.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import fvarrui.cuestionarios.model.Hoja;
import fvarrui.cuestionarios.ui.components.JLimitedTextField;
import fvarrui.cuestionarios.ui.layouts.WrapLayout;

@SuppressWarnings("serial")
public class HojaRespuestasDialog extends JDialog {

	private CuestionarioFrame padre;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField nombreText;

	private String respuestas;
	private JPanel respuestasPanel;
	private List<JTextField> textos = new ArrayList<JTextField>();
	private Hoja hoja;
	private JButton aceptarButton;
	private JButton cancelarButton;

	public HojaRespuestasDialog(CuestionarioFrame padre, Hoja hoja) {
		this.padre = padre;
		this.hoja = hoja;
		setModal(true);
		setTitle("Hoja de respuestas");
		setBounds(100, 100, 950, 600);
		setLocationRelativeTo(padre);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JPanel nombrePanel = new JPanel();
			contentPanel.add(nombrePanel, BorderLayout.NORTH);
			GridBagLayout gbl_nombrePanel = new GridBagLayout();
			gbl_nombrePanel.columnWidths = new int[] { 0, 0, 0 };
			gbl_nombrePanel.rowHeights = new int[] { 0, 0 };
			gbl_nombrePanel.columnWeights = new double[] { 0.0, 1.0,
					Double.MIN_VALUE };
			gbl_nombrePanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
			nombrePanel.setLayout(gbl_nombrePanel);
			{
				JLabel nombreLabel = new JLabel("Nombre:");
				GridBagConstraints gbc_nombreLabel = new GridBagConstraints();
				gbc_nombreLabel.insets = new Insets(0, 0, 0, 5);
				gbc_nombreLabel.anchor = GridBagConstraints.EAST;
				gbc_nombreLabel.gridx = 0;
				gbc_nombreLabel.gridy = 0;
				nombrePanel.add(nombreLabel, gbc_nombreLabel);
			}
			{
				nombreText = new JTextField();
				GridBagConstraints gbc_nombreText = new GridBagConstraints();
				gbc_nombreText.fill = GridBagConstraints.HORIZONTAL;
				gbc_nombreText.gridx = 1;
				gbc_nombreText.gridy = 0;
				nombrePanel.add(nombreText, gbc_nombreText);
				nombreText.setColumns(10);
			}
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportBorder(null);
			scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scrollPane.setBorder(BorderFactory.createEmptyBorder());
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				respuestasPanel = new JPanel();
				scrollPane.setViewportView(respuestasPanel);
				WrapLayout wl_respuestasPanel = new WrapLayout();
				wl_respuestasPanel.setAlignment(FlowLayout.LEFT);
				respuestasPanel.setLayout(wl_respuestasPanel);
			}
		}
		{
			JPanel botonesPanel = new JPanel();
			botonesPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(botonesPanel, BorderLayout.SOUTH);
			{
				aceptarButton = new JButton("A\u00F1adir");
				aceptarButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onAceptarButton(e);
					}
				});
				aceptarButton.setActionCommand("OK");
				botonesPanel.add(aceptarButton);
				getRootPane().setDefaultButton(aceptarButton);
			}
			{
				cancelarButton = new JButton("Cerrar");
				cancelarButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						onCancelarButton(e);
					}
				});
				{
					JButton limpiarButton = new JButton("Limpiar");
					limpiarButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							onLimpiarButton();
						}
					});
					botonesPanel.add(limpiarButton);
				}
				cancelarButton.setActionCommand("Cancel");
				botonesPanel.add(cancelarButton);
			}
		}
		inicializar();
	}

	private void inicializar() {
		if (hoja != null) {
			nombreText.setText(hoja.getNombre());
			setRespuestas(hoja.getRespuestas());
			aceptarButton.setText("Guardar cambios");
			cancelarButton.setText("Cancelar");
			setTotalRespuestas(hoja.getRespuestas().length());
		}
	}

	protected void onLimpiarButton() {
		limpiar();
	}

	protected void onCancelarButton(ActionEvent e) {
		dispose();
	}
	
	private void limpiar() {
		nombreText.setText("");
		for (JTextField texto : textos) {
			texto.setText("-");
		}
		nombreText.requestFocus();
	}
	
	private void textosToRespuestas() {
		respuestas = "";
		for (JTextField text : textos) {
			if (text.getText().isEmpty() || " ".equals(text.getText()))
				respuestas += "-";
			else
				respuestas += text.getText();
		}
	}

	protected void onAceptarButton(ActionEvent e) {
		if (nombreText.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe especificar el nombre", "Error", JOptionPane.ERROR_MESSAGE);
			nombreText.requestFocus();
		} else if (hoja == null) {
			textosToRespuestas();
			padre.nuevaHoja(new Hoja(nombreText.getText(), respuestas));
			limpiar();
			nombreText.requestFocus();
		} else {
			textosToRespuestas();
			hoja.setNombre(nombreText.getText());
			hoja.setRespuestas(respuestas);
			dispose();
		}
	}

	public String getRespuestas() {
		return respuestas.toUpperCase();
	}

	public void setRespuestas(String respuestas) {
		this.respuestas = respuestas.toUpperCase();
	}
	
	public String getNombre() {
		return nombreText.getText();
	}
	
	public void setNombre(String nombre) {
		nombreText.setText(nombre);
	}

	public void setTotalRespuestas(Integer totalRespuestas) {
		respuestasPanel.removeAll();
		textos.clear();
		for (int i = 1; i <= totalRespuestas; i++) {
			char respuesta = (respuestas != null && respuestas.length() >= i) ? respuestas.charAt(i - 1) : '-';
			
			JLabel label = new JLabel("" + i);
			label.setPreferredSize(new Dimension(30, label.getPreferredSize().height));
			label.setHorizontalAlignment(JLabel.RIGHT);
			
			final JTextField text = new JLimitedTextField(1);
			text.setText("" + respuesta);
			text.setColumns(4);
			text.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyChar() != KeyEvent.VK_BACK_SPACE && 
						e.getKeyChar() != KeyEvent.VK_DELETE) 
						text.transferFocus();
				}
			});
			text.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					text.selectAll();
				}
				@Override
				public void focusLost(FocusEvent e) {
					if (text.getText().isEmpty()) text.setText("-");
					text.setText(text.getText().toUpperCase());
				}
			});
			
			textos.add(text);
			
			JPanel panel = new JPanel();
			panel.setBorder(null);
			panel.add(label);
			panel.add(text);
			
			respuestasPanel.add(panel);

		}
		respuestasPanel.revalidate();
		respuestasPanel.repaint();
	}

}
