package mx.gob.jovenes.guanajuato.model;

/**
 * Created by Uriel on 19/02/2016.
 */
public class Alimento {
    private long id;
    private String nombre;
    private String descripcion;
    private float cantidad;
    private float gramos;
    private float calorias;
    private float eqFrutas;
    private float eqVerduras;
    private float eqPOA;
    private float eqCerealesTuber;
    private float eqLeguminosas;
    private float eqLacteos;
    private float eqAceites;
    private float eqAzucares;
    private double grasas;
    private double proteinas;
    private double carbohidratos;
    private String foto1;
    private String foto2;
    private char estado;
    private char platillo;
    private GrupoAlimento grupoAlimento;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public float getGramos() {
        return gramos;
    }

    public void setGramos(float gramos) {
        this.gramos = gramos;
    }

    public float getCalorias() {
        return calorias;
    }

    public void setCalorias(float calorias) {
        this.calorias = calorias;
    }

    public float getEqFrutas() {
        return eqFrutas;
    }

    public void setEqFrutas(float eqFrutas) {
        this.eqFrutas = eqFrutas;
    }

    public float getEqVerduras() {
        return eqVerduras;
    }

    public void setEqVerduras(float eqVerduras) {
        this.eqVerduras = eqVerduras;
    }

    public float getEqPOA() {
        return eqPOA;
    }

    public void setEqPOA(float eqPOA) {
        this.eqPOA = eqPOA;
    }

    public float getEqCerealesTuber() {
        return eqCerealesTuber;
    }

    public void setEqCerealesTuber(float eqCerealesTuber) {
        this.eqCerealesTuber = eqCerealesTuber;
    }

    public float getEqLeguminosas() {
        return eqLeguminosas;
    }

    public void setEqLeguminosas(float eqLeguminosas) {
        this.eqLeguminosas = eqLeguminosas;
    }

    public float getEqLacteos() {
        return eqLacteos;
    }

    public void setEqLacteos(float eqLacteos) {
        this.eqLacteos = eqLacteos;
    }

    public float getEqAceites() {
        return eqAceites;
    }

    public void setEqAceites(float eqAceites) {
        this.eqAceites = eqAceites;
    }

    public float getEqAzucares() {
        return eqAzucares;
    }

    public void setEqAzucares(float eqAzucares) {
        this.eqAzucares = eqAzucares;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public String getFoto1() {
        return foto1;
    }

    public void setFoto1(String foto1) {
        this.foto1 = foto1;
    }

    public String getFoto2() {
        return foto2;
    }

    public void setFoto2(String foto2) {
        this.foto2 = foto2;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public char getPlatillo() {
        return platillo;
    }

    public void setPlatillo(char platillo) {
        this.platillo = platillo;
    }

    public GrupoAlimento getGrupoAlimento() {
        return grupoAlimento;
    }

    public void setGrupoAlimento(GrupoAlimento grupoAlimento) {
        this.grupoAlimento = grupoAlimento;
    }



}
