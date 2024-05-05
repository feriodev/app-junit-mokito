package com.pe.feriodev.code.repository;

import java.util.List;

public interface PreguntaRepository {

	List<String> findPreguntasPorExamenId(Long id);
	
	void guardarVarias(List<String> preguntas);
}
