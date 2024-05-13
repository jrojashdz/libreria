/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libreria.util;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import libreria.model.Libros;

/**
 *
 * @author Yeoshua
 */
public class Util {

    public DefaultTableModel addToTable(List<Libros> libros) {
        String[] columnas = {"Código", "Nombre", "Autor","Editorial"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{},columnas);
        Object rowData[] = new Object[4];
        for(Libros libro: libros) {
            rowData[0] = libro.getCodigo();
            rowData[1] = libro.getNombre();
            rowData[2] = libro.getAutor();
            rowData[3] = libro.getEditorial();
            model.addRow(rowData);
        }
       return model; 
    }
}
