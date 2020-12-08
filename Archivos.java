       
import java.util.*;
import java.io.*;

// Clase para el manejo de los archivos con las cantidades a evaluar
public class Archivos{
    protected Archivos archivo = null;
    protected String ruta;

    // Se debe especificar la ruta del archivo en el constructor
    private Archivos(String r){
        ruta = r;
    }

    public static Archivos archivo(String r){
        Archivos aux = new Archivos(r);
        if(aux.archivo == null)
            aux.archivo = aux;
        else
            aux.archivo.ruta = r;
        return aux.archivo;
    }

    private String eliminarEspacios(String cadena){
        cadena = cadena.replaceAll(" ",""); // Elimina los espacios, tab y saltos de línea
        return cadena;
    }
    private ArrayList<String> getLineas(){
        ArrayList<String> lineas = new ArrayList<String>();
        try{ 
            // Se abre el archivo para su lectura
            File archivo = new File(ruta);
            Scanner lector = new Scanner(archivo);
            while(lector.hasNextLine())
                lineas.add(eliminarEspacios(lector.nextLine()));
            lector.close();
        }catch(Exception e){
            AjustePuntos.error(e.getMessage());
        }
        return lineas;
    }

    public ArrayList<Double[]> getTablaValores(){
        ArrayList<Double[]> valores = new ArrayList<Double[]>();     
        for(String linea:getLineas()){
            String[] valoresCadena = linea.split(",");
            Double[] valoresNumero = new Double[valoresCadena.length];

            for(int i=0; i<valoresCadena.length; i++){
                valoresNumero[i]=Double.parseDouble(valoresCadena[i]); 
            }
            valores.add(valoresNumero);
        }
        return valores;
    }
}