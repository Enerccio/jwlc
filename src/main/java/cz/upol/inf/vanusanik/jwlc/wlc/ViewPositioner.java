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
package cz.upol.inf.vanusanik.jwlc.wlc;

import cz.upol.inf.vanusanik.jwlc.JWLC;
import cz.upol.inf.vanusanik.jwlc.Utils;
import cz.upol.inf.vanusanik.jwlc.geometry.Geometry;
import cz.upol.inf.vanusanik.jwlc.geometry.Point;
import cz.upol.inf.vanusanik.jwlc.geometry.Size;

public class ViewPositioner {

	private View handler;
	
	ViewPositioner(View handler) {
		this.handler = handler;
	}
	
	public Geometry getAnchorRect() {
		return Geometry.from(JWLC.nativeHandler().wlc_view_positioner_get_anchor_rect(handler.to()));
	}
	
	public Size getSize() {		
		return Size.from(JWLC.nativeHandler().wlc_view_positioner_get_size(handler.to()));
	}
	
	public Point getOffset() {
		return Point.from(JWLC.nativeHandler().wlc_view_positioner_get_offset(handler.to()));
	}
	
	public long getAnchor() {
		return Utils.getUnsignedInt(JWLC.nativeHandler().wlc_view_positioner_get_anchor(handler.to()));
	}
	
	public long getGravity() {
		return Utils.getUnsignedInt(JWLC.nativeHandler().wlc_view_positioner_get_gravity(handler.to()));
	}
	
	public long getConstraintAdjustment() {
		return Utils.getUnsignedInt(JWLC.nativeHandler()
				.wlc_view_positioner_get_constraint_adjustment(handler.to()));
	}
}
