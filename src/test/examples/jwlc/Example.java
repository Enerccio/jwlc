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

import java.util.List;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Keyboard;
import cz.upol.inf.vanusanik.jwlc.Mouse;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.geometry.Size;
import cz.upol.inf.vanusanik.jwlc.layouts.LinuxInput;
import cz.upol.inf.vanusanik.jwlc.layouts.XKB;
import cz.upol.inf.vanusanik.jwlc.wlc.ButtonState;
import cz.upol.inf.vanusanik.jwlc.wlc.KeyState;
import cz.upol.inf.vanusanik.jwlc.wlc.LogType;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifier;
import cz.upol.inf.vanusanik.jwlc.wlc.Modifiers;
import cz.upol.inf.vanusanik.jwlc.wlc.Output;
import cz.upol.inf.vanusanik.jwlc.wlc.ResizeEdge;
import cz.upol.inf.vanusanik.jwlc.wlc.View;
import cz.upol.inf.vanusanik.jwlc.wlc.ViewState;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.KeyboardCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.LoggerCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.OutputResolutionCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerButtonCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.PointerMotionCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewCreatedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewDestroyedCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewFocusCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestGeometryCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestMoveCallback;
import cz.upol.inf.vanusanik.jwlc.wlc.callbacks.ViewRequestResizeCallback;

public class Example {

	private static class Action {
		private View view;
		private Point grab;
		private long edges;

		public View getView() {
			return view;
		}

		public void setView(View view) {
			this.view = view;
		}

		public Point getGrab() {
			return grab;
		}

		public void setGrab(Point grab) {
			this.grab = grab;
		}

		public long getEdges() {
			return edges;
		}

		public void setEdges(long edges) {
			this.edges = edges;
		}
	}

	private static class Compositor {
		private Action action = new Action();

		public Action getAction() {
			return action;
		}
	}

	public static void main(String[] args) throws Exception {
		new Example().run();
	}

	private JWLC wlc;
	private Compositor c = new Compositor();

	private void relayout(Output output) {
		if (output == null)
			return;
		
		Size r = output.getVirtualResolution();
		if (r == null)
			return;

		List<View> views = output.getViews();

		int positioned = 0;
		for (View v : views) {			
			if (v.positioner.getAnchorRect() == null)
				++positioned;
		}

		boolean toggle = false;
		int y = 0;
		int n = (1 + positioned) / 2 > 1 ? (1 + positioned) / 2 : 1;
		int w = r.getW() / 2;
		int h = r.getH() / n;
		int ew = Math.max(r.getW() - w * 2, 0);
		int eh = Math.max(r.getH() - h * 2, 0);
		int j = 0;
		for (View v : views) {
			Geometry anchorRect = v.positioner.getAnchorRect();
			if (anchorRect == null) {
				Geometry g = new Geometry(toggle ? w + ew : 0, y,
						(!toggle && j == positioned - 1 ? r.getW() : (toggle ? w : w + ew)), j < 2 ? h + eh : h);
				v.setGeometry(0, g);
				y = y + (!(toggle = !toggle) ? g.getSize().getH() : 0);
				++j;
			} else {
				Size sizeReq = v.positioner.getSize();
				if (sizeReq.getH() <= 0 || sizeReq.getW() <= 0) {
					Geometry current = v.getGeometry();
					sizeReq = current.getSize();
				}
				Geometry g = new Geometry(anchorRect.getOrigin(), sizeReq);
				View parent = v.getParent();
				if (parent != null) {
					Geometry parentGeometry = parent.getGeometry();
					g.getOrigin().setX(g.getOrigin().getX() + parentGeometry.getOrigin().getX());
					g.getOrigin().setY(g.getOrigin().getY() + parentGeometry.getOrigin().getY());
				}
				v.setGeometry(0, g);
			}
		}
	}

	protected void startInteractiveMove(View view, Point point) {
		startInteractiveAction(view, point);
	}

