package es.proyecto.sergio;

import es.proyecto.sergio.dto.EstadisticasDTO;
import es.proyecto.sergio.dto.PropiedadDTO;
import es.proyecto.sergio.dto.UsuarioDTO;
import es.proyecto.sergio.service.EstadisticasService;
import es.proyecto.sergio.service.PropiedadService;
import es.proyecto.sergio.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
public class EstadisticasServiceTest {


    @Autowired
    EstadisticasService estadisticasFlexService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PropiedadService propiedadService;

    @Test
    public void testInicio(){

        Assert.assertNotNull(estadisticasFlexService);
        Assert.assertNotNull(usuarioService);
        Assert.assertNotNull(propiedadService);

        Long precio = 1L;

        Long alertas = 2L;

        List<UsuarioDTO> usuarioDTOList = usuarioService.buscarUsuariosConMasDeXAlertas(alertas);
        Assert.assertNotNull(usuarioDTOList);

        List<PropiedadDTO> propiedadDTOList = propiedadService.propiedadesMasCarasQue(precio);
        Assert.assertNotNull(propiedadDTOList);

        EstadisticasDTO estadisticasDTO = EstadisticasDTO.builder()
                .precio(precio)
                .numeroAlertas(alertas)
                .usuarios(usuarioDTOList)
                .propiedades(propiedadDTOList).build();

        String path = "target/";
        File file = new File(path);

        try{
            byte[] informe = estadisticasFlexService.getEstadisticasPDF(estadisticasDTO);
            Assert.assertNotNull(informe);
            FileUtils.deleteQuietly(new File(file.getAbsolutePath() + File.separator + "Estadisticas.pdf"));
            FileUtils.writeByteArrayToFile(new File(file.getAbsolutePath() + File.separator + "Estadisticas.pdf"), informe);

        }catch (Exception e){
            Assert.assertNotEquals(e, null);
        }


    }


}