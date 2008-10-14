package org.limewire.hello.base.move;

import org.limewire.hello.base.time.Duration;
import org.limewire.hello.base.time.Now;

public class Move {
	
	// Make

	/** Document the result of a successful blocking data transfer of 1 or more bytes. */
	public Move(Now start, int size) {
		if (size < 1) throw new IndexOutOfBoundsException();
		this.duration = new Duration(start); // Record now as the stop time
		this.size = size;
	}
	
	// Look
	
	/** How long this Move took to complete, the time before and after the blocking call that did it. */
	public final Duration duration;
	
	/** The number of bytes we moved, 1 or more. */
	public final int size;
}
