import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class vector implements Comparable<vector>{
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
    String id;
    int nvariables;
    
    public vector(int sizem, int sizeb, double ajb, double bjb, int indiceP, int indiceV){
        sizebm = (int) Math.pow(2, sizem); // obtenemos valor maximo del binario para la variable "m"
        while(true){
            binariom = ThreadLocalRandom.current().nextInt(0, sizebm + 1);//generamos un aleatorio entre 0 y el maximo valor del binario "m"
            m = -100d + binariom*(200d/(sizebm-1));//calculamos el valor de m basado en el valor de la cadena binaria que el corresponde
            if(m<=100 && m>=-100)break;//verificamos que cumpla la condicion de los limtes, si no lo hace repetimos el valor.
        }
        sizebb = (int) Math.pow(2, sizeb);// obtenemos valor maximo del binario para la variable "b"
        while(true){
            binariob = ThreadLocalRandom.current().nextInt(0, sizebb + 1);//generamos un aleatorio entre 0 y el maximo valor del binario "b"
            b = ajb + binariob*((bjb-ajb)/(sizebb-1));//calculamos el valor de b basado en el valor de la cadena binaria que el corresponde
            if(bjb>= b && b>=ajb)break;//verificamos que cumpla la condicion de los limites, si no lo hace repetimos el valor.
        }
        String binariosm = Integer.toBinaryString(binariom);
        String binariosb = Integer.toBinaryString(binariob);
        for(int i = binariosm.length(); i < sizem; i++){
            binariosm = "0" + binariosm;
        }
        for(int i = binariosb.length(); i < sizeb; i++){
            binariosb = "0" + binariosb;
        }
        binario = binariosm + binariosb;
        id = "P"+indiceP+"-V" + indiceV;
        repetido = 0;
        nvariables = 2;
   }
    
    public vector(int sizeb, double ajb, double bjb, int indiceP, int indiceV){
        sizebb = (int) Math.pow(2, sizeb);// obtenemos valor maximo del binario para la variable "b"
        while(true){
            binariob = ThreadLocalRandom.current().nextInt(0, sizebb + 1);//generamos un aleatorio entre 0 y el maximo valor del binario "b"
            b = ajb + binariob*((bjb-ajb)/(sizebb-1));//calculamos el valor de b basado en el valor de la cadena binaria que el corresponde
            if(bjb>= b && b>=ajb)break;//verificamos que cumpla la condicion de los limites, si no lo hace repetimos el valor.
        }
        String binariosb = Integer.toBinaryString(binariob);
        for(int i = binariosb.length(); i < sizeb; i++){
            binariosb = "0" + binariosb;
        }
        binario = binariosb;
        id = "P"+indiceP+"-V" + indiceV;
        repetido = 0;
        r2=true;
        r3=true;
        r4=true;
        m=101;
        binariom = -1;
        nvariables = 1;
   }
    
    public vector(){
        
    }
    
    public vector(int sizem, int sizeb, double ajb, double bjb, int indiceP, int indiceV, String nb){//constructor para un vector de 2 variables
        sizebm = (int) Math.pow(2, sizem);
        sizebb = (int) Math.pow(2, sizeb); 
        cambiaB(nb,sizem,sizeb,ajb,bjb);
        id = "P"+indiceP+"-V" + indiceV;
        repetido = 0;
        r1=true;
        r2=true;
        r3=true;
        r4=true;
        nvariables = 2;
    }
    
    public vector(int sizeb, double ajb, double bjb, int indiceP, int indiceV, String nb){//constructor para un vector de 1 variable
        sizebb = (int) Math.pow(2, sizeb); 
        cambiaB1(nb,sizeb,ajb,bjb);
        id = "P"+indiceP+"-V" + indiceV;
        repetido = 0;
        r1=true;
        r2=true;
        r3=true;
        r4=true;
        nvariables = 2;
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
    
     public void cambiaB1(String nb, int sizeb, double ajb, double bjb){
        String n2 = "";        
        for(int i = 0; i < sizeb; i++){
            n2 += nb.toCharArray()[i];
        }
        binariob = Integer.parseInt(n2, 2);
        b = ajb + binariob*((bjb-ajb)/(sizebb-1));
        binario = nb;
    }
    
    public void cambiaB(String nb, int sizem, int sizeb, double ajb, double bjb){
        String n1 = "";
        for(int i = 0; i < sizem; i++){
            n1 += nb.toCharArray()[i];
        }
        binariom = Integer.parseInt(n1, 2);
        m = -100d + binariom*(200d/(sizebm-1));
        String n2 = "";        
        for(int i = sizem; i < (sizem+sizeb); i++){
            n2 += nb.toCharArray()[i];
        }
        binariob = Integer.parseInt(n2, 2);
        b = ajb + binariob*((bjb-ajb)/(sizebb-1));
        binario = nb;
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
    
    public double porcentajeZ(double totZ, double zAcum){
        zporcentaje = z/totZ; // obtenemos el porcentaje en decimal que le corresponde al vector
        //if(zporcentaje<0)zporcentaje=zporcentaje*-1;// si el procentaje llegase a quedar negativo lo volvemos positivo
        zacumulado = zAcum + zporcentaje; //obtenemos el valor del % acumulado para est vector
        return zacumulado;
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

    public String getBinario() {
        return binario;
    }

    public double getZ() {
        return z;
    }

    public double getZporcentaje() {
        return zporcentaje;
    }

    public double getZacumulado() {
        return zacumulado;
    }

    public int getRepetido() {
        return repetido;
    }

    public String getId() {
        return id;
    }

    public void setRepetido(int repetido) {
        this.repetido = repetido;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
    public vector[] acomoda(vector[] v){ // metodo que recibe un arreglo de vectores y lo acomoda de mayor a menor
        Arrays.sort(v);
        return v;
    }
    
    @Override
    public int compareTo(vector t) { // metodo para acomodar un arreglo de vectores de mayor a menor
       if(repetido < t.getRepetido()){
           return 1;
       }
       if(repetido > t.getRepetido()){
           return -1;
       }
       return 0;
    }
    
    public void newId(int idpoblacion, int idVector){
        id = "P"+idpoblacion + "-V"+idVector;
    }

    public int getSizebm() {
        return sizebm;
    }

    public void setSizebm(int sizebm) {
        this.sizebm = sizebm;
    }

    public int getSizebb() {
        return sizebb;
    }

    public void setSizebb(int sizebb) {
        this.sizebb = sizebb;
    }

    public int getBinariom() {
        return binariom;
    }

    public void setBinariom(int binariom) {
        this.binariom = binariom;
    }

    public int getBinariob() {
        return binariob;
    }

    public void setBinariob(int binariob) {
        this.binariob = binariob;
    }

    public void setBinario(String binario) {
        this.binario = binario;
    }

    public void setM(double m) {
        this.m = m;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setZporcentaje(double zporcentaje) {
        this.zporcentaje = zporcentaje;
    }

    public void setZacumulado(double zacumulado) {
        this.zacumulado = zacumulado;
    }
    
    
}
