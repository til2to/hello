package org.limewire.hello.base.flow;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.limewire.hello.base.state.Close;
import org.limewire.hello.base.state.View;
import org.limewire.hello.base.user.Cell;
import org.limewire.hello.base.user.Dialog;
import org.limewire.hello.base.user.Panel;
import org.limewire.hello.base.user.Refresh;
import org.limewire.hello.base.user.SelectTextArea;
import org.limewire.hello.base.user.TextMenu;

/** The Hash dialog box on the screen that lets the user hash a file. */
public class HashDialog extends Close {
	
	// Program

	// Run just this dialog box as the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	HashDialog dialog = new HashDialog();
        		dialog.dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Make closing the window close the program
            }
        });
    }
    
    // Dialog

    /** Show the Hash dialog on the screen to let the user hash a file. */
	public HashDialog() {

		// Make dialog contents
		path = new JTextField(); // Path box
		new TextMenu(path);
		status = new SelectTextArea(); // Status text
		size = new SelectTextArea();
		value = new SelectTextArea();
		browse = new BrowseAction(); // Actions behind buttons
		start = new StartAction();
		stop = new StopAction();
		reset = new ResetAction();
		close = new CloseAction();

		// Lay them out
		Panel bar1 = Panel.row();
		bar1.add(Cell.wrap(path).fillWide());
		bar1.add(Cell.wrap(new JButton(browse)));
		Panel bar2 = Panel.row();
		bar2.add(Cell.wrap(new JButton(start)));
		bar2.add(Cell.wrap(new JButton(stop)));
		bar2.add(Cell.wrap(new JButton(reset)));
		bar2.add(Cell.wrap(new JButton(close)));
		Panel panel = new Panel();
		panel.border();
		panel.place(0, 0, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("Path")));
		panel.place(0, 1, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Status")));
		panel.place(0, 2, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Size")));
		panel.place(0, 3, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Value")));
		panel.place(1, 0, 1, 1, 0, 1, 0, 0, Cell.wrap(bar1.jpanel).fillWide());
		panel.place(1, 1, 1, 1, 1, 1, 0, 0, Cell.wrap(status).fillWide());
		panel.place(1, 2, 1, 1, 1, 1, 0, 0, Cell.wrap(size).fillWide());
		panel.place(1, 3, 1, 1, 1, 1, 0, 0, Cell.wrap(value).fillWide());
		panel.place(1, 4, 1, 1, 1, 1, 0, 0, Cell.wrap(bar2.jpanel).lowerLeft().grow());

		// Make our Hash object that will do what this dialog shows
		hash = new Hash();

		// Make our inner View object and connect the Feed object's model to it
		view = new MyView();
		hash.model.add(view); // When the Feed Model changes, it will call our view.refresh() method
		view.refresh();

		// Make the dialog box and show it on the screen
		dialog = Dialog.make("Hash File");
		dialog.setContentPane(panel.jpanel); // Put everything we layed out in the dialog box
		dialog.addWindowListener(new MyWindowListener());         // Have Java tell us when the user closes the window
		Dialog.show(dialog, 600, 180);
	}
	
	/** The Hash object this dialog is a view of. */
	private final Hash hash;

	/** The dialog box on the screen. */
	private final JDialog dialog;
	/** The box the user types the path in. */
	private final JTextField path;
	/** The hash status text output box. */
	private final SelectTextArea status;
	/** The hash size text output box. */
	private final SelectTextArea size;
	/** The hash value text output box. */
	private final SelectTextArea value;
	/** The Action behind the Browse button. */
	private final Action browse;
	/** The Action behind the Start button. */
	private final Action start;
	/** The Action behind the Stop button. */
	private final Action stop;
	/** The Action behind the Reset button. */
	private final Action reset;
	/** The Action behind the Close button. */
	private final Action close;

	/** Make this object put away resources and not change or work again. */
	public void close() {
		if (already()) return;
		dialog.dispose();
		hash.close();
	}
	
	// When the user clicks the dialog's corner X, Java calls this windowClosing() method and then takes the dialog off the screen
	private class MyWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			close();
		}
	}
	
	// The user clicked the Close button
	private class CloseAction extends AbstractAction {
		public CloseAction() { super("Close"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			close();
		}
	}

	// The user clicked the Browse button
	private class BrowseAction extends AbstractAction {
		public BrowseAction() { super("Browse..."); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			Dialog.chooseFile(dialog, path); // Show the choice box to the user, and set the path text
		}
	}
	
	// The user clicked the Start button
	private class StartAction extends AbstractAction {
		public StartAction() { super("Start"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			hash.start(path.getText());
		}
	}
	
	// The user clicked the Stop button
	private class StopAction extends AbstractAction {
		public StopAction() { super("Stop"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			hash.stop();
		}
	}

	// The user clicked the Reset button
	private class ResetAction extends AbstractAction {
		public ResetAction() { super("Reset"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {
			hash.reset();
		}
	}

	// View

	// When our Model underneath changes, it calls these methods
	private final View view;
	private class MyView implements View {

		// The Model beneath changed, we need to update what we show the user
		public void refresh() {
			Refresh.text(status, hash.model.status());
			Refresh.text(size, hash.model.size());
			Refresh.text(value, hash.model.value());
			
			Refresh.edit(path, hash.model.canStart());
			
			Refresh.can(browse, hash.model.canStart());
			Refresh.can(start, hash.model.canStart());
			Refresh.can(reset, hash.model.canReset());
			Refresh.can(stop, hash.model.canStop());
		}

		// The Model beneath closed, take this View off the screen
		public void vanish() { me().close(); }
	}
	
	/** Give inner classes a link to this outer HashFileDialog object. */
	private HashDialog me() { return this; }
}
