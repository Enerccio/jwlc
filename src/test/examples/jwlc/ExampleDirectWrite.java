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
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.render.Framebuffer;
import cz.upol.inf.vanusanik.jwlc.render.PixelFormat;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.Output;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputPreRenderCallback;

public class ExampleDirectWrite {

	public static void main(String[] args) throws Exception {
		JWLC.setLoggerCallback(new LoggerCallback() {

			public void onLog(LogType type, String message) {
				System.out.println(type + ": " + message);
			}
		});

		Output.setCreatedCallback(new OutputCreatedCallback() {

			public boolean onCreated(Output output) {
				System.out.println("Output id " + output);
				System.out.println(output.getViews());
				return true;
			}
		});

		final BufferedImage im = ImageIO.read(
				ExampleDirectWrite.class.getResourceAsStream("/wayland.png"));
		final byte[] actualData = new byte[4 * im.getHeight() * im.getWidth()];

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

		Output.setPreRenderCallback(new OutputPreRenderCallback() {

			public void onPreRender(Output handle) {
				Framebuffer.writePixels(PixelFormat.RGBA8888,
						new Geometry(0, 0, im.getWidth(), im.getHeight()),
						actualData);
			}
		});

		JWLC.init();
		JWLC.run();
	}

}
