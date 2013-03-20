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
package org.n52.sos.ds.hibernate.entities;

public class GeometryColumnsId implements java.io.Serializable {
    private static final long serialVersionUID = -5614715132428715199L;
    private String FTableCatalog;
    private String FTableSchema;
    private String FTableName;
    private String FGeometryColumn;
    private Integer coordDimension;
    private Integer srid;
    private String type;

    public GeometryColumnsId() {
    }

    public GeometryColumnsId(String FTableCatalog, String FTableSchema, String FTableName, String FGeometryColumn,
                             Integer coordDimension, Integer srid, String type) {
        this.FTableCatalog = FTableCatalog;
        this.FTableSchema = FTableSchema;
        this.FTableName = FTableName;
        this.FGeometryColumn = FGeometryColumn;
        this.coordDimension = coordDimension;
        this.srid = srid;
        this.type = type;
    }

    public String getFTableCatalog() {
        return this.FTableCatalog;
    }

    public void setFTableCatalog(String FTableCatalog) {
        this.FTableCatalog = FTableCatalog;
    }

    public String getFTableSchema() {
        return this.FTableSchema;
    }

    public void setFTableSchema(String FTableSchema) {
        this.FTableSchema = FTableSchema;
    }

    public String getFTableName() {
        return this.FTableName;
    }

    public void setFTableName(String FTableName) {
        this.FTableName = FTableName;
    }

    public String getFGeometryColumn() {
        return this.FGeometryColumn;
    }

    public void setFGeometryColumn(String FGeometryColumn) {
        this.FGeometryColumn = FGeometryColumn;
    }

    public Integer getCoordDimension() {
        return this.coordDimension;
    }

    public void setCoordDimension(Integer coordDimension) {
        this.coordDimension = coordDimension;
    }

    public Integer getSrid() {
        return this.srid;
    }

    public void setSrid(Integer srid) {
        this.srid = srid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean equals(Object other) {
        if ((this == other)) {
            return true;
        }
        if ((other == null)) {
            return false;
        }
        if (!(other instanceof GeometryColumnsId)) {
            return false;
        }
        GeometryColumnsId castOther = (GeometryColumnsId) other;

        return ((this.getFTableCatalog() == castOther.getFTableCatalog()) || (this.getFTableCatalog() != null
                                                                              && castOther.getFTableCatalog() != null
                                                                              && this.getFTableCatalog()
                .equals(castOther.getFTableCatalog())))
               && ((this.getFTableSchema() == castOther.getFTableSchema()) || (this.getFTableSchema() != null
                                                                               && castOther.getFTableSchema() != null
                                                                               && this.getFTableSchema().equals(
                castOther.getFTableSchema())))
               && ((this.getFTableName() == castOther.getFTableName()) || (this.getFTableName() != null
                                                                           && castOther.getFTableName() != null && this
                .getFTableName().equals(castOther.getFTableName())))
               && ((this.getFGeometryColumn() == castOther.getFGeometryColumn()) || (this.getFGeometryColumn() != null
                                                                                     && castOther.getFGeometryColumn()
                                                                                        != null && this
                .getFGeometryColumn().equals(
                castOther.getFGeometryColumn())))
               && ((this.getCoordDimension() == castOther.getCoordDimension()) || (this.getCoordDimension() != null
                                                                                   && castOther.getCoordDimension()
                                                                                      != null && this
                .getCoordDimension().equals(
                castOther.getCoordDimension())))
               && ((this.getSrid() == castOther.getSrid()) || (this.getSrid() != null && castOther.getSrid() != null
                                                               && this
                .getSrid().equals(castOther.getSrid())))
               && ((this.getType() == castOther.getType()) || (this.getType() != null && castOther.getType() != null
                                                               && this
                .getType().equals(castOther.getType())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getFTableCatalog() == null ? 0 : this.getFTableCatalog().hashCode());
        result = 37 * result + (getFTableSchema() == null ? 0 : this.getFTableSchema().hashCode());
        result = 37 * result + (getFTableName() == null ? 0 : this.getFTableName().hashCode());
        result = 37 * result + (getFGeometryColumn() == null ? 0 : this.getFGeometryColumn().hashCode());
        result = 37 * result + (getCoordDimension() == null ? 0 : this.getCoordDimension().hashCode());
        result = 37 * result + (getSrid() == null ? 0 : this.getSrid().hashCode());
        result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
        return result;
    }
}
