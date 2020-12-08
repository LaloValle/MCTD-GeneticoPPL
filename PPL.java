import java.util.ArrayList;

public class PPL {
    private FuncionObjetivo FO;
    private ArrayList<Double[]> restricciones;
    private int minmax;

    static final int LINEAL = 1;
    static final int GAUSIANA = 2;

    /**
     * Constructor del PPL
     * 
     * @param funcionObjetivo: Si es LINEAL o GAUSIANA
     */
    public PPL(int funcionObjetivo,int MinMax){
        FO = new FuncionObjetivo(funcionObjetivo);

        restricciones = new ArrayList<Double[]>();

        minmax = MinMax;
    }

    /**
     * Ingresa la restricción para una variable actada por debajo y arriba
     * 
     * @param variable
     * @param limiteMayor
     * @param limiteMenor
     * @return agregada
     */
    public void agregarRestriccion(double limiteMenor, double limiteMayor){
        Double restriccion[] = {
            limiteMenor,
            limiteMayor
        };

        restricciones.add(restriccion);
    }

    /**
     *  Ingresa la restricción para una variable igualada a un valor
     * 
     * @param limite
     * @return
     */
    public void agregarRestriccion(double limite){
        Double restriccion[] = {
            limite,
            limite
        };

        restricciones.add(restriccion);
    }

    /**
     * Calcula la función objetivo para los valores ingresados
     * 
     * @param m: pendiente
     * @param b: término independiente
     * @return Z: valor de la F.O.
     */
    public double Z(double m,double b){
        return minmax * FO.funcionObjetivo(m,b);
    }

    /**
     * Verifica si se cumple para una restricción el valor ingresado.
     * Retorna verdadero o falso
     * 
     * @param numeroRestriccion
     * @param valor
     * @return cumple
     */
    public boolean r(int numeroRestriccion,double valor){
        Double[] restriccionAux = restricciones.get(numeroRestriccion-1);
        return restriccionAux[0] <= valor && valor <= restriccionAux[1];
    }

    /**
     * Retorna el límite inferior de una restricción
     * 
     * @param numeroRestriccion
     * @return limiteInferior
     */
    public double limiteInferiorR(int numeroRestriccion){
        return restricciones.get(numeroRestriccion-1)[0];
    }

    /**
     * Retorna el límite superior de una restricción
     * 
     * @param numeroRestriccion
     * @return limiteSuperior
     */
    public double limiteSuperiorR(int numeroRestriccion){
        return restricciones.get(numeroRestriccion-1)[1];
    }
}

class FuncionObjetivo {
    private int funcion;

    public FuncionObjetivo(int funcionObjetivo){
        funcion = funcionObjetivo;
    }

    public static double ecuacionPuntoPendiente(double x,double m,double b){
        return m*x + b;
    }

    public static double ecuacionGausiana(double x,double m,double k){
        return Math.exp(-1*k*Math.sqrt(x-m));
    }

    public double funcionObjetivo(double m,double b){
        double suma = 0.0;
        for(Double[] pares:LogicaAjustePuntos.logica().getPuntos()){
            suma += Math.abs(
                ((funcion == PPL.LINEAL)? ecuacionPuntoPendiente(pares[0],m,b) : ecuacionGausiana(pares[0],b,m) )//f(x0)
                - pares[1] //y0
            );
        }
        return suma;
    }   
}