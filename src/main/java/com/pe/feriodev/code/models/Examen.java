package com.pe.feriodev.code.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Examen {

	private Long id;
	private String nombre;
	private List<String> preguntas;
	
	public Examen(Long id, String nombre) {
		this.id = id;
		this.nombre = nombre;
		this.preguntas = new ArrayList<>();
	}
}
