
import java.util.concurrent.ThreadLocalRandom;

public class algoritmoGenetico {
    static final int NUMERO_DE_CAMBIOS_MINIMO = 10;
    static final double PORCENTAJE_DE_CAMBIO = 0.001d;
    vector mejorVectorAnterior;//alamcena el mejor vector de la iteracion anterior
    int numIteraciones; //alamacena el numero de iteraciones que se haran
    int tamañoPoblacion; //alamacena el tamaño de la poblacion
    int poblaciones; //alamacena el numero de poblaciones
    vector[] vectores;
    int mejores;
    int mjm;
    int mjb;
    int bitsPrecision;
    double ajb;
    double bjb;
    double ztot;
    int idpoblacion;
    boolean mutacion;
    int nvariables;
    double porcentajeCambio;
    int ncambiosMinimos;
    PPL ppl;
    long end;
    
    public algoritmoGenetico(int numIteraciones, int tamañoPoblacion, int poblaciones, int bitsPrecision, double ajb, double bjb, int nvariables, PPL problema){ // Constructor que respalda cada valor
        mejorVectorAnterior = null;
        this.numIteraciones = numIteraciones;
        this.tamañoPoblacion = tamañoPoblacion;
        this.poblaciones = poblaciones;
        this.bitsPrecision = bitsPrecision;
        if(nvariables == 2)mjm = (int) Math.ceil((Math.log(200*Math.pow(10, bitsPrecision)))/Math.log(2));//generamos el mjm
        else mjm = 0;
        this.ajb = ajb;//guardamos el ajb
        this.bjb = bjb;//guardamos al bjb
        mjb = (int) Math.ceil((Math.log((bjb-ajb)*Math.pow(10, bitsPrecision)))/Math.log(2));//generamos el mjb
        ztot = 0;
        mejores = 0;
        this.nvariables = nvariables;
        porcentajeCambio = 0;
        ppl = problema;
        ncambiosMinimos = 0;
    }
     
    public void startAlgoritmo(){ // al ejecutar se inicia el algoritmo en si 
        end = System.currentTimeMillis() + 29*1000;
        ztot = 0;
        mejores = 0;
        idpoblacion = 0;
        if(poblaciones > 0 && tamañoPoblacion > 0){
            vectores = new vector[tamañoPoblacion];
            for(int i = 0; i < tamañoPoblacion; i++){
                if(nvariables==2){
                    vectores[i] = new vector(mjm, mjb, ppl.limiteInferiorR(2), ppl.limiteSuperiorR(2) ,idpoblacion,i); //enviamos valores para iniciar el vector
                    vectores[i].setR2(ppl.r(2,vectores[i].getB())); //definimos si cumple la r2
                    vectores[i].setR3(true); //definimos si cumple la r3
                    vectores[i].setR4(true); //definimos si cumple la r4
                }else{
                    vectores[i] = new vector(mjb, ppl.limiteInferiorR(1), ppl.limiteSuperiorR(1),idpoblacion,i); //enviamos valores para iniciar el vector con 1 variable
                }
                vectores[i].setR1(ppl.r(1,vectores[i].getM())); //definimos si cumple la r1              
                vectores[i].setZ(ppl.Z(vectores[i].getM(),vectores[i].getB())); //definimos el valor de z
                ztot += vectores[i].getZ(); // sumamos la z a la variable para guardar la suma de todas las z
            }
            
            double zacum = 0;
            for(int i = 0; i < tamañoPoblacion; i++){
                zacum = vectores[i].porcentajeZ(ztot, zacum); // hacemos que cada vector obtenga su porcentaje correspondiente de z
            }
            for(int i = 0; i < tamañoPoblacion; i++){
               double ran = ThreadLocalRandom.current().nextDouble(0, 1); // generamos un random entre 0 y 1
               for(int x = 0; x < tamañoPoblacion; x++){
                   if(vectores[x].rangoVector(ran)){ // evaluamos, al avanzar de vector en vector, y estar ordenados los porcentajes, si el numero aleatorio es menor significa que esta dentro del rango que le corresponde
                       if(vectores[x].getRepetido()==1){
                           mejores++;// si es la primera vez que aparece este vector lo guarda, y llevar cuantos vectores avanzaran a la siguiente poblacion
                       }
                       break;
                   }
               }
            }
            mejores = tamañoPoblacion-mejores;
            vector[] mejoresvectores = new vector[mejores];//guardamos un array del tamaño de los vectores que avanzaran
            int indice = 0;//indice para ver en que vector va del array anterior
            vectores = vectores[0].acomoda(vectores);
            for(int i = 0; i < tamañoPoblacion; i++){
              if(vectores[i].getRepetido() == 0){ //evaluamos si algun aleatorio entro en el rango del vector
                  mejoresvectores[indice] = vectores[i]; // guardamos el vector
                  indice ++;//aumentamos el contador
              }else break;               
            }
            mejoresvectores = mejoresvectores[0].acomoda(mejoresvectores);//ejecutamos el metodo para acomodar el array de vectores
            ztot = 0;
            idpoblacion++;
            System.out.println("------------------------------------------");
            System.out.println("Poblacion: 1");
            imprimeVector(mejoresvectores[0]);
            mejorVectorAnterior = mejoresvectores[0];
            for(int i = 0; i < mejores; i++){
                if(nvariables == 2){
                    vectores[i] = new vector(mjm, mjb, ppl.limiteInferiorR(2), ppl.limiteSuperiorR(2), idpoblacion, i, mejoresvectores[i].getBinario()); // generamos en la pocisión i un nuveo vector, con los atributos de la lista de mejores vectores para 2 variables
                    vectores[i].setR2(ppl.r(2,vectores[i].getB())); //definimos si cumple la r2
                    vectores[i].setR3(true); //definimos si cumple la r3
                    vectores[i].setR4(true); //definimos si cumple la r4
                }
                else vectores[i] = new vector(mjb, ppl.limiteInferiorR(1), ppl.limiteSuperiorR(1),idpoblacion,i); // generamos en la pocisión i un nuveo vector, con los atributos de la lista de mejores vectores para 1 varaible
                vectores[i].setR1(ppl.r(1,vectores[i].getM())); //definimos si cumple la r1
                vectores[i].setZ(ppl.Z(vectores[i].getM(),vectores[i].getB())); //definimos el valor de z
                ztot += vectores[i].getZ(); // sumamos la z a la variable para guardar la suma de todas las z
            }
        }
        for(int i = 0; i < poblaciones; i++){
            if(System.currentTimeMillis() > end) break;
           nuevaP();
           if(ncambiosMinimos == NUMERO_DE_CAMBIOS_MINIMO)break;
        }
    }
    
