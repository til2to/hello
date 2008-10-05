package org.limewire.hello.base.flow.valve;

import org.limewire.hello.base.data.Bin;

/** Ways to control a Valve. */
public interface Valve {
	
	// Required

	/** Access this Valve's input Bin to give it data, null if in use or doesn't have one. */
	public Bin in();
	/** Access this Valve's output Bin to get the data it processed, null if in use or doesn't have one. */
	public Bin out();

	/** Tell this Valve to start if possible. */
	public void start();
	/** Have this Valve stop if it's done, and throw the exception that stopped it. */
	public void stop() throws Exception;

	/** true if this Valve is done and empty of data. */
	public boolean isEmpty();
}
