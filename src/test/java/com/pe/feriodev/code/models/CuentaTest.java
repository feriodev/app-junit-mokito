package com.pe.feriodev.code.models;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumingThat;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.pe.feriodev.code.exceptions.DineroInsuficienteException;

class CuentaTest {

	Cuenta cuenta;

	@BeforeAll
	static void initAll() {
		System.out.println("Inicializando el Test");
	}

	@AfterAll
	static void endAll() {
		System.out.println("Finalizando Test");
	}

	@BeforeEach
	void initMethodTest(TestInfo testInfo, TestReporter testReporter) {
		this.cuenta = new Cuenta("Andres", new BigDecimal("1000"));		
		System.out.println("Ejecutando : " + testInfo.getDisplayName() + " - " +
				testInfo.getTestMethod().get().getName() + " con los tags " + testInfo.getTags());
	}

	@AfterEach
	void endMethodTest() {
		System.out.println("Pruebas finalizadas");
	}

	@Nested
	class CuentaBancoTest{		
				
		@Test
		@DisplayName("TestNombreCuenta")
		void testNombreCuenta() {
			Cuenta cuenta = new Cuenta();
			cuenta.setPersona("Andres");
			cuenta.setSaldo(new BigDecimal(500));

			String esperado = "Andres";
			String real = cuenta.getPersona();

			assertEquals(esperado, real);
			assertTrue(real.equals("Andres"));
		}

		@Test
		void testSaldoCuenta() {
			assertEquals(1000, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
		}

		@Test
		void testReferencia() {
			Cuenta cuenta = new Cuenta("Andres", new BigDecimal("500.10"));
			Cuenta cuenta1 = new Cuenta("Andres", new BigDecimal("500.10"));
			assertEquals(cuenta, cuenta1);
		}

		@Test
		void testDebitoCuenta() {
			cuenta.debito(new BigDecimal(100));

			assertNotNull(cuenta.getSaldo());
			assertEquals(900, cuenta.getSaldo().intValue());
			assertEquals("900", cuenta.getSaldo().toPlainString());
		}

		@Test
		void testCreditoCuenta() {
			cuenta.credito(new BigDecimal(100));

			assertNotNull(cuenta.getSaldo());
			assertEquals(1100, cuenta.getSaldo().intValue());
			assertEquals("1100", cuenta.getSaldo().toPlainString());
		}

		@Test
		void testDineroInsuficienteException() {
			Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
				cuenta.debito(new BigDecimal(1500));
			});
			assertEquals("Dinero Insuficiente", exception.getMessage());
		}

		@Test
		void testTransferirDineroCuentas() {
			Cuenta cuenta1 = new Cuenta("Andres", new BigDecimal("2500"));
			Cuenta cuenta2 = new Cuenta("Fernando", new BigDecimal("1500"));
			Banco banco = new Banco();
			banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
			assertEquals("1000", cuenta2.getSaldo().toPlainString());
			assertEquals("3000", cuenta1.getSaldo().toPlainString());
		}

		@Test
		void testRelacionBancoCuentas() {
			Cuenta cuenta1 = new Cuenta("Andres", new BigDecimal("2500"));
			Cuenta cuenta2 = new Cuenta("Fernando", new BigDecimal("1500"));
			Banco banco = new Banco("BCP");
			banco.addCuenta(cuenta1);
			banco.addCuenta(cuenta2);
			banco.transferir(cuenta2, cuenta1, new BigDecimal(500));

			assertAll(() -> {
				assertEquals(2, banco.getCuentas().size(), () -> "El tamaño de las cuentas debe ser de 2");
			}, () -> {
				assertEquals("BCP", cuenta1.getBanco().getNombre(), () -> "El nombre del banco debe ser BCP");
			}, () -> {
				assertEquals("Fernando", banco.getCuentas().stream().filter(c -> c.getPersona().equals("Fernando"))
						.findFirst().get().getPersona());
			}, () -> {
				assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Fernando")));
			});
		}
	}
	
	@Nested
	class EnabledDisabledTest{
		@Test
		@Disabled
		void printSystemProperties() {
			Properties properties = System.getProperties();
			properties.forEach((k, v) -> System.out.println(k + " : " + v));
		}
		
		@Test
		@EnabledOnOs(OS.WINDOWS)
		void testSoloWindows() {}
		
		@Test
		@EnabledOnOs({OS.LINUX, OS.MAC})
		void testEnabledOnOs() {}
		
		@Test
		@EnabledOnJre(JRE.JAVA_8)
		void testEnabledOnJre() {}
	}
	
	@Nested
	class AssummingTest{
		
		@Tag("Assumming")
		@Test
		void testaAsumeFalse() {
	
			boolean esDev = "dev".equals(System.getProperty("ENV"));

			assumeFalse(esDev);
			assertEquals(1000, cuenta.getSaldo().doubleValue());
			assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
		}
		
		@Test
		void testAssumingThat() {
			boolean esDev = "dev".equals(System.getProperty("ENV"));

			assumingThat(esDev, ()-> {
				assertEquals(1000, cuenta.getSaldo().doubleValue());
				assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
			});
			
		}
	}
	
	@Tag("param")
	@Nested
	class ParameterTest{
		@DisplayName("RepeatedTest")
		@RepeatedTest(value = 5, name = "{displayName} - Repeticion #{currentRepetition} de {totalRepetitions}")
		void repeatedTest(RepetitionInfo info) {
			if (info.getCurrentRepetition() == 3) {			
				System.out.println("Estamos en la repeticion " + info.getCurrentRepetition());
			}
			UUID uuid = UUID. randomUUID();
			System.out.println(uuid.toString());
		}
		
		@DisplayName("ParameterizedTestValueSource")
		@ParameterizedTest(name = "{displayName} - Parametro #{index} ejecutando valor {0}")
		@ValueSource(ints = {100, 200, 500})
		void parameterizedTestValueSource(int value) {
			System.out.println("Parametro: "+ value);
		}
		
		@DisplayName("ParameterizedCsvValueSource")
		@ParameterizedTest(name = "{displayName} - Parametro #{index} ejecutando valor {1}")
		@CsvSource({"1,100", "2,200"})
		void parameterizedCsvValueSource(String index, String value) {
			System.out.println("Index: "+ index + ": " + value);
		}
		
		@DisplayName("ParameterizedCsvFileValueSource")
		@ParameterizedTest(name = "{displayName} - Ejecutando valor {arguments}")
		@CsvFileSource(resources = "/data.csv")
		void parameterizedCsvFileValueSource(String value) {
			System.out.println("Value: " + value);
		}
			
	}
	
	@Tag("param")
	@DisplayName("ParameterizedMethodSource")
	@ParameterizedTest(name = "{displayName} - Ejecutando valor {arguments}")
	@MethodSource("methodList")
	void parameterizedMethodSource(String value) {
		System.out.println("Value: " + value);
	}

	static List<String> methodList(){
		return Arrays.asList("100", "500", "1000");
	}
	
	@Tag("Timeout")
	@Nested
	class TimeoutTest{
		@Test
		@Timeout(5)
		void testTimeout() throws InterruptedException {
			TimeUnit.SECONDS.sleep(4);
		}
		
		@Test
		void testTimeoutAssert() {
			assertTimeout(Duration.ofSeconds(5), () -> {
				TimeUnit.SECONDS.sleep(4);
			});
		}
	}

}
