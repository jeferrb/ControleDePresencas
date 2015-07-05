package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPrincipal extends Activity {

	protected static final String TAG = "ActivityPrincipal";
	private final String TIPO_PROFESSOR = "Professor";
	private final String TIPO_ALUNO = "Aluno";
	private String userName, userType;

/*	private String[] nomesTurmas;
	private String[] IdTurmas;
	private boolean[] isOpenTurmas;
	*/
	private Spinner listaTurmas;
	private String[] nomesDisciplinasTurmas;
	/*private String[] IdTurmas;
	private boolean[] isOpenTurmas;*/
	private ArrayList<ItemConsultaTurma> ret;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);

		listaTurmas = (Spinner) findViewById(R.id.spinnerTurmas);
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
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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
		// http://crunchify.com/how-to-iterate-through-java-list-4-way-to-iterate-through-loop/
		String retorno = RestClient.doRequisition("aula/usuario/" + this.userName + "/tipo/" + this.userType);
		//ArrayList<ItemConsultaTurma> ret = XmlManager.manageXmlTurmas(retorno);
		//nomesTurmas = new String[ret.size()];
		// String retorno = "";
		this.ret = XmlManager.manageXmlTurmas(retorno);
		
		/*nomesDisciplinas = new String[ret.size()];
		IdTurmas = new String[ret.size()];
		isOpenTurmas = new boolean[ret.size()];
		for (int i = 0; i < ret.size(); i++) {
			IdTurmas[i] = ret.get(i)[0];
			nomesDisciplinas[i] = ret.get(i)[1];
			isOpenTurmas[i] = Boolean.parseBoolean(ret.get(i)[2]);
		}*/
		String[] nomesDisciplinasTurmas = new String[this.ret.size()];
		
		for(int i=0; i< this.ret.size(); i++){
			 nomesDisciplinasTurmas[i] = this.ret.get(i).getNomeDisciplina() +"- Turma "+ this.ret.get(i).getIdTurma();
		}
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomesTurmas);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomesDisciplinasTurmas);
		listaTurmas = (Spinner) findViewById(R.id.spinnerTurmas);
		listaTurmas.setAdapter(adapter);
		listaTurmas.setOnItemSelectedListener(setBotoes(this.ret));
		
	}
	
	public OnItemSelectedListener setBotoes(final ArrayList<ItemConsultaTurma> ret){
		return(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Button butaoIniciarAula = (Button) findViewById(R.id.buttonIniciarAula);
				Button butaoConsultarTurma = (Button) findViewById(R.id.ButtonConsultarTurma);
				
				try {// may occur a fail or there are no classes registered
					if (userType.equals("Aluno")) {
						//butaoIniciarAula.setEnabled(isOpenTurmas[position]);
						butaoIniciarAula.setEnabled(ret.get(position).chamadaAberta);
						//butaoConsultarTurma.setEnabled(isOpenTurmas[position]);
						butaoConsultarTurma.setEnabled(ret.get(position).chamadaAberta);
					} else if (userType.equals("Professor")) {
						butaoIniciarAula.setText("Abrir Aula");
						butaoIniciarAula.setEnabled(!ret.get(position).chamadaAberta);
						butaoConsultarTurma.setEnabled(ret.get(position).chamadaAberta);
					}

				} catch (Exception e) {
					butaoIniciarAula.setEnabled(false);
					butaoConsultarTurma.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}

	@Override
	protected void onPause() {
		// myTimer.cancel();
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
				// Do nothing
			}
		});
		builder.show();
	}

	void doLogout() {
		// Deslogar no servidor
		String retorno = RestClient.doRequisition("login/usuario/" + this.userName + "/tipo/" + this.userType);
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

	public void onClickIniciarChamada(View v) {
		String retorno = "";
		if (userType.equals("Professor")) {
			// Iniciar chamada http://10.0.0.105:8080/CPresenca/api/aula/usuario/Eliane/turmaId/1/posix/1/posiy/1
			// Check Aluno http://10.0.0.105:8080/CPresenca/api/ aula/aluno/Eliane/turmaId/1
			GPSTracker gps = new GPSTracker(ActivityPrincipal.this);
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			// GPSTracker class & a class object
			gps = new GPSTracker(this);
			// Check if GPS enabled
			if (gps.canGetLocation()) {
				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				Log.e("latitude", Double.toString(latitude));
				Log.e("longitude", Double.toString(longitude));
				showToastMessage("Entrando em aula\nLat: " + latitude + "\nLong: " + longitude);
				retorno = RestClient.doRequisition("aula/usuario/" + userName + "/turmaId/" + this.ret.get(listaTurmas.getSelectedItemPosition()).getIdTurma() + "/posix/" + latitude+ "/posiy/" + longitude);
				
			} else {
				// Can't get location.
				// GPS or network is not enabled.
				// Ask user to enable GPS/network in settings.
				gps.showSettingsAlert();
			}
		} else if (userType.equals("Aluno")) {
			retorno = RestClient.doRequisition("aula/aluno/" + userName + "/turmaId/" + this.ret.get(listaTurmas.getSelectedItemPosition()).getIdTurma());
		}
//TODO
		//TODO
		if (!retorno.equals("")) {
			Log.e(TAG, retorno);
			String ret[] = XmlManager.manageXmlInicairChamada(retorno);
			if (ret[0].equals("true")) {
				showToastMessage("Aula iniciada");
				Bundle params = new Bundle();
				params.putString("nome", this.userName);
				params.putString("turmaID", this.ret.get(listaTurmas.getSelectedItemPosition()).getIdTurma());
				params.putString("tipo", this.userType);
				params.putString("chamdaID", (this.userType.equals("Aluno") ? ret[1] : ""));
				params.putString("nomeDisciplina", this.ret.get(listaTurmas.getSelectedItemPosition()).getNomeDisciplina());
				Intent intent = new Intent(this, ActivityAula.class);
				intent.putExtras(params);
				startActivity(intent);
			} else {
				showPopUpMessage(ret[1]);
			}
		}
	}

	public void onClickDeslogar(View v) {
		askForLogout();
	}

	public void onClickConsultarTurmas(View v) {
		if(this.userType.equals("Professor")){
			Bundle params = new Bundle();
			params.putString("turmaID", this.ret.get(listaTurmas.getSelectedItemPosition()).getIdTurma());
			params.putString("nomeDisciplina", this.ret.get(listaTurmas.getSelectedItemPosition()).getNomeDisciplina());
	
			Intent intent = new Intent(this, ActivityProfConsAlunoTurma.class);
			intent.putExtras(params);
			startActivity(intent);
		}
	}

}
