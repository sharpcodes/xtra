package org.rest.automation.decorators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;


public class MarshellerUtil<E> {

    private static Logger LOGGER = LogManager.getLogger(MarshellerUtil.class);


    /**
     * @param entity      class whose instance is to be marshalled
     * @param instance    the instance to be marshalled
     * @param destination the file for the destination location in which the marshalled results can be dropped into
     */
    public static <T> void marshalResource(Class<T> entity, T instance, File destination) {

        try {

            JAXBContext jaxbContext = JAXBContext.newInstance(entity);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(instance, destination);

        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error when writing XML output files" + e.getMessage());
        }


    }
}
