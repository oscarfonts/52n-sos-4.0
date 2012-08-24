package org.n52.sos.ds.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.n52.sos.decode.IDecoder;
import org.n52.sos.ds.IConnectionProvider;
import org.n52.sos.ds.IDataSourceInitializator;
import org.n52.sos.ds.hibernate.util.HibernateCriteriaTransactionalUtilities;
import org.n52.sos.encode.IEncoder;
import org.n52.sos.ogc.ows.OwsExceptionReport;
import org.n52.sos.service.Configurator;
import org.n52.sos.service.ServiceConstants.SupportedTypeKey;
import org.n52.sos.util.Util4Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceInitializator implements IDataSourceInitializator {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceInitializator.class);

    /**
     * Instance of the IConnectionProvider
     */
    private IConnectionProvider connectionProvider;

    public DataSourceInitializator() {
        connectionProvider = Configurator.getInstance().getConnectionProvider();
    }

    public void initializeDataSource() throws OwsExceptionReport {
        Session session = null;
        Transaction transaction = null;
        try {
            Map<SupportedTypeKey, Set<String>> typeMap = getTypeMap();
            session = (Session) connectionProvider.getConnection();
            transaction = session.beginTransaction();
            initializeSupportedFeatureOfInterestTypes(typeMap.get(SupportedTypeKey.FeatureType), session);
            initializeSupportedObservationTypes(typeMap.get(SupportedTypeKey.ObservationType), session);
            initializeSupportedProcedureDescriptionFormats(typeMap.get(SupportedTypeKey.ProcedureDescriptionFormat),
                    session);
            initializeSupportedSweTypes(typeMap.get(SupportedTypeKey.SweType), session);
            // initializeSupportedResultStructureTypes(session);
            
            // TODO: add responseFormat table and insert supported responseFormats
            
            session.flush();
            session.clear();
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null) {
                transaction.rollback();
            }
            String exceptionText = "Error while initializing CapabilitiesCache!";
            LOGGER.debug(exceptionText, he);
            throw Util4Exceptions.createNoApplicableCodeException(he, exceptionText);
        } finally {
            connectionProvider.returnConnection(session);
        }
        LOGGER.info("\n******\n DataSource initialized successfully!\n******\n");

    }

    private Map<SupportedTypeKey, Set<String>> getTypeMap() {
        List<Map<SupportedTypeKey, Set<String>>> list = new ArrayList<Map<SupportedTypeKey, Set<String>>>();
        for (List<IDecoder> decoders : Configurator.getInstance().getDecoderMap().values()) {
            for (IDecoder decoder : decoders) {
                list.add(decoder.getSupportedTypes());
            }
        }
        for (IEncoder encoder : Configurator.getInstance().getEncoderMap().values()) {
            list.add(encoder.getSupportedTypes());
        }
        Map<SupportedTypeKey, Set<String>> typeMap = new HashMap<SupportedTypeKey, Set<String>>(0);
        for (Map<SupportedTypeKey, Set<String>> map : list) {
            for (SupportedTypeKey typeKey : map.keySet()) {
                if (typeMap.containsKey(typeKey)) {
                    typeMap.get(typeKey).addAll(map.get(typeKey));
                } else {
                    typeMap.put(typeKey, map.get(typeKey));
                }
            }
        }
        return typeMap;
    }

    private void initializeSupportedFeatureOfInterestTypes(Set<String> featureTypes, Session session) {
        if (featureTypes != null) {
            HibernateCriteriaTransactionalUtilities.insertFeatureOfInterestTypes(featureTypes, session);
        }
    }

    private void initializeSupportedObservationTypes(Set<String> observationTypes, Session session) {
        if (observationTypes != null) {
            HibernateCriteriaTransactionalUtilities.insertObservationTypes(observationTypes, session);
        }
    }

    private void initializeSupportedProcedureDescriptionFormats(Set<String> procedureDescriptionFormats,
            Session session) {
        if (procedureDescriptionFormats != null) {
            HibernateCriteriaTransactionalUtilities.insertProcedureDescriptionsFormats(procedureDescriptionFormats,
                    session);
        }
    }

    private void initializeSupportedSweTypes(Set<String> set, Session session) {
        // TODO Auto-generated method stub

    }

    private void initializeSupportedResultStructureTypes(Session session) {
        // TODO Auto-generated method stub

    }

}