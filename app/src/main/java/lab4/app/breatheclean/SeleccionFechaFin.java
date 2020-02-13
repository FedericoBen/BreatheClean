package lab4.app.breatheclean;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

public class SeleccionFechaFin {



    public interface FechaSeleccionada {
        void fechaSelecFin(String fechaFin);
    }




    private FechaSeleccionada interfaz;

    private String fechaFin = "";


    public SeleccionFechaFin(Context context, FechaSeleccionada actividad){
        interfaz=actividad;

        final Dialog dialogFin = new Dialog(context);
        dialogFin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogFin.setCancelable(true);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogFin.setContentView(R.layout.seleccion_fechas_fin);

        CalendarView fechaFin = dialogFin.findViewById(R.id.calendarViewFin);
        Button acepatarFrchaFin = dialogFin.findViewById(R.id.btn_fechaFin);


        fechaFin.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                SeleccionFechaFin.this.fechaFin =dayOfMonth+"-"+(month+1)+"-"+year;
            }
        });



        acepatarFrchaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interfaz.fechaSelecFin(SeleccionFechaFin.this.fechaFin);
                dialogFin.dismiss();
            }
        });

        dialogFin.show();

    }

}
