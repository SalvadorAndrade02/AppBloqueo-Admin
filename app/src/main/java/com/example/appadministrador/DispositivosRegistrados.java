package com.example.appadministrador;

public class DispositivosRegistrados {
    //Datos del telefono
    private String dispositivoId;
    private String userId;
    private String IMEI;
    private String marcaTelefono;
    private String Precio;

    //---Datos del cliente---
    private String nombreCliente;
    private String domicilio;
    private String edad;

    //---Datos de pago---

    private String periodoPago;
    private String pagoSemanal;
    private String semanasTotal;
    private String montoTotal;

    //---Estado---
    private String estado;

    public DispositivosRegistrados() {
    }

    public DispositivosRegistrados(String dispositivoId, String userId, String IMEI, String marcaTelefono, String precio, String nombreCliente, String domicilio, String edad, String periodoPago, String pagoSemanal, String semanasTotal, String montoTotal, String estado) {
        this.dispositivoId = dispositivoId;
        this.userId = userId;
        this.IMEI = IMEI;
        this.marcaTelefono = marcaTelefono;
        Precio = precio;
        this.nombreCliente = nombreCliente;
        this.domicilio = domicilio;
        this.edad = edad;
        this.periodoPago = periodoPago;
        this.pagoSemanal = pagoSemanal;
        this.semanasTotal = semanasTotal;
        this.montoTotal = montoTotal;
        this.estado = estado;
    }

    public String getIMEI() {
        return IMEI;
    }

    public String getMarcaTelefono() {
        return marcaTelefono;
    }

    public String getPrecio() {
        return Precio;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public String getEdad() {
        return edad;
    }

    public String getPeriodoPago() {
        return periodoPago;
    }

    public String getPagoSemanal() {
        return pagoSemanal;
    }

    public String getSemanasTotal() {
        return semanasTotal;
    }

    public String getMontoTotal() {
        return montoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public void setMarcaTelefono(String marcaTelefono) {
        this.marcaTelefono = marcaTelefono;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setPeriodoPago(String periodoPago) {
        this.periodoPago = periodoPago;
    }

    public void setPagoSemanal(String pagoSemanal) {
        this.pagoSemanal = pagoSemanal;
    }

    public void setSemanasTotal(String semanasTotal) {
        this.semanasTotal = semanasTotal;
    }

    public void setMontoTotal(String montoTotal) {
        this.montoTotal = montoTotal;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDispositivoId() {
        return dispositivoId;
    }

    public void setDispositivoId(String dispositivoId) {
        this.dispositivoId = dispositivoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

