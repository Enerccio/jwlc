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

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cz.upol.inf.vanusanik.jwlc.Compositor;
import cz.upol.inf.vanusanik.jwlc.EventLoop;
import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.wlc.FileDescriptorEvent;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.CompositorReadyCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.EventLoopFDEvent;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;

public class ExampleEventLoopFD {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		JWLC.setLoggerCallback(new LoggerCallback() {

			public void onLog(LogType type, String message) {
				System.out.println(type + ": " + message);
			}
		});

		final String pipePath = "/tmp/mypipe";
		Process p = Runtime.getRuntime().exec("mkfifo " + pipePath);
		p.waitFor();
		
		final File f = new File(pipePath);
		
		new Thread() {
			
			public void run() {
				try {
					FileOutputStream fos = new FileOutputStream(f);
					Thread.sleep(3000);
					fos.write("Hello, world".getBytes());
					fos.flush();
				} catch (Exception e) {
					
				}
			}
			
		}.start();
		
		Compositor.setReadyCallback(new CompositorReadyCallback() {
			
			public void onReady() {
				try {
					EventLoop.addFileDescriptorEvent(new FileInputStream(f).getFD(), 
							FileDescriptorEvent.READABLE, 
							new EventLoopFDEvent() {
						
						public int onFDAvailable(FileDescriptor fd, long mask, Object data) {
							System.out.println("Custom data from pipe " + data + ", mask " + mask);
							FileInputStream fis = new FileInputStream(fd);
							byte[] in = new byte[1024];
							try {
								fis.read(in);
								System.out.print(new String(in));
							} catch (IOException e) {
								return 0;
							} finally {
								f.delete();
							}
							return 0;
						}
					}, pipePath);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				JWLC.terminate();
			}
		});
		
		JWLC.init();		
		JWLC.run();
	}

}
