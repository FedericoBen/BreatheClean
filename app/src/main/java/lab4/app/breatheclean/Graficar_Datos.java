package lab4.app.breatheclean;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Graficar_Datos extends AppCompatActivity {


    private TextView nFechaIni, nFechaFin;
    private TextView nHoraIni, nHoraFin;


    private Button Consultar, MP, CO2;

    private String FI = "";
    private String FF = "";
    private String horaIniSeleccionada;
    private String minIniSeleccionada;
    private String horaFinSeleccionada;
    private String minFinSeleccionada;

    private int iYear, iMonth, iDay;
    private int ano;
    private int mes;
    private int dia;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficar__datos);


        context = this;


        nFechaIni = findViewById(R.id.tv_FechaInicio);
        nFechaFin = findViewById(R.id.tv_FechaFin);

        nHoraIni = findViewById(R.id.tv_HoraInicio);
        nHoraFin = findViewById(R.id.tv_HoraFin);

        Consultar = (Button) findViewById(R.id.button);
        MP = (Button) findViewById(R.id.btn_Mp);
        CO2 = (Button) findViewById(R.id.btn_Co2);

        MP.setVisibility(View.INVISIBLE);
        CO2.setVisibility(View.INVISIBLE);



        nHoraIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!nFechaIni.getText().equals("")&&!nFechaFin.getText().equals("")){
                    nHoraIni.setText("");
                    SelecHoraIni();
                }else{
                    Toast.makeText(context, "Se debe ingresar un rango de fechas primero", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nHoraIni.getText().equals("")) {
                    SelecHoraFin();
                }else{
                    Toast.makeText(context, "Se debe ingresar una hora inicial primero", Toast.LENGTH_SHORT).show();
                }
            }
        });


        nFechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelecFechaIni();
            }
        });
        nFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelecFechaFin();
            }
        });


        Consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nFechaIni.getText().equals("") && !nFechaFin.getText().equals("")) {
                    CO2 co2 = new CO2();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    final Bundle bundle = new Bundle();
                    bundle.putString("fechaInicialCo2", nFechaIni.getText().toString());
                    bundle.putString("fechaFinalCo2", nFechaFin.getText().toString());
                    bundle.putString("horaInicialCo2", nHoraIni.getText().toString());
                    bundle.putString("horaFinalCo2", nHoraFin.getText().toString());
                    co2.setArguments(bundle);
                    transaction.replace(R.id.frg_Contenedor, co2);
                    transaction.commit();
                    Toast.makeText(context, "Leyendo valores", Toast.LENGTH_SHORT).show();
                    MP.setVisibility(View.VISIBLE);
                    CO2.setVisibility(View.VISIBLE);
                    CO2.setBackgroundColor(Color.TRANSPARENT);
                    CO2.setClickable(false);
                    MP.setBackgroundColor(Color.GRAY);
                }else{
                    Toast.makeText(context, "Debe introducior un rango de fechas primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if (!nFechaIni.getText().equals("") && !nFechaFin.getText().equals("")) {

                MatPart matParticulado = new MatPart();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                final Bundle bundle = new Bundle();
                bundle.putString("fechaInicialMp", nFechaIni.getText().toString());
                bundle.putString("fechaFinalMp", nFechaFin.getText().toString());
                bundle.putString("horaInicialCo2", nHoraIni.getText().toString());
                bundle.putString("horaFinalCo2", nHoraFin.getText().toString());
                matParticulado.setArguments(bundle);
                transaction.replace(R.id.frg_Contenedor, matParticulado);
                transaction.commit();
                Toast.makeText(context, "Leyendo valores", Toast.LENGTH_SHORT).show();
                MP.setVisibility(View.VISIBLE);
                CO2.setVisibility(View.VISIBLE);
                MP.setClickable(false);
                CO2.setClickable(true);
                CO2.setBackgroundColor(Color.GRAY);
                MP.setBackgroundColor(Color.TRANSPARENT);

            }else{
                Toast.makeText(context, "Debe introducior un rango de fechas primero", Toast.LENGTH_SHORT).show();
            }
            }
        });

        CO2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nFechaIni.getText().equals("") && !nFechaFin.getText().equals("")) {
                    CO2 co2 = new CO2();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    final Bundle bundle = new Bundle();
                    bundle.putString("fechaInicialCo2", nFechaIni.getText().toString());
                    bundle.putString("fechaFinalCo2", nFechaFin.getText().toString());
                    bundle.putString("horaInicialCo2", nHoraIni.getText().toString());
                    bundle.putString("horaFinalCo2", nHoraFin.getText().toString());
                    co2.setArguments(bundle);
                    transaction.replace(R.id.frg_Contenedor, co2);
                    transaction.commit();
                    Toast.makeText(context, "Leyendo valores", Toast.LENGTH_SHORT).show();
                    MP.setVisibility(View.VISIBLE);
                    CO2.setVisibility(View.VISIBLE);
                    MP.setClickable(true);
                    CO2.setClickable(false);
                    CO2.setBackgroundColor(Color.TRANSPARENT);
                    MP.setBackgroundColor(Color.GRAY);
                }else{
                    Toast.makeText(context, "Debe introducior un rango de fechas primero", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //  Se selecciona la fecha inicial
    public void SelecFechaIni(){


        final Dialog dialogIni = new Dialog(context);
        dialogIni.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogIni.setCancelable(true);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogIni.setContentView(R.layout.seleccion_fechas_ini);

        CalendarView fechaIni = dialogIni.findViewById(R.id.calendarViewIni);
        final Button acepatarFrchaIni = dialogIni.findViewById(R.id.btn_fechaIni);
        final TextView infoFechaIni = dialogIni.findViewById(R.id.tv_infoFechaIni);

        acepatarFrchaIni.setVisibility(View.INVISIBLE);

        final Calendar c = Calendar.getInstance();
        ano = c.get(Calendar.YEAR);
        mes = c.get(Calendar.MONTH);
        dia = c.get(Calendar.DAY_OF_MONTH);

        fechaIni.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                if(ano>=year && mes>=month && dia>=dayOfMonth) {
                    iYear=year;
                    iMonth=month;
                    iDay=dayOfMonth;
                    FI = dayOfMonth + "-" + (month + 1) + "-" + year;
                    acepatarFrchaIni.setVisibility(View.VISIBLE);
                    infoFechaIni.setVisibility(View.INVISIBLE);
                }else if(ano>= year && mes>month) {
                    iYear=year;
                    iMonth=month;
                    iDay=dayOfMonth;
                    FI = dayOfMonth + "-" + (month + 1) + "-" + year;
                    acepatarFrchaIni.setVisibility(View.VISIBLE);
                    infoFechaIni.setVisibility(View.INVISIBLE);
                }else if(ano> year) {
                    iYear=year;
                    iMonth=month;
                    iDay=dayOfMonth;
                    FI = dayOfMonth + "-" + (month + 1) + "-" + year;
                    acepatarFrchaIni.setVisibility(View.VISIBLE);
                    infoFechaIni.setVisibility(View.INVISIBLE);
                }else {
                    acepatarFrchaIni.setVisibility(View.INVISIBLE);
                    infoFechaIni.setVisibility(View.VISIBLE);
                    Toast.makeText(Graficar_Datos.this, "Este dia aun no se han tomado medidas ", Toast.LENGTH_SHORT).show();
                }

            }
        });


        acepatarFrchaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!FI.equals("")) {
                    nFechaIni.setText(FI);
                    dialogIni.dismiss();
                }else{
                    Toast.makeText(context, "Debe seleccionar una fecha primero", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialogIni.show();


    }

    //  Se selecciona le fecha final
    public void SelecFechaFin(){


        if(nFechaIni.getText().toString().equals("")){
            Toast.makeText(context, "Debe seleccionar primero una fecha de inicio", Toast.LENGTH_SHORT).show();
        }else {
            //new SeleccionFechaFin(context, Graficar_Datos.this);
            final Dialog dialogFin = new Dialog(context);
            dialogFin.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogFin.setCancelable(true);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogFin.setContentView(R.layout.seleccion_fechas_fin);

            CalendarView fechaFin = dialogFin.findViewById(R.id.calendarViewFin);
            final Button acepatarFrchaFin = dialogFin.findViewById(R.id.btn_fechaFin);
            final TextView infoFechaFin = dialogFin.findViewById(R.id.tv_infoFechaFin);

            acepatarFrchaFin.setVisibility(View.INVISIBLE);

            final Calendar c = Calendar.getInstance();

            ano = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);


            //Se selecciona la fecha en el calendario
            fechaFin.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                    if(iYear<=year && iMonth<=month && iDay<=dayOfMonth) {

                        if(ano>=year && mes>=month && dia>=dayOfMonth) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = dayOfMonth + "-" + (month + 1) + "-" + year;
                        }else if(ano>= year && mes>month) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = dayOfMonth + "-" + (month + 1) + "-" + year;
                        }else if(ano> year) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = dayOfMonth + "-" + (month + 1) + "-" + year;
                        }else {
                            acepatarFrchaFin.setVisibility(View.INVISIBLE);
                            infoFechaFin.setVisibility(View.VISIBLE);
                            Toast.makeText(Graficar_Datos.this, "Este dia aun no se han tomado medidas ", Toast.LENGTH_SHORT).show();
                        }

                    }else if(iYear<= year && iMonth<month) {
                        if(ano>=year && mes>=month && dia>=dayOfMonth) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = dayOfMonth + "-" + (month + 1) + "-" + year;
                        }else if(ano>= year && mes>month) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = dayOfMonth + "-" + (month + 1) + "-" + year;
                        }else if(ano> year) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = dayOfMonth + "-" + (month + 1) + "-" + year;
                        }else {
                            acepatarFrchaFin.setVisibility(View.INVISIBLE);
                            infoFechaFin.setVisibility(View.VISIBLE);
                            Toast.makeText(Graficar_Datos.this, "Este dia aun no se han tomado medidas ", Toast.LENGTH_SHORT).show();
                        }
                    }else if(iYear< year) {
                        if(ano>=year && mes>=month && dia>=dayOfMonth) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = year + "-" + (month + 1) + "-" + dayOfMonth;;
                        }else if(ano>= year && mes>month) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = year + "-" + (month + 1) + "-" + dayOfMonth;;
                        }else if(ano> year) {
                            infoFechaFin.setVisibility(View.INVISIBLE);
                            acepatarFrchaFin.setVisibility(View.VISIBLE);
                            FF = year + "-" + (month + 1) + "-" + dayOfMonth;;
                        }else {
                            acepatarFrchaFin.setVisibility(View.INVISIBLE);
                            infoFechaFin.setVisibility(View.VISIBLE);
                            Toast.makeText(Graficar_Datos.this, "Este dia aun no se han tomado medidas ", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        acepatarFrchaFin.setVisibility(View.INVISIBLE);
                        FF="";
                        Toast.makeText(Graficar_Datos.this, "La fecha final no puede ser menor que la inicial ", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            acepatarFrchaFin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nFechaFin.setText(FF.toString());
                    dialogFin.dismiss();
                }
            });

            dialogFin.show();

        }

    }

    public void SelecHoraIni(){
        final Dialog dialogIni = new Dialog(context);
        dialogIni.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogIni.setCancelable(true);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogIni.setContentView(R.layout.seleccion_hora_ini);

        final Spinner Horas;
        final Spinner Minutos;
        final Button Aceptar;


        Horas =  dialogIni.findViewById(R.id.sp_Horas);
        Minutos =  dialogIni.findViewById(R.id.sp_Minutos);
        Aceptar = dialogIni.findViewById(R.id.btn_horaIni);


        //Aqui se agrgann las horas a el espiner correspondiente
        ArrayList<String> horas = new ArrayList<String>();
        for(int i=1;i<=24;i++){
            horas.add(""+i);
        }

        //Aqui se agrgann los min a el espiner correspondiente
        final ArrayList<String> minutos = new ArrayList<String>();
        for(int i=0;i<=59;i++){
            minutos.add(""+i);
        }

        ArrayAdapter adpHoras = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, horas);
        Horas.setAdapter(adpHoras);

        ArrayAdapter adpMinutos = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, minutos);
        Minutos.setAdapter(adpMinutos);


        Horas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaIniSeleccionada = (String) Horas.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Minutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minIniSeleccionada = (String) Minutos.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nHoraIni.setText(horaIniSeleccionada+":"+minIniSeleccionada);
                dialogIni.dismiss();
            }
        });

        dialogIni.show();
    }

    public void SelecHoraFin(){
        final Dialog dialogIni = new Dialog(context);
        dialogIni.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogIni.setCancelable(true);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogIni.setContentView(R.layout.seleccion_hora_fin);

        final Spinner Horas;
        final Spinner Minutos;
        final Button Aceptar;


        Horas =  dialogIni.findViewById(R.id.sp_Horas);
        Minutos =  dialogIni.findViewById(R.id.sp_Minutos);
        Aceptar = dialogIni.findViewById(R.id.btn_horaIni);


        //Aqui se agrgann las horas a el espiner correspondiente
        ArrayList<String> horas = new ArrayList<String>();
        for(int i=1;i<=24;i++){
            horas.add(""+i);
        }

        //Aqui se agrgann los min a el espiner correspondiente
        final ArrayList<String> minutos = new ArrayList<String>();
        for(int i=0;i<=59;i++){
            minutos.add(""+i);
        }

        ArrayAdapter adpHoras = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, horas);
        Horas.setAdapter(adpHoras);

        ArrayAdapter adpMinutos = new ArrayAdapter(context,android.R.layout.simple_spinner_dropdown_item, minutos);
        Minutos.setAdapter(adpMinutos);


        Horas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                horaFinSeleccionada = (String) Horas.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Minutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minFinSeleccionada = (String) Minutos.getAdapter().getItem(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyy");
        Date fechaDateIni = null;
        Date fechaDateFin = null;
        Date compare = null;

        try {
            fechaDateIni = formato.parse((String) nFechaIni.getText());
            fechaDateFin = formato.parse((String) nFechaFin.getText());
        } catch (ParseException ex) {
            System.out.println(ex);
        }

        if(fechaDateIni.equals(fechaDateFin)){

            float HoraIniTotal=Float.parseFloat(horaIniSeleccionada)*60+Integer.parseInt(minIniSeleccionada);
            float HoraFinTotal=Float.parseFloat(horaFinSeleccionada)*60+Integer.parseInt(minFinSeleccionada);

            if(HoraIniTotal>HoraFinTotal || HoraIniTotal==HoraFinTotal) {
                Toast.makeText(context, "Es te rango de horas no es valido", Toast.LENGTH_SHORT).show();
            }else{


                        nHoraFin.setText(horaFinSeleccionada + ":" + minFinSeleccionada);
                        dialogIni.dismiss();

            }


        }else {

                    nHoraFin.setText(horaFinSeleccionada + ":" + minFinSeleccionada);
                    dialogIni.dismiss();

        }
            }
        });
        dialogIni.show();
    }

}


