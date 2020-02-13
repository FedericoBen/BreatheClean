package lab4.app.breatheclean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DatosMedidos implements Comparator<DatosMedidos> {

    private String CO2, Fecha, Hora, ppm25, ppm1, ppm10;

    public String getCO2() {
        return CO2;
    }

    public void setCO2(String CO2) {
        this.CO2 = CO2;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getPpm25() {
        return ppm25;
    }

    public void setPpm25(String ppm25) {
        this.ppm25 = ppm25;
    }

    public String getPpm1() {
        return ppm1;
    }

    public void setPpm1(String ppm1) {
        this.ppm1 = ppm1;
    }

    public String getPpm10() {
        return ppm10;
    }

    public void setPpm10(String ppm10) {
        this.ppm10 = ppm10;
    }

    @Override
    public int compare(DatosMedidos o1, DatosMedidos o2) {
        SimpleDateFormat formato = new SimpleDateFormat("HH:mm:ss");
        Date fechaDate1 = null;
        Date fechaDate2 = null;

        try {
            fechaDate1 = formato.parse(o1.getHora());
            fechaDate2 = formato.parse(o2.getHora());
        }
        catch (ParseException ex)
        {
            System.out.println(ex);
        }

        if(fechaDate1.before(fechaDate2)){
            return -1;
        }
        if(fechaDate1.after(fechaDate2)){
            return  0;
        }else{
            return  1;
        }
    }
}
