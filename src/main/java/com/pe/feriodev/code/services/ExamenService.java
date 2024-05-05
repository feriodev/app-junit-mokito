package com.pe.feriodev.code.services;

import java.util.Optional;

import com.pe.feriodev.code.models.Examen;

public interface ExamenService {

	Optional<Examen> findExamenPorNombre(String nombre);
	
	Examen findExamenPorNombreConPreguntas(String nombre);
	
	Examen guardar(Examen examen);
	
}
