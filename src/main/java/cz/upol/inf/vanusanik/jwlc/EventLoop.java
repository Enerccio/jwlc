package cz.upol.inf.vanusanik.jwlc;

import java.io.FileDescriptor;
import java.util.HashMap;
import java.util.Map;

import com.sun.jna.Pointer;

import cz.upol.inf.vanusanik.jwlc.Callbacks.fd_callback;
import cz.upol.inf.vanusanik.jwlc.Callbacks.timer_callback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopEvent;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopFDEvent;

public class EventLoop {

	private static long usedData = 0;
	private static Map<Long, Object> dataMap = new HashMap<Long, Object>();
	private static Map<Long, EventSource> sourceMap = new HashMap<Long, EventSource>();

	public static EventSource addFileDescriptorEvent(FileDescriptor fd, long mask, final EventLoopFDEvent cb,
			Object customData) {
		Assert.assertNotNull(fd);
		Assert.assertNotNull(cb);

		synchronized (EventLoop.class) {
			long dataPointer = usedData++;
			dataMap.put(dataPointer, customData);
			EventSource event = EventSource.from(JWLC.nativeHandler().wlc_event_loop_add_fd(Utils.getFD(fd),
					Utils.getAsUnsignedInt(mask), new fd_callback() {

						public int callback(int fd, int mask, Pointer data) {
							Object customData = dataMap.get(Pointer.nativeValue(data));
							EventSource event = sourceMap.get(Pointer.nativeValue(data));
							return cb.onFDAvailable(event, Utils.createFD(fd), Utils.getUnsignedInt(mask), customData);
						}

					}, new Pointer(dataPointer)), dataPointer);
			sourceMap.put(dataPointer, event);
			return event;
		}
	}

	public static EventSource addEvent(final EventLoopEvent cb, Object customData) {
		Assert.assertNotNull(cb);

		synchronized (EventLoop.class) {
			long dataPointer = usedData++;
			dataMap.put(dataPointer, customData);

			EventSource event = EventSource.from(JWLC.nativeHandler().wlc_event_loop_add_timer(new timer_callback() {

				public int callback(Pointer data) {
					Object customData = dataMap.get(Pointer.nativeValue(data));
					EventSource event = sourceMap.get(Pointer.nativeValue(data));
					return cb.onEvent(event, customData);
				}
			}, new Pointer(dataPointer)), dataPointer);
			sourceMap.put(dataPointer, event);
			return event;
		}
	}

	public static boolean timerUpdate(EventSource source, int msdelay) {
		Assert.assertNotNull(source);
		Assert.assertNotNull(source.to());
		if (Pointer.nativeValue(source.to()) == 0)
			throw new NullPointerException();

		return JWLC.nativeHandler().wlc_event_source_timer_update(source.to(), msdelay);
	}

	public static void remove(EventSource source) {
		Assert.assertNotNull(source);
		Assert.assertNotNull(source.to());
		if (Pointer.nativeValue(source.to()) == 0)
			throw new NullPointerException();

		JWLC.nativeHandler().wlc_event_source_remove(source.to());
	}
}
