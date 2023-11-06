package org.example;

// anadir .jar mysql-connector
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;

public class Conexion {
    String bd="ventasbd";
    String url= "jdbc:mysql://localhost:3306/";
    String usuario="root";
    String contrasena="6603";
    String driver ="com.mysql.cj.jdbc.Driver";
    Connection cx;

    //Constructor
    public Conexion(){
    }
    //Conectar a la base de datos
    public Connection conectar(){
        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(url+bd, usuario,contrasena);
            System.out.println("Se pudo conectar a la base de datos "+bd);
            return cx;
        } catch (ClassNotFoundException | SQLException ex){
            System.out.println("No se pudo conectar a la base de datos "+bd);
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx;
    }

    //Desconectar a la base de datos
    public Connection desconectar(){
        try {
            cx.close();
            return cx;
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cx;
    }

}

class ConsultaSql {
    static Conexion conex = new Conexion();
    static Connection conet;
    static Statement st;
    static ResultSet rs;
    int idc;


    public static void consultar(){
        conet = conex.conectar();
        String sql = "select * from factura";
        try{
            st = conet.createStatement();
            rs = st.executeQuery(sql);
            Object[] fact = new Object[4];

            while(rs.next()){
                fact[0] = rs.getInt("id");
                fact[1] = rs.getString("nombre");
                fact[2] = rs.getString("nombreprod");
                fact[3] = rs.getDate("fecha");
                System.out.println("Factura no: " + fact[0]);
                System.out.println("Nombre cli: " + fact[1]);
                System.out.println("Nombre producto: " + fact[2]);
                System.out.println("Fecha: " + fact[3].toString());
                System.out.println("-------------------------");
            }

        }catch(SQLException e){
            System.out.println(e);
        }
        conet = conex.desconectar();
    }

    public static boolean consultar_id_repetido(int id) throws SQLException {
        conet = conex.conectar();
        String sql = "select * from factura where id="+id;
        try{
            st = conet.createStatement();
            rs = st.executeQuery(sql);
        }catch(SQLException e){
            System.out.println(e);
        }
        conet = conex.desconectar();
        String resultado=rs.toString();
        if (resultado.contains("-1")){
            return true;
        }else {
            return false;
        }
    }

    public static void anadirElemento(DatoSql dato){
        conet = conex.conectar();
        String sql = "INSERT INTO factura (id, nombre, correo, dni, idprod, nombreprod, uni_med, cantidad, fecha, importe) VALUES (\'"+dato.getId()+"\', \'"+dato.getNombre()+"\', \'"+dato.getCorreo()+"\', \'"+dato.getDni()+"\', \'"+dato.getIdprod()+"\', \'"+dato.getNombreprod()+"\', \'"+dato.getUni_med()+"\', \'"+dato.getCantidad()+"\', \'"+dato.getFecha().toString()+"\', \'"+dato.getImporte()+"\');";
        try{
            st = conet.createStatement();
            st.executeUpdate(sql);
            System.out.println("Se ha agregado el elemento correctamente");

        }catch(SQLException e){
            System.out.println(e);
        }
        conet = conex.desconectar();
    }
    public static void borrarElemento(DatoSql dato){
        conet = conex.conectar();
        String sql = "DELETE FROM factura WHERE id= \'"+dato.getId()+"\';";
        try{
            st = conet.createStatement();
            st.executeUpdate(sql);
            System.out.println("Se ha eliminado el elemento correctamente");

        }catch(SQLException e){
            System.out.println(e);
        }
        conet = conex.desconectar();
    }
}
class DatoSql {
    private int id;
    private String nombre;
    private String correo;
    private String dni;
    private int idprod;
    private String uni_med;
    private int cantidad;
    private float importe;
    private Date fecha;
    private String nombreprod;

    public DatoSql(){
    }
    public DatoSql(int id,String nombre,String correo,String dni, int idprod, String nombreprod,String uni_med,int cantidad,LocalDate fechaa, float importe) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.dni = dni;
        this.idprod = idprod;
        this.uni_med = uni_med;
        this.cantidad = cantidad;
        this.fecha = Date.valueOf(fechaa);
        this.importe = importe;
        this.nombreprod = nombreprod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public int getIdprod() {
        return idprod;
    }

    public void setIdprod(int idprod) {
        this.idprod = idprod;
    }

    public String getUni_med() {
        return uni_med;
    }

    public void setUni_med(String uni_med) {
        this.uni_med = uni_med;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    public float getImporte() {
        return importe;
    }

    public void setImporte(float importe) {
        this.importe = importe;
    }

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombreprod() {
        return nombreprod;
    }
    public void setNombreprod(String nombreprod) {
        this.nombreprod = nombreprod;
    }
}