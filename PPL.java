import java.util.ArrayList;

public class PPL {
    private FuncionObjetivo FO;
    private ArrayList<Double[]> restricciones;
    private ArrayList<Double[]> paresOrdenados;

    /**
     * Constructor del PPL
     * 
     * @param funcionObjetivo: Si es LINEAL o GAUSIANA
     */
    public PPL(ArrayListdouble funcionObjetivo){
        FuncionObjetivo(funcionObjetivo);

        restricciones = new ArrayList<Double[]>();
        paresOrdenados = new ArrayList<Double[]>();
    }

    /**
     * Tabla de los puntos ingresados para el ajuste de la función
     * 
     * @param puntos
     */
    public void setPuntos(ArrayList<Double[]> puntos){
        paresOrdenados = puntos;
    }

    /**
     * Ingresa la restricción para una variable actada por debajo y arriba
     * Verdadero si fue ingresada
     * Falso cuando no se tiene los pares ordenados(puntos)
     * 
     * @param variable
     * @param limiteMayor
     * @param limiteMenor
     * @return agregada
     */
    public boolean agregarRestriccion(char variable, double limiteMayor, double limiteMenor){
        if(paresOrdenados.isEmpty())
            return false;

        Double restriccion[] = {
            limiteMenor,
            limiteMayor
        };

        restricciones.add(restriccion);
        return true;
    }

    /**
     * Calcula la función objetivo para los valores ingresados
     * 
     * @param m: pendiente
     * @param b: término independiente
     * @return Z: valor de la F.O.
     */
    public double Z(double m,double b){
        return FO.funcionObjetivo(paresOrdenados,m,b);
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
        restriccionAux = restricciones.get(numeroRestriccion-1);
        return restriccionAux[0] <= valor && valor <= restriccionAux[1];
    }

    private class FuncionObjetivo {
        static final double LINEAL = 1;
        static final double GAUSIANA = 2;

        private double funcion;

        public FuncionObjetivo(double funcionObjetivo){
            funcion = funcionObjetivo;
        }

        private ecuacionPuntoPendiente(double x,double m,double b){
            return m*x + b;
        }

        private ecuacionGausiana(double x,double m,double k){
            return Math.exp(-1*k*Math.sqrt(x-m));
        }

        public double funcionObjetivo(ArrayList<Double[]> puntos,double m,double b){
            double suma = 0.0;
            for(Double[] pares:puntos){
                suma += Math.abs(
                    ((funcion == LINEAL)? ecuacionPuntoPendiente(pares[0],m,b) : ecuacionGausiana(pares[0],m,b) )//f(x0)
                    - pares[1] //y0
                );
            }
        }   
    }
}