package org.limewire.hello.feed.user;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.limewire.hello.base.state.View;
import org.limewire.hello.base.user.Cell;
import org.limewire.hello.base.user.Describe;
import org.limewire.hello.base.user.Dialog;
import org.limewire.hello.base.user.Panel;
import org.limewire.hello.base.user.SelectTextArea;
import org.limewire.hello.feed.Episode;

/** The Episode Properties dialog box on the screen that shows the user the properties of a podcast episode. */
public class EpisodePropertiesDialog {
	
	// -------- Dialog --------

	/** The Episode this dialog box is showing the properties of. */
	private Episode episode;

	/** Show the properties of Episode in a dialog box. */
	public EpisodePropertiesDialog(Episode episode) {

		// Save the given link
		this.episode = episode;

		// Make our inner View object and connect the Episode object's model to it
		view = new MyView();
		episode.model.add(view); // When the Episode Model changes, it will call our view.refresh() method

		// Text areas
		status      = new SelectTextArea(episode.model.status()); // Get text from the Episode's Model
		title       = new SelectTextArea(episode.model.episode());
		time        = new SelectTextArea(episode.model.time());
		date        = new SelectTextArea(episode.model.date());
		description = new SelectTextArea(episode.model.description());
		address     = new SelectTextArea(episode.model.address());

		// Buttons
		Panel bar = Panel.row();
		bar.add(Cell.wrap(new JButton(new CloseAction())));

		// Lay out controls
		Panel panel = new Panel();
		panel.border();
		panel.place(0, 0, 1, 1, 0, 0, 0, 0, Cell.wrap(new JLabel("Status")));
		panel.place(0, 1, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Episode")));
		panel.place(0, 2, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Time")));
		panel.place(0, 3, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Date")));
		panel.place(0, 4, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Description")));
		panel.place(0, 5, 1, 1, 1, 0, 0, 0, Cell.wrap(new JLabel("Address")));
		panel.place(1, 0, 1, 1, 0, 1, 0, 0, Cell.wrap(status).fillWide());
		panel.place(1, 1, 1, 1, 1, 1, 0, 0, Cell.wrap(title).fillWide());
		panel.place(1, 2, 1, 1, 1, 1, 0, 0, Cell.wrap(time).fillWide());
		panel.place(1, 3, 1, 1, 1, 1, 0, 0, Cell.wrap(date).fillWide());
		panel.place(1, 4, 1, 1, 1, 1, 0, 0, Cell.wrap(description).fillWide());
		panel.place(1, 5, 1, 1, 1, 1, 0, 0, Cell.wrap(address).fillWide());
		panel.place(1, 6, 1, 1, 1, 1, 0, 0, Cell.wrap(bar.jpanel).grow().lowerLeft());

		// Make the dialog box and show it on the screen
		dialog = Dialog.make("Episode Properties");
		dialog.setContentPane(panel.jpanel); // Put everything we layed out in the dialog box
		Dialog.show(dialog, 500, 300);
	}
	
	/** The Java JDialog object which is the dialog box on the screen. */
	private JDialog dialog;

	/** The Status text area in the dialog. */
	private SelectTextArea status;
	/** The Episode title text area in the dialog. */
	private SelectTextArea title;
	/** The Time duration text area in the dialog. */
	private SelectTextArea time;
	/** The release Date text area in the dialog. */
	private SelectTextArea date;
	/** The Description subtitle text are in the dialog. */
	private SelectTextArea description;
	/** The Address text area in the dialog. */
	private SelectTextArea address;

	// -------- View --------

	// When the Episode's Model changes, it calls the methods here
	private View view;
	private class MyView extends View {

		// The Episode Model changed, we need to update our text for the user
		public void receive() {
			Describe.update(status,      episode.model.status()); // Get text from the Episode's Model
			Describe.update(title,       episode.model.episode());
			Describe.update(time,        episode.model.time());
			Describe.update(date,        episode.model.date());
			Describe.update(description, episode.model.description());
			Describe.update(address,     episode.model.address());
		}

		// The Episode closed, we need to close this properties dialog
		public void close() {
			me().close(); // Call EpisodePropertiesDialog.close()
		}
	}
	
	// -------- Buttons --------

	// The user clicked the Close button
	private class CloseAction extends AbstractAction {
		public CloseAction() { super("Close"); } // Specify the button text
		public void actionPerformed(ActionEvent a) {

			// Close the dialog box
			close();
		}
	}

	// -------- Methods --------

	/** Close this dialog box. */
	private void close() {
		episode.model.remove(view); // Disconnect us from the Model below
		dialog.dispose(); // Close the dialog box
	}
	
	/** Give inner classes a link to this outer EpisodePropertiesDialog object. */
	private EpisodePropertiesDialog me() { return this; }
}
