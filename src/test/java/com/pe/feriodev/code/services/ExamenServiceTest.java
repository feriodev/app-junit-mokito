package com.pe.feriodev.code.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.pe.feriodev.code.models.Examen;
import com.pe.feriodev.code.repository.ExamenRepository;
import com.pe.feriodev.code.repository.ExamenRepositoryImpl;
import com.pe.feriodev.code.repository.PreguntaRepository;
import com.pe.feriodev.code.repository.PreguntaRepositoryImpl;

@ExtendWith(MockitoExtension.class)
class ExamenServiceTest {

	List<Examen> data;
	List<Examen> dataEmpty;
	List<String> preguntas;
	Examen returnExamen;

	@Mock
	ExamenRepository repository;
	@Mock
	PreguntaRepositoryImpl preguntaRepository;
	@InjectMocks
	ExamenServiceImpl service;
	@Captor
	ArgumentCaptor<Long> captor;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		data = Arrays.asList(new Examen(1L, "Matematica"), new Examen(2L, "Lenguaje"));
		dataEmpty = Collections.emptyList();
		preguntas = Arrays.asList("Aritmetica", "Integrales", "Derivadas");
		returnExamen = new Examen(3L, "Historia");
	}

	@Test
	void testFindExamenPorNombre() {
		when(repository.findAll()).thenReturn(data);
		Optional<Examen> examen = service.findExamenPorNombre("Matematica");
		assertTrue(examen.isPresent());
		assertEquals(1L, examen.orElseThrow().getId());
		assertEquals("Matematica", examen.orElseThrow().getNombre());
	}

	@Test
	void testFindExamenPorNombreEmpty() {
		when(repository.findAll()).thenReturn(dataEmpty);
		Optional<Examen> examen = service.findExamenPorNombre("Matematica");
		assertTrue(examen.isEmpty());
	}

	@Test
	void testPreguntasExamen() {
		when(repository.findAll()).thenReturn(data);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		Examen examen = service.findExamenPorNombreConPreguntas("Lenguaje");
		assertEquals(3, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("Aritmetica"));
	}

	@Test
	void testPreguntasExamenVerify() {
		when(repository.findAll()).thenReturn(data);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		Examen examen = service.findExamenPorNombreConPreguntas("Lenguaje");
		assertEquals(3, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().contains("Aritmetica"));
		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void testNoExisteExamenVerify() {
		when(repository.findAll()).thenReturn(data);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		Examen examen = service.findExamenPorNombreConPreguntas("Matematica");
		assertNotNull(examen);
		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void testGuardarExamen() {
		// Given
		returnExamen.setPreguntas(preguntas);
		when(repository.guardar(any(Examen.class))).then(new Answer<Examen>() {
			Long secuencia = 3L;

			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				return examen;
			}
		});

		// When
		Examen examen = service.guardar(returnExamen);

		// Then
		assertNotNull(examen.getId());
		assertEquals(3L, examen.getId());
		assertEquals("Historia", examen.getNombre());
		verify(repository).guardar(any(Examen.class));
		verify(preguntaRepository).guardarVarias(anyList());
	}

	@Test
	void testManejoException() {
		when(repository.findAll()).thenReturn(data);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenThrow(IllegalArgumentException.class);
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			service.findExamenPorNombreConPreguntas("Matematica");
		});
		assertEquals(IllegalArgumentException.class, exception.getClass());
		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void testArgumentMatchers() {
		when(repository.findAll()).thenReturn(data);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		service.findExamenPorNombreConPreguntas("Matematica");

		verify(repository).findAll();
		// verify(preguntaRepository).findPreguntasPorExamenId(argThat(arg ->
		// arg.equals(1L)));
		// verify(preguntaRepository).findPreguntasPorExamenId(eq(1L));
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgs()));
	}

	@Test
	void testArgumentMatchers2() {
		when(repository.findAll()).thenReturn(data);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		service.findExamenPorNombreConPreguntas("Matematica");

		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(new MiArgs()));
	}

	public static class MiArgs implements ArgumentMatcher<Long> {

		@Override
		public boolean matches(Long argument) {
			return argument != null && argument > 0;
		}

		@Override
		public String toString() {
			return "Error de MiArgs:: Los argumentos son nulos o negativos";
		}
	}

	@Test
	void testArgumentMatchers3() {
		when(repository.findAll()).thenReturn(data);
		when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		service.findExamenPorNombreConPreguntas("Matematica");

		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(argThat(argument -> {
			return argument != null && argument > 0;
		}));
	}

	@Test
	void testArgumentCaptor() {
		when(repository.findAll()).thenReturn(data);
		// when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		service.findExamenPorNombreConPreguntas("Matematica");

		verify(repository).findAll();
		verify(preguntaRepository).findPreguntasPorExamenId(captor.capture());

		assertEquals(1L, captor.getValue());
	}

	@Test
	void testDoThrow() {
		returnExamen.setPreguntas(preguntas);
		doThrow(IllegalArgumentException.class).when(preguntaRepository).guardarVarias(anyList());

		assertThrows(IllegalArgumentException.class, () -> {
			service.guardar(returnExamen);
		});
	}

	@Test
	void testDoAnswer() {
		when(repository.findAll()).thenReturn(data);
		// when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		doAnswer(invocation -> {
			Long id = invocation.getArgument(0);
			return id == 1L ? preguntas : Collections.emptyList();
		}).when(preguntaRepository).findPreguntasPorExamenId(anyLong());

		Examen examen = service.findExamenPorNombreConPreguntas("Lenguaje");

		assertEquals(2L, examen.getId());
		assertEquals("Lenguaje", examen.getNombre());
		assertEquals(0, examen.getPreguntas().size());
		assertTrue(examen.getPreguntas().isEmpty());

		verify(preguntaRepository).findPreguntasPorExamenId(anyLong());
	}

	@Test
	void testDoAnswerGuardarExamen() {
		// Given
		returnExamen.setPreguntas(preguntas);

		doAnswer(new Answer<Examen>() {
			Long secuencia = 3L;

			public Examen answer(InvocationOnMock invocation) throws Throwable {
				Examen examen = invocation.getArgument(0);
				examen.setId(secuencia++);
				return examen;
			}
		}).when(repository).guardar(returnExamen);

		// When
		Examen examen = service.guardar(returnExamen);

		// Then
		assertNotNull(examen.getId());
		assertEquals(3L, examen.getId());
		assertEquals("Historia", examen.getNombre());
		verify(repository).guardar(any(Examen.class));
		verify(preguntaRepository).guardarVarias(anyList());
	}

	@Test
	void testDoCallRealMethod() {
		when(repository.findAll()).thenReturn(data);
		// when(preguntaRepository.findPreguntasPorExamenId(anyLong())).thenReturn(preguntas);
		doCallRealMethod().when(preguntaRepository).findPreguntasPorExamenId(anyLong());

		Examen examen = service.findExamenPorNombreConPreguntas("Matematica");
		assertEquals("Matematica", examen.getNombre());
	}

	@Test
	void testSpy() {
		ExamenRepository examenRepositorySpy = spy(ExamenRepositoryImpl.class);
		PreguntaRepository preguntaRepositorySpy = spy(PreguntaRepositoryImpl.class);

		ExamenService examenService = new ExamenServiceImpl(examenRepositorySpy, preguntaRepositorySpy);

		List<String> preguntas = Arrays.asList("Geometria");
		doReturn(preguntas).when(preguntaRepositorySpy).findPreguntasPorExamenId(anyLong());

		Examen examen = examenService.findExamenPorNombreConPreguntas("Matematica");

		assertEquals(1L, examen.getId());
		assertEquals("Matematica", examen.getNombre());
		assertEquals(1, examen.getPreguntas().size());
		
		verify(examenRepositorySpy).findAll();
	}
	
	@Test
	void testOrdenDeInvocaciones() {
		when(repository.findAll()).thenReturn(data);

		service.findExamenPorNombreConPreguntas("Matematica");
		service.findExamenPorNombreConPreguntas("Lenguaje");
		
		InOrder inOrder = inOrder(preguntaRepository);
		inOrder.verify(preguntaRepository).findPreguntasPorExamenId(1L);
		inOrder.verify(preguntaRepository).findPreguntasPorExamenId(2L);
	}
	
	@Test
	void testOrdenDeInvocaciones2() {
		when(repository.findAll()).thenReturn(data);

		service.findExamenPorNombreConPreguntas("Matematica");
		service.findExamenPorNombreConPreguntas("Lenguaje");
		
		InOrder inOrder = inOrder(repository, preguntaRepository);
		inOrder.verify(repository).findAll();
		inOrder.verify(preguntaRepository).findPreguntasPorExamenId(1L);
		inOrder.verify(repository).findAll();
		inOrder.verify(preguntaRepository).findPreguntasPorExamenId(2L);
	}
	
	@Test
	void testNumeroDeInvocaciones() {
		when(repository.findAll()).thenReturn(data);

		service.findExamenPorNombreConPreguntas("Matematica");
		
		verify(preguntaRepository, times(2)).findPreguntasPorExamenId(1L);
		verify(preguntaRepository, atLeast(1)).findPreguntasPorExamenId(1L);
		verify(preguntaRepository, atLeastOnce()).findPreguntasPorExamenId(1L);
		verify(preguntaRepository, atMost(2)).findPreguntasPorExamenId(1L);
		verify(preguntaRepository, atMostOnce()).findPreguntasPorExamenId(1L);
	}
	
	@Test
	void testNumeroDeInvocaciones2() {
		when(repository.findAll()).thenReturn(Collections.emptyList());

		service.findExamenPorNombreConPreguntas("Matematica");
		
		verify(preguntaRepository, never()).findPreguntasPorExamenId(1L);
		verifyNoInteractions(preguntaRepository);
		verify(repository, times(1)).findAll();
		verify(repository, atLeast(1)).findAll();
		verify(repository, atLeastOnce()).findAll();
		verify(repository, atMost(2)).findAll();
		verify(repository, atMostOnce()).findAll();
	}
}
