package lab4.app.breatheclean;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, ActivityCompat.OnRequestPermissionsResultCallback {

    private GoogleMap mMap;
    private GoogleMap senUb;
    private Marker marcador;

    private double lat = 0.0;
    private double lon = 0.0;

    private Location loc;
    private LocationManager locManager;


    //variables de sesion
    private String newAdmin;
    private String admin;

    private TextView nUsuario;
    private Button nNewMarcador;
    private CountDownTimer countDownTimer;

    DatabaseReference reff;
    DatabaseReference reffMostrarMarcadores;


    private int cont = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //Estos dos componentes son para definir las prioridad que tiene el usuario que lo esta utilizando

        nUsuario = findViewById(R.id.tv_Usuario);
        nNewMarcador = findViewById(R.id.btn_AddMarcador);

        //Se captura la variable extra que se envia al inisciar sesion o al ingresar como usuario y de primera instancia
        //se inicia el boton del marcador como invisible para mas adelante determinar si se muestra para el administrador
        //o si se deja oculto para el invitado

        final Bundle dueno = this.getIntent().getExtras();
        nNewMarcador.setVisibility(View.INVISIBLE);

        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
        //Se le introduce un retardo para que le de tiempo al mapa de cargar los marcadores exitente y hubicar al usuario correspondiente

                cont++;
                if(cont == 1){
                    nUsuario.setText("Cargando .");
                }else if(cont == 2){
                    nUsuario.setText("Cargando . .");
                }else if(cont == 3) {
                    nUsuario.setText("Cargando . . .");
                }
            }

            @Override
            public void onFinish() {

                //Se lee la variable dueno, para realizar la determinacion si es o no administrador y se
                //guardan las variables de sesion correspondiente
                //se utiliza un tru catch ya que sii el usuario es un administrador y hay una carga
                //en este punto no habra variables extra traidas del anterior view, cosa
                //que no dejaria cargar correctamente y fallaria, por eso si falla en ese punto,
                //el plan de contingencia escargar las variables previamente capturadas

                try {
                    newAdmin = dueno.getString("usuario").toString();
                    guardar_preferencias();
                    cargar_Preferencias();
                }catch(Exception e){
                    cargar_Preferencias();
                }


                //Aqui se decide si el que3 esta usando la aoplicacion es un usuario o un administrador
                if(admin.equals("Invitado")){
                    nUsuario.setText("Invitado");
                    nNewMarcador.setVisibility(View.INVISIBLE);
                }else{
                    nUsuario.setText("Administrador");
                    nNewMarcador.setVisibility(View.VISIBLE);
                }

                //Se le adiciona la escicha al boton en caso de que administrador quiera addicionar un marcador
                nNewMarcador.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddMarcador();
                    }
                });

                mostrarMarcadores();
            }
        }.start();

    }

    //En este metodo, que esta por defecto, en el cual si llega a existir una mala carga o algo
    // va a volver a tratar para motrar d emanera correcta los marcadores
    @Override
    protected void onResume() {
        super.onResume();
        mostrarMarcadores();
    }

    //En esta funcion se raliza una consulta a firebase para buscar todos los marcadores
    // que se tengan guardados y como estos se debe mostrar
    public void mostrarMarcadores(){

        reffMostrarMarcadores = FirebaseDatabase.getInstance().getReference().child("Marcadores");

        reffMostrarMarcadores.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    Marcadores A = objSnapshot.getValue(Marcadores.class);

                    agregarMarcador(Double.parseDouble(A.getLat()),Double.parseDouble(A.getLon()),A.getUid());

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Este metodo se encarga de ralizar una carga d elos marcadores a traves  de la utilidad de una venta
    //emergente que va a ser enviada por un dialog, donde se va a pedir lo que es la latitud y la longitud
    public void AddMarcador(){
        final Dialog AddMarcadorNuevo = new Dialog(MapsActivity.this);
        AddMarcadorNuevo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        AddMarcadorNuevo.setCancelable(true);
        AddMarcadorNuevo.setContentView(R.layout.add_marcador);

       final TextView nNewLong = AddMarcadorNuevo.findViewById(R.id.txt_longitud);
       final TextView nNewLat = AddMarcadorNuevo.findViewById(R.id.txt_latitud);
       final TextView uid = AddMarcadorNuevo.findViewById(R.id.txt_uid);
        Button nAddMarcador = AddMarcadorNuevo.findViewById(R.id.btn_NewMarcador);

        reff = FirebaseDatabase.getInstance().getReference().child("Marcadores");

        nAddMarcador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nNewLong.getText().toString().equals("") && !nNewLat.getText().toString().equals("") && !uid.getText().toString().equals("")){
                    Marcadores marcadores = new Marcadores();
                    marcadores.setLon(nNewLong.getText().toString());
                    marcadores.setLat(nNewLat.getText().toString());
                    marcadores.setUid(uid.getText().toString());


                    reff.child(marcadores.getUid()).setValue(marcadores);
                    AddMarcadorNuevo.dismiss();

                }else{
                    Toast.makeText(MapsActivity.this, "Debe ingresar todos los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AddMarcadorNuevo.show();

    }


    @Override
    public void onBackPressed(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MapsActivity.this,MainActivity.class);
        startActivity(intent);
    }



    //Este metodo se encarga d ecargar las variables d esesion en caso de que el usuario sea un administrador
    private void cargar_Preferencias() {
        SharedPreferences preferences = getSharedPreferences("Correos", Context.MODE_PRIVATE);
        admin = preferences.getString("user","no exite la informacion");
    }

    //Este metodo se encarga de guardar las variables de sesion correspondientes
    private void guardar_preferencias(){
        SharedPreferences preferences = getSharedPreferences("Correos", Context.MODE_PRIVATE);

        String usuario = newAdmin;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user",usuario);
        editor.commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        senUb= googleMap;


        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);

        senUb.setOnMarkerClickListener(this);
        senUb.setOnMarkerDragListener(this);
    }


    //Con est funcion lo que se realiza es la creacion de los marcadores correspondientes
    // y se les parametriza para que tengan la ubicacion y el tamañoo deseados
    private void agregarMarcador(double lat, double lon, String id) {
        LatLng coordenadas = new LatLng(lat, lon);
        CameraUpdate miUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 14);
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas).title(id));
        mMap.animateCamera(miUbicacion);
    }

    //para el caso de los marcadores se les adiciona este metodo para la escucha, pero se realiza
    // un filtraje para saber si el tocar el marcador este debe mostrar solamente las estadisticas
    // de dicho sensoor o si debe mostrar las opciones del administrador
    @Override
    public boolean onMarkerClick(final Marker marker) {

        reff = FirebaseDatabase.getInstance().getReference().child("Marcadores");


        if(admin.equals("Invitado")){
            startActivity(new Intent(MapsActivity.this,Graficar_Datos.class));
        }else{
            final Dialog opMarcador = new Dialog(MapsActivity.this);
            opMarcador.requestWindowFeature(Window.FEATURE_NO_TITLE);
            opMarcador.setCancelable(true);
            opMarcador.setContentView(R.layout.opciones_admin);
            opMarcador.show();

            Button nRevisar = opMarcador.findViewById(R.id.btn_Revisar);
            Button nElimnar = opMarcador.findViewById(R.id.btn_Eliminar);

            nRevisar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MapsActivity.this,Graficar_Datos.class);
                    startActivity(intent);
                    opMarcador.dismiss();
                }
            });


            nElimnar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    AlertDialog.Builder miDialogo = new AlertDialog.Builder(MapsActivity.this);
                    miDialogo.setMessage("Seguro quiere eliminar este Marcador?");
                    miDialogo.setCancelable(false);

                    miDialogo.setPositiveButton("Si" ,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(MapsActivity.this,marker.getTitle(), Toast.LENGTH_SHORT).show();
                            reff.child(marker.getTitle()).removeValue();
                            Toast.makeText(MapsActivity.this, "Marcador eliminado", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MapsActivity.this,MapsActivity.class);
                            startActivity(intent);
                            opMarcador.dismiss();
                        }
                    });
                    miDialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    miDialogo.show();
                }
            });
        }


        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }
}
