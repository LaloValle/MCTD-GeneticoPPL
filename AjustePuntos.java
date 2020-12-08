/*
	IPN - ESCOM
	Métodos Cuantitativos para la Toma de Decisiones
	Prof: Lopez Rojas Ariel
	Grupo: 3CM8
	Segundo exámen parcial: Ajuste de puntos a una función lineal y una gausiana 
	----------------------------------------------------------------------------
    Creación: 5/Diciembre/2020
    Creado por: 
        + César Adrián Díaz Gonzales
		+ Luis Eduardo Valle Martínez
		
	Funcionamiento
	>>>>>>>>>>>>>>

		Se cuenta con 3 parámetros configurables para el programa:
			i:entero / Número de iteraciones
			p:entero / Número de poblaciones
			t:entero / Número de individuos por población
			b:entero / Valor de la precisión

        Es necesario indicar la lista de los puntos a los que se ajustan las
        funciones en el archivo externo con nombre 'Puntos'
*/

import java.util.*;

import javax.swing.text.StyledEditorKit;

import java.lang.Math;

public class AjustePuntos {
	// Constantes de colores para impresión en consola
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_BLUE = "\u001B[34m";

	// Imprime un mensaje de error y termina la ejecución del programa
	public static void error(String mensaje) {
		System.out.printf(ANSI_YELLOW + "\nERROR >> %s" + ANSI_RESET + "\n", mensaje);
		System.exit(-1);
	}

	// Imprime un mensaje con cierta estructura en la consola
	public static void mensaje(String mensaje) {
		System.out.printf(ANSI_GREEN + "\n --- %s ---" + ANSI_RESET + "\n", mensaje);
    }

	public static void main(String[] args) {
        /**
         * Parámetros configurables
         */
        int iteraciones = 1,
        poblaciones = 100, //Indica poblaciones ilimitadas
        individuos = 100,
        precision = 0;

		LogicaAjustePuntos logica = LogicaAjustePuntos.logica();
		logica.getPuntos();

		for(String argumento:args){
            int indiceValor = argumento.indexOf(":"); 

            if(indiceValor > -1){ // Es un parámetro configurable
                int valor = (int)Integer.parseInt(argumento.substring(indiceValor+1));

                switch(argumento.charAt(0)){
                    case 'i':
                        iteraciones = valor;
                        break;
                    case 'p':
                        poblaciones = valor;
                        break;
                    case 't':
                        individuos = valor;
                        break;
                    case 'b':
                        precision = valor;
                        break;
                }
             }
		}
		/*
			Configuración de los problemas y sus restricciones
        */
        PPL PPLineal = new PPL(PPL.LINEAL,PPL.MINIMIZACION);
        PPLineal.agregarRestriccion('m',-100,100); // m
		PPLineal.agregarRestriccion('b',-1*logica.limiteTerminoIndependiente(),logica.limiteTerminoIndependiente()); // b
		
		PPL PPLGausiano = new PPL(PPL.GAUSIANA,PPL.MINIMIZACION);
		PPLGausiano.agregarRestriccion('m',logica.abcisaDeOrdenadaMayor()); // m
		PPLGausiano.agregarRestriccion('k',0,5); // k
		
		System.out.printf("Z:%g\n",PPLineal.Z(0.0,0.0));
		System.out.printf("ajm %g , bjm %g , ajb %g , bjb %g\n",PPLineal.limiteInferiorR(1),PPLineal.limiteSuperiorR(1),PPLineal.limiteInferiorR(2),PPLineal.limiteSuperiorR(2));
		System.out.printf("ajm %g , bjm %g , ajb %g , bjb %g\n",PPLGausiano.limiteInferiorR(1),PPLGausiano.limiteSuperiorR(1),PPLGausiano.limiteInferiorR(2),PPLGausiano.limiteSuperiorR(2));

        /* algoritmoGenetico genetico = new algoritmoGenetico(iteraciones, individuos, poblaciones, precision, PPLineal.limiteInferiorR(2),PPLineal.limiteSuperiorR(2),2,PPLineal);
		genetico.startAlgoritmo();
		vector mejorVector = genetico.mejorVector(); */

		TempladoSimulado templadoLineal = new TempladoSimulado(PPLineal);
		Estado estadoFinalLineal = templadoLineal.templadoSimulado();

		TempladoSimulado templadoGausiano = new TempladoSimulado(PPLGausiano);
		Estado estadoFinalGausiano = templadoGausiano.templadoSimulado();

		/**
		 * Graficación de los puntos y el ajuste lineal y gausiano
		 */
		Graficas grafica = new Graficas(1, 1);
		int indiceColeccion = -1;

		for(Double[] par: logica.getPuntos()){
			indiceColeccion = grafica.agregarParOrdenado(
				"Puntos", 
				par[0],par[1], 
				indiceColeccion);
		}

		for(Double[] par: logica.getPuntos()){
			indiceColeccion = grafica.agregarParOrdenado(
				"Lineal", 
				par[0],FuncionObjetivo.ecuacionPuntoPendiente(par[0],estadoFinalLineal.getSolucion(0),estadoFinalLineal.getSolucion(1)),
				indiceColeccion);
		}

		grafica.crearGrafica(
			"Graficación del ajuste lineal",
			"Eje abcisas", 
			"Eje ordenadas", 
			indiceColeccion);
		
		grafica.mostrarGrafica();

		
		indiceColeccion = -1;

		for(Double[] par: logica.getPuntos()){
			indiceColeccion = grafica.agregarParOrdenado(
				"Puntos", 
				par[0],par[1], 
				indiceColeccion);
		}

		for(Double[] par: logica.getPuntos()){
			indiceColeccion = grafica.agregarParOrdenado(
				"Lineal", 
				par[0], FuncionObjetivo.ecuacionGausiana(par[0],estadoFinalGausiano.getSolucion(0),estadoFinalGausiano.getSolucion(1)),
				indiceColeccion);
		}

		grafica.crearGrafica(
			"Graficación del ajuste gausiano",
			"Eje abcisas", 
			"Eje ordenadas", 
			indiceColeccion);

		grafica.mostrarGrafica();
	}
}

class LogicaAjustePuntos{
	private LogicaAjustePuntos instancia = null;

	ArrayList<Double[]> paresOrdenados;

	private LogicaAjustePuntos(){
		paresOrdenados = new ArrayList<Double[]>();
	}

	public static LogicaAjustePuntos logica(){
		LogicaAjustePuntos log = new LogicaAjustePuntos();
		if(log.instancia == null)
			log.instancia = log;
		return log.instancia;
	}

	/**
	 * Retorna los puntos en un ArrayList con un arreglo de tipo Double como la lista
	 * @return
	 */
    public ArrayList<Double[]> getPuntos(){
		if(paresOrdenados.isEmpty())
			paresOrdenados = Archivos.archivo("./Puntos.txt").getTablaValores();
		return paresOrdenados;
	}
	
	public void imprimirPuntos(){
		for(Double[] pares:paresOrdenados)
			System.out.printf("x:%g | y:%g\n",pares[0],pares[1]);
	}
	
	/**
	 * Retorna el valor de la sumatoria de los valores de la abcisas al cuadrado
	 * 
	 * @return limite
	 */
    public double limiteTerminoIndependiente(){
        double sumatoria = 0;
        for(Double[] pares:paresOrdenados){
            sumatoria += pares[0];
        }
        return Math.pow(sumatoria,2);
	}
	
	/**
	 * Retorna el valor de la abcisa correspondiente al valor máximo de las ordenadas
	 * @return
	 */
	public double abcisaDeOrdenadaMayor(){
		int indiceMayor = 0;
		for(int i=0;i< paresOrdenados.size();i++){
			if(paresOrdenados.get(i)[1]>paresOrdenados.get(indiceMayor)[1])
				indiceMayor = i;
		}
		return paresOrdenados.get(indiceMayor)[0];
	}

	public double abcisaMayor(){
		int indiceMayor = 0;
		for(int i=0;i< paresOrdenados.size();i++){
			if(paresOrdenados.get(i)[0]>paresOrdenados.get(indiceMayor)[0])
				indiceMayor = i;
		}
		return paresOrdenados.get(indiceMayor)[0];

	}
}