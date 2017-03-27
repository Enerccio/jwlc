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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Keyboard;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.render.Framebuffer;
import cz.upol.inf.vanusanik.jwlc.render.PixelFormat;
import cz.upol.inf.vanusanik.jwlc.wlc.KeyState;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.Output;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.KeyboardCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputPreRenderCallback;

public class ExampleWriteText {
	
	private static BufferedImage im = new BufferedImage(200, 100, BufferedImage.TYPE_4BYTE_ABGR);
	private static byte[] actualData = new byte[4 * im.getWidth() * im.getHeight()];
	private static Output output;

	public static void main(String[] args) throws Exception {
		JWLC.setLoggerCallback(new LoggerCallback() {

			public void onLog(LogType type, String message) {
				System.out.println(type + ": " + message);
			}
		});
		
		Keyboard.setKeyboardCallback(new KeyboardCallback() {

			public boolean onKeyboard(View view, long time, Modifiers modifiers,
					long key, KeyState state) {
				if (state == KeyState.STATE_RELEASED) {
					clear();
				} else {
					draw(Keyboard.getUtf32CharsForKey(key, modifiers));
				}
				return false;
			}
			
		});

		Output.setPreRenderCallback(new OutputPreRenderCallback() {

			public void onPreRender(Output handle) {
				Framebuffer.writePixels(PixelFormat.RGBA8888,
						new Geometry(100, 100, im.getWidth(), im.getHeight()),
						actualData);
			}
		});
		
		Output.setCreatedCallback(new OutputCreatedCallback() {
			
			public boolean onCreated(Output handle) {
				output = handle;
				return true;
			}
		});

		JWLC.init();
		JWLC.run();
	}
	
	private static void draw(char[] utf32CharsForKey) {
		Graphics2D g = (Graphics2D)im.getGraphics();
		g.setColor(Color.red);
		g.drawString(new String(utf32CharsForKey), 20, 50);
		flipToBuffer();
	}

	private static void clear() {
		Graphics2D g = (Graphics2D)im.getGraphics();
		g.fillRect(0, 0, im.getWidth(), im.getHeight());
		flipToBuffer();
	}
	
	public static void flipToBuffer() {
		int pos = 0;
		
		for (int j = 0; j < im.getHeight(); j++) 
			for (int i = 0; i < im.getWidth(); i++) {
				int argb = im.getRGB(i, j);
				Color c = new Color(argb);

				byte r = Utils.getAsUnsignedByte((short) c.getRed());
				byte g = Utils.getAsUnsignedByte((short) c.getGreen());
				byte b = Utils.getAsUnsignedByte((short) c.getBlue());

				actualData[pos++] = r;
				actualData[pos++] = g;
				actualData[pos++] = b;
				actualData[pos++] = -1; // 255
			}
		
		output.requestRepaint();
	}

}
