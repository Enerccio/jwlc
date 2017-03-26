/*
 * MIT License
 * 
 * Copyright (c) 2017 Peter Vaňušanik
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cz.upol.inf.vanusanik.jwlc;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Callbacks.fd_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.timer_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopEvent;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopFDEvent;

public class Event implements PointerContainer {

	private final Pointer handle;

	protected Event(Pointer handle) {
		this.handle = handle;
	}

	public static Event from(Pointer handle) {
		if (handle == null)
			return null;
		return new Event(handle);
	}

	public Pointer to() {
		return handle;
	}

	public Pointer getHandle() {
		return handle;
	}

	@Override
	public String toString() {
		return "Event [handle=" + handle + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((handle == null) ? 0 : handle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (handle == null) {
			if (other.handle != null)
				return false;
		} else if (!handle.equals(other.handle))
			return false;
		return true;
	}

	private static long usedData = 0;
	private static Map<Long, Event> sourceMap = new HashMap<Long, Event>();

	public static Event addFileDescriptorEvent(FileDescriptor fd, long mask,
			final EventLoopFDEvent cb) {
		Assert.assertNotNull(fd);
		Assert.assertNotNull(cb);

		synchronized (Event.class) {
			long dataPointer = usedData++;
			Event event = Event.from(
					JWLC.nativeHandler().wlc_event_loop_add_fd(Utils.getFD(fd),
							Utils.getAsUnsignedInt(mask), new fd_callback() {

								public int callback(int fd, int mask,
										Pointer data) {
									Event event = sourceMap
											.get(Pointer.nativeValue(data));
									return cb.onFDAvailable(event,
											Utils.createFD(fd),
											Utils.getUnsignedInt(mask));
								}

							}, new Pointer(dataPointer)));
			sourceMap.put(dataPointer, event);
			return event;
		}
	}

	public static Event addEvent(final EventLoopEvent cb) {
		Assert.assertNotNull(cb);

		synchronized (Event.class) {
			long dataPointer = usedData++;

			Event event = Event.from(JWLC.nativeHandler()
					.wlc_event_loop_add_timer(new timer_callback() {

						public int callback(Pointer data) {
							Event event = sourceMap
									.get(Pointer.nativeValue(data));
							return cb.onEvent(event);
						}
					}, new Pointer(dataPointer)));
			sourceMap.put(dataPointer, event);
			return event;
		}
	}

	public boolean timerUpdate(int msdelay) {
		Assert.assertNotNull(to());
		if (Pointer.nativeValue(to()) == 0)
			throw new NullPointerException();

		return JWLC.nativeHandler().wlc_event_source_timer_update(to(),
				msdelay);
	}

	public void remove() {
		Assert.assertNotNull(to());
		if (Pointer.nativeValue(to()) == 0)
			throw new NullPointerException();

		JWLC.nativeHandler().wlc_event_source_remove(to());
	}
}
