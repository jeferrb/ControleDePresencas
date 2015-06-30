package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	private String nome, tipo;
	private String[] nomesTurmas;
	private String[] IdTurmas ;
	private boolean[] isOpenTurmas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_principal);

		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				this.nome = params.getString("nome");
				this.tipo = params.getString("tipo");

				showToastMessage("Usuario: " + nome + "\n" + "Senha: " + tipo);
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

		if (this.tipo.equals(TIPO_PROFESSOR)) {
			textView.setText(TIPO_PROFESSOR);
			
		} else {
			textView.setText(TIPO_ALUNO);
			imgView.setImageResource(R.drawable.aluno);
		}

		textView = (TextView) findViewById(R.id.textViewNome);
		textView.setText(this.nome);
	}
	public void listarTurmas() {
		//http://crunchify.com/how-to-iterate-through-java-list-4-way-to-iterate-through-loop/
		String retorno = RestClient.doRequisition("aula/usuario/"+this.nome+"/tipo/"+this.tipo);
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
				butaoIniciarAula.setEnabled(isOpenTurmas[position]);
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
		params.putString("nome", this.nome);
		params.putString("turmaID", nomesTurmas[listaTurmas.getSelectedItemPosition()]);
		
		Intent intent = new Intent(this, ActivityAula.class);
		intent.putExtras(params);
		
		startActivity(intent);
		finish();
	}
}
