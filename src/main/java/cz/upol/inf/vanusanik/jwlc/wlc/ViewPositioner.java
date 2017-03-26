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

/**
 * Positioner abstraction wrapper class.
 * 
 * @author enerccio
 *
 */
public class ViewPositioner {

	private View handler;

	ViewPositioner(View handler) {
		this.handler = handler;
	}

	/**
	 * Get anchor rectangle requested by positioner, as defined in xdg-shell v6.
	 * Returns null if view has no valid positioner.
	 * 
	 * @return
	 */
	public Geometry getAnchorRect() {
		return Geometry.from(JWLC.nativeHandler()
				.wlc_view_positioner_get_anchor_rect(handler.to()));
	}

	/**
	 * Get size requested by positioner, as defined in xdg-shell v6. Returns
	 * null if view has no valid positioner.
	 * 
	 * @return
	 */
	public Size getSize() {
		return Size.from(JWLC.nativeHandler()
				.wlc_view_positioner_get_size(handler.to()));
	}

	/**
	 * Get offset requested by positioner, as defined in xdg-shell v6. Returns
	 * null if view has no valid positioner, or default value (0, 0) if
	 * positioner has no offset set.
	 * 
	 * @return
	 */
	public Point getOffset() {
		return Point.from(JWLC.nativeHandler()
				.wlc_view_positioner_get_offset(handler.to()));
	}

	/**
	 * Get anchor requested by positioner, as defined in xdg-shell v6. Returns
	 * default value {@link PositionerAnchor#NONE} if view has no valid
	 * positioner or if positioner has no anchor set.
	 * 
	 * @return
	 */
	public long getAnchor() {
		return Utils.getUnsignedInt(JWLC.nativeHandler()
				.wlc_view_positioner_get_anchor(handler.to()));
	}

	/**
	 * Get anchor requested by positioner, as defined in xdg-shell v6. Returns
	 * default value {@link PositionerGravity#NONE} if view has no valid
	 * positioner or if positioner has no gravity set.
	 * 
	 * @return
	 */
	public long getGravity() {
		return Utils.getUnsignedInt(JWLC.nativeHandler()
				.wlc_view_positioner_get_gravity(handler.to()));
	}

	/**
	 * Get constraint adjustment requested by positioner, as defined in
	 * xdg-shell v6. Returns default value
	 * {@link PositionerConstraintAdjustment#NONE} if view has no valid
	 * positioner or if positioner has no constraint adjustment set.
	 * 
	 * @return
	 */
	public long getConstraintAdjustment() {
		return Utils.getUnsignedInt(JWLC.nativeHandler()
				.wlc_view_positioner_get_constraint_adjustment(handler.to()));
	}
}
