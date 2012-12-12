/**
 * Copyright (C) 2012
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
package org.n52.sos.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.n52.sos.service.ConfigurationException;
import org.n52.sos.service.AbstractLoggingConfigurator;
import org.n52.sos.service.AbstractLoggingConfigurator.Appender;
import org.n52.sos.service.AbstractLoggingConfigurator.Level;
import org.slf4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LogBackLoggingConfigurator extends AbstractLoggingConfigurator {

    private static final String CONFIGURATION_FILE_NAME = "/logback.xml";
    private static final String AN_LEVEL = "level";
    private static final String AN_NAME = "name";
    private static final String AN_VALUE = "value";
    private static final String AN_REF = "ref";
    private static final String EN_ROLLING_POLICY = "rollingPolicy";
    private static final String EN_MAX_HISTORY = "maxHistory";
    private static final String EN_APPENDER = "appender";
    private static final String EN_APPENDER_REF = "appender-ref";
    private static final String EN_ROOT = "root";
    private static final String EN_LOGGER = "logger";
    private static final String NOT_FOUND_ERROR_MESSAGE = "Can't find Logback configuration file.";
    private static final String UNPARSABLE_ERROR_MESSAGE = "Can't parse configuration file.";
    private static final String UNWRITABLE_ERROR_MESSAGE = "Can't write configuration file.";
    private static final int WRITE_DELAY = 4000;
    private static final ReadWriteLock lock = new ReentrantReadWriteLock();
    private Document cache = null;
    private File configuration = null;
    private DelayedWriteThread delayedWriteThread = null;

    private class DelayedWriteThread extends Thread {

        private Document doc;
        private boolean canceled = false;

        DelayedWriteThread(Document doc) {
            this.doc = doc;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(WRITE_DELAY);
                synchronized (this) {
                    if (!canceled) {
                        write();
                    }
                }
            } catch (InterruptedException e) {
            }
        }

        void cancel() {
            synchronized (this) {
                canceled = true;
            }
        }

        void write() {
            lock.writeLock().lock();
            log.debug("Writing LogBack configuration file!");
            try {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(configuration);
                    Transformer trans = TransformerFactory.newInstance().newTransformer();
                    trans.setOutputProperty(OutputKeys.INDENT, "yes");
                    OutputStreamWriter writer = new OutputStreamWriter(out);
                    trans.transform(new DOMSource(doc), new StreamResult(writer));
                } catch (TransformerException ex) {
                    log.error(UNWRITABLE_ERROR_MESSAGE, ex);
                } catch (IOException ex) {
                    log.error(UNWRITABLE_ERROR_MESSAGE, ex);
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            log.error(UNWRITABLE_ERROR_MESSAGE, e);
                        }
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public LogBackLoggingConfigurator() throws ConfigurationException {
        this(CONFIGURATION_FILE_NAME);
    }

    public LogBackLoggingConfigurator(String filename) throws ConfigurationException {
        this(getFile(filename));
    }

    private static File getFile(String name) throws ConfigurationException {
        File f = new File(name);
        if (f.exists()) {
            return f;
        }
        URL url = LogBackLoggingConfigurator.class.getResource(name);
        try {
            return new File(url.toURI());
        } catch (Exception ex) {
            log.error(NOT_FOUND_ERROR_MESSAGE, ex);
            throw new ConfigurationException(NOT_FOUND_ERROR_MESSAGE, ex);
        }
    }

    public LogBackLoggingConfigurator(File file) throws ConfigurationException {
        configuration = file;
        if (configuration == null || !configuration.exists()) {
            log.error(NOT_FOUND_ERROR_MESSAGE);
            throw new ConfigurationException(NOT_FOUND_ERROR_MESSAGE);
        }
        log.info("Using Logback Config File: {}", configuration.getAbsolutePath());
    }

    private Document read() throws ConfigurationException {
        lock.readLock().lock();
        try {
            try {
                if (cache == null) {
                    cache = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(configuration);
                }
                return cache;
            } catch (ParserConfigurationException ex) {
                log.error(UNPARSABLE_ERROR_MESSAGE, ex);
                throw new ConfigurationException(UNPARSABLE_ERROR_MESSAGE, ex);
            } catch (SAXException ex) {
                log.error(UNPARSABLE_ERROR_MESSAGE, ex);
                throw new ConfigurationException(UNPARSABLE_ERROR_MESSAGE, ex);
            } catch (IOException ex) {
                log.error(UNPARSABLE_ERROR_MESSAGE, ex);
                throw new ConfigurationException(UNPARSABLE_ERROR_MESSAGE, ex);
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    private void write() {
        lock.writeLock().lock();
        try {
            /* delay the actual writing to aggregate changes to one IO task */
            if (this.delayedWriteThread != null) {
                this.delayedWriteThread.cancel();
                this.delayedWriteThread.interrupt();
            }
            this.delayedWriteThread = new DelayedWriteThread(this.cache);
            this.delayedWriteThread.start();
        } finally {
            lock.writeLock().unlock();
        }
        
    }

    @Override
    public boolean setMaxHistory(int days) {
        lock.writeLock().lock();
        try {
            Document doc = read();
            List<Element> appender = getChildren(doc.getDocumentElement(), EN_APPENDER);
            for (Element a : appender) {
                if (getAttribute(a, AN_NAME).getValue().equals(Appender.FILE.name)) {
                    Element rollingPolicy = getSingleChildren(a, EN_ROLLING_POLICY);
                    Element maxHistory = getSingleChildren(rollingPolicy, EN_MAX_HISTORY);
                    int before = -1;
                    try {
                        before = Integer.parseInt(maxHistory.getTextContent());
                    } catch (NumberFormatException e) {
                    }
                    if (before != days) {
                        log.debug("Setting max logging history to {} days.", days);
                        maxHistory.setTextContent(String.valueOf(days));
                    }
                }
            }
            write();
        } catch (ConfigurationException e) {
            log.error(UNPARSABLE_ERROR_MESSAGE, e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
        return true;
    }

    @Override
    public Set<Appender> getEnabledAppender() {
        lock.readLock().lock();
        Set<Appender> appender = new HashSet<Appender>(Appender.values().length);
        try {
            List<Element> refs = getChildren(getRoot(read().getDocumentElement()), EN_APPENDER_REF);
            for (Element ref : refs) {
                appender.add(Appender.byName(getAttribute(ref, AN_REF).getValue()));
            }
        } catch (ConfigurationException e) {
            log.error(UNPARSABLE_ERROR_MESSAGE, e);
            return Collections.emptySet();
        } finally {
            lock.readLock().unlock();
        }
        return appender;
    }

    @Override
    public boolean isEnabled(Appender a) {
        lock.readLock().lock();
        try {
            return getEnabledAppender().contains(a);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean enableAppender(Appender a, boolean enable) {
        lock.writeLock().lock();
        try {
            Document doc = read();
            Element root = getRoot(doc.getDocumentElement());
            Element refNode = null;
            List<Element> refs = getChildren(root, EN_APPENDER_REF);
            for (Element ref : refs) {
                if (getAttribute(ref, AN_REF).getValue().equals(a.name)) {
                    refNode = ref;
                    break;
                }
            }
            if (enable && refNode == null) {
                log.debug("Enabling {} logging appender", a.name);
                refNode = doc.createElement(EN_APPENDER_REF);
                refNode.setAttribute(AN_REF, a.name);
                root.appendChild(refNode);
                write();
            } else if (!enable && refNode != null) {
                log.debug("Disabling {} logging appender", a.name);
                root.removeChild(refNode);
                write();
            }
            return true;
        } catch (ConfigurationException e) {
            log.error(UNPARSABLE_ERROR_MESSAGE, e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private Element getSingleChildren(Node parent, String name) throws ConfigurationException {
        NodeList nl = parent.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);
            if (name.equals(n.getNodeName())) {
                return (Element) n;
            }

        }
        throw new ConfigurationException("<" + name + "> not found!");
    }

    private Element getRoot(Node configuration) throws ConfigurationException {
        return getSingleChildren(configuration, EN_ROOT);
    }

    private Attr getAttribute(Node x, String name) throws ConfigurationException {
        NamedNodeMap attributes = x.getAttributes();
        Attr a = (Attr) attributes.getNamedItem(name);
        if (a != null) {
            return a;
        }
        throw new ConfigurationException("Missing attribute: " + name);
    }

    @Override
    public boolean setRootLogLevel(Level level) {
        lock.writeLock().lock();
        try {
            try {
                Document doc = read();
                Element root = getRoot(doc.getDocumentElement());
                String currentLevel = getAttribute(root, AN_LEVEL).getValue();
                if (Level.valueOf(currentLevel) == level) {
                    return true;
                }
                log.debug("Setting root logging level to {}", level);
                root.setAttribute(AN_LEVEL, level.toString());
                write();
            } catch (ConfigurationException e) {
                log.error(UNPARSABLE_ERROR_MESSAGE, e);
                return false;
            }
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private List<Element> getChildren(Element parent, String name) {
        NodeList nl = parent.getChildNodes();
        ArrayList<Element> childs = new ArrayList<Element>(nl.getLength());
        for (int i = 0; i < nl.getLength(); ++i) {
            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE && nl.item(i).getNodeName().equals(name)) {
                childs.add((Element) nl.item(i));
            }
        }
        return childs;
    }

    @Override
    public boolean setLoggerLevel(String id, Level level) {
        lock.writeLock().lock();
        try {
            if (id.equals(Logger.ROOT_LOGGER_NAME)) {
                return setRootLogLevel(level);
            }
            Document doc = read();
            Element conf = doc.getDocumentElement();
            Element l = null;
            List<Element> loggers = getChildren(conf, EN_LOGGER);
            for (Element logger : loggers) {
                if (getAttribute(logger, AN_NAME).getValue().equals(id)) {
                    l = logger;
                }
            }
            if (l == null) {
                log.debug("Setting logging level of {} to {}.", id, level);
                l = doc.createElement(id);
                l.setAttribute(AN_NAME, id);
                l.setAttribute(AN_LEVEL, level.name());
                conf.appendChild(l);
                write();
            } else {
                String oldLevel = l.getAttribute(AN_LEVEL);
                if (!oldLevel.equals(level.name())) {
                    log.debug("Setting logging level of {} to {}.", id, level);
                    l.setAttribute(AN_LEVEL, level.name());
                    write();
                }
            }
            return true;
        } catch (ConfigurationException e) {
            log.error(UNPARSABLE_ERROR_MESSAGE, e);
            return false;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Level getRootLogLevel() {
        lock.readLock().lock();
        try {
            Level level = null;
            try {
                Document doc = read();
                Element root = getRoot(doc.getDocumentElement());
                String currentLevel = getAttribute(root, AN_LEVEL).getValue();
                level = Level.valueOf(currentLevel);
            } catch (ConfigurationException e) {
                log.error(UNPARSABLE_ERROR_MESSAGE, e);
            }
            return level;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Level> getLoggerLevels() {
        lock.readLock().lock();
        try {
            Map<String, Level> levels = new HashMap<String, Level>();
            try {
                List<Element> loggers = getChildren(read().getDocumentElement(), EN_LOGGER);
                for (Element logger : loggers) {
                    levels.put(getAttribute(logger, AN_NAME).getValue(),
                            Level.valueOf(getAttribute(logger, AN_LEVEL).getValue()));
                }
            } catch (ConfigurationException e) {
                log.error(UNPARSABLE_ERROR_MESSAGE, e);
            }
            return levels;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Level getLoggerLevel(String id) {
        lock.readLock().lock();
        try {
            if (id.equals(Logger.ROOT_LOGGER_NAME)) {
                return getRootLogLevel();
            }
            return getLoggerLevels().get(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getMaxHistory() {
        lock.readLock().lock();
        try {
            int max = -1;
            try {
                Document doc = read();
                List<Element> appender = getChildren(doc.getDocumentElement(), EN_APPENDER);
                for (Element a : appender) {
                    if (getAttribute(a, AN_NAME).getValue().equals(Appender.FILE.name)) {
                        try {
                            max = Integer.parseInt(getSingleChildren(
                                    getSingleChildren(a, EN_ROLLING_POLICY), EN_MAX_HISTORY)
                                    .getTextContent());
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            } catch (ConfigurationException e) {
                log.error(UNPARSABLE_ERROR_MESSAGE, e);
            }
            return max;
        } finally {
            lock.readLock().unlock();
        }
    }
}