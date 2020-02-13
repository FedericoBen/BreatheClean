package lab4.app.breatheclean;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;


public class CO2 extends Fragment {


    private BarChart barChart;
    private TextView nMedidaInstan;
    private Spinner fecharOrden;
    private String nFechaIni ;
    private String nFechaFin ;
    private String nHoraIni;
    private String nHoraFin;
    private int iYear,iMonth,iDay;
    private int ano;
    private int mes;
    private int dia;
    private ArrayList<DatosMedidos> datosMedidosOrganizar;

    Context context;
    DatabaseReference reffDatosGenerados;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_co2, container, false);


        context = getContext();
        barChart = (BarChart) view.findViewById(R.id.lineChart);
        nMedidaInstan = (TextView) view.findViewById(R.id.tv_Valoractual);
        fecharOrden =(Spinner) view.findViewById(R.id.sp_Medidas);


        datosMedidosOrganizar = new ArrayList<>();

        //SE OBTIENEN LAS FECHAS Y HORAS QUE SE PASARON DESDE EL ACTIVITY OPRINCIPAL
        try {
            if(getArguments()!=null){
                nFechaIni = getArguments().getString("fechaInicialCo2","no hay nada");
                nFechaFin = getArguments().getString("fechaFinalCo2", "no hay nada");
                nHoraIni = getArguments().getString("horaInicialCo2","no hay nada");
                nHoraFin = getArguments().getString("horaFinalCo2", "no hay nada");
            }
        }catch (Exception e){
            Toast.makeText(getContext(), "NO SE TRASMITIERON DATOS", Toast.LENGTH_SHORT).show();
        }


                //GenerarDatos();

                reffDatosGenerados = FirebaseDatabase.getInstance().getReference().child("1");
                reffDatosGenerados.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                            Date fechaDateIni = null;
                            Date fechaDateFin = null;
                            Date fechaComparar = null;

                            try {
                                fechaDateIni = formato.parse(nFechaIni);
                                fechaDateFin = formato.parse(nFechaFin);
                            } catch (ParseException ex) {
                                System.out.println(ex);
                            }


                            for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                                DatosMedidos A = objSnapshot.getValue(DatosMedidos.class);


                                try {
                                    fechaComparar = formato.parse(A.getFecha());
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }

                                String nHoraAnalizar = A.getHora();

                                //DEPURACION DE LA HORA DEL DATO PARA OBTENER HORA Y MINUTOS

                                SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
                                Date HoraInicio = null;
                                Date HoraFin = null;
                                Date HoraAnalizar = null;
                                Date finDia = null;
                                Date iniDia = null;


                                try {
                                    HoraInicio = formatoHora.parse(nHoraIni + ":00");
                                    HoraFin = formatoHora.parse(nHoraFin + ":00");
                                    finDia = formatoHora.parse("24:00:00");
                                    iniDia = formatoHora.parse("00:00:00");
                                    HoraAnalizar = formatoHora.parse(nHoraAnalizar);
                                } catch (ParseException ex) {
                                    System.out.println(ex);
                                }

                                if (fechaDateIni.before(fechaComparar) && fechaDateFin.after(fechaComparar)) {
                                    Toast.makeText(context, "Rango1", Toast.LENGTH_SHORT).show();
                                    if (HoraInicio.after(HoraFin)) {
                                        if ((HoraInicio.before(HoraAnalizar) && finDia.after(HoraAnalizar)) ||
                                                (iniDia.before(HoraAnalizar) && HoraFin.after(HoraAnalizar))) {
                                            datosMedidosOrganizar.add(A);
                                        }
                                    } else {
                                        if (HoraInicio.before(HoraAnalizar) && HoraFin.after(HoraAnalizar)) {
                                            datosMedidosOrganizar.add(A);
                                        }
                                    }
                                }
                                if(fechaDateIni.after(fechaComparar) || fechaDateFin.before(fechaComparar)){
                                    Toast.makeText(context, "En estos dias no se tomaron medidas", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(context, "Rango3", Toast.LENGTH_SHORT).show();
                                    if (HoraInicio.before(HoraAnalizar) && HoraFin.after(HoraAnalizar)) {
                                        datosMedidosOrganizar.add(A);
                                    }
                                }
                                //nMedidaInstan.setText(dataSnapshot.getChildrenCount()+"");
                            }


                            //Se organizan los datos segun su fecha

                            String info = "fecha          |  co2 \n";

                            Collections.sort(datosMedidosOrganizar, new DatosMedidos());
                            /*try {
                                nMedidaInstan.setText(datosMedidosOrganizar.get(0).getCO2()+" PPM");

                            }catch (Exception e){
                                Toast.makeText(context, "no se ha podido leer el ultimo valor", Toast.LENGTH_SHORT).show();
                            }

                             */
                        ArrayList<String> medidas = new ArrayList<String>();



                            for (int i = 0; i < datosMedidosOrganizar.size(); i++) {

                                info = info + datosMedidosOrganizar.get(i).getHora()+"\n";

                                if(datosMedidosOrganizar.get(i).getFecha().toCharArray().length==10) {

                                    medidas.add(datosMedidosOrganizar.get(i).getHora() +" | " +datosMedidosOrganizar.get(i).getFecha() + ": \n" + datosMedidosOrganizar.get(i).getCO2() + "ppm");
                                }
                                if(datosMedidosOrganizar.get(i).getFecha().toCharArray().length==9) {
                                    medidas.add(datosMedidosOrganizar.get(i).getHora() +" | "+datosMedidosOrganizar.get(i).getFecha() + ":   \n" + datosMedidosOrganizar.get(i).getCO2() + "ppm");
                                }
                                if(datosMedidosOrganizar.get(i).getFecha().toCharArray().length==8) {
                                    medidas.add(datosMedidosOrganizar.get(i).getHora() +" | "+datosMedidosOrganizar.get(i).getFecha() + ":     \n" + datosMedidosOrganizar.get(i).getCO2() + "ppm");
                                }

                            }

                        ArrayAdapter adpMedidas = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, medidas);
                        fecharOrden.setAdapter(adpMedidas);

                            Graficador();
                            datosMedidosOrganizar.clear();





                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            return view;

    }

    //Funcion para graficar
    public void Graficador(){

        barChart.clear();
        barChart.notifyDataSetChanged();

        //SE HACE LA RESPÉCTIVA GRAFICA
        float contador = 0;
        ArrayList<String> etiquetas = new ArrayList<>();
        ArrayList<BarEntry> entradas = new ArrayList<>();
        for (DatosMedidos datosMedidos:datosMedidosOrganizar) {
            contador++;
            entradas.add(new BarEntry(contador, Float.parseFloat(datosMedidos.getCO2())));
            etiquetas.add(datosMedidos.getHora());
        }
        BarDataSet barDataSet= new BarDataSet(entradas, "CO2");
        BarData lineData = new BarData();
        lineData.addDataSet(barDataSet);
        lineData.notifyDataChanged();
        barChart.setData(lineData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(etiquetas));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(true);
        xAxis.setAxisMinimum(0);



        //colores
        barDataSet.setValueTextColor(Color.WHITE);
        barDataSet.setValueTextColor(Color.WHITE);
        lineData.setValueTextColor(Color.WHITE);
        lineData.setValueTextSize(10);


        //esto es para cambiar los colores
        barChart.setDrawBorders(true);
        YAxis axisRight = barChart.getAxisRight();
        axisRight.setTextColor(Color.WHITE);
        YAxis axisLeft = barChart.getAxisLeft();
        axisLeft.setTextColor(Color.WHITE);
        XAxis axisTop = barChart.getXAxis();
        axisTop.setTextColor(Color.WHITE);
        onStart();
    }

}
