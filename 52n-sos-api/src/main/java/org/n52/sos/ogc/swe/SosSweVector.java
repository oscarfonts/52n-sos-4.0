/**
 * Copyright (C) 2013
 * by 52 North Initiative for Geospatial Open Source Software GmbH
 *
 * Contact: Andreas Wytzisk
 * 52 North Initiative for Geospatial Open Source Software GmbH
 * Martin-Luther-King-Weg 24
 * 48155 Muenster, Germany
 * info@52north.org
 *
 * This program is free software; you can redistribute and/or modify it under
 * the terms of the GNU General Public License version 2 as published by the
 * Free Software Foundation.
 *
 * This program is distributed WITHOUT ANY WARRANTY; even without the implied
 * WARRANTY OF MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program (see gnu-gpl v2.txt). If not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA or
 * visit the Free Software Foundation web page, http://www.fsf.org.
 */

package org.n52.sos.ogc.swe;

import java.util.List;


public class SosSweVector extends SosSweAbstractDataComponent {

    private List<SosSweCoordinate<?>> coordinates;

    public SosSweVector(
            List<SosSweCoordinate<?>> coordinates) {
        this.coordinates = coordinates;
    }

    public SosSweVector() {
        this(null);
    }

    public List<SosSweCoordinate<?>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<SosSweCoordinate<?>> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isSetCoordinates() {
        return getCoordinates() != null && !getCoordinates().isEmpty();
    }

    @Override
    public String toString() {
        return String.format("%s[coordinates=%s]", getClass().getSimpleName(), getCoordinates());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.coordinates != null ? this.coordinates.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SosSweVector other = (SosSweVector) obj;
        if (this.coordinates != other.coordinates && (this.coordinates == null || !this.coordinates
                .equals(other.coordinates))) {
            return false;
        }
        return true;
    }
}