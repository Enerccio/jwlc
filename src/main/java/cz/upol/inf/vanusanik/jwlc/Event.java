package cz.upol.inf.vanusanik.jwlc;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Callbacks.fd_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.timer_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopEvent;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopFDEvent;

public class Event {

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

	private static long usedData = 0;
	private static Map<Long, Event> sourceMap = new HashMap<Long, Event>();

	public static Event addFileDescriptorEvent(FileDescriptor fd,
			long mask, final EventLoopFDEvent cb) {
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
