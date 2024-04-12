package com.grupo1.infraestructure.adapter;

import com.grupo1.domain.aggregates.dto.AnalisisClinicoDTO;
import com.grupo1.domain.aggregates.dto.ConsultaMedicaDTO;
import com.grupo1.domain.aggregates.dto.NombreAnalisisDTO;
import com.grupo1.domain.aggregates.request.RequestRegister;
import com.grupo1.domain.aggregates.request.RequestResultado;
import com.grupo1.domain.aggregates.response.MsExternalToHInsuranceRimacResponse;
import com.grupo1.domain.aggregates.response.ResponseBase;
import com.grupo1.infraestructure.client.ToMSExternalApi;
import com.grupo1.infraestructure.entity.*;
import com.grupo1.infraestructure.mapper.GenericMapper;
import com.grupo1.infraestructure.repository.AnalisisRepository;
import com.grupo1.infraestructure.repository.ConsultaMedicaRepository;
import com.grupo1.infraestructure.repository.NombreAnalisisRepository;
import com.grupo1.infraestructure.util.CurrentTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalisisAdapterTest {

    @Mock
    private AnalisisRepository analisisRepository;
    @Mock
    private NombreAnalisisRepository nombreAnalisisRepository;
    @Mock
    private ConsultaMedicaRepository consultaMedicaRepository;
    @Mock
    private GenericMapper genericMapper;
    @Mock
    private ToMSExternalApi toMSExternalApi;

    @InjectMocks
    private AnalisisAdapter analisisAdapter;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void buscarAnalisisEntityOut_analisisExists_returnsResponseBase() {
        Long id = 123L;
        AnalisisClinicoEntity analisisEntity = new AnalisisClinicoEntity();
        Optional<AnalisisClinicoEntity> optional = Optional.of(analisisEntity);

        when(analisisRepository.findById(id)).thenReturn(optional);

        ResponseBase result = analisisAdapter.BuscarAnalisisEntityOut(id);

        assertEquals(200, result.getCode());
        assertEquals("Registro encontrado con exito", result.getMessage());
        assertEquals(optional, result.getData());

        verify(analisisRepository).findById(id);
    }

    @Test
    void buscarAnalisisEntityOut_analisisNotExists_returnsResponseBase() {
        Long id = 123L;
        Optional<AnalisisClinicoEntity> optional = Optional.empty();

        when(analisisRepository.findById(id)).thenReturn(optional);

        ResponseBase result = analisisAdapter.BuscarAnalisisEntityOut(id);

        assertEquals(404, result.getCode());
        assertEquals("Registro no encontrado", result.getMessage());
        assertEquals(null, result.getData());

        verify(analisisRepository).findById(id);
    }

    @Test
    void buscarAnalisisDtoOut_analisisExists_returnsResponseBase() {
        Long id = 123L;
        AnalisisClinicoEntity analisisEntity = new AnalisisClinicoEntity();
        ConsultaMedicaEntity consultaMedicaEntity = new ConsultaMedicaEntity();
        analisisEntity.setConsulta(consultaMedicaEntity);
        Optional<AnalisisClinicoEntity> optional1 = Optional.of(analisisEntity);
        DoctorEntity doctor = new DoctorEntity();
        consultaMedicaEntity.setDoctor(doctor);

        PacienteEntity pacienteEntity = new PacienteEntity();
        consultaMedicaEntity.setPaciente(pacienteEntity);

        when(analisisRepository.findById(id)).thenReturn(optional1);
        AnalisisClinicoDTO analisisDtos = BuildAnalisisClinicoDTO(optional1.get());

        ResponseBase result = analisisAdapter.BuscarAnalisisDtoOut(id);

        assertEquals(200, result.getCode());
        assertEquals("Registro encontrado con exito", result.getMessage());
        //assertEquals(analisisDtos, result.getData());

    }

    @Test
    void buscarAnalisisDtoOut_analisisNotExists_returnsResponseBase() {

        Long id = 123L;
        Optional<AnalisisClinicoEntity> optional = Optional.empty();

        when(analisisRepository.findById(id)).thenReturn(optional);

        ResponseBase result = analisisAdapter.BuscarAnalisisDtoOut(id);

        assertEquals(404, result.getCode());
        assertEquals("Registro no encontrado", result.getMessage());
        assertEquals(null, result.getData());

        verify(analisisRepository).findById(id);
        verifyNoInteractions(genericMapper);
    }

    @Test
    void buscarAllEnableAnalisisDtoOut_analisisExist_returnsResponseBase() {

        ConsultaMedicaEntity consultaMedicaEntity = new ConsultaMedicaEntity();
        AnalisisClinicoEntity analisisClinicoEntity = new AnalisisClinicoEntity();
        analisisClinicoEntity.setConsulta(consultaMedicaEntity);

        DoctorEntity doctor = new DoctorEntity();
        consultaMedicaEntity.setDoctor(doctor);

        PacienteEntity pacienteEntity = new PacienteEntity();
        consultaMedicaEntity.setPaciente(pacienteEntity);

        List<AnalisisClinicoEntity> listaEntity = new ArrayList<>();
        listaEntity.add(analisisClinicoEntity);

        Set<AnalisisClinicoDTO> listaDto = new HashSet<>();
        listaDto.add(BuildAnalisisClinicoDTO(analisisClinicoEntity));

        when(analisisRepository.findByEstado(true)).thenReturn(listaEntity);

        ResponseBase result = analisisAdapter.BuscarAllEnableAnalisisDtoOut();

        assertEquals(200, result.getCode());
        assertEquals("Solicitud exitosa", result.getMessage());
        //assertEquals(listaDto, result.getData());

        verify(analisisRepository).findByEstado(true);
    }

    @Test
    void buscarAllEnableAnalisisDtoOut_analisisNotExist_returnsResponseBase() {

        List<AnalisisClinicoEntity> listaEntity = new ArrayList<>();

        when(analisisRepository.findByEstado(true)).thenReturn(listaEntity);

        ResponseBase result = analisisAdapter.BuscarAllEnableAnalisisDtoOut();

        assertEquals(200, result.getCode());
        assertEquals("Solicitud exitosa", result.getMessage());
        assertEquals(Collections.emptySet(), result.getData());

        verify(analisisRepository).findByEstado(true);
        verifyNoInteractions(genericMapper);
    }

    @Test
    void registerAnalisisOut_validRequest_returnsResponseBase() {

        RequestRegister requestRegister = new RequestRegister();
        requestRegister.setIdConsulta(1L);
        requestRegister.setNombreAnalisis("Nombre de análisis válido");

        TriajeEntity triajeEntity = new TriajeEntity();
        triajeEntity.setIdTriaje(1L);
        triajeEntity.setFechaTriaje(new Timestamp(System.currentTimeMillis()));

        ConsultaMedicaEntity consultaMedicaEntity = new ConsultaMedicaEntity();
        consultaMedicaEntity.setIdConsulta(1L);
        PacienteEntity pacienteEntity = new PacienteEntity();
        pacienteEntity.setNumDocumento("71236548");
        consultaMedicaEntity.setPaciente(pacienteEntity);
        consultaMedicaEntity.setTriaje(triajeEntity);


        NombreAnalisisEntity nombreAnalisisEntity = new NombreAnalisisEntity();
        nombreAnalisisEntity.setEstado(true);

        MsExternalToHInsuranceRimacResponse cobertura = new MsExternalToHInsuranceRimacResponse();
        cobertura.setCobertura(0.5f);
        cobertura.setNomAseguradora("Complejidad");

        Optional<ConsultaMedicaEntity> optional1 = Optional.of(consultaMedicaEntity);
        Optional<NombreAnalisisEntity> optional2 = Optional.of(nombreAnalisisEntity);

        when(consultaMedicaRepository.findById(consultaMedicaEntity.getIdConsulta())).thenReturn(optional1);
        when(nombreAnalisisRepository.findByAnalisis(requestRegister.getNombreAnalisis())).thenReturn(optional2);
        when(toMSExternalApi.getInfoExtRimac(optional1.get().getPaciente().getNumDocumento(), optional2.get().getComplejidad())).thenReturn(cobertura);
        when(analisisRepository.save(any(AnalisisClinicoEntity.class))).thenReturn(new AnalisisClinicoEntity());

        ResponseBase result = analisisAdapter.RegisterAnalisisOut(requestRegister, "Usuario");

        assertEquals(201, result.getCode());
        assertEquals("Analisis Clinico registrado con exito. ", result.getMessage());
        assertNotNull(result.getData());

        verify(consultaMedicaRepository).findById(consultaMedicaEntity.getIdConsulta());
        verify(nombreAnalisisRepository).findByAnalisis(requestRegister.getNombreAnalisis());
        verify(toMSExternalApi).getInfoExtRimac(optional1.get().getPaciente().getNumDocumento(), optional2.get().getComplejidad());
        verify(analisisRepository).save(any(AnalisisClinicoEntity.class));
    }

    @Test
    void registerAnalisisOut_invalidConsulta_returns404() {

        RequestRegister requestRegister = new RequestRegister();
        requestRegister.setIdConsulta(1L);
        requestRegister.setNombreAnalisis("Nombre de análisis válido");

        when(consultaMedicaRepository.findById(requestRegister.getIdConsulta())).thenReturn(Optional.empty());

        ResponseBase result = analisisAdapter.RegisterAnalisisOut(requestRegister, "Usuario");

        assertEquals(404, result.getCode());
        assertEquals("No se encontró el registro de consulta", result.getMessage());

        verify(consultaMedicaRepository).findById(requestRegister.getIdConsulta());
        verifyNoInteractions(nombreAnalisisRepository, toMSExternalApi, analisisRepository);
    }

    @Test
    void registerAnalisisOut_invalidNombreAnalisis_returns404() {

        RequestRegister requestRegister = new RequestRegister();
        requestRegister.setIdConsulta(1L);
        requestRegister.setNombreAnalisis("Nombre de análisis inválido");

        TriajeEntity triajeEntity = new TriajeEntity();
        triajeEntity.setIdTriaje(1L);
        triajeEntity.setFechaTriaje(new Timestamp(System.currentTimeMillis()));

        ConsultaMedicaEntity consultaMedicaEntity = new ConsultaMedicaEntity();
        consultaMedicaEntity.setIdConsulta(requestRegister.getIdConsulta());
        consultaMedicaEntity.setTriaje(triajeEntity);
        when(consultaMedicaRepository.findById(requestRegister.getIdConsulta())).thenReturn(Optional.of(consultaMedicaEntity));

        when(nombreAnalisisRepository.findByAnalisis("Nombre de análisis inválido")).thenReturn(Optional.empty());

        ResponseBase result = analisisAdapter.RegisterAnalisisOut(requestRegister, "Usuario");

        assertEquals(404, result.getCode());
        assertEquals("No se encontró el nombre de analisis", result.getMessage());

        verify(consultaMedicaRepository).findById(requestRegister.getIdConsulta());
        verify(nombreAnalisisRepository).findByAnalisis("Nombre de análisis inválido");
        verifyNoInteractions(toMSExternalApi, analisisRepository);
    }

    @Test
    void updateTomaMuestraAnalisisOut_analisisExistsAndNotReceived_returnsResponseBase() {

        AnalisisClinicoEntity analisisEntity = new AnalisisClinicoEntity();
        analisisEntity.setIdAnalisis(1L);
        analisisEntity.setEstado(true);

        String username = "testUser";

        when(analisisRepository.findById(analisisEntity.getIdAnalisis())).thenReturn(Optional.of(analisisEntity));
        when(analisisRepository.save(any())).thenReturn(analisisEntity);

        ResponseBase result = analisisAdapter.UpdateTomaMuestraAnalisisOut(1L, username);

        assertEquals(200, result.getCode());
        assertEquals("Recepcion de muestra exitosa", result.getMessage());
        assertEquals(analisisEntity, result.getData());

        verify(analisisRepository).findById(analisisEntity.getIdAnalisis());
        verify(analisisRepository).save(analisisEntity);
    }

    @Test
    void updateTomaMuestraAnalisisOut_analisisExistsAndReceived_returnsResponseBase() {

        AnalisisClinicoEntity analisisEntity = new AnalisisClinicoEntity();
        analisisEntity.setIdAnalisis(1L);
        analisisEntity.setEstado(true);
        analisisEntity.setUsuarioRecepcion("receivedUser");

        when(analisisRepository.findById(1L)).thenReturn(Optional.of(analisisEntity));

        ResponseBase result = analisisAdapter.UpdateTomaMuestraAnalisisOut(1L, "testUser");

        assertEquals(406, result.getCode());
        assertEquals("Ya existe un registro de toma de muestra.", result.getMessage());
        assertEquals(null, result.getData());

        verify(analisisRepository).findById(1L);
        verifyNoMoreInteractions(analisisRepository); // No save should occur
    }

    @Test
    void updateTomaMuestraAnalisisOut_analisisNotExists_returnsResponseBase() {

        when(analisisRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseBase result = analisisAdapter.UpdateTomaMuestraAnalisisOut(1L, "testUser");

        assertEquals(404, result.getCode());
        assertEquals("Registro no encontrado", result.getMessage());
        assertEquals(null, result.getData());

        verify(analisisRepository).findById(1L);
        verifyNoMoreInteractions(analisisRepository); // No save should occur
    }

    @Test
    void updateResultadoAnalisisOut_analisisExistAndEstadoTrueAndResultadoNull_returnsResponseBase() {

        RequestResultado requestResultado = new RequestResultado();
        requestResultado.setIdAnalisis(1L);
        requestResultado.setResultado("Resultado del análisis");
        String username = "usuario";

        AnalisisClinicoEntity analisisEntity = new AnalisisClinicoEntity();
        analisisEntity.setIdAnalisis(1L);
        analisisEntity.setEstado(true);
        analisisEntity.setUsuarioRecepcion("usuarioRecepcion");

        when(analisisRepository.findById(1L)).thenReturn(Optional.of(analisisEntity));
        when(analisisRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseBase result = analisisAdapter.UpdateResultadoAnalisisOut(requestResultado, username);

        assertEquals(200, result.getCode());
        assertEquals("Registro de resultado exitoso.", result.getMessage());
        assertEquals(analisisEntity, result.getData());
        assertEquals("usuario", analisisEntity.getUsuarioModificacion());
        assertNotNull(analisisEntity.getFechaModificacion());
        assertEquals("Resultado del análisis", analisisEntity.getResultado());

        verify(analisisRepository).findById(1L);
        verify(analisisRepository).save(analisisEntity);
    }

    @Test
    void updateResultadoAnalisisOut_analisisNotExist_returnsResponseBase() {
        RequestResultado requestResultado = new RequestResultado();
        requestResultado.setIdAnalisis(1L);
        requestResultado.setResultado("Resultado del análisis");
        String username = "usuario";

        when(analisisRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseBase result = analisisAdapter.UpdateResultadoAnalisisOut(requestResultado, username);

        assertEquals(404, result.getCode());
        assertEquals("Registro no encontrado", result.getMessage());
        assertNull(result.getData());

        verify(analisisRepository).findById(1L);
        verifyNoMoreInteractions(analisisRepository);
    }

    @Test
    void deleteAnalisisOut_analisisExistsAndNotReceived_returnsResponseBase() {

        long id = 1L;
        String username = "testUser";
        AnalisisClinicoEntity analisisEntity = new AnalisisClinicoEntity();
        analisisEntity.setUsuarioRecepcion(null);

        when(analisisRepository.findById(id)).thenReturn(Optional.of(analisisEntity));
        when(analisisRepository.save(any())).thenReturn(analisisEntity);

        ResponseBase result = analisisAdapter.DeleteAnalisisOut(id, username);

        assertEquals(200, result.getCode());
        assertEquals("Registro borrado con exito", result.getMessage());
        assertEquals(analisisEntity, result.getData());

        verify(analisisRepository).findById(id);
        verify(analisisRepository).save(analisisEntity);
    }

    @Test
    void deleteAnalisisOut_analisisNotExist_returnsResponseBase() {

        long id = 1L;
        String username = "testUser";

        when(analisisRepository.findById(id)).thenReturn(Optional.empty());

        ResponseBase result = analisisAdapter.DeleteAnalisisOut(id, username);

        assertEquals(404, result.getCode());
        assertEquals("Registro no encontrado o muestra ya fue recepcionada.", result.getMessage());
        assertEquals(null, result.getData());

        verify(analisisRepository).findById(id);
    }

    @Test
    void deleteAnalisisOut_analisisExistsAndReceived_returnsResponseBase() {

        long id = 1L;
        String username = "testUser";
        AnalisisClinicoEntity analisisEntity = new AnalisisClinicoEntity();
        analisisEntity.setUsuarioRecepcion("receivingUser");

        when(analisisRepository.findById(id)).thenReturn(Optional.of(analisisEntity));

        ResponseBase result = analisisAdapter.DeleteAnalisisOut(id, username);

        assertEquals(404, result.getCode());
        assertEquals("Registro no encontrado o muestra ya fue recepcionada.", result.getMessage());
        assertEquals(null, result.getData());

        verify(analisisRepository).findById(id);
        verifyNoMoreInteractions(analisisRepository);

    }

    private AnalisisClinicoDTO BuildAnalisisClinicoDTO(AnalisisClinicoEntity analisisEntity){
        Timestamp fechaRecepcion = Timestamp.valueOf("2022-03-21 12:00:00");
        NombreAnalisisDTO nombreAnalisisDTO = new NombreAnalisisDTO();
        ConsultaMedicaDTO consultaMedicaDTO = new ConsultaMedicaDTO();
        AnalisisClinicoDTO analisisClinicoDTO = new AnalisisClinicoDTO();

        when(genericMapper.mapNombreAnalisisEntityToNombreAnalisisDTO(analisisEntity.getNombreAnalisis())).thenReturn(nombreAnalisisDTO);
        when(genericMapper.mapConsultaMedicaEntityToConsultaMedicaDTO(analisisEntity.getConsulta())).thenReturn(consultaMedicaDTO);
        analisisClinicoDTO.setIdAnalisis(1L);
        analisisClinicoDTO.setNombreSeguro("Rimac");
        analisisClinicoDTO.setCoberturaSeguro(100f);
        analisisClinicoDTO.setResultado("resultado");
        analisisClinicoDTO.setUsuarioRecepcion("usuario.");
        analisisClinicoDTO.setFechaRecepcion(fechaRecepcion);
        analisisClinicoDTO.setNombreDoctor("Jose"+ " " +
                "Perez");
        analisisClinicoDTO.setNombrePaciente("Maria"+ " " +
                "Garcia");
        analisisClinicoDTO.setNombreAnalisis(nombreAnalisisDTO);
        analisisClinicoDTO.setConsulta(consultaMedicaDTO);

        return analisisClinicoDTO;
    }

}