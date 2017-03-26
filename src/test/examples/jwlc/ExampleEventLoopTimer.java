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
package jwlc;

import cz.upol.inf.vanusanik.jwlc.Compositor;
import cz.upol.inf.vanusanik.jwlc.Event;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.CompositorReadyCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopEvent;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;

public class ExampleEventLoopTimer {

	public static void main(String[] args) throws Exception {
		JWLC.setLoggerCallback(new LoggerCallback() {

			public void onLog(LogType type, String message) {
				System.out.println(type + ": " + message);
			}
		});

		Compositor.setReadyCallback(new CompositorReadyCallback() {

			public void onReady() {
				Event src = Event.addEvent(new EventLoopEvent() {

					public int onEvent(Event event) {
						System.out.println("test1");
						event.timerUpdate(1000);
						return 0;
					}
				});
				Event src2 = Event.addEvent(new EventLoopEvent() {

					public int onEvent(Event event) {
						System.out.println("test2");
						event.remove();
						JWLC.terminate();
						return 0;
					}
				});

				src.timerUpdate(1000);
				src2.timerUpdate(2500);
			}
		});

		JWLC.init();
		JWLC.run();
	}

}
