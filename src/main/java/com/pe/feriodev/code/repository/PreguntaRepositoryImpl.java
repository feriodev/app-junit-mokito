package com.pe.feriodev.code.repository;

import java.util.Arrays;
import java.util.List;

public class PreguntaRepositoryImpl implements PreguntaRepository {

	@Override
	public List<String> findPreguntasPorExamenId(Long id) {
		System.out.println("PreguntaRepositoryImpl.findPreguntasPorExamenId");
		return Arrays.asList("Aritmetica", "Integrales", "Derivadas");
	}

	@Override
	public void guardarVarias(List<String> preguntas) {
		System.out.println("PreguntaRepositoryImpl.guardarVarias");
	}

}
