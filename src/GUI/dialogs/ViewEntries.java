package GUI.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import parts.Element;
import parts.Spent;
import GUI.Frame;
import GUI.handlers.AccountHandler;

public class ViewEntries extends Dialog implements MouseListener {

	private Element elm;

	private Spent[] entries;

	private JTable table;
	private JScrollPane tableS;

	private JButton add;
	private JButton remove;

	private AccountHandler handler;

	public ViewEntries(Frame frame, Element elm, AccountHandler handler) {
		super(frame);
		this.elm = elm;
		this.handler = handler;
		setSize(300, 355);
		setLocation();

		setLayout(null);

		update();

		iniTable();

		tableS = new JScrollPane(table);
		tableS.setSize(285, 285);
		tableS.setLocation(5, 5);
		add(tableS);

		iniButtons();

	}

	private void iniTable() {

		final TableCellRenderer taRend = new TableCellRenderer() {

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				JTextArea tmp = new JTextArea((String) value);
				tmp.setBackground(isSelected ? new Color(180, 200, 255)
				: Color.WHITE);
				if (isSelected && hasFocus) {
					tmp.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1,
							new Color(120, 140, 255)));
				}
				return tmp;
			}
		};

		table = new JTable(new TableModel() {

			@Override
			public void addTableModelListener(TableModelListener l) {
				// Do nothing

			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {

				Class out = null;
				if (columnIndex == 0) {
					out = Date.class;
				} else if (columnIndex == 1) {
					out = Double.class;
				} else if (columnIndex == 2) {
					out = String.class;
				}

				return out;
			}

			@Override
			public int getColumnCount() {
				return 3;
			}

			@Override
			public String getColumnName(int columnIndex) {
				String out = "";
				if (columnIndex == 0) {
					out = "Date";
				} else if (columnIndex == 1) {
					out = "Amount";
				} else if (columnIndex == 2) {
					out = "Description";
				}
				return out;
			}

			@Override
			public int getRowCount() {
				return entries.length;
			}

			@Override
			public Object getValueAt(int rowIndex, int columnIndex) {
				Spent tmp = entries[rowIndex];
				Object out = null;
				if (columnIndex == 0) {
					out = tmp.getDate();
				} else if (columnIndex == 1) {
					out = tmp.getAmount();
				} else if (columnIndex == 2) {
					out = tmp.getDescription();
				}

				return out;
			}

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}

			@Override
			public void removeTableModelListener(TableModelListener l) {
				// Do nothing

			}

			@Override
			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				// Not applicable

			}

		}) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public TableCellRenderer getCellRenderer(int row, int column) {
				if ((column == 2)) {
					return taRend;
				}
				// else...
				return super.getCellRenderer(row, column);
			}

		};

		table.setAutoCreateRowSorter(true);

		resizeRows();

		table.addMouseListener(this);
		
		// table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private void resizeRows() {
		for (int i = 0; i < table.getModel().getRowCount(); i++) {
			Component c = table.getCellRenderer(i, 2)
					.getTableCellRendererComponent(table,
							table.getModel().getValueAt(i, 2), false, false, i,
							2);
			table.setRowHeight(i, (int) c.getPreferredSize().getHeight());
		}
	}

	private void iniButtons() {
		add = new JButton("Add");
		add.setSize(80, 25);
		add.setLocation(getWidth() / 2 - add.getWidth(), 295);
		add.addActionListener(this);
		add(add);

		remove = new JButton("Remove");
		remove.setSize(90, 25);
		remove.setLocation(getWidth() / 2 + 5, 295);
		remove.addActionListener(this);
		add(remove);

	}

	private void update() {
		entries = elm.getSpent();
		if (table != null) {
			table.doLayout();
			table.tableChanged(new TableModelEvent(table.getModel()));
			resizeRows();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(add)) {

			handler.add();
			update();

		} else if (e.getSource().equals(remove)) {
			int tmp = JOptionPane.showConfirmDialog(this,
					"Do you realy want to remove the selected entries?",
					"Remove entries?", JOptionPane.YES_NO_OPTION);

			if (tmp == JOptionPane.YES_OPTION) {
				int[] selection = table.getSelectedRows();
				for (int i = 0; i < selection.length; i++) {
					Spent tmpS = entries[selection[i]];
					elm.removeSpent(tmpS);
				}
				update();
			}

		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() !=2){
			return;

		}
		
		int selection = table.getSelectedRow();
		Spent sp = entries[selection];
		handler.edit(sp);
		update();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// Do nothing

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// Do nothing

	}

}
