package lab4.app.breatheclean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //Variable para relacionar con la vista
    private TextView nInvitados;
    private TextView nAdmin;

    //Coneccion para autentificacion con Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Se capturan respectivameten cada uno de los TextView
        nInvitados = findViewById(R.id.tv_Invitados);
        nAdmin = findViewById(R.id.tv_Admin);

        //Se Obtiene la autenticacion de la instancia de Firebase;
        mAuth = FirebaseAuth.getInstance();

        //Se lee si el usuario a sido logeado en caso de que un administrador entre anteriormente
        FirebaseUser user = mAuth.getCurrentUser();
        updateUI(user);

        //Se crean dos acciones sobre el click o tocar d ecada uno de los TextView de tal manera que
        // si es un invitado o usuario casual el cual no posee permisos de administrador pase a la pantalla del mapa sin el permiso
        // en este caso se ve en el valor que se le esta pasando extra, llamado "Invitado"
        nInvitados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("usuario", "Invitado");
                startActivity(intent);
            }
        });
        //En caso de ser un administrador va primero a enviarlo a una pantalla para que este inicie secion
        nAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginAdmin.class);
                startActivity(intent);
            }
        });
    }

    //Este metodo se implementa para que si un administrador a iniciado secion anteriormente, y se a
    // destruido la aplicacion o se a salido de ella, no tenga que volver a hacerlo, sino que quede con
    // la sesion iniciada hasta que desida cerrarla.

    private void updateUI(FirebaseUser user) {
        if(user!=null){
            Intent intent = new Intent(MainActivity.this,MapsActivity.class);
            startActivity(intent);
            isFinishing();
        }
    }
}
