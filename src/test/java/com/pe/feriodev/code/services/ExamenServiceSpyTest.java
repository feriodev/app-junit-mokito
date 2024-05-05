package com.pe.feriodev.code.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pe.feriodev.code.models.Examen;
import com.pe.feriodev.code.repository.ExamenRepositoryImpl;
import com.pe.feriodev.code.repository.PreguntaRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class ExamenServiceSpyTest {

	List<Examen> data;
	List<Examen> dataEmpty;
	List<String> preguntas;
	Examen returnExamen;

	@Spy
	ExamenRepositoryImpl examenRepository;
	@Spy
	PreguntaRepositoryImpl preguntaRepository;
	@InjectMocks
	ExamenServiceImpl service;

	@Test
	void testSpy() {
		List<String> preguntas = Arrays.asList("Geometria");
		doReturn(preguntas).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

		Examen examen = service.findExamenPorNombreConPreguntas("Matematica");

		assertEquals(1L, examen.getId());
		assertEquals("Matematica", examen.getNombre());
		assertEquals(1, examen.getPreguntas().size());
		
		verify(examenRepository).findAll();
	}
}
