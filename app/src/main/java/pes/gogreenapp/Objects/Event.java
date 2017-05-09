package pes.gogreenapp.Objects;

import java.util.Date;


public class Event {
    private Integer id;
    private String titulo;
    private String descripcion;
    private Integer puntos;
    private String direccion;
    private String empresa;
    private Date fecha;
    private String hora;
    private Byte[] imagen;

    public Event(Integer id, String titulo, String descripcion, Integer puntos, String direccion,String empresa, Date fecha, String hora, Byte[] imagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.direccion = direccion;
        this.empresa = empresa;
        this.fecha = fecha;
        this.hora = hora;
        this.imagen = imagen;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getPuntos() {
        return puntos;
    }

    public void setPuntos(Integer puntos) {
        this.puntos = puntos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        String[] split = hora.split(":");
        return split[0];
    }

    public String getMin() {
        String[] split = hora.split(":");
        return split[1];
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Byte[] getImagen() {
        return imagen;
    }

    public void setImagen(Byte[] imagen) {
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

