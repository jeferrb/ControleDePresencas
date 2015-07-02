package com.example.controledepresencas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ActivityLogin extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	public void onClickLogar (View v){
		String login = ((EditText)findViewById(R.id.editTextUsuario)).getText().toString();
		String senha = ((EditText) findViewById(R.id.editTextSenha)).getText().toString();
		
		if(!(login != null && login.length()>0 && senha != null && senha.length()>0)) {
			this.showPopUpMessage("Login e Senha são campos obligatórios");
			return;
		}
		
		//return success, fail or the type of the userName.
		String retorno = RestClient.doRequisition("login/usuario/" + login + "/senha/" + senha);
		String[] ret = XmlManager.manageXmlLogin(retorno);
		
		String resultado = ret[0];
		
		if (resultado.equals("Aluno")||resultado.equals("Professor")) {
			Bundle params = new Bundle ();
			params.putString("nome", login);
			params.putString("tipo", resultado);
			
			Intent intent = new Intent(this, ActivityPrincipal.class);
			intent.putExtras(params);
			
			startActivity(intent);
			finish();
		}else{
			this.showPopUpMessage(resultado);
		}
	}
	
	public void onClickSairAplicativo(View v){
		finish();
	}
	public void onClickButton(View v){
		//TODO consertar essa gambi de debug
		Bundle params = new Bundle ();
		params.putString("nome", "Joao");
		params.putString("tipo", "Aluno");
		
		Intent intent = new Intent(this, ActivityPrincipal.class);
		intent.putExtras(params);
		
		startActivity(intent);
		finish();
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	public void showPopUpMessage(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(message);
		alertDialog.show();
	}
	
}
