package com.pe.feriodev.code.models;

import java.math.BigDecimal;

import com.pe.feriodev.code.exceptions.DineroInsuficienteException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

	private String persona;
	private BigDecimal saldo;
	private Banco banco;
	
	public Cuenta(String persona, BigDecimal saldo) {
		this.persona = persona;
		this.saldo = saldo;
		this.banco = new Banco();
	}
	
	public void debito(BigDecimal monto) {
		BigDecimal nuevoSaldo = this.saldo.subtract(monto);
		if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
			throw new DineroInsuficienteException("Dinero Insuficiente");
		}		
		setSaldo(getSaldo().subtract(monto));
	}
	
	public void credito(BigDecimal monto) {
		setSaldo(getSaldo().add(monto));
	}
	
	@Override
	public boolean equals(Object obj) {		
		if (!(obj instanceof Cuenta)) {
			return false;
		}		
		
		Cuenta cuenta = (Cuenta) obj;
		if (this.persona == null || this.saldo == null) {
			return false;
		}
		return this.persona.equals(cuenta.getPersona()) && 
				this.saldo.equals(cuenta.getSaldo());
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
