package org.rest.automation.specutil;

import org.apache.commons.lang.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.automation.model.ProcessStatus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by sharpcodes on 5/12/15.
 */
public class NullCheckWrapperUtil {

    private static Logger LOGGER = LogManager.getLogger(NullCheckWrapperUtil.class);

    /**
     * An util method used for null checks
     *
     * @param object instance that requires a null check on certain fields
     * @param fields field names for which a not null constraint is needed
     * @return true if all fields are not null
     */
    public static ProcessStatus checkIfNotNull(Object object, String... fields) {

        if (object != null && fields != null) {
            for (String field : fields) {
                Object value = null;
                try {
                    Field fieldData = object.getClass().getDeclaredField(field);
                    if (fieldData.isAccessible()) {
                        Object fieldValue = fieldData.get(object);
                        if (fieldValue == null) {
                            return ProcessStatus.createInstance(false, field + "value is missing");
                        }

                    } else {
                        Method method = object.getClass().getMethod("get" + WordUtils.capitalize(field), null);
                        value = method.invoke(object, null);
                    }

                    if (value == null) {
                        return ProcessStatus.createInstance(false, field + " is missing");
                    }

                } catch (Exception e) {
                    LOGGER.warn("No such field " + field + " Skipping to next field" + " " + e.getMessage());
                    return ProcessStatus.createInstance(false, "Unexpected error " + field);

                }

            }
            return ProcessStatus.createInstance(true, "OK");
        } else {
            return ProcessStatus.createInstance(false, "null values found");
        }


    }

}
