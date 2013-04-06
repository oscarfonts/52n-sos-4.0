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
package org.n52.sos.decode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.opengis.swe.x20.AbstractDataComponentDocument;
import net.opengis.swe.x20.AbstractDataComponentType;
import net.opengis.swe.x20.AbstractEncodingType;
import net.opengis.swe.x20.AnyScalarPropertyType;
import net.opengis.swe.x20.BooleanType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.CountPropertyType;
import net.opengis.swe.x20.CountRangeType;
import net.opengis.swe.x20.CountType;
import net.opengis.swe.x20.DataArrayDocument;
import net.opengis.swe.x20.DataArrayPropertyType;
import net.opengis.swe.x20.DataArrayType;
import net.opengis.swe.x20.DataRecordDocument;
import net.opengis.swe.x20.DataRecordType;
import net.opengis.swe.x20.DataRecordType.Field;
import net.opengis.swe.x20.EncodedValuesPropertyType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;
import net.opengis.swe.x20.TextEncodingDocument;
import net.opengis.swe.x20.TextEncodingType;
import net.opengis.swe.x20.TextType;
import net.opengis.swe.x20.TimeRangeType;
import net.opengis.swe.x20.TimeType;
import net.opengis.swe.x20.VectorType.Coordinate;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.joda.time.DateTime;
import org.n52.sos.exception.ows.InvalidParameterValueException;
import org.n52.sos.exception.ows.concrete.NotYetSupportedException;
import org.n52.sos.exception.ows.concrete.UnsupportedDecoderInputException;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.ogc.swe.RangeValue;
import org.n52.sos.ogc.swe.SWEConstants;
import org.n52.sos.ogc.swe.SWEConstants.SweCoordinateName;
import org.n52.sos.ogc.swe.SosSweAbstractDataComponent;
import org.n52.sos.ogc.swe.SosSweCoordinate;
import org.n52.sos.ogc.swe.SosSweDataArray;
import org.n52.sos.ogc.swe.SosSweDataRecord;
import org.n52.sos.ogc.swe.SosSweField;
import org.n52.sos.ogc.swe.encoding.SosSweAbstractEncoding;
import org.n52.sos.ogc.swe.encoding.SosSweTextEncoding;
import org.n52.sos.ogc.swe.simpleType.SosSweAbstractSimpleType;
import org.n52.sos.ogc.swe.simpleType.SosSweBoolean;
import org.n52.sos.ogc.swe.simpleType.SosSweCategory;
import org.n52.sos.ogc.swe.simpleType.SosSweCount;
import org.n52.sos.ogc.swe.simpleType.SosSweCountRange;
import org.n52.sos.ogc.swe.simpleType.SosSweQuality;
import org.n52.sos.ogc.swe.simpleType.SosSweQuantity;
import org.n52.sos.ogc.swe.simpleType.SosSweQuantityRange;
import org.n52.sos.ogc.swe.simpleType.SosSweText;
import org.n52.sos.ogc.swe.simpleType.SosSweTime;
import org.n52.sos.ogc.swe.simpleType.SosSweTimeRange;
import org.n52.sos.service.ServiceConstants.SupportedTypeKey;
import org.n52.sos.util.CodingHelper;
import org.n52.sos.util.DateTimeHelper;
import org.n52.sos.util.StringHelper;
import org.n52.sos.util.XmlOptionsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SweCommonDecoderV20 implements Decoder<Object, Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SweCommonDecoderV20.class);

    private static final Set<DecoderKey> DECODER_KEYS = CodingHelper.decoderKeysForElements(
            SWEConstants.NS_SWE_20,
            DataArrayPropertyType.class,
            DataArrayDocument.class,
            DataArrayType.class,
            DataRecordDocument.class,
            DataRecordType.class,
            CountType.class,
            QuantityType.class,
            TextType.class,
            Coordinate[].class,
            AnyScalarPropertyType[].class,
            TextEncodingDocument.class,
            TextEncodingType.class,
            AbstractDataComponentDocument.class,
            AbstractDataComponentType.class);

    public SweCommonDecoderV20() {
        LOGGER.debug("Decoder for the following keys initialized successfully: {}!",
                StringHelper.join(", ", DECODER_KEYS));
    }

    @Override
    public Set<DecoderKey> getDecoderKeyTypes() {
        return Collections.unmodifiableSet(DECODER_KEYS);
    }

    @Override
    public Map<SupportedTypeKey, Set<String>> getSupportedTypes() {
        return Collections.emptyMap();
    }

    @Override
    public Set<String> getConformanceClasses() {
        return Collections.emptySet();
    }

    @Override
    public Object decode(Object element) throws OwsExceptionReport {
        if (element instanceof DataArrayPropertyType) {
            DataArrayPropertyType dataArrayPropertyType = (DataArrayPropertyType) element;
            return parseAbstractDataComponent(dataArrayPropertyType.getDataArray1());
        } else if (element instanceof AbstractDataComponentDocument) {
            return parseAbstractDataComponentDocument((AbstractDataComponentDocument) element);
        } else if (element instanceof AbstractDataComponentType) {
            return parseAbstractDataComponent((AbstractDataComponentType) element);
        } else if (element instanceof Coordinate[]) {
            return parseCoordinates((Coordinate[]) element);
        } else if (element instanceof AnyScalarPropertyType[]) {
            return parseAnyScalarPropertyTypeArray((AnyScalarPropertyType[]) element);
        } else if (element instanceof TextEncodingDocument) {
            TextEncodingDocument textEncodingDoc = (TextEncodingDocument) element;
            SosSweTextEncoding sosTextEncoding = parseTextEncoding(textEncodingDoc.getTextEncoding());
            sosTextEncoding.setXml(textEncodingDoc.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
            return sosTextEncoding;
        } else if (element instanceof TextEncodingType) {
            TextEncodingDocument textEncodingDoc =
                    TextEncodingDocument.Factory.newInstance(XmlOptionsHelper.getInstance().getXmlOptions());
            TextEncodingType textEncoding = (TextEncodingType) element;
            textEncodingDoc.setTextEncoding(textEncoding);
            SosSweTextEncoding sosTextEncoding = parseTextEncoding(textEncoding);
            sosTextEncoding.setXml(textEncodingDoc.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
            return sosTextEncoding;
        } else {
            throw new UnsupportedDecoderInputException(this, element);
        }
    }

    private SosSweAbstractDataComponent parseAbstractDataComponent(AbstractDataComponentType abstractDataComponent)
            throws OwsExceptionReport {
        SosSweAbstractDataComponent sosAbstractDataComponent = null;
        if (abstractDataComponent instanceof BooleanType) {
            sosAbstractDataComponent = parseBoolean((BooleanType) abstractDataComponent);
        } else if (abstractDataComponent instanceof CategoryType) {
            sosAbstractDataComponent = parseCategory((CategoryType) abstractDataComponent);
        } else if (abstractDataComponent instanceof CountRangeType) {
            sosAbstractDataComponent = parseCountRange((CountRangeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof CountType) {
            sosAbstractDataComponent = parseCount((CountType) abstractDataComponent);
        } else if (abstractDataComponent instanceof QuantityType) {
            sosAbstractDataComponent = parseQuantity((QuantityType) abstractDataComponent);
        } else if (abstractDataComponent instanceof QuantityRangeType) {
            sosAbstractDataComponent = parseQuantityRange((QuantityRangeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof TextType) {
            sosAbstractDataComponent = parseText((TextType) abstractDataComponent);
        } else if (abstractDataComponent instanceof TimeType) {
            sosAbstractDataComponent = parseTime((TimeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof TimeRangeType) {
            sosAbstractDataComponent = parseTimeRange((TimeRangeType) abstractDataComponent);
        } else if (abstractDataComponent instanceof DataArrayDocument) {
            sosAbstractDataComponent = parseDataArray(((DataArrayDocument) abstractDataComponent).getDataArray1());
        } else if (abstractDataComponent instanceof DataRecordType) {
            SosSweDataRecord sosDataRecord = parseDataRecord((DataRecordType) abstractDataComponent);
            DataRecordDocument dataRecordDoc =
                    DataRecordDocument.Factory.newInstance(XmlOptionsHelper.getInstance().getXmlOptions());
            dataRecordDoc.setDataRecord((DataRecordType) abstractDataComponent);
            sosDataRecord.setXml(dataRecordDoc.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
            sosAbstractDataComponent = sosDataRecord;
        } else if (abstractDataComponent instanceof DataArrayType) {
            SosSweDataArray sosDataArray = parseDataArray((DataArrayType) abstractDataComponent);
            DataArrayDocument dataArrayDoc =
                    DataArrayDocument.Factory.newInstance(XmlOptionsHelper.getInstance().getXmlOptions());
            dataArrayDoc.setDataArray1((DataArrayType) abstractDataComponent);
            sosDataArray.setXml(dataArrayDoc.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
            sosAbstractDataComponent = sosDataArray;
        }
        if (sosAbstractDataComponent != null) {
            if (abstractDataComponent.isSetDefinition()) {
                sosAbstractDataComponent.setDefinition(abstractDataComponent.getDefinition());
            }
            if (abstractDataComponent.isSetDescription()) {
                sosAbstractDataComponent.setDescription(abstractDataComponent.getDescription());
            }
            if (abstractDataComponent.isSetIdentifier()) {
                sosAbstractDataComponent.setIdentifier(abstractDataComponent.getIdentifier());
            }
            if (abstractDataComponent.isSetLabel()) {
                sosAbstractDataComponent.setLabel(abstractDataComponent.getLabel());
            }
        }
        return sosAbstractDataComponent;
    }

    private Object parseAbstractDataComponentDocument(AbstractDataComponentDocument abstractDataComponentDoc)
            throws OwsExceptionReport {
        SosSweAbstractDataComponent sosAbstractDataComponent =
                parseAbstractDataComponent(abstractDataComponentDoc.getAbstractDataComponent());
        sosAbstractDataComponent.setXml(abstractDataComponentDoc.xmlText(XmlOptionsHelper.getInstance()
                .getXmlOptions()));
        return sosAbstractDataComponent;
        // if (element instanceof DataArrayDocument) {
        // DataArrayDocument dataArrayDoc = (DataArrayDocument) element;
        // SosSweDataArray sosDataArray =
        // parseDataArray(dataArrayDoc.getDataArray1());
        // sosDataArray.setXml(dataArrayDoc.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
        // return sosDataArray;
        // } else if (element instanceof DataRecordDocument) {
        // DataRecordDocument dataRecordDoc = (DataRecordDocument) element;
        // SosSweDataRecord sosDataRecord =
        // parseDataRecord(dataRecordDoc.getDataRecord());
        // sosDataRecord.setXml(dataRecordDoc.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
        // return sosDataRecord;
        // }
        // return null;
    }

    private SosSweDataArray parseDataArray(DataArrayType xbDataArray) throws OwsExceptionReport {
        SosSweDataArray sosSweDataArray = new SosSweDataArray();
        
        CountPropertyType elementCount = xbDataArray.getElementCount();
        if (elementCount != null) { 
            sosSweDataArray.setElementCount(parseElementCount(elementCount));
        }
        
        // parse data record to elementType
        DataArrayType.ElementType xbElementType = xbDataArray.getElementType();
        if (xbElementType != null && xbElementType.getAbstractDataComponent() != null) {
            sosSweDataArray.setElementType(parseAbstractDataComponent(xbElementType.getAbstractDataComponent()));
        }
        if (xbDataArray.isSetEncoding()) {
            sosSweDataArray.setEncoding(parseEncoding(xbDataArray.getEncoding().getAbstractEncoding()));
        }

        // parse values
        if (xbDataArray.isSetValues()) {
            sosSweDataArray.setValues(parseValues(sosSweDataArray.getElementCount(), sosSweDataArray.getElementType(),
                    sosSweDataArray.getEncoding(), xbDataArray.getValues()));
        }
        // set XML
        DataArrayDocument dataArrayDoc =
                DataArrayDocument.Factory.newInstance(XmlOptionsHelper.getInstance().getXmlOptions());
        dataArrayDoc.setDataArray1(xbDataArray);
        sosSweDataArray.setXml(dataArrayDoc.xmlText(XmlOptionsHelper.getInstance().getXmlOptions()));
        return sosSweDataArray;
    }

    private List<List<String>> parseValues(SosSweCount elementCount, SosSweAbstractDataComponent elementType,
            SosSweAbstractEncoding encoding, EncodedValuesPropertyType encodedValuesPropertyType)
            throws OwsExceptionReport {
        assert elementCount != null;
        assert elementType != null;
        assert encoding != null;
        if (checkParameterTypes(elementType, encoding)) {
            // Get swe values String via cursor as String
            String values;
            // TODO replace XmlCursor
            /*
             * if (encodedValuesPropertyType.schemaType() == XmlString.type) {
             * XmlString xbString
             */
            // @see SosDecoderv20#parseResultValues
            XmlCursor xbCursor = encodedValuesPropertyType.newCursor();
            xbCursor.toFirstContentToken();
            if (xbCursor.isText()) {
                values = xbCursor.getTextValue().trim();
                xbCursor.dispose();
                if (values != null && !values.isEmpty()) {
                    SosSweTextEncoding textEncoding = (SosSweTextEncoding) encoding;

                    String[] blocks = values.split(textEncoding.getBlockSeparator());
                    List<List<String>> resultValues = new ArrayList<List<String>>(blocks.length);
                    for (String block : blocks) {
                        String[] tokens = block.split(textEncoding.getTokenSeparator());
                        List<String> tokenList = Arrays.asList(tokens);
                        resultValues.add(tokenList);
                    }
                    return resultValues;
                }
            }
        }
        assert false;
        return null;
    }

    private boolean checkParameterTypes(SosSweAbstractDataComponent elementType, SosSweAbstractEncoding encoding)
            throws OwsExceptionReport {
        if (!(encoding instanceof SosSweTextEncoding)) {
            throw new NotYetSupportedException("encodingType", encoding);
        }
        if (!(elementType instanceof SosSweDataRecord)) {
            throw new NotYetSupportedException("elementType", elementType);
        }
        return true;
    }

    private SosSweAbstractEncoding parseEncoding(AbstractEncodingType abstractEncodingType) throws OwsExceptionReport {
        assert abstractEncodingType != null;
        if (abstractEncodingType instanceof TextEncodingType) {
            return parseTextEncoding((TextEncodingType) abstractEncodingType);
        }
        throw new NotYetSupportedException("encodingType", abstractEncodingType, TextEncodingType.type.getName());
    }

    private SosSweDataRecord parseDataRecord(DataRecordType dataRecord) throws OwsExceptionReport {
        SosSweDataRecord sosSweDataRecord = new SosSweDataRecord();
        for (Field field : dataRecord.getFieldArray()) {
            sosSweDataRecord.addField(new SosSweField(field.getName(), parseAbstractDataComponent(field
                    .getAbstractDataComponent())));
        }
        return sosSweDataRecord;
    }

    private SosSweBoolean parseBoolean(BooleanType xbBoolean) throws OwsExceptionReport {
    	SosSweBoolean sosBoolean = new SosSweBoolean();
    	if (xbBoolean.isSetValue())
    	{
    		sosBoolean.setValue(xbBoolean.getValue());
    	}
    	return sosBoolean;
    }

    private SosSweCategory parseCategory(CategoryType xbCategory) throws OwsExceptionReport {
    	SosSweCategory sosSweCategory = new SosSweCategory();
    	if (xbCategory.isSetCodeSpace() && xbCategory.getCodeSpace().isSetHref())
    	{
    		sosSweCategory.setCodeSpace(xbCategory.getCodeSpace().getHref());
    	}
    	if (xbCategory.isSetValue())
    	{
    		sosSweCategory.setValue(xbCategory.getValue());
    	}
    	return sosSweCategory;
    }

    private SosSweCount parseCount(CountType count) throws OwsExceptionReport {
        SosSweCount sosCount = new SosSweCount();
        if (count.getQualityArray() != null) {
            sosCount.setQuality(parseQuality(count.getQualityArray()));
        }
        if (count.isSetValue()) {
            sosCount.setValue(count.getValue().intValue());
        }
        return sosCount;
    }

    private SosSweCountRange parseCountRange(CountRangeType countRange) throws OwsExceptionReport {
        throw new NotYetSupportedException("CountRange");
    }

    // private SosSweAbstractSimpleType
    // parseObservableProperty(ObservableProperty
    // observableProperty) {
    // ObservableProperty xbObsProp = (ObservableProperty) observableProperty;
    // SosSweText sosObservableProperty = new SosSweText();
    // if (xbObsProp.isSetDefinition()) {
    // sosObservableProperty.setDefinition(xbObsProp.getDefinition());
    // }
    // return sosObservableProperty;
    // }

    private SosSweQuantity parseQuantity(QuantityType xbQuantity) throws OwsExceptionReport {
        SosSweQuantity sosQuantity = new SosSweQuantity();
        if (xbQuantity.isSetAxisID()) {
            sosQuantity.setAxisID(xbQuantity.getAxisID());
        }
        if (xbQuantity.getQualityArray() != null) {
            sosQuantity.setQuality(parseQuality(xbQuantity.getQualityArray()));
        }
        sosQuantity.setUom(xbQuantity.getUom().getCode());
        if (xbQuantity.isSetValue()) {
            sosQuantity.setValue(Double.valueOf(xbQuantity.getValue()));
        }
        return sosQuantity;
    }

    private SosSweQuantityRange parseQuantityRange(QuantityRangeType quantityRange) throws OwsExceptionReport {
        throw new NotYetSupportedException("QuantityRange");
    }

    private SosSweText parseText(TextType xbText) {
        SosSweText sosText = new SosSweText();
        if (xbText.isSetValue()) {
            sosText.setValue(xbText.getValue());
        }
        return sosText;
    }

    private SosSweTime parseTime(TimeType xbTime) throws OwsExceptionReport {
        SosSweTime sosTime = new SosSweTime();
        if (xbTime.isSetValue()) {
            sosTime.setValue(DateTimeHelper.parseIsoString2DateTime(xbTime.getValue().toString()));
        }
        if (xbTime.getUom() != null) {
            sosTime.setUom(xbTime.getUom().getHref());
        }
        return sosTime;
    }

    private SosSweTimeRange parseTimeRange(TimeRangeType xbTime) throws OwsExceptionReport {
        SosSweTimeRange sosTimeRange = new SosSweTimeRange();
        if (xbTime.isSetValue()) {
            // FIXME check if this parses correct
            List<?> value = xbTime.getValue();
            if (value != null && !value.isEmpty()) {
                RangeValue<DateTime> range = new RangeValue<DateTime>();
                boolean first = true;
                for (Object object : value) {
                    if (first) {
                        range.setRangeStart(DateTimeHelper.parseIsoString2DateTime(xbTime.getValue().toString()));
                        first = false;
                    }
                    range.setRangeEnd(DateTimeHelper.parseIsoString2DateTime(xbTime.getValue().toString()));
                }
                sosTimeRange.setValue(range);
            }
        }
        if (xbTime.getUom() != null) {
            sosTimeRange.setUom(xbTime.getUom().getHref());
        }
        return sosTimeRange;
    }

    private SosSweQuality parseQuality(XmlObject[] qualityArray) throws OwsExceptionReport {
        if (qualityArray == null || qualityArray.length == 0) {
            return null;
        }
        throw new NotYetSupportedException("Quality");
    }

    // private SosSMLPosition parsePosition(PositionType position) throws
    // OwsExceptionReport {
    // SosSMLPosition sosSMLPosition = new SosSMLPosition();
    // if (position.isSetLocation()) {
    // if (position.getLocation().isSetVector()) {
    // if (position.getLocation().getVector().isSetReferenceFrame()) {
    // sosSMLPosition.setReferenceFrame(position.getLocation().getVector().getReferenceFrame());
    // }
    // sosSMLPosition.setPosition(parseCoordinates(position.getLocation().getVector().getCoordinateArray()));
    // }
    // }
    // return sosSMLPosition;
    // }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<SosSweCoordinate<?>> parseCoordinates(Coordinate[] coordinateArray) throws OwsExceptionReport {
        List<SosSweCoordinate<?>> sosCoordinates = new ArrayList<SosSweCoordinate<?>>(coordinateArray.length);
        for (Coordinate xbCoordinate : coordinateArray) {
            if (xbCoordinate.isSetQuantity()) {
                sosCoordinates.add(new SosSweCoordinate(checkCoordinateName(xbCoordinate.getName()),
                        (SosSweAbstractSimpleType)parseAbstractDataComponent(xbCoordinate.getQuantity())));
            } else {
                throw new InvalidParameterValueException().at("Position")
                        .withMessage("Error when parsing the Coordinates of Position: It must be of type Quantity!");
            }
            return sosCoordinates;
        }
        return null;
    }

    private SweCoordinateName checkCoordinateName(String name) throws OwsExceptionReport {
        if (name.equals(SweCoordinateName.easting.name())) {
            return SweCoordinateName.easting;
        } else if (name.equals(SweCoordinateName.northing.name())) {
            return SweCoordinateName.northing;
        } else if (name.equals(SweCoordinateName.altitude.name())) {
            return SweCoordinateName.altitude;
        } else {
            throw new InvalidParameterValueException().at("Position")
                    .withMessage("The coordinate name is neighter 'easting' nor 'northing' nor 'altitude'!");
        }
    }

    private List<SosSweField> parseAnyScalarPropertyTypeArray(AnyScalarPropertyType[] fieldArray)
            throws OwsExceptionReport {
        List<SosSweField> sosFields = new ArrayList<SosSweField>(fieldArray.length);
        for (AnyScalarPropertyType xbField : fieldArray) {
            // if (xbField.isSetBoolean()) {
            // sosFields.add(new SosSweField(xbField.getName(),
            // parseAbstractDataComponent(xbField.getBoolean())));
            // } else if (xbField.isSetCategory()) {
            // sosFields.add(new SosSweField(xbField.getName(),
            // parseAbstractDataComponent(xbField.getCategory())));
            // } else if (xbField.isSetCount()) {
            // sosFields.add(new SosSweField(xbField.getName(),
            // parseAbstractDataComponent(xbField.getCount())));
            // } else if (xbField.isSetQuantity()) {
            // sosFields.add(new SosSweField(xbField.getName(),
            // parseAbstractDataComponent(xbField.getQuantity())));
            // } else if (xbField.isSetText()) {
            // sosFields.add(new SosSweField(xbField.getName(),
            // parseAbstractDataComponent(xbField.getText())));
            // } else if (xbField.isSetTime()) {
            // sosFields.add(new SosSweField(xbField.getName(),
            // parseAbstractDataComponent(xbField.getTime())));
            // }
        }
        return sosFields;
    }

    private SosSweTextEncoding parseTextEncoding(TextEncodingType textEncoding) {
        SosSweTextEncoding sosTextEncoding = new SosSweTextEncoding();
        sosTextEncoding.setBlockSeparator(textEncoding.getBlockSeparator());
        sosTextEncoding.setTokenSeparator(textEncoding.getTokenSeparator());
        if (textEncoding.isSetDecimalSeparator()) {
            sosTextEncoding.setDecimalSeparator(textEncoding.getDecimalSeparator());
        }
        if (textEncoding.isSetCollapseWhiteSpaces()) {
            sosTextEncoding.setCollapseWhiteSpaces(textEncoding.getCollapseWhiteSpaces());
        }
        return sosTextEncoding;
    }

    private SosSweCount parseElementCount(CountPropertyType elementCount) throws OwsExceptionReport {
        if (elementCount.isSetCount()) {
            return (SosSweCount)parseAbstractDataComponent(elementCount.getCount());
        }
        return null;
    }
}
