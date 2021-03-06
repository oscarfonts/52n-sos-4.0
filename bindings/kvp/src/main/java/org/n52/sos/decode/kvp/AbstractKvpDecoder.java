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
package org.n52.sos.decode.kvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import org.joda.time.DateTime;
import org.n52.sos.decode.Decoder;
import org.n52.sos.exception.ows.InvalidParameterValueException;
import org.n52.sos.exception.ows.concrete.DateTimeParseException;
import org.n52.sos.ogc.filter.FilterConstants.SpatialOperator;
import org.n52.sos.ogc.filter.FilterConstants.TimeOperator;
import org.n52.sos.ogc.filter.SpatialFilter;
import org.n52.sos.ogc.filter.TemporalFilter;
import org.n52.sos.ogc.gml.time.TimeInstant;
import org.n52.sos.ogc.gml.time.TimePeriod;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.sos.SosConstants.IndeterminateTime;
import org.n52.sos.request.AbstractServiceRequest;
import org.n52.sos.service.ServiceConfiguration;
import org.n52.sos.service.ServiceConstants;
import org.n52.sos.util.DateTimeHelper;
import org.n52.sos.util.JTSHelper;
import org.n52.sos.util.SosHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractKvpDecoder implements Decoder<AbstractServiceRequest, Map<String, String>> {

    protected static final Logger LOGGER = LoggerFactory.getLogger(GetCapabilitiesKvpDecoder.class);

    @Override
    public Set<String> getConformanceClasses() {
        return Collections.emptySet();
    }
    
    @Override
    public Map<ServiceConstants.SupportedTypeKey, Set<String>> getSupportedTypes() {
        return Collections.emptyMap();
    }

    protected List<TemporalFilter> parseValidTime(String parameterValue, String parameterName) throws OwsExceptionReport,
                                                                                                      DateTimeParseException {
        List<TemporalFilter> filterList = new ArrayList<TemporalFilter>(1);
        filterList.add(createTemporalFilterFromValue(parameterValue, null));
        return filterList;
    }

    protected SpatialFilter parseSpatialFilter(List<String> parameterValues, String parameterName)
            throws OwsExceptionReport {
        if (!parameterValues.isEmpty()) {
            if (!(parameterValues instanceof RandomAccess)) {
                parameterValues = new ArrayList<String>(parameterValues);
            }
            SpatialFilter spatialFilter = new SpatialFilter();

            boolean hasSrid = false;

            spatialFilter.setValueReference(parameterValues.get(0));

            int srid = 4326;
            if (parameterValues.get(parameterValues.size() - 1).startsWith(getSrsNamePrefixSosV2())
                    || parameterValues.get(parameterValues.size() - 1).startsWith(getSrsNamePrefix())) {
                hasSrid = true;
                srid = SosHelper.parseSrsName(parameterValues.get(parameterValues.size() - 1));
            }

            List<String> coordinates;
            if (hasSrid) {
                coordinates = parameterValues.subList(1, parameterValues.size() - 1);
            } else {
                coordinates = parameterValues.subList(1, parameterValues.size());
            }

            if (coordinates.size() != 4) {
                throw new InvalidParameterValueException().at(parameterName)
                        .withMessage("The parameter value is not valid!");
            }
            String lowerCorner = String.format(Locale.US, "%f %f", new Float(coordinates.get(0)), new Float(coordinates.get(1)));
            String upperCorner = String.format(Locale.US, "%f %f", new Float(coordinates.get(2)), new Float(coordinates.get(3)));
            spatialFilter.setGeometry(JTSHelper.createGeometryFromWKT(JTSHelper.createWKTPolygonFromEnvelope(lowerCorner, upperCorner), srid));
            spatialFilter.setOperator(SpatialOperator.BBOX);
            return spatialFilter;
        }
        return null;
    }

    protected List<TemporalFilter> parseTemporalFilter(List<String> parameterValues, String parameterName)
            throws OwsExceptionReport, DateTimeParseException {
        List<TemporalFilter> filterList = new ArrayList<TemporalFilter>(1);
        if (parameterValues.size() != 2) {
            throw new InvalidParameterValueException().withMessage("The parameter value is not valid!");
        }
        filterList.add(createTemporalFilterFromValue(parameterValues.get(1), parameterValues.get(0)));
        return filterList;
    }

    protected Map<String, String> parseNamespaces(String parameterValues) {
        List<String> array =
                Arrays.asList(parameterValues.replaceAll("\\),", "").replaceAll("\\)", "").split("xmlns\\("));
        Map<String, String> namespaces = new HashMap<String, String>(array.size());
        for (String string : array) {
            if (string != null && !string.isEmpty()) {
                String[] s = string.split(",");
                namespaces.put(s[0], s[1]);
            }
        }
        return namespaces;
    }
    
    private TemporalFilter createTemporalFilterFromValue(String value, String valueReference) throws OwsExceptionReport,
                                                                                                     DateTimeParseException {
        TemporalFilter temporalFilter = new TemporalFilter();
        temporalFilter.setValueReference(valueReference);
        String[] times = value.split("/");

        if (times.length == 1) {
            TimeInstant ti = new TimeInstant();
            if (IndeterminateTime.contains(times[0])) {
                ti.setIndeterminateValue(times[0]);
            } else {
                DateTime instant = DateTimeHelper.parseIsoString2DateTime(times[0]);
                ti.setValue(instant);
                String valueSplit = null;
                if (times[0].contains("Z")) {
                    valueSplit = times[0].substring(0, times[0].indexOf('Z'));
                } else if (times[0].contains("+")) {
                    valueSplit = times[0].substring(0, times[0].indexOf('+'));
                }
                if (valueSplit != null) {
                    ti.setRequestedTimeLength(valueSplit.length());
                } else {
                    ti.setRequestedTimeLength(times[0].length());
                }  
            }
            temporalFilter.setOperator(TimeOperator.TM_Equals);
            temporalFilter.setTime(ti);
        } else if (times.length == 2) {
            DateTime start = DateTimeHelper.parseIsoString2DateTime(times[0]);
            // check if end time is a full ISO 8106 string
            String valueSplit = null;
            if (times[1].contains("Z")) {
                valueSplit = times[1].substring(0, times[1].indexOf('Z'));
            } else if (times[1].contains("+")) {
                valueSplit = times[1].substring(0, times[1].indexOf('+'));
            }
            DateTime end;
            if (valueSplit != null) {
                end = DateTimeHelper.setDateTime2EndOfDay4RequestedEndPosition(
                      DateTimeHelper.parseIsoString2DateTime(times[1]), valueSplit.length());
            } else {
                end = DateTimeHelper.setDateTime2EndOfDay4RequestedEndPosition(
                      DateTimeHelper.parseIsoString2DateTime(times[1]), times[1].length());
            }
            TimePeriod tp = new TimePeriod();
            tp.setStart(start);
            tp.setEnd(end);
            temporalFilter.setOperator(TimeOperator.TM_During);
            temporalFilter.setTime(tp);

        } else {
            throw new InvalidParameterValueException().withMessage("The paramter value '%s' is invalid!", value);
        }
        return temporalFilter;
    }

    protected String getSrsNamePrefix() {
        return ServiceConfiguration.getInstance().getSrsNamePrefix();
    }

    protected String getSrsNamePrefixSosV2() {
        return ServiceConfiguration.getInstance().getSrsNamePrefixSosV2();
    }

}
