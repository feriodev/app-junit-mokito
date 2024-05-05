package com.pe.feriodev.code.repository;

import java.util.List;

import com.pe.feriodev.code.models.Examen;

public interface ExamenRepository {

	List<Examen> findAll();
	
	Examen guardar(Examen examen);
	
}
