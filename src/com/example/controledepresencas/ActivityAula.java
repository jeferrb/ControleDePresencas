package com.example.controledepresencas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ActivityAula extends Activity {
	private String turmaID, userName, userType;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aula);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				this.turmaID = params.getString("turmaID");
				this.userName = params.getString("nome");
				this.userType = params.getString("tipo");
				showToastMessage("ID da turma: " + turmaID);
			}
		}
		
	}
	
	public void onClickEncerrarAula(View view){
		final ActivityAula thisActivity = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Tem certeza que desaja fechar a aula?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		        	//TODO
		        	String retorno = "";
		        	if (userType.equals("Professor")){
		        		retorno = RestClient.doRequisition("aula/turmaid/" + turmaID);
		        	}else if (userType.equals("Aluno")){
		        		retorno = RestClient.doRequisition("aula/aluno/" + userName);
		        	}
		       		retorno = XmlManager.manageXmlFinishClass(retorno);
		       		if (retorno.equals("Sucesso")) {
		       			Bundle params = new Bundle ();
						params.putString("nome", userName);
						params.putString("tipo", userType);
						
						Intent intent = new Intent(thisActivity, ActivityPrincipal.class);
						intent.putExtras(params);
						
						startActivity(intent);
						finish();
		       		} else {
		       			showPopUpMessage(retorno);
		       		}
		           }
		       }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		        	   //Stay here!
		           }
		       });
		builder.show();
	}
	private void showPopUpMessage(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(message);
		alertDialog.show();
	}
	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
	@Override
	public void onBackPressed() {
		//TODO
	    //askForLogout();
	}
}
