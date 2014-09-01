package org.jcamp.parser;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import org.jcamp.spectrum.notes.NoteDescriptor;

/**
 * factory for creating marshaller for spectrum notes.
 * @author Thomas Weber
 */
public class NoteMarshallerFactory {
    private static NoteMarshallerFactory theInstance = null;
    private static Hashtable marshallerTable = new Hashtable();
    private static java.lang.Class DEFAULT_MARSHALLER;

    /**
     * NotesMarshallerFactory constructor comment.
     */
    private NoteMarshallerFactory() {
        super();
        initFactory();
    }

    /**
     * create marshaller for note
     * 
     * @return com.creon.chem.jcamp.IJCAMPNoteMarshaller
     * @param descriptor org.jcamp.spectrum.notes.NoteDescriptor
     */
    public IJCAMPNoteMarshaller findByDescriptor(NoteDescriptor descriptor) {
        IJCAMPNoteMarshaller marshaller = (IJCAMPNoteMarshaller) marshallerTable.get(descriptor);
        if (marshaller == null) {
            try {
                marshaller = (IJCAMPNoteMarshaller) DEFAULT_MARSHALLER.newInstance();
                marshaller.setKey(descriptor.getKey());
                marshaller.setJCAMPLabel(descriptor.getName()); // unknown jcamp labels stored here
            } catch (InstantiationException e) {
                return null; // never reached 
            } catch (IllegalAccessException e) {
                return null; // never reached 
            }
        }
        return marshaller;
    }

    /**
     * factory singleton accessor method.
     * 
     * @return com.creon.chem.jcamp.NoteMarshallerFactory
     */
    public static NoteMarshallerFactory getInstance() {
	if (theInstance == null) {
            theInstance = new NoteMarshallerFactory();
        }

        return theInstance;
    }

    /**
     * initialization method.
     * reads notes.properties file for initialization
     * 
     */
    private void initFactory() {
	marshallerTable.put(NoteDescriptor.TITLE, new IgnoreNoteMarshaller(NoteDescriptor.TITLE.getKey()));
        marshallerTable.put(NoteDescriptor.JCAMPDX, new IgnoreNoteMarshaller(NoteDescriptor.JCAMPDX.getKey()));
        Properties notesProps = new Properties();
        java.io.InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream("notes.properties");
	     if (is == null)
                return;
            notesProps.load(is);
            String defaultJCAMPMarshaller = (String) notesProps.get("default.jcamp.marshaller");
            if (defaultJCAMPMarshaller != null) {

                try {
                    DEFAULT_MARSHALLER = Class.forName(defaultJCAMPMarshaller);
                    if (!IJCAMPNoteMarshaller.class.isAssignableFrom(DEFAULT_MARSHALLER))
                        DEFAULT_MARSHALLER = DefaultNoteMarshaller.class;
                } catch (Exception e) {
                    DEFAULT_MARSHALLER = DefaultNoteMarshaller.class;
                }
            }

            Enumeration notesNames = notesProps.propertyNames();
            while (notesNames.hasMoreElements()) {
                String key = (String) notesNames.nextElement();
                IJCAMPNoteMarshaller marshaller = null;
                if (key.indexOf('.') < 0) {
                    NoteDescriptor descr = NoteDescriptor.findByKey(key);
                    if (descr == null)
                        continue;
                    String jcamp = (String) notesProps.get(key + ".jcamp");
                    if (jcamp == null)
                        marshaller = new IgnoreNoteMarshaller(key);
                    String jcampMarshallerClassName = (String) notesProps.get(key + ".jcamp.marshaller");
                    Class jcampMarshallerClass;
                    if (jcampMarshallerClassName == null)
                        jcampMarshallerClass = DEFAULT_MARSHALLER;
                    else {
                        try {
                            jcampMarshallerClass = Class.forName(jcampMarshallerClassName);
                        } catch (ClassNotFoundException e) {
                            jcampMarshallerClass = DEFAULT_MARSHALLER;
                        }
                        if (!IJCAMPNoteMarshaller.class.isAssignableFrom(jcampMarshallerClass))
                            jcampMarshallerClass = DEFAULT_MARSHALLER;
                    }
                    try {
                        marshaller = (IJCAMPNoteMarshaller) jcampMarshallerClass.newInstance();
                        marshaller.setKey(key);
                        marshaller.setJCAMPLabel(jcamp);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e.getMessage());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                    marshallerTable.put(descr, marshaller);
                }
            }
        } catch (java.io.IOException e) {
		System.out.println("error");
		e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {
            }
        }
    }
}