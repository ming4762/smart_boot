package com.gc.starter.sap.jco;

import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.Environment;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

import java.util.Properties;

@Setter
@Slf4j
public class RfcManager implements DisposableBean {
    private JCoDestination destination;
    private JcoProvider provider;

    public void loadProvider(String pool, Properties connectProperties) {
        try {
            if (!Environment.isDestinationDataProviderRegistered()) {
                log.info("Registering Destination Provider R3");
                Environment.registerDestinationDataProvider(provider);
                provider.changePropertiesForABAP_AS(pool, connectProperties);
            } else {
                log.info("Destination Provider already set..R3");
            }
        } catch (IllegalStateException e) {
            log.error(e.getMessage(), e);
        }
    }

    public JCoDestination getDestination() throws JCoException {
        if (destination == null) {
            destination = JCoDestinationManager.getDestination("ABAP_AS_POOL");
        }
        return destination;
    }

    public JCoFunction getFunction(String functionName) throws JCoException {
        return getDestination().getRepository().getFunctionTemplate(functionName).getFunction();
    }

    public void execute(JCoFunction function) throws JCoException {
        log.debug("SAP Function Name : " + function.getName());
        JCoParameterList paramList = function.getImportParameterList();

        if (paramList != null) {
            log.debug("Function Import Structure : " + paramList.toString());
        }

        function.execute(getDestination());
        paramList = function.getExportParameterList();

        if (paramList != null) {
            log.debug("Function Export Structure : " + paramList.toString());
        }
    }

    public void execute(JCoFunction function, String returnStr) throws JCoException {
        log.debug("SAP Function Name : " + function.getName());
        JCoParameterList paramList = function.getImportParameterList();

        if (paramList != null) {
            log.debug("Function Import Structure : " + paramList.toString());
        }

        function.execute(getDestination());
        paramList = function.getExportParameterList();

        if (paramList != null) {
            log.debug("Function Export Structure : " + paramList.toString());
            log.debug(function.getName() + "_FUNC return :" + function.getExportParameterList().getString(returnStr));
        }
    }

    /**
     * 销毁时执行 注销Destination
     *
     * @throws Exception Exception
     */
    @Override
    public void destroy() throws Exception {
        try {
            com.sap.conn.jco.ext.Environment.unregisterDestinationDataProvider(provider);
        } catch (IllegalStateException providerAlreadyRegisteredException) {
            log.error("注销Destination 失败:" + providerAlreadyRegisteredException.getLocalizedMessage(), providerAlreadyRegisteredException);
            throw new Exception(providerAlreadyRegisteredException);
        }
    }
}
