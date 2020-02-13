package lab4.app.breatheclean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginAdmin extends AppCompatActivity {

    private static final String TAG = " " ;

    //Varkiables para campurar del layaout
    private EditText nEmail;
    private EditText nPassword;
    private Button nIngresar;

    private String nemail;

    //Coneccion para autentificacion con Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        //Se capturan tanto lo edit text como el boton que va a generar la accion
        nEmail = (EditText) findViewById(R.id.txt_Correo);
        nPassword=(EditText) findViewById(R.id.txt_Pass);
        nIngresar=(Button) findViewById(R.id.btn_Ingresar);

        //Se obtiene la instancia de Fire Base
        mAuth = FirebaseAuth.getInstance();

        //Si se oprime el boton se verifica Inicio de sesion
        nIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se crean variables temporales las cuales van a capturar los valores de cada uno de los
                //EditText
                final String email,password;
                email=nEmail.getText().toString();
                password=nPassword.getText().toString();

                //En los siguientes dos ifs se comprueba que los dos valores leido previamente no se encuentren vacios
                //y que en el caso de la contraseña no tenga menos de 6 caracteres
                if(email.equals("")){
                    Toast.makeText(LoginAdmin.this, "El correo no puede estar vacio",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                if(password.length()<6 || password.equals("")){
                    Toast.makeText(LoginAdmin.this, "La cotraseña debe tener por lo menos 6 caracteres",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                //Si se a podido comprovar que los valores son validos para verificar se compueba que los dos si se encuentren registrados
                //por medio de la instancia que se realizo de Firebase
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginAdmin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Por medio de la verificacion de la tarea si esta a sido exitosa, quiere decir que si se encuentra regitrado
                        if (task.isSuccessful()) {
                            //Ya que se a logeado se requiere de enviar a la ventana del mapa pero con el permiso del administrador que
                            // en este caso es su correo correspondiente
                            Log.d(TAG, "createUserWithEmail:success");
                            Intent intent = new Intent(LoginAdmin.this,MapsActivity.class);
                            intent.putExtra("usuario",email);
                            nemail=email;
                            startActivity(intent);

                            //Se instancia el usuario para que la aplicacion entienda que este si inicio sesion
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Toast.makeText(LoginAdmin.this, "Autenticacion exitosa.",
                                    Toast.LENGTH_SHORT).show();
                            //Intent misAvisos = new Intent(MainActivity.this, ServicioAvisos.class);
                            // MainActivity.this.startService(misAvisos);

                        } else {
                            // Si la rtarea de autentificar es fallida entonces manda un mensaje de error y limpia los espacios
                            // para brindar un nuvo intento de inisio de sesion
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(LoginAdmin.this, "Autenticacion fallida.",
                                    Toast.LENGTH_SHORT).show();

                            nEmail.setText("");
                            nPassword.setText("");

                            //Es clave enviar un null para que asi no se inicie la sesion de forma global
                            updateUI(null);
                        }
                    }
                });

            }
        });

    }

    //Este metodo es el que toma la accion del boton de atras que se tiene por defecto y lo que realiza es
    //devolver a la ventan principal
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginAdmin.this,MainActivity.class);
    }

    //Cundo se incia en caso de que por algun motivo la aplicacion vuelva de la ventna de maps por accidente o
    // error y el usuario ya se encuentre en un inicio de sesion, no se salga sino que compruve y mantenga la sesion en caso de si estarlo
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }


    private void updateUI(FirebaseUser user) {

        if(user!=null){
            Intent intent = new Intent(LoginAdmin.this,MapsActivity.class);
            intent.putExtra("usuario",nemail);
            startActivity(intent);
            isFinishing();
        }

    }



}

