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
package org.n52.sos.ds.hibernate.cache;

import org.n52.sos.ds.hibernate.cache.base.OfferingCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.StaticCapabilitiesCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.CompositePhenomenonCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.RelatedFeaturesCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.FeatureOfInterestCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.ResultTemplateCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.ObservationIdentifiersCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.EventTimeCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.ProcedureCacheUpdate;
import org.n52.sos.ds.hibernate.cache.base.ObservablePropertiesCacheUpdate;

/**
 * @author Christian Autermann <c.autermann@52north.org>
 */
public class InitialCacheUpdate extends CompositeCacheUpdate {

    public InitialCacheUpdate() {
        super(new OfferingCacheUpdate(),
              new ProcedureCacheUpdate(),
              new ObservablePropertiesCacheUpdate(),
              new FeatureOfInterestCacheUpdate(),
              new RelatedFeaturesCacheUpdate(),
              new CompositePhenomenonCacheUpdate(),
              new StaticCapabilitiesCacheUpdate(),
              new ObservationIdentifiersCacheUpdate(),
              new ResultTemplateCacheUpdate(),
              new EventTimeCacheUpdate());
    }
    
}