	private boolean startInteractiveAction(View view, Point point) {
		if (c.getAction().getView() != null)
			return false;

		c.getAction().setView(view);
		c.getAction().setGrab(point);
		view.bringToFront();
		return true;
	}	

	protected void stopInteractiveAction() {
		if (c.getAction().getView() == null)
			return;
		
		c.getAction().getView().setState(ViewState.RESIZING, false);
		c.getAction().setView(null);
		c.getAction().setGrab(null);
	}

	protected void startInteractiveResize(View view, long edges, Point origin) {
		Geometry g = view.getGeometry();
		if (g == null || !startInteractiveAction(view, origin))
			return;

		int halfw = g.getOrigin().getX() + g.getSize().getW() / 2;
		int halfh = g.getOrigin().getY() + g.getSize().getH() / 2;

		if (c.getAction().getEdges() != edges) {
			long newEdges = (origin.getX() < halfw ? ResizeEdge.LEFT : (origin.getX() > halfw ? ResizeEdge.RIGHT : 0))
					| (origin.getY() < halfh ? ResizeEdge.TOP : (origin.getY() > halfh ? ResizeEdge.BOTTOM : 0));
			c.getAction().setEdges(newEdges);
		}
		
		view.setState(ViewState.RESIZING, true);
	}

	protected View getTopmost(Output output, int i) {
		if (output == null)
			return null;
		List<View> views = output.getViews();
		return i < views.size() ? views.get(i) : null;
	}

