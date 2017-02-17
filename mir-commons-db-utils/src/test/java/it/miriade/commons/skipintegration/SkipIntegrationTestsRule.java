package it.miriade.commons.skipintegration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.Assume;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Regola per l'esecuzione condizionale dei test JUnit
 * 
 * @author svaponi
 */
public class SkipIntegrationTestsRule implements MethodRule {

	/**
	 * Proprietà che indica se abilitare i test di integrazione, ovvero quei
	 * test che dipendono dall'ambiente.<br/>
	 * Durante gli sviluppi è plausibile voler invocare dei test che utilizzino
	 * un db locale o remoto per verificare le funzionalità della libreria.
	 * Questi test sono considerati di integrazione perchè dipendono
	 * dall'ambiente, e perciò sono normalmente disabilitati. Per abilitare i
	 * test di integrazione aggiungere <code>run.integration.tests</code> al
	 * comando di invocazione degli unit test sulla libreria.
	 * 
	 * <pre>
	 * mvn test -Drun.integration.tests
	 * </pre>
	 */
	final static String[] RUN_INTEGRATION_TESTS = { "run.integration.tests", "rit" };

	static Boolean skipIntegrationTests;

	public boolean shouldSkip() {
		if (skipIntegrationTests == null) {
			boolean runIntegrationTests = System.getProperty(RUN_INTEGRATION_TESTS[0]) != null
					|| System.getProperty(RUN_INTEGRATION_TESTS[1]) != null;
			skipIntegrationTests = !(runIntegrationTests);
			if (skipIntegrationTests)
				System.out.println("Skipping integration tests");
			else
				System.out.printf("Running integration tests, \"%s\" detected \n", RUN_INTEGRATION_TESTS[0]);

		}
		return skipIntegrationTests;
	}

	/**
	 * Annotation che forza lo skip dei test di integrazione a meno che non
	 * siano esplicitamente richiesti con il parametro
	 * <code>run.integration.tests</code>.
	 * 
	 * @see CuseDefaultSpec#RUN_INTEGRATION_TESTS
	 * @author svaponi
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.METHOD })
	public @interface SkipIntegrationTests {
	}

	@Override
	public Statement apply(Statement base, FrameworkMethod method, Object target) {
		Statement result = base;
		if (method.getAnnotation(SkipIntegrationTests.class) != null && shouldSkip()) {
			result = new Statement() {

				@Override
				public void evaluate() throws Throwable {
					Assume.assumeTrue("Skipping integration tests", false);
				}
			};
		}
		return result;
	}
}