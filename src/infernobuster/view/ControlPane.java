package infernobuster.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.*;

import infernobuster.controller.Controller;
import infernobuster.model.Anomaly;
import infernobuster.model.FWType;
import infernobuster.model.Model;
import infernobuster.model.RuleListener;

public class ControlPane extends JPanel implements RuleListener {
	private static final long serialVersionUID = 1702526798477578409L;
	
	private Model model;
	private Table table;
	private HashMap<Anomaly,AnomalyLabel> labels;
	private HashMap<Anomaly,JCheckBox> filters;
	private JMenuItem export;
	private JButton add;
	private JButton remove;
	private JButton focus;
	private JButton unfocus;
	
	private Controller controller;
	
	public ControlPane(Model model) {
		this.model = model;
		
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		
		JMenuItem open = new JMenuItem("Open File..");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FWType type = controller.selectType();
				if (type != null) {
					controller.openFile(type);
				}
			}
		});
		
		JPanel toolPanel = new JPanel();
		toolPanel.setLayout(new BorderLayout());
		
		JPanel filterPanel = new JPanel();
		filterPanel.setLayout(new GridLayout(2,4));
		filterPanel.setBorder(BorderFactory.createTitledBorder("Filters"));
		
		filters = new HashMap<Anomaly,JCheckBox>();
		for (Anomaly anomaly : Anomaly.values()) {
            JCheckBox checkBox = new JCheckBox(anomaly.toString());
            checkBox.setEnabled(false);
            
            filters.put(anomaly, checkBox);
            filterPanel.add(checkBox);
            checkBox.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					controller.handleCheckBox(e);
				}
            });
        }
		
		JPanel statsPanel = new JPanel();
		statsPanel.setLayout(new GridLayout(4,2));
		statsPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
		
		labels = new HashMap<Anomaly,AnomalyLabel>();
		for (Anomaly anomaly : Anomaly.values()) {
            AnomalyLabel label = new AnomalyLabel(anomaly, anomaly.toString() + ": 0");
            
            statsPanel.add(label);
            labels.put(anomaly, label);
        }
		
		JPanel ruleControlPanel = new JPanel();
		remove = new JButton("Remove");
		remove.setEnabled(false);
		remove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.removeRule();
			}
		});
		
		add = new JButton("Add");
		add.setEnabled(false);
		
		add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.addRule();
			}
		});
		
		focus = new JButton("Focus");
		focus.setEnabled(false);
		focus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.focus();
			}
		});
		
		unfocus = new JButton("Unfocus");
		unfocus.setEnabled(false);
		unfocus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.unfocus();
			}
		});
		
		ruleControlPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		ruleControlPanel.setBorder(BorderFactory.createTitledBorder("Rule Control"));
		ruleControlPanel.add(add);
		ruleControlPanel.add(remove);
		ruleControlPanel.add(focus);
		ruleControlPanel.add(unfocus);
		
		toolPanel.add(filterPanel, BorderLayout.WEST);
		toolPanel.add(statsPanel, BorderLayout.CENTER);
		toolPanel.add(ruleControlPanel, BorderLayout.SOUTH);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		
		export = new JMenuItem("Export As");
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					controller.exportFile();
			}
		});
		export.setEnabled(false);
		
		menu.add(open);
		menu.add(export);
		menuBar.add(menu);
		
		
		table = new Table(this.model);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		add(menuBar, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		
		panel.add(table, BorderLayout.WEST);
		panel.add(toolPanel, BorderLayout.NORTH);
		
		model.addRuleListener(this);
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}

	@Override
	public void anomalyDetected(Model model) {
		HashMap<Anomaly, Integer> stats = model.getNumOfAnomalies();
		for (HashMap.Entry<Anomaly, AnomalyLabel> entry : labels.entrySet()) {
			AnomalyLabel label = entry.getValue();
			
			label.setText(entry.getKey().toString() + ": " + stats.getOrDefault(entry.getKey(),0));
        }
	}

	@Override
	public void ruleModified(Model model) {
		HashMap<Anomaly, Integer> stats = model.getNumOfAnomalies();
		for (HashMap.Entry<Anomaly, JCheckBox> entry : filters.entrySet()) {
			entry.getValue().setEnabled(stats.getOrDefault(entry.getKey(), 0) > 0);
        }
		
		remove.setEnabled(model.getRules().size() > 0);
	}

	@Override
	public void ruleFocused(Model model) {
		
		
	}
	
	public Table getTable() {
		return table;
	}

	public HashMap<Anomaly, AnomalyLabel> getLabels() {
		return labels;
	}

	public HashMap<Anomaly, JCheckBox> getFilters() {
		return filters;
	}

	public JMenuItem getExport() {
		return export;
	}

	public JButton getAdd() {
		return add;
	}

	public JButton getRemove() {
		return remove;
	}

	public JButton getFocus() {
		return focus;
	}

	public JButton getUnfocus() {
		return unfocus;
	}
	
}
