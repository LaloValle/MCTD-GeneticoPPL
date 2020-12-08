import java.util.Random;

import java.util.ArrayList;

public class TempladoSimulado {

    protected final int TOPE = 10000;
    protected boolean terminar = false;
    
    protected double TActual; // Temperatura
    protected Estado estadoMejor; // Estado actual
    protected int iteracionActual;

    protected ArrayList<Estado> historialEstados;
    protected double sigmas[]; // desviaciones estándar

    protected PPL ppl;

    public TempladoSimulado(PPL problema){
        ppl = problema;
        iteracionActual = 0;

        sigmas = new double[ppl.getNumeroVariables()];
        historialEstados = new ArrayList<Estado>();

        calcularSigmas();
    }

    /*
        Métodos de configuración del algoritmo
    */

    private void calcularSigmas(){
        for(int i=0;i<ppl.getNumeroVariables();i++){
            sigmas[i] = (ppl.limiteSuperiorR(i+1)-ppl.limiteInferiorR(i+1))/6;
            System.out.printf("Sigma[%d] = %g\n",i,sigmas[i]);
        }
    }

    // Genera el primer estado
    private void estadoInicial(){
        double referencias[] = new double[sigmas.length];
        for(int i=0;i<sigmas.length;i++){
            referencias[i] = (ppl.limiteSuperiorR(i+1)+ppl.limiteInferiorR(i+1))/2;
            System.out.printf("limS:%g, limI:%g, Referencias[%d] = %g\n",ppl.limiteSuperiorR(i+1),ppl.limiteInferiorR(i+1),i,referencias[i]);
        }

        double Zc = ppl.Z(referencias[0],referencias[1]);
        TActual = Zc*0.2;
        System.out.printf("TActual:T%g \n",TActual);

        estadoMejor = new Estado(
            iteracionActual, 
            TActual, 
            referencias,
            Zc);

        calcularSoluciones(estadoMejor);
        calcularProbabilidadAceptacion(estadoMejor);

        historialEstados.add(estadoMejor);
    }

    private Estado crearEstado(){
        Estado estadoAux = null;
        int pasos = 0;

        while(true && !terminar){
            estadoAux = new Estado(
                iteracionActual,
                TActual,
                estadoMejor.getSoluciones(),
                estadoMejor.getZn());
            calcularSoluciones(estadoAux);
            if(calcularProbabilidadAceptacion(estadoAux) > estadoMejor.getProbabilidadAceptacion())
                break;
            else{
                pasos ++;
                if(pasos == TOPE)
                    topeRebasado("No se pudo encontrar un estado con probabilidad mejor");
            }
        }
        return estadoAux;
    }

    // Activa la bandera para terminar la ejecución del algoritmo
    private void topeRebasado(String razon){
        terminar = true;
        System.out.printf("Algoritmo Templado >> Término(%s)\n",razon);
    }

    /* 
        Métodos
    */

    private void calcularSoluciones(Estado estado){
        double aleatorios[] = new double[sigmas.length];
        double soluciones[] = new double[sigmas.length];
        int indice = 0;
        int pasos = 0;
        double solucionAux,
        Zn;

        // Se obtienen los valores para las variables y se verifica si el valor cumple las restricciones
        while(indice<soluciones.length){
            aleatorios[indice] = new Random().nextGaussian()*sigmas[indice];

            solucionAux = aleatorios[indice]+estadoMejor.getSolucionReferencia(indice);
            if(ppl.r(indice+1,solucionAux)){
                soluciones[indice] = solucionAux;
                indice ++;
                pasos = 0;
            }else{
                pasos ++;
                if(pasos > TOPE){
                    topeRebasado("No se pudo encontrar un estado con valores que cumplieran las restricciones");
                    break;
                }
            }
        }

        if(!terminar){
            Zn = ppl.Z(soluciones[0],soluciones[1]);
            estado.agregarSoluciones(aleatorios, soluciones, Zn);
        }
    }

    private double calcularProbabilidadAceptacion(Estado estado){
        double probabilidadAceptacion = Math.exp(((estado.getZn()*ppl.minmax())-(estado.getZc()*ppl.minmax()))/TActual);
        estado.setProbabilidadAceptacion(probabilidadAceptacion);
        return probabilidadAceptacion;
    }

    // Inicia la ejecución del algoritmo
    public Estado templadoSimulado(){
        estadoInicial();

        // Primer iteración
        realizarIteracion();

        while(!terminar){
            TActual *= 0.5;
            realizarIteracion();
        }

        imprimirHistorialEstados();
        imprimirEstadoFinal();
        return estadoMejor;
    }

