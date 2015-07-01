package com.example.controledepresencas;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPrincipal extends Activity {

	private final String TIPO_PROFESSOR = "Professor";
	private final String TIPO_ALUNO = "Aluno";
	private String userName, userType;
	private String[] nomesTurmas;
	private String[] IdTurmas ;
	private boolean[] isOpenTurmas;
	private long timeOut = 100;//milliseconds
	private Timer myTimer;
	private boolean isEmAula = false;
	private GPSTracker gps = new GPSTracker(ActivityPrincipal.this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				this.userName = params.getString("nome");
				this.userType = params.getString("tipo");
				showToastMessage("Usuario: " + userName + "\n" + "Senha: " + userType);
			}
		}
		ajustarTela();
		listarTurmas();

	}

	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
				.show();
	}

	public void ajustarTela() {
		// Ajustando Tela Aluno e Professor
		TextView textView;
		textView = (TextView) findViewById(R.id.textViewTipoUsuario);
		ImageView imgView;
		imgView = (ImageView) findViewById(R.id.imageViewProfessorAluno);
		if (this.userType.equals(TIPO_PROFESSOR)) {
			textView.setText(TIPO_PROFESSOR);
			
		} else {
			textView.setText(TIPO_ALUNO);
			imgView.setImageResource(R.drawable.aluno);
		}
		textView = (TextView) findViewById(R.id.textViewNome);
		textView.setText(this.userName);
	}
	public void listarTurmas() {
		//http://crunchify.com/how-to-iterate-through-java-list-4-way-to-iterate-through-loop/
		//String retorno = RestClient.doRequisition("aula/usuario/"+this.userName+"/userType/"+this.userType);
		String retorno = "";
		ArrayList<String[]> ret = XmlManager.manageXmlTurmas(retorno);
		nomesTurmas = new String[ret.size()];
		IdTurmas = new String[ret.size()];
		isOpenTurmas = new boolean[ret.size()];
		for (int i = 0; i < ret.size(); i++) {
			IdTurmas[i] = ret.get(i)[0];
			nomesTurmas[i] = ret.get(i)[1];
			isOpenTurmas[i] = Boolean.parseBoolean(ret.get(i)[2]);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomesTurmas);
		Spinner listaTurmas = (Spinner) findViewById(R.id.spinnerTurmas);
		
		listaTurmas.setAdapter(adapter);
		
		listaTurmas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Button butaoIniciarAula = (Button) findViewById(R.id.buttonIniciarAula);
				Button butaoConsultarTurma = (Button) findViewById(R.id.ButtonConsultarTurma);
				try {//may occur a fail or there are no classes registered
					butaoIniciarAula.setEnabled(isOpenTurmas[position]);
					butaoConsultarTurma.setEnabled(isOpenTurmas[position]);
				} catch (Exception e) {
					butaoIniciarAula.setEnabled(false);
					butaoConsultarTurma.setEnabled(false);
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// Do nothing
			}
		});
	}
	
	public void onClickIniciarAula(){
		Spinner listaTurmas = (Spinner) findViewById(R.id.spinnerTurmas);
		
		Bundle params = new Bundle ();
		params.putString("userName", this.userName);
		params.putString("turmaID", nomesTurmas[listaTurmas.getSelectedItemPosition()]);
		
		Intent intent = new Intent(this, ActivityAula.class);
		intent.putExtras(params);
		
		startActivity(intent);
		finish();
	}
	
	public void sendTick(){
		 // GPSTracker class & a class object
	    gps = new GPSTracker(this);
        // Check if GPS enabled
        if(gps.canGetLocation()) {
           tickScheduler();
            
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
	}

	private void tickScheduler() {
		myTimer = new Timer();
		myTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Handler handler = new Handler(Looper.getMainLooper());
				handler.post(new Runnable() {
				        @Override
				        public void run() {double latitude = gps.getLatitude();
							double longitude = gps.getLongitude();
							Log.e("latitude", Double.toString(latitude));
							Log.e("longitude", Double.toString(longitude));
				        	Toast.makeText(getApplicationContext(), "Tick envido\nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
				        	
				        	//TODO de acordo com a resposta do TICK atualizar isEmAula
				        	String retorno = RestClient.doRequisition("aula/aluno/" + userName + "/posix/" + Double.toString(latitude) + "/posiy/" + Double.toString(longitude));
				    		String[] ret = XmlManager.manageXmlTick(retorno);
				    		
				        }
				    });
				if (!isEmAula) {
					myTimer.cancel();
					gps.stopUsingGPS();
				}
			}
		}, 0, timeOut);
	}

	TimerTask doThis;

	@Override
	protected void onPause() {
	    //myTimer.cancel();
	    super.onPause();
	}

	public void askForLogout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Tem certeza que desaja sair?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		        	   doLogout();
		           }
		       }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
		           @Override
				public void onClick(DialogInterface dialog, int id) {
		        	   //Do nothing
		           }
		       });
		builder.show();
	}
	
	void doLogout() {
		// Deslogar no servidor
		String retorno = RestClient.doRequisition("login/usuario/" + this.userName + "/userType/" + this.userType);
		retorno = XmlManager.manageXmlLogout(retorno);
		if (retorno.equals("Sucesso")) {
			finish();
		} else {
			showPopUpMessage(retorno);
		}
	}
	
	private void showPopUpMessage(String message) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setMessage(message);
		alertDialog.show();
	}
	
	@Override
	public void onBackPressed() {
	    askForLogout();
	}
	
	public void onClickIniciarAula(View v){
		//TODO 
	}
	public void onClickDeslogar(View v){
		askForLogout();
	}
	public void debugTick(View v){
		sendTick();
	}
	
}
