package es.proyecto.sergio.service;

import es.proyecto.sergio.dto.EstadisticasDTO;
import es.proyecto.sergio.util.EstadisticasKeys;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
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
public class EstadisticasService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PropiedadService propiedadServiceService;

    public byte[] getEstadisticasPDF(EstadisticasDTO estadisticasDTO) throws  Exception{

        try{
            JasperPrint jasperPrint = generarJasperPrint(estadisticasDTO);

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

    private JasperPrint generarJasperPrint(EstadisticasDTO estadisticasDTO) throws Exception {

        InputStream estadisticasReportStream = getClass().getResourceAsStream(EstadisticasKeys.VALUE_ESTATIDISTICAS_JASPER);
        try{


            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(estadisticasReportStream);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put(EstadisticasKeys.PARAM_PROPIEDADES, estadisticasDTO.getPropiedades() != null ? estadisticasDTO.getPropiedades() : null);
            parameters.put(EstadisticasKeys.PARAM_USUARIOS, estadisticasDTO.getUsuarios() != null ? estadisticasDTO.getUsuarios() : null);
            parameters.put(EstadisticasKeys.PARAM_PRECIO, estadisticasDTO.getPrecio());
            parameters.put(EstadisticasKeys.PARAM_NUMERO_ALERTAS, estadisticasDTO.getNumeroAlertas());
            parameters.put(EstadisticasKeys.PARAM_LIST_PROPIEDADES, estadisticasDTO.getPropiedades());
            parameters.put(EstadisticasKeys.PARAM_LIST_USUARIOS, estadisticasDTO.getUsuarios());

            parameters.put(EstadisticasKeys.PARAM_USUARIOS_ESTADISTICAS, getClass().getResourceAsStream(EstadisticasKeys.VALUE_USUARIOS_ESTADISTICAS));
            parameters.put(EstadisticasKeys.PARAM_PROPIEDADES_ESTADISTICAS, getClass().getResourceAsStream(EstadisticasKeys.VALUE_PROPIEDADES_ESTADISTICAS));

            return JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
        }
        catch (JRException e){
            log.error(e.getMessage(), e);
            throw new Exception(e.getMessage());
        }

    }



}
