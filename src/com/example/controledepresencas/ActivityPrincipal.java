package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledepresencas.model.ItemConsultaTurma;

public class ActivityPrincipal extends Activity {

	protected static final String TAG = "ActivityPrincipal";
	private final String TIPO_PROFESSOR = "Professor";
	private final String TIPO_ALUNO = "Aluno";
	private String userName, userType;
	private Spinner listaTurmas;
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
			TextView texto = (TextView) findViewById(R.id.textViewTempoTick);
			TextView texto2 = (TextView) findViewById(R.id.textViewPorcValAula);
			EditText texto3 = (EditText) findViewById(R.id.editTextTempoTick);
			EditText texto4 = (EditText) findViewById(R.id.editTextPorcValAula);
			texto.setVisibility(View.INVISIBLE);
			texto2.setVisibility(View.INVISIBLE);
			texto3.setVisibility(View.INVISIBLE);
			texto4.setVisibility(View.INVISIBLE);
			
			
			textView.setText(TIPO_ALUNO);
			imgView.setImageResource(R.drawable.aluno);
		}
		textView = (TextView) findViewById(R.id.textViewNome);
		textView.setText(this.userName);
	}

	public void listarTurmas() {
		// http://crunchify.com/how-to-iterate-through-java-list-4-way-to-iterate-through-loop/
		String retorno = RestClient.doRequisition("aula/usuario/" + this.userName + "/tipo/" + this.userType);

		this.ret = XmlManager.manageXmlTurmas(retorno);

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
						butaoIniciarAula.setEnabled(ret.get(position).isChamadaAberta());
					} else if (userType.equals("Professor")) {
						butaoIniciarAula.setText("Abrir Aula");
						butaoIniciarAula.setEnabled(!ret.get(position).isChamadaAberta());
					}

				} catch (Exception e) {
					butaoIniciarAula.setEnabled(false);
					butaoConsultarTurma.setEnabled(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {				
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
			String porpe = ((EditText)findViewById(R.id.editTextPorcValAula)).getText().toString();
			String dura =((EditText)findViewById(R.id.editTextTempoTick)).getText().toString();
			if(dura.length()==0||porpe.length()==0){
				showToastMessage("Os parametros de aula são campos obrigatórios");
				return;
			}
			try {
				Log.i("porpe", porpe);
				Log.i("dura", dura);
				if(Integer.parseInt(porpe)>100||Integer.parseInt(porpe)<0){
					showToastMessage("Porcentagem vai de 0 a 100");
					return;
				}
				if(Integer.parseInt(dura)<1){
					showToastMessage("Duração da aula muito pequena");
					return;
				}
			} catch (Exception e) {
				showToastMessage("Parametros de aula inválidos");
				e.printStackTrace();
				return;
			}
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
				retorno = RestClient.doRequisition("aula/usuario/" + userName + "/turmaId/" + this.ret.get(listaTurmas.getSelectedItemPosition()).getIdTurma() + "/posix/" + latitude+ "/posiy/" + longitude + "/porpre/" + porpe +	"/dura/" + dura);
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
				if(this.userType.equals("Aluno")) params.putInt("freqEnvioTick", Integer.parseInt(ret[2]));
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
		Bundle params = new Bundle();
		Intent intent;
		if(this.userType.equals("Professor")){
			//Usuario do tipo professor
			params.putString("turmaID", this.ret.get(listaTurmas.getSelectedItemPosition()).getIdTurma());
			params.putString("nomeDisciplina", this.ret.get(listaTurmas.getSelectedItemPosition()).getNomeDisciplina());
			intent = new Intent(this, ActivityProfConsAlunoTurma.class);
			intent.putExtras(params);
			startActivity(intent);
		}
		else{
			//Usuario do tipo aluno
			params.putString("turmaID", this.ret.get(listaTurmas.getSelectedItemPosition()).getIdTurma());
			params.putString("nomeDisciplina", this.ret.get(listaTurmas.getSelectedItemPosition()).getNomeDisciplina());
			params.putString("usuarioAluno", this.userName);
			intent = new Intent(this, ActivityAlunoConsPresencas.class);
			intent.putExtras(params);
			startActivity(intent);
		}
	}
	public void onClickRefreshScreen(View view){
		listarTurmas();
	}

}
