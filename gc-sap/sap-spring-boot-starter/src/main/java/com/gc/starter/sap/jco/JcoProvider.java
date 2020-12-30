package com.gc.starter.sap.jco;


import com.sap.conn.jco.ext.DataProviderException;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.util.HashMap;
import java.util.Properties;


public class JcoProvider implements DestinationDataProvider {

    private final HashMap<String, Properties> secureDBStorage = new HashMap<String, Properties>();
    private DestinationDataEventListener el;

    @Override
    public Properties getDestinationProperties(String destinationName) {
        try {
            // read the destination from DB
            Properties p = secureDBStorage.get(destinationName);

            if (p != null) {
                // check if all is correct, for example
                if (p.isEmpty()) {
                    throw new DataProviderException(DataProviderException.Reason.INVALID_CONFIGURATION, "destination configuration is incorrect", null);
                }
                return p;
            }
            return null;
        } catch (RuntimeException re) {
            throw new DataProviderException(DataProviderException.Reason.INTERNAL_ERROR, re);
        }
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
        this.el = eventListener;
    }

    @Override
    public boolean supportsEvents() {
        return true;
    }

    public void changePropertiesForABAP_AS(String destName, Properties properties) {
        synchronized (secureDBStorage) {
            if (properties == null) {
                if (secureDBStorage.remove(destName) != null) {
                    el.deleted(destName);
                }
            } else {
                secureDBStorage.put(destName, properties);
                el.updated(destName); // create or updated
            }
        }
    }
}
