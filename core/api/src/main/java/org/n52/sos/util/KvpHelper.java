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
package org.n52.sos.util;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.n52.sos.exception.ows.InvalidParameterValueException;
import org.n52.sos.exception.ows.MissingParameterValueException;

/**
 * Utility class for Key-Value-Pair (KVP) requests
 * 
 */
public final class KvpHelper {

    public static Map<String, String> getKvpParameterValueMap(HttpServletRequest req) {
        Map<String, String> kvp = new HashMap<String, String>();
        Enumeration<?> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            // all key names to lower case
            String key = (String) parameterNames.nextElement();
            kvp.put(checkKeyForPrefix(key), req.getParameter(key));
        }
        return kvp;
    }

    private static String checkKeyForPrefix(String key) {
        return key.replace("amp;", "").toLowerCase();
    }

    public static String checkParameterSingleValue(String parameterValue, String parameterName)
            throws MissingParameterValueException, InvalidParameterValueException {
        if (checkParameterMultipleValues(parameterValue, parameterName).size() == 1) {
            return parameterValue;
        } else {
            throw new InvalidParameterValueException(parameterName, parameterValue);
        }
    }

    public static String checkParameterSingleValue(String parameterValue, Enum<?> parameterName)
            throws MissingParameterValueException, InvalidParameterValueException {
        return checkParameterSingleValue(parameterValue, parameterName.name());
    }

    public static List<String> checkParameterMultipleValues(String parameterValues, String parameterName)
            throws MissingParameterValueException, InvalidParameterValueException {
        if (parameterValues.isEmpty()) {
            throw new MissingParameterValueException(parameterName);
        }
        List<String> splittedParameterValues = Arrays.asList(parameterValues.split(","));
        for (String parameterValue : splittedParameterValues) {
            if (isNullOrEmpty(parameterValue)) {
                throw new MissingParameterValueException(parameterName);
            }
        }
        return splittedParameterValues;
    }

    public static List<String> checkParameterMultipleValues(String parameterValues, Enum<?> parameterName) throws
            MissingParameterValueException, InvalidParameterValueException {
        return checkParameterMultipleValues(parameterValues, parameterName.name());
    }

    public static void checkParameterValue(String parameterValue, String parameterName) throws
            MissingParameterValueException, InvalidParameterValueException {
        if (isNullOrEmpty(parameterValue)) {
            throw new MissingParameterValueException(parameterName);
        }
    }

    public static void checkParameterValue(String parameterValue, Enum<?> parameterName) throws
            MissingParameterValueException, InvalidParameterValueException {
        checkParameterValue(parameterValue, parameterName.name());
    }

    public static boolean isNullOrEmpty(String parameterValue) {
        return parameterValue == null || parameterValue.isEmpty();
    }

    private static String getParameterValue(String parameterName, Map<String, String> parameterMap) {
        for (String key : parameterMap.keySet()) {
            if (key.equalsIgnoreCase(parameterName)) {
                 return parameterMap.get(key);
            }
        }
        return null;
    }

    public static String getParameterValue(Enum<?> parameterName, Map<String, String> parameterMap) {
        return getParameterValue(parameterName.name(), parameterMap);
    }
    
    private KvpHelper() {
    }
}
