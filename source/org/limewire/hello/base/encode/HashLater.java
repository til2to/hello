package org.limewire.hello.base.encode;

import org.jdesktop.swingworker.SwingWorker;
import org.limewire.hello.base.data.Bin;
import org.limewire.hello.base.size.Move;
import org.limewire.hello.base.size.Range;
import org.limewire.hello.base.state.Later;
import org.limewire.hello.base.state.Update;
import org.limewire.hello.base.time.Now;

public class HashLater extends Later {
	
	// Make

	/** SHA1 hash and clear the contents of bin with the given Hash object, don't look at hash or bin until this is closed. */
	public HashLater(Update above, Hash hash, Bin bin, Range range) {
		this.above = above; // We'll tell above when we're done
		
		// Save the input
		this.hash = hash;
		this.bin = bin;
		this.range = range;

		work = new MySwingWorker();
		work.execute(); // Have a worker thread call doInBackground() now
	}

	/** The Hash object we use. */
	private final Hash hash;
	/** The Bin we put the data in. */
	private final Bin bin;
	/** The limit of how much data we'll hash. */
	private final Range range;

	// Result
	
	/** true when we're done, or throws the exception that made us give up. */
	public Move result() throws Exception { return (Move)check(move); }
	private Move move;
	
	// Inside

	/** Our SwingWorker with a worker thread that runs our code that blocks. */
	private class MySwingWorker extends SwingWorker<Void, Void> {
		private Exception workException; // References the worker thread can safely set
		private Move workMove;

		// A worker thread will call this method
		public Void doInBackground() {
			try {
				
				Now start = new Now();
				int ask = range.ask(bin.size());
				hash.add(bin.data().begin(ask));
				bin.remove(ask);
				workMove = new Move(start, ask);

			} catch (Exception e) { workException = e; } // Catch the exception our code threw
			return null;
		}

		// Once doInBackground() returns, the normal event thread calls this done() method
		public void done() {
			if (closed()) return; // Don't let anything change if we're already closed
			try { get(); } catch (Exception e) { exception = e; } // Get the exception worker threw
			if (workException != null) exception = workException; // Get the exception our code threw
			if (exception == null) { // No exception, save what worker did
				
				move = workMove;
			}
			close(); // We're done
			above.send(); // Tell update we've changed
		}
	}
}
