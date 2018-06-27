package com.xudre.valiacaologin;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    public EditText emailField;
    public EditText passwordField;
    public Button enterButton;

    /**
     * Executado assim que a Activity é criada e os elementos de interface se tornaram acessíveis.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Setup();
    }

    /**
     * Realiza os ajustes iniciais nos elementos da interface.
     */
    protected void Setup() {
        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.password);
        enterButton = findViewById(R.id.enter);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enter();
            }
        });
    }

    /**
     * Ação do usuário para entrar no sistema.
     */
    protected void Enter() {
        emailField.setError(null);
        passwordField.setError(null);

        HideKeyboard(); // Escondemos automaticamente o teclado para melhor User Experience...

        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        switch (Validate(email, password)) {
            case 10:
                // O e-mail não foi preenchido:
                emailField.setError("O campo de e-mail é obrigatório.");
                emailField.requestFocus();

                break;
            case 11:
                // O e-mail não tem um formato válido:
                emailField.setError("O e-mail parece inválido.");
                emailField.requestFocus();

                break;
            case 20:
                // A senha não foi preenchida:
                passwordField.setError("O campo de senha é obrigatório.");
                passwordField.requestFocus();

                break;
            case 21:
                // A senha não tem o comprimento esperado:
                passwordField.setError("A senha precisa ter no mínimo 6 caracteres.");
                passwordField.requestFocus();

                break;
            case 22:
                // A senha não tem o padrão esperado:
                passwordField.setError("A senha parece inválida.");
                passwordField.requestFocus();

                break;
            default:
                // Assumimos que qualquer retorno não tratado é válido:
                Authenticate(email, password);

                break;
        }
    }

    /**
     * Valida os dados de e-mail e senha do usuário.
     *
     * @param email E-mail do usuário
     * @param password Senha do usuário
     * @return int
     */
    protected int Validate(String email, String password) {
        if (TextUtils.isEmpty(email)) return 10;

        // Aqui verificamos se o e-mail tem um padrão esperado:
        Pattern emailRegex = Pattern.compile(
            "^[a-z0-9._-]+@([a-z0-9]+\\.)+[a-z0-9]+$",
            Pattern.CASE_INSENSITIVE
        );

        if (emailRegex.matcher(email).matches() == false) return 11;

        if (TextUtils.isEmpty(password)) return 20;

        if (password.length() < 6) return 21;

        // Aqui verificamos se a senha tem um padrão esperado:
        if (!password.matches("^[a-zA-Z_0-9]+$")) return 22;

        return 200;
    }

    /**
     * Envia os dados de e-mail e senha do usuário pro servidor.
     *
     * @param email E-mail do usuário
     * @param password Senha do usuário
     */
    protected void Authenticate(String email, String password) {
        // Aqui enviamos as informações de usuário já pré-validadas para o servidor:

        // TODO Por hora iremos fazer um envio FAKE...

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Definimos o título do alerta:
        builder.setTitle("Autenticação");
        // Definimos um botão padrão:
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        if (email.matches("^pedro@xudre\\.com|\\w+@gmail.com$")) {
            builder.setMessage("Login realizado com sucesso!");
        } else {
            builder.setMessage("Usuário inválido, tente novamente.");
        }

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    /**
     * Esconde o teclado virtual.
     */
    protected void HideKeyboard() {
        View focus = getCurrentFocus();

        if (focus == null) return;

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        manager.hideSoftInputFromWindow(focus.getWindowToken(), 0);
    }

}
