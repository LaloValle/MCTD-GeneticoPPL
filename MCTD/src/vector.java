import java.util.concurrent.ThreadLocalRandom;

public class vector {
    String binario;
    double m;
    double b;
    double z;
    double zporcentaje;
    double zacumulado;
    int repetido;
    int sizebm;
    int sizebb;
    int binariom;
    int binariob;
    boolean r1;
    boolean r2;
    boolean r3;
    boolean r4;
    
    public vector(int sizem, int sizeb, int ajb, int bjb){
        sizebm = (int) Math.pow(2, sizem); // obtenemos valor maximo del binario para la variable "m"
        while(true){
            binariom = ThreadLocalRandom.current().nextInt(0, sizebm + 1);//generamos un aleatorio entre 0 y el maximo valor del binario "m"
            m = -100 + binariom*(200/sizebm-1);//calculamos el valor de m basado en el valor de la cadena binaria que el corresponde
            if(m<=100 && m>=-100)break;//verificamos que cumpla la condicion de los limtes, si no lo hace repetimos el valor.
        }
        sizebb = (int) Math.pow(2, sizeb);// obtenemos valor maximo del binario para la variable "b"
        while(true){
            binariob = ThreadLocalRandom.current().nextInt(0, sizebb + 1);//generamos un aleatorio entre 0 y el maximo valor del binario "b"
            b = ajb + binariob*((bjb-ajb)/(sizebb-1));//calculamos el valor de b basado en el valor de la cadena binaria que el corresponde
            if(bjb>= b && b>=ajb)break;//verificamos que cumpla la condicion de los limites, si no lo hace repetimos el valor.
        }
        binario = Long.toBinaryString(Double.doubleToRawLongBits(binariom)) + Long.toBinaryString(Double.doubleToRawLongBits(binariob));//Generamos el vector en si(o su binario), para esto, concatenamos los valores de los binarios obtenidos anteriormente
    }
    
    public void setZ(double z){
        if(r1&&r2&&r3&&r4){ //verificamos que las condiciones se cumplan;
            this.z = z; // guardamos el valor de z para este vector
        }else{
            System.out.println("Error, las restricciones no se cumplen");//Aviso de que no se cumplen las restricciones
        }
    }
    
    public void newM(int sizem){
      sizebm = (int) Math.pow(2, sizem); // obtenemos valor maximo del binario para la variable "m"
        while(true){
            binariom = ThreadLocalRandom.current().nextInt(0, sizebm + 1);//generamos un aleatorio entre 0 y el maximo valor del binario "m"
            m = -100 + binariom*(200/sizebm-1);//calculamos el valor de m basado en el valor de la cadena binaria que el corresponde
            if(m<=100 && m>=-100)break;//verificamos que cumpla la condicion de los limtes, si no lo hace repetimos el valor.
        }  
    }
    
    public void newB(int sizeb, int ajb, int bjb){
      sizebb = (int) Math.pow(2, sizeb);// obtenemos valor maximo del binario para la variable "b"
        while(true){
            binariob = ThreadLocalRandom.current().nextInt(0, sizebb + 1);//generamos un aleatorio entre 0 y el maximo valor del binario "b"
            b = ajb + binariob*((bjb-ajb)/(sizebb-1));//calculamos el valor de b basado en el valor de la cadena binaria que el corresponde
            if(bjb>= b && b>=ajb)break;//verificamos que cumpla la condicion de los limites, si no lo hace repetimos el valor.
        }  
    }
    
    public void generaBinario(){
         binario = Long.toBinaryString(Double.doubleToRawLongBits(binariom)) + Long.toBinaryString(Double.doubleToRawLongBits(binariob));//Generamos el vector en si(o su binario), para esto, concatenamos los valores de los binarios obtenidos anteriormente
    }
    
    public void porcentajeZ(double totZ, double zAcum){
        zporcentaje = z/totZ; // obtenemos el porcentaje en decimal que le corresponde al vector
        zacumulado = zAcum + zporcentaje; //obtenemos el valor del % acumulado para est vector
    }
    
    public boolean rangoVector(double random){
        if(random <= zacumulado){ // el random recibido esta entre 0 y 1, aqui se evalua si es menor
            repetido++; // En caso de que sea menor se le suma a repetido 1
            return true; // regresa este boleano para terminar la busqueda
        }else return false; // En caso de que sea mayor, no entra en rango y manda false para evaluar el siguiente 
    }
    
    public double getM() {
        return m;
    }

    public double getB() {
        return b;
    }

    public void setR1(boolean r1) {
        this.r1 = r1;
    }

    public void setR2(boolean r2) {
        this.r2 = r2;
    }

    public void setR3(boolean r3) {
        this.r3 = r3;
    }

    public void setR4(boolean r4) {
        this.r4 = r4;
    }
    
    
}