    public void realizarIteracion(){
        Estado estadoAux,
        estadoMejorAux = estadoMejor;

        for(int i=0;i<5 && !terminar;i++){
            iteracionActual ++;
            estadoAux = crearEstado();
            if(estadoAux.getProbabilidadAceptacion() > estadoMejorAux.getProbabilidadAceptacion())
                estadoMejorAux = estadoAux;
            historialEstados.add(estadoAux); // Se agregan todos los estados generados
        }

        if(!terminar)
            estadoMejor = estadoMejorAux; // Se cambia el estado con la mejor probabilidad
    }

    public void imprimirHistorialEstados(){
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%5s %10s %15s %10s %20s %25s %15s %10s\n", "I","T","Referencias","Zc","Aleatorios","Soluciones","Zn","P.A.");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        for(Estado estado:historialEstados)
            estado.imprimirEstado();
    }

    public void imprimirEstadoFinal(){
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%70s\n", "Estado Final");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");
        estadoMejor.imprimirEstado();
        System.out.println("----------------------------------------------------------------------------------------------------------------------------");

    }
}

class Estado{
    protected int iteracion;
    protected double T;
    protected double solucionesReferencia[]; // Valores de las soluciones anteriores
    protected double Zc;
    protected double aleatorios[];
    protected double soluciones[];
    protected double Zn;
    protected double probabilidadAceptacion;

    public Estado(int iteracion,double T,double solucionesReferencia[],double Zc){
        this.iteracion = iteracion;
        this.T = T;
        this.solucionesReferencia = solucionesReferencia;
        this.Zc = Zc;
    }
    
    public Estado(int iteracion,double T,double solucionesReferencia[], double aleatorios[], double Zc, double soluciones[],double Zn,double probabilidadAceptacion){
        this.iteracion = iteracion;
        this.T = T;
        this.solucionesReferencia = solucionesReferencia;
        this.Zc = Zc;
        setZc(Zc);
        setAletorios(aleatorios);
        setSoluciones(soluciones);
        setZn(Zn);
        setProbabilidadAceptacion(probabilidadAceptacion);
    }

    /*
        Getters y Setters
    */
    public void setIteracion(int iteracion) {
        this.iteracion = iteracion;
    }

    public void setT(double t) {
        T = t;
    }

    public void setSolucionesReferencia(double[] solucionesReferencia) {
        this.solucionesReferencia = solucionesReferencia;
    }

    public void setZc(double Zc){
        this.Zc = Zc;
    }

    public void setAleatorios(double[] aleatorios) {
        this.aleatorios = aleatorios;
    }

    public void setAletorios(double aleatorios[]){
        this.aleatorios = aleatorios;
    }

    public void setSoluciones(double soluciones[]){
        this.soluciones = soluciones;
    }

    public void setZn(double Zn){
        this.Zn = Zn;
    }

    public void setProbabilidadAceptacion(double probabilidadAceptacion){
        this.probabilidadAceptacion = probabilidadAceptacion;
    }
    
    public int getIteracion() {
        return iteracion;
    }

    public double getT() {
        return T;
    }

    public double[] getSolucionesReferencia() {
        return solucionesReferencia;
    }

    public double getZc() {
        return Zc;
    }

    public double[] getAleatorios() {
        return aleatorios;
    }

    public double[] getSoluciones() {
        return soluciones;
    }

    public double getZn() {
        return Zn;
    }

    public double getProbabilidadAceptacion() {
        return probabilidadAceptacion;
    }

    public double getSolucionReferencia(int indice){
        return solucionesReferencia[indice];
    }

    public double getSolucion(int indice){
        return soluciones[indice];
    }

    /*
        Métodos
    */

    public void agregarSoluciones(double aleatorios[],double soluciones[],double Zn){
        this.aleatorios = aleatorios;
        this.soluciones = soluciones;
        this.Zn = Zn;
    }

    public void imprimirEstado(){
        System.out.printf("%5d %10g", iteracion,T);
        for(double referencia:solucionesReferencia)
            System.out.printf("%10g", referencia);
        System.out.printf("%10g", Zc);
        for(double aleatorio:aleatorios)
            System.out.printf("%10g", aleatorio);
        for(double solucion:soluciones)
            System.out.printf("%15g", solucion);
        System.out.printf("%15g", Zn);
        System.out.print("|");
        System.out.printf("%10g\n", probabilidadAceptacion);
    }
}
