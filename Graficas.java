import java.util.*;
import javax.swing.*;
import java.awt.*;

// Librerías de graficación
import org.jfree.chart.*;
import org.jfree.data.xy.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.*;

// Clase encargada de generar la UI y las gráficas
public class Graficas{
    protected JFrame ventana;
    protected JPanel panel;

    protected JFreeChart grafica;
    protected ArrayList<XYSeriesCollection> conjuntoColecciones;


    public Graficas(int filas, int columnas){
        conjuntoColecciones = new ArrayList<XYSeriesCollection>();

        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1000,540);

        panel = new JPanel();
        panel.setLayout(new GridLayout(filas,columnas));
        ventana.add(panel);
    }

    public void mostrarGrafica(){
        ventana.setVisible(true);
    }

    // Crea el panel con los cuadrantes que contiene a las gráficas determinadas en el conjunto de XYSeresCollection
    public void crearGrafica(String titulo, String etiquetaX, String etiquetaY, int indiceConjuntoColecciones){
        grafica = ChartFactory.createXYLineChart(
            titulo,
            etiquetaX,
            etiquetaY,
            conjuntoColecciones.get(indiceConjuntoColecciones),
            PlotOrientation.VERTICAL,
            true, true, false
            );

        ChartPanel panelGrafica = new ChartPanel(grafica);
        panelGrafica.setPreferredSize(new java.awt.Dimension(500,500));

        XYPlot graficaPuntos = grafica.getXYPlot( );
      
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer( );
        renderer.setSeriesPaint( 0 , Color.RED );
        renderer.setSeriesPaint( 1 , Color.BLUE );
        renderer.setSeriesStroke( 0 , new BasicStroke( 4.0f ) );
        renderer.setSeriesStroke( 1 , new BasicStroke( 3.0f ) );
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        graficaPuntos.setRenderer( renderer ); 

        panel.add(panelGrafica);
    }

    // Permite agregar los pares ordenados a una serie que se guardará en una colección con índice pasado por parámetro
    // En el caso de que el índice sea -1 se crea una nueva colección y se retorna el ínidice de la colección en el ArrayList de XYSeriesCollection
    public int agregarParOrdenado(String nombreGrafo, double x, double y, int indiceConjuntoColecciones){
        XYSeriesCollection coleccion ;

        if(indiceConjuntoColecciones == -1){ // Se agrega el par ordenado a una XYSeries nueva que se agrega a una colección de series también nueva
            XYSeries puntosOrdenados = new XYSeries(nombreGrafo);
            coleccion = new XYSeriesCollection(puntosOrdenados);

            puntosOrdenados.add(x,y);

            conjuntoColecciones.add(coleccion);
            return conjuntoColecciones.size()-1;

        }else{ // Se agrega el par ordenado a una serie con colección existente
            coleccion = conjuntoColecciones.get(indiceConjuntoColecciones);

            if(coleccion.getSeriesIndex(nombreGrafo) != -1){
                coleccion.getSeries(nombreGrafo).add(x,y);

            }else{ // Se agrega una nueva XYSerie en una colección de series ya existente
                XYSeries puntosOrdenados = new XYSeries(nombreGrafo);
                puntosOrdenados.add(x,y);

                coleccion.addSeries(puntosOrdenados);
            }
        }
        return indiceConjuntoColecciones;
    }

    public int agregarPunto(String nombreGrafo, double x, double y, int indiceConjuntoColecciones){
        XYSeriesCollection coleccion ;

        if(indiceConjuntoColecciones == -1){ // Se agrega el par ordenado a una XYSeries nueva que se agrega a una colección de series también nueva
            XYSeries puntosOrdenados = new XYSeries(nombreGrafo);
            coleccion = new XYSeriesCollection(puntosOrdenados);

            puntosOrdenados.add(x,y);

            conjuntoColecciones.add(coleccion);
            return conjuntoColecciones.size()-1;

        }else{ // Se agrega el par ordenado a una serie con colección existente
            coleccion = conjuntoColecciones.get(indiceConjuntoColecciones);

            if(coleccion.getSeriesIndex(nombreGrafo) != -1){
                coleccion.getSeries(nombreGrafo).add(x,y);

            }else{ // Se agrega una nueva XYSerie en una colección de series ya existente
                XYSeries puntosOrdenados = new XYSeries(nombreGrafo);
                puntosOrdenados.add(x,y);

                coleccion.addSeries(puntosOrdenados);
            }
        }
        return indiceConjuntoColecciones;
    }

}