package es.proyecto.sergio.service;

import es.proyecto.sergio.dto.EstadisticasFlex;
import es.proyecto.sergio.service.utils.EstadisticasFlexUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.apache.commons.collections.map.HashedMap;
import org.apache.el.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Lazy
public class EstadisticasFlexService {

    @Autowired
    private MessageSource messageSource;

    public byte[] getEstadisticasFlex(EstadisticasFlex estadisticasFlex) throws  Exception{

        try{
            JasperPrint jasperPrint = generarJasperPrint(estadisticasFlex);

            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
            reportConfig.setSizePageToContent(true);
            reportConfig.setForceLineBreakPolicy(false);


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            OutputStreamExporterOutput outputStream = new SimpleOutputStreamExporterOutput(bos);
            exporter.setConfiguration(reportConfig);
            exporter.setExporterOutput(outputStream);
            exporter.exportReport();

            return bos.toByteArray();
        }catch(Exception e){
            log.error("Error generando el resguardo del informe" + e.getStackTrace());
            return null;
        }

    }

    private JasperPrint generarJasperPrint(EstadisticasFlex estadisticasFlex) throws Exception {

        InputStream estadisticasReportStream = getClass().getResourceAsStream(EstadisticasFlexUtil.VALUE_ESTATIDISTICASFLEX_JASPER);
        try{
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(estadisticasReportStream);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(EstadisticasFlexUtil.PARAM_PROPIEDADES, estadisticasFlex.getPropiedades() != null ? estadisticasFlex.getPropiedades() : null);
            parameters.put(EstadisticasFlexUtil.PARAM_USUARIOS, estadisticasFlex.getUsuarios() != null ? estadisticasFlex.getUsuarios() : null);
            parameters.put(EstadisticasFlexUtil.PARAM_ALERTAS, estadisticasFlex.getAlertas() != null ? estadisticasFlex.getAlertas() : null);
            parameters.put(EstadisticasFlexUtil.PARAM_PRECIO, estadisticasFlex.getPrecio() != 0 ? estadisticasFlex.getPrecio() : 0);
            parameters.put(EstadisticasFlexUtil.PARAM_NUMERO_ALERTAS, estadisticasFlex.getNumeroAlertas() != 0 ? estadisticasFlex.getNumeroAlertas() : 0);
            parameters.put(EstadisticasFlexUtil.PARAM_NUMERO_CARACTERES, estadisticasFlex.getNumeroCaracteres() != 0 ? estadisticasFlex.getNumeroCaracteres() : 0);



            return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        }
        catch (JRException e){
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }

    }

}