	private void run() throws Exception {
		wlc = JWLC.getJWLCHandler();

		wlc.setLoggerCallback(new LoggerCallback() {

			public void onLog(LogType type, String message) {
				System.out.println(message);
			}
		});

		Output.setResolutionCallback(new OutputResolutionCallback() {

			public void onOutputResolution(Output handle, Size fromSize, Size toSize) {
				relayout(handle);
			}
		});

		View.setCreatedCallback(new ViewCreatedCallback() {

			public boolean onViewCreated(View view) {
				Output output = view.getOutput();
				view.setMask(output.getMask());
				view.bringToFront();
				view.focus();
				relayout(output);
				return true;
			}
		});

		View.setDestroyedCallback(new ViewDestroyedCallback() {

			public void onViewDestroyed(View view) {
				View topmost = getTopmost(view.getOutput(), 0);
				if (topmost == null)
					View.unfocus();
				else
					topmost.focus();
				
				relayout(view.getOutput());
			}
		});

		View.setFocusCallback(new ViewFocusCallback() {

			public void onFocusChange(View view, boolean focusState) {
				view.setState(ViewState.ACTIVATED, focusState);
			}
		});

		View.setRequestMoveCallback(new ViewRequestMoveCallback() {

			public void onRequestMove(View view, Point point) {
				startInteractiveMove(view, point);
			}
		});

		View.setRequestResizeCallback(new ViewRequestResizeCallback() {

			public void onRequestResize(View view, long edges, Point origin) {
				startInteractiveResize(view, edges, origin);
			}
		});
		
		View.setRequestGeometry(new ViewRequestGeometryCallback() {
			
			public void onRequestGeometry(View view, Geometry geometry) {
				// stub intentionally to ignore geometry requests.
			}
		});
		
		Keyboard.setKeyboardCallback(new KeyboardCallback() {
			
			public boolean onKeyboard(View view, long time, Modifiers modifiers, long key, KeyState state) {
				long sym = Keyboard.getSymkeyForKey(key, null);
				
				if (state == KeyState.STATE_PRESSED) {
					if (view != null) {
						if ((modifiers.getMods() & Modifier.CTRL) > 0 && sym == XKB.XKB_KEY_q) {
							view.close();
							return true;
						} else if ((modifiers.getMods() & Modifier.CTRL) > 0 && sym == XKB.XKB_KEY_Down) {
							view.sendToBack();
							
							View topmost = getTopmost(view.getOutput(), 0);
							if (topmost == null)
								View.unfocus();
							else
								topmost.focus();
							
							return true;
						}
					}
					
					if ((modifiers.getMods() & Modifier.CTRL) > 0 && sym == XKB.XKB_KEY_Escape) {
						wlc.terminate();
						return true;
					} else if ((modifiers.getMods() & Modifier.CTRL) > 0 && sym == XKB.XKB_KEY_Return) {
						String terminal = System.getenv("TERMINAL") != null ? System.getenv("TERMINAL") : "weston-terminal";
						JWLC.exec(terminal, new String[]{terminal});
						return true;
					} else if ((modifiers.getMods() & Modifier.CTRL) > 0 && sym >= XKB.XKB_KEY_1 && sym <= XKB.XKB_KEY_9) {
						List<Output> outputs = Output.getOutputs();
						long scale = sym - XKB.XKB_KEY_1 + 1;
						
						for (Output output : outputs) {
							output.setResolution(output.getResolution(), scale);
						}
						
						System.out.println("Scale: " + scale);
						return true;
					}
				}
				
				return false;
			}
		});
		
		Mouse.setPointerButtonCallback(new PointerButtonCallback() {
			
			public boolean onPointerButton(View view, long time, Modifiers mods, long button, ButtonState state, Point position) {
				if (state == ButtonState.STATE_PRESSED) {
					if (view == null)
						View.unfocus();
					else {
						view.focus();
						
						if ((mods.getMods() & Modifier.CTRL) > 0 && button == LinuxInput.BTN_LEFT)  {
							startInteractiveMove(view, position);
						}
						if ((mods.getMods() & Modifier.CTRL) > 0 && button == LinuxInput.BTN_RIGHT)  {
							startInteractiveResize(view, 0, position);
						}
						
					}
				} else {
					stopInteractiveAction();
				}
				
				return false;
			}
		});
		
		Mouse.setPointerMotionCallback(new PointerMotionCallback() {
			
			public boolean onPointerMotion(View view, long time, Point position) {
				if (c.getAction().getView() != null) {
					int dx = position.getX() - c.getAction().getGrab().getX();
					int dy = position.getY() - c.getAction().getGrab().getY();
					Geometry g = c.getAction().getView().getGeometry();
					if (c.getAction().getEdges() > 0) {
						Size min = new Size(80, 40);
						Geometry n = new Geometry(g.getOrigin(), g.getSize());
						if ((c.getAction().getEdges() & ResizeEdge.LEFT) > 0) {
							n.getSize().setW(n.getSize().getW() - dx);
							n.getOrigin().setX(n.getOrigin().getX() + dx);
						} else if ((c.getAction().getEdges() & ResizeEdge.RIGHT) > 0) {
							n.getSize().setW(n.getSize().getW() + dx);
						}
						
						if ((c.getAction().getEdges() & ResizeEdge.TOP) > 0) {
							n.getSize().setH(n.getSize().getH() - dy);
							n.getOrigin().setY(n.getOrigin().getY() + dy);
						} else if ((c.getAction().getEdges() & ResizeEdge.BOTTOM) > 0) {
							n.getSize().setH(n.getSize().getH() + dy);
						}
						
						if (n.getSize().getW() >= min.getW()) {
							g.getOrigin().setX(n.getOrigin().getX());
							g.getSize().setW(n.getSize().getW());
						}
						
						if (n.getSize().getH() >= min.getH()) {
							g.getOrigin().setY(n.getOrigin().getY());
							g.getSize().setH(n.getSize().getH());
						}
						
						c.getAction().getView().setGeometry(0, g);
					} else {
						g.getOrigin().setX(g.getOrigin().getX() + dx);
						g.getOrigin().setY(g.getOrigin().getY() + dy);
						c.getAction().getView().setGeometry(0, g);
					}
					
					c.getAction().setGrab(position);
				}
				
				Mouse.setPointerPosition(position);
				return c.getAction().getView() != null;
			}
		});

		wlc.init();
		wlc.run();
	}

}
