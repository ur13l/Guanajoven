package mx.gob.jovenes.guanajuato.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ichema on 5/18/16.
 */
public class Perfil {
    private final static String ID = "perfil_id";
    public final static String NOMBRE_COMPLETO = "perfil_nombre_completo";
    public final static String GENERO = "perfil_genero";
    public final static String FECHA = "perfil_fecha";
    public final static String OCUPACION = "perfil_ocupacion";
    public final static String CODIGO_POSTAL= "perfil_codigo_postal";
    public final static String TELEFONO = "perfil_telefono";
    public final static String PESO = "perfil_peso";
    public final static String ESTATURA = "perfil_estatura";
    public final static String PRESION = "perfil_presion";
    public final static String GLUCOSA = "perfil_glucosa";
    public final static String LESION = "perfil_lesion";
    public final static String ACTIVIDAD = "perfil_actividad";
    public final static String SUCCESS = "perfil_success";
    private int id;
    private String nombre_completo;
    private Integer genero;
    private String fecha;
    private Integer ocupacion;
    private Integer codigo_postal;
    private String telefono;
    private float peso;
    private float estatura;
    private Integer presion;
    private Integer glucosa;
    private Integer lesion;
    private Integer actividad;
    private String success;
    private static SharedPreferences prefs;

    public Perfil(Context ctx){
        prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public int getID() {
        int id = prefs.getInt(ID,0);
        return id;
    }

    private void setID(int id){
        prefs.edit().putInt(ID, id).commit();
        this.id = id;
    }

    public String getNombreCompleto() {
        String nombre_completo = prefs.getString(NOMBRE_COMPLETO,"");
        return nombre_completo;
    }

    public void setNombreCompleto(String nombre_completo) {
        prefs.edit().putString(NOMBRE_COMPLETO, nombre_completo).commit();
        this.nombre_completo = nombre_completo;
    }

    public int getGenero() {
        int genero = prefs.getInt(GENERO, 0);
        return genero;
    }

    public void setGenero(Integer genero){
        if(genero == null){
            prefs.edit().remove(GENERO).commit();
        }
        else {
            prefs.edit().putInt(GENERO, genero).commit();
        }
        this.genero = genero;
    }

    public String getFecha() {
        String fecha = prefs.getString(FECHA,"");
        return fecha;
    }

    public void setFecha(String fecha) {
        prefs.edit().putString(FECHA, fecha).commit();
        this.fecha = fecha;
    }

    public int getOcupacion() {
        int ocupacion = prefs.getInt(OCUPACION, 0);
        return ocupacion;
    }

    public void setOcupacion(Integer ocupacion){
        if(ocupacion == null){
            prefs.edit().remove(OCUPACION).commit();
        }
        else
            prefs.edit().putInt(OCUPACION, ocupacion).commit();
        this.ocupacion = ocupacion;
    }

    public int getCodigo_postal() {
        int codigo_postal = prefs.getInt(CODIGO_POSTAL, 0);
        return codigo_postal;
    }

    public void setCodigo_postal(Integer codigo_postal){
        if(codigo_postal == null){
            prefs.edit().remove(CODIGO_POSTAL).commit();
        }
        else
            prefs.edit().putInt(CODIGO_POSTAL, codigo_postal).commit();
        this.codigo_postal = codigo_postal;
    }

    public String getTelefono() {
        String telefono = prefs.getString(TELEFONO,"");
        return telefono;
    }

    public void setTelefono(String telefono) {
        prefs.edit().putString(TELEFONO, telefono).commit();
        this.telefono = telefono;
    }

    public float getPeso() {
        float peso = prefs.getFloat(PESO,0);
        return peso;
    }

    public void setPeso(float peso) {
        prefs.edit().putFloat(PESO, peso).commit();
        this.peso = peso;
    }

    public float getEstatura() {
        float estatura = prefs.getFloat(ESTATURA,0);
        return estatura;
    }

    public void setEstatura(float estatura) {
        prefs.edit().putFloat(ESTATURA, estatura).commit();
        this.estatura = estatura;
    }

    public Integer getPresion(){
        Integer presion = prefs.getInt(PRESION,0);
        return presion;
    }

    public void setPresion(Integer presion){
        prefs.edit().putInt(PRESION, presion).commit();
        this.presion = presion;
    }

    public Integer getGlucosa(){
        Integer glucosa = prefs.getInt(GLUCOSA,0);
        return glucosa;
    }

    public void setGlucosa(Integer glucosa){
        prefs.edit().putInt(GLUCOSA, glucosa).commit();
        this.glucosa = glucosa;
    }

    public Integer getActividad(){
        Integer actividad = prefs.getInt(ACTIVIDAD,0);
        return actividad;
    }

    public void setActividad(Integer actividad){
        prefs.edit().putInt(ACTIVIDAD, actividad).commit();
        this.actividad = actividad;
    }

    public Integer getLesion(){
        Integer lesion = prefs.getInt(LESION,0);
        return lesion;
    }

    public void setLesion(Integer lesion){
        prefs.edit().putInt(LESION, lesion).commit();
        this.lesion = lesion;
    }

    public String getSuccess() {
        String success = prefs.getString(SUCCESS,"");
        return success;
    }

    public void setSuccess(String success) {
        prefs.edit().putString(SUCCESS, success).commit();
        this.success = success;
    }

    public static void borrarPerfil(){
        prefs.edit().remove(ID).commit();
        prefs.edit().remove(NOMBRE_COMPLETO).commit();
        prefs.edit().remove(GENERO).commit();
        prefs.edit().remove(FECHA).commit();
        prefs.edit().remove(OCUPACION).commit();
    }
}
