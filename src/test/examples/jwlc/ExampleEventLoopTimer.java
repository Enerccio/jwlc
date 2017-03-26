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
import cz.upol.inf.vanusanik.jwlc.EventLoop;
import cz.upol.inf.vanusanik.jwlc.EventSource;
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
				EventSource src = EventLoop.addEvent(new EventLoopEvent() {

					public int onEvent(EventSource event) {
						System.out.println("test1");
						EventLoop.timerUpdate(event, 1000);
						return 0;
					}
				});
				EventSource src2 = EventLoop.addEvent(new EventLoopEvent() {

					public int onEvent(EventSource event) {
						System.out.println("test2");
						JWLC.terminate();
						return 0;
					}
				});

				EventLoop.timerUpdate(src, 1000);
				EventLoop.timerUpdate(src2, 2500);
			}
		});

		JWLC.init();
		JWLC.run();
	}

}
