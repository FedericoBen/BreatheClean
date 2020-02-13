package lab4.app.breatheclean;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

public class SeleccionFecha {

    public interface FechaSeleccionada {
        void fechaSelecIni(String fechaIni);
    }

    private FechaSeleccionada interfaz;

    private String fechaIni = "";



    public SeleccionFecha(Context context, FechaSeleccionada actividad) {
        interfaz = actividad;

        final Dialog dialogIni = new Dialog(context);
        dialogIni.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogIni.setCancelable(true);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogIni.setContentView(R.layout.seleccion_fechas_ini);

        CalendarView fechaIni = dialogIni.findViewById(R.id.calendarViewIni);
        Button acepatarFrchaIni = dialogIni.findViewById(R.id.btn_fechaIni);


        fechaIni.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SeleccionFecha.this.fechaIni = dayOfMonth + "-" + (month + 1) + "-" + year;
            }
        });


        acepatarFrchaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.fechaSelecIni(SeleccionFecha.this.fechaIni);
                dialogIni.dismiss();
            }
        });


        dialogIni.show();

    }

}
