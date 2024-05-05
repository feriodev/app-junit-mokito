package com.pe.feriodev.code.services;

import java.util.List;
import java.util.Optional;

import com.pe.feriodev.code.models.Examen;
import com.pe.feriodev.code.repository.ExamenRepository;
import com.pe.feriodev.code.repository.PreguntaRepository;

public class ExamenServiceImpl implements ExamenService{

	private ExamenRepository examenRespository;
	private PreguntaRepository preguntaRepository;
	
	public ExamenServiceImpl(ExamenRepository examenRespository, 
			PreguntaRepository preguntaRepository) {
		this.examenRespository = examenRespository;
		this.preguntaRepository = preguntaRepository;
	}
	
	@Override
	public Optional<Examen> findExamenPorNombre(String nombre) {
		return examenRespository.findAll()
			.stream()
			.filter(ex -> ex.getNombre().equals(nombre))
			.findFirst();
	}

	@Override
	public Examen findExamenPorNombreConPreguntas(String nombre) {
		Optional<Examen> exOptional = findExamenPorNombre(nombre);
		Examen examen = null;
		if (exOptional.isPresent()) {
			examen = exOptional.orElseThrow();
			List<String> preguntas = preguntaRepository.findPreguntasPorExamenId(examen.getId());
			examen.setPreguntas(preguntas);
		}
		return examen;
	}

	@Override
	public Examen guardar(Examen examen) {
		if (!examen.getPreguntas().isEmpty()) {
			preguntaRepository.guardarVarias(examen.getPreguntas());
		}
		return examenRespository.guardar(examen);
	}

}
