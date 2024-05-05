package com.pe.feriodev.code.repository;

import java.util.Arrays;
import java.util.List;

import com.pe.feriodev.code.models.Examen;

public class ExamenRepositoryImpl implements ExamenRepository{

	@Override
	public List<Examen> findAll() {
		System.out.println("ExamenRepositoryImpl.findAll");
		return Arrays.asList(new Examen(1L, "Matematica"), new Examen(2L, "Lenguaje"));
	}

	@Override
	public Examen guardar(Examen examen) {
		System.out.println("ExamenRepositoryImpl.guardar");
		return new Examen(3L, "Historia");
	}

}
