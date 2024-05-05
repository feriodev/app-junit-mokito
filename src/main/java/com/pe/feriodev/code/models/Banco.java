package com.pe.feriodev.code.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banco {

	private String nombre;
	private List<Cuenta> cuentas;
	
	public Banco(String nombre) {
		this.nombre = nombre;
		this.cuentas = new ArrayList<>();
	}
	
	public void addCuenta(Cuenta cuenta) {
		getCuentas().add(cuenta);
		cuenta.setBanco(this);
	}
	
	public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {		
		origen.debito(monto);
		destino.credito(monto);		
	}
}
