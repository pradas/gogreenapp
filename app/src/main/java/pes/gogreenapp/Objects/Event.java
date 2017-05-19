package pes.gogreenapp.Objects;

import android.util.Base64;

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
    private String min;
    private byte[] imagen;

    public Event(Integer id, String titulo, String descripcion, Integer puntos, String direccion,String empresa, Date fecha, String imagen) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.puntos = puntos;
        this.direccion = direccion;
        this.empresa = empresa;
        this.fecha = fecha;
        this.hora = String.valueOf(fecha.getHours());
        this.min = String.valueOf(fecha.getMinutes());
        if (imagen != null) {
            this.imagen = Base64.decode(imagen, 0);
        }

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

    public String getHora() { return this.hora; }

    public String getMin() { return this.min; }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}

