package com.example.controledepresencas;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAula extends Activity {
	private final String TIPO_PROFESSOR = "Professor";
	private final String TIPO_ALUNO = "Aluno";
	private String turmaID, userName, userType;

	private long timeOut = 100;// milliseconds
	private Timer myTimer;
	private GPSTracker gps = new GPSTracker(ActivityAula.this);
	final ActivityAula thisActivity = this;

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
		ajustarTela();
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

	public void onClickEncerrarAula(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Tem certeza que desaja fechar a aula?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				enviarSolicitacaoEncerrarAula();
			}
		}).setNegativeButton("Não", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Stay here!
			}
		});
		builder.show();
	}

	private void enviarSolicitacaoEncerrarAula() {
		String retorno = "";
		if (userType.equals("Professor")) {
			retorno = RestClient.doRequisition("aula/turmaid/" + turmaID);
		} else if (userType.equals("Aluno")) {
			retorno = RestClient.doRequisition("aula/aluno/" + userName);
		}
		retorno = XmlManager.manageXmlFinishClass(retorno);
		// TODO
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

	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		onClickEncerrarAula(null);
	}

	public void sendTick() {
		// GPSTracker class & a class object
		gps = new GPSTracker(this);
		// Check if GPS enabled
		if (gps.canGetLocation()) {
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
					public void run() {
						double latitude = gps.getLatitude();
						double longitude = gps.getLongitude();
						Log.e("latitude", Double.toString(latitude));
						Log.e("longitude", Double.toString(longitude));
						showToastMessage("Lat: " + latitude + "\nLong: " + longitude);
						String retorno = RestClient.doRequisition("aula/aluno/" + userName + "/posix/" + Double.toString(latitude) + "/posiy/" + Double.toString(longitude));
						String[] ret = XmlManager.manageXmlTick(retorno);
						if (!ret[0].equals("Sucesso")) {
							// TODO
							showToastMessage("Aula encerrada XX% de presença");
							encerrarAulaLocalmente();
						}
					}
				});
			}

			private void encerrarAulaLocalmente() {
				try {
					gps.stopUsingGPS();
					myTimer.cancel();
					Bundle params = new Bundle();
					params.putString("nome", userName);
					params.putString("tipo", userType);

					Intent intent = new Intent(thisActivity, ActivityPrincipal.class);
					intent.putExtras(params);
					startActivity(intent);
					finish();
				} catch (Exception e) {
					// Do Nothing
				}
			}
		}, 0, timeOut);
	}
	
	public void listarPresencasFinalAula(){
		//Precisa passar o nome da turma tamb�m...
		Bundle params = new Bundle();
		params.putString("nome", this.userName);
		params.putString("turmaID", this.turmaID);

		Intent intent = new Intent(this, ActivityRelatorioFinalAula.class);
		intent.putExtras(params);

		startActivity(intent);	
	}
	
}