    public void nuevaP(){
        for(int i = mejores; i < tamañoPoblacion; i++){
            int rnm = ThreadLocalRandom.current().nextInt(0, 100);
            int r1 = mejores-1;     
            if(mejores > 2)
            r1 = ThreadLocalRandom.current().nextInt(1,mejores-1); //seleccionamos un vector al azar entre los mejores
            String newBinario = ""; 
            int r2 = (mejores < 2)? mejores -1 : ThreadLocalRandom.current().nextInt(0,mejores-1) ;
            if(rnm < 51){
                int tamaño = ThreadLocalRandom.current().nextInt(0, mjm+mjb);//generamos un numero al azar para mezclar los vectores
                if(r1 > mejores/2){ // si se cumple esta condicion, tomamos la primera parte del vector 0 y el resto del vector seleccionado al azar
                     char[] bin = vectores[r2].getBinario().toCharArray();
                     for(int x = 0; x < tamaño; x++){
                         newBinario += bin[x] + "";
                     }
                     for(int x = tamaño; x < (mjm+mjb); x++){
                         newBinario += vectores[r1].getBinario().toCharArray()[x] + "";
                     }
                }else{ // si se cumple esta condicion, tomamos la primera parte del vector elegido al azar y el resto del vector 0
                     for(int x = 0; x < tamaño; x++){
                         newBinario += vectores[r1].getBinario().toCharArray()[x] + "";
                     }
                     for(int x = tamaño; x < (mjm+mjb); x++){
                         newBinario += vectores[r2].getBinario().toCharArray()[x] + "";
                     }
                }
           }else{
               int bitMutado= ThreadLocalRandom.current().nextInt(0,(mjm+mjb));
               newBinario = vectores[r1].getBinario();
               if((newBinario.toCharArray()[bitMutado]+"").equals("1"))newBinario.toCharArray()[bitMutado] = '0';
               else newBinario.toCharArray()[bitMutado] = '1';
           }
           if(nvariables == 2)vectores[i] = new vector(mjm, mjb, ppl.limiteInferiorR(2), ppl.limiteSuperiorR(2), idpoblacion, i, newBinario);
           else vectores[i] = new vector(mjb,  ppl.limiteInferiorR(1), ppl.limiteSuperiorR(1), idpoblacion, i, newBinario);
           vectores[i].setZ(ppl.Z(vectores[i].getM(),vectores[i].getB())); //definimos el valor de z
           ztot += vectores[i].getZ(); // sumamos la z a la variable para guardar la suma de todas las z
        }
       
        double zacum = 0;
        for(int i = 0; i < tamañoPoblacion; i++){
            zacum = vectores[i].porcentajeZ(ztot, zacum); // hacemos que cada vector obtenga su porcentaje correspondiente de z
        }
        mejores = 0;
        for(int i = 0; i < tamañoPoblacion; i++){
           double ran = ThreadLocalRandom.current().nextDouble(0, 1); // generamos un random entre 0 y 1
           for(int x = 0; x < tamañoPoblacion; x++){
               if(vectores[x].rangoVector(ran)){ // evaluamos, al avanzar de vector en vector, y estar ordenados los porcentajes, si el numero aleatorio es menor significa que esta dentro del rango que le corresponde
                   if(vectores[x].getRepetido()==1){
                       mejores++;// si es la primera vez que aparece este vector lo guarda, y llevar cuantos vectores avanzaran a la siguiente poblacion
                   }
                   break;
               }
           }
        }
        mejores = tamañoPoblacion-mejores;
        vector[] mejoresvectores = new vector[mejores];//guardamos un array del tamaño de los vectores que avanzaran
        int indice = 0;//indice para ver en que vector va del array anterior
        vectores = vectores[0].acomoda(vectores);
        for(int i = 0; i < tamañoPoblacion; i++){
            if(vectores[i].getRepetido() == 0){ //evaluamos si algun aleatorio entro en el rango del vector
                mejoresvectores[indice] = vectores[i]; // guardamos el vector
                indice ++;//aumentamos el contador
            }else break;               
        }
        mejoresvectores = mejoresvectores[0].acomoda(mejoresvectores);//ejecutamos el metodo para acomodar el array de vectores
        double cambio = mejorVectorAnterior.getZ() - mejoresvectores[0].getZ();
        if(cambio < 0) cambio=cambio*-1;
        porcentajeCambio = cambio*100/mejorVectorAnterior.getZ();
        if(porcentajeCambio < 0) porcentajeCambio=porcentajeCambio*-1;
        if(porcentajeCambio < PORCENTAJE_DE_CAMBIO)ncambiosMinimos++;
        else ncambiosMinimos = 0;
        if(ncambiosMinimos == NUMERO_DE_CAMBIOS_MINIMO)return;
        ztot = 0;
        idpoblacion++;
        System.out.println("------------------------------------------");
        System.out.println("Poblacion: " + idpoblacion);
        imprimeVector(mejoresvectores[0]);
        mejorVectorAnterior = mejoresvectores[0];
        double alto = mejoresvectores[0].getZ();
        for(int i=0;i<mejores;i++){
            if(mejoresvectores[0].getRepetido() == mejoresvectores[i].getRepetido()){
                if(alto < mejoresvectores[i].getZ()){
                    alto = mejoresvectores[i].getZ();
                    mejorVectorAnterior = mejoresvectores[i];
                }
            }
            else break;
        }
        /* imprimeVector(mejorVectorAnterior);
        imprimeVector(mejoresvectores[0]); */
         for(int i = 0; i < mejores; i++){
            if(nvariables == 2){
                vectores[i] = new vector(mjm, mjb, ppl.limiteInferiorR(2), ppl.limiteSuperiorR(2), idpoblacion, i, mejoresvectores[i].getBinario()); // generamos en la pocisión i un nuveo vector, con los atributos de la lista de mejores vectores para 2 variables
                vectores[i].setR2(ppl.r(2,vectores[i].getB())); //definimos si cumple la r2
                vectores[i].setR3(true); //definimos si cumple la r3
                vectores[i].setR4(true); //definimos si cumple la r4
            }
            else vectores[i] = new vector(mjb, ppl.limiteInferiorR(1), ppl.limiteSuperiorR(1),idpoblacion,i); // generamos en la pocisión i un nuveo vector, con los atributos de la lista de mejores vectores para 1 varaible
            vectores[i].setR1(ppl.r(1,vectores[i].getM())); //definimos si cumple la r1
            vectores[i].setZ(ppl.Z(vectores[i].getM(),vectores[i].getB())); //definimos el valor de z
            ztot += vectores[i].getZ(); // sumamos la z a la variable para guardar la suma de todas las z
        }
    }
    
    public void imprimeVector(vector v){
        if(nvariables==2){
            System.out.println(v.getId()+"\t|"+String.format("%.2f",v.getM())+"\t|"+String.format("%.2f",v.getB())+"\t|"
                +v.getBinario()+"\t|"+String.format("%.2f",v.getZ())+"\t|"+String.format("%.2f",v.getZacumulado())+"\t|"
                +v.getRepetido()); //imprime algunos de los valores del vector
        }else{
            System.out.println(v.getId()+"\t|"+String.format("%.2f",v.getB())+"\t|"
                +v.getBinario()+"\t|"+String.format("%.2f",v.getZ())+"\t|"+String.format("%.2f",v.getZacumulado())+"\t|"
                +v.getRepetido()); //imprime algunos de los valores del vector
        }
    }   
    
    public vector mejorVector(){
        return mejorVectorAnterior;
    }
    
    public double mejorValorM(){
        return mejorVectorAnterior.getM();
    }
    
    public double mejorValorB(){
        return mejorVectorAnterior.getB();
    }
    
    public double mejorValorZ(){
        return mejorVectorAnterior.getZ();
    }
}