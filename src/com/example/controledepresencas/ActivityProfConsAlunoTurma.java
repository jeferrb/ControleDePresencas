package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.controledepresencas.model.ItemAlunoTurma;
import com.example.controledepresencas.model.ItemPresencaAlunoTurma;

public class ActivityProfConsAlunoTurma extends Activity {
	private String nomeDisciplina;
	private String turmaId;
	private ArrayList<ItemPresencaAlunoTurma> retListPresAlunoTurma;
	private ArrayList<ItemAlunoTurma> retListAlunoTurma;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prof_cons_aluno_turma);
		
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				this.turmaId = params.getString("turmaID");
				this.nomeDisciplina = params.getString("nomeDisciplina");
				showToastMessage("TurmaID: " + this.turmaId + "\n" + "Disciplina: " + this.nomeDisciplina);
			}
		}
		ajustaTelaActivity();
		listarAlunos();
	}
	
	public void ajustaTelaActivity(){
		TextView textNomeDisciplina =  (TextView) findViewById(R.id.textViewNomeDisciplina);
		textNomeDisciplina.setText(this.nomeDisciplina);
		TextView textNomeTurma = (TextView) findViewById(R.id.textViewTurma);
		textNomeTurma.append(" "+this.turmaId);;
		
	}

	public void listarAlunos() {
		// aqui vai a chamada rest para listar todos os nomesAlunos de uma
		// determinada turma...
		
		String retorno = RestClient.doRequisition("presenca/turmaId/"+this.turmaId);
		retListAlunoTurma =  XmlManager.manageXmlAlunoTurma(retorno);
		
		String[] nomesAlunos = new String[retListAlunoTurma.size()];
		
		for(int i=0; i< this.retListAlunoTurma.size(); i++){
			 nomesAlunos[i] = this.retListAlunoTurma.get(i).getNomeAluno();
			 Log.i("Lista AlunoTurma", retListAlunoTurma.get(i).getIdAluno()+" - "+ retListAlunoTurma.get(i).getNomeAluno()+" - "+ retListAlunoTurma.get(i).getUsuarioAluno());
		}
		
		
		Spinner listaAlunos = (Spinner) findViewById(R.id.spinnerAlunos);
		
		 
				
		//String[] nomesDisciplinasTurmas = new String[this.ret.size()];
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomesAlunos);
		listaAlunos = (Spinner) findViewById(R.id.spinnerAlunos);
		listaAlunos.setAdapter(adapter);
		listaAlunos.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				listarPresencas(position);				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {				
			}
		});
		
	}

	public void listarPresencas(int pos) {
		
		String retorno = RestClient.doRequisition("presenca/usuario/"+retListAlunoTurma.get(pos).getUsuarioAluno()+"/turmaId/"+this.turmaId);
		
		this.retListPresAlunoTurma = XmlManager.manageXmlPresencaAlunoTurma(retorno);
		
		Log.i("ItemPresencaAlunoTurma", this.retListPresAlunoTurma.toString());
		
		ListView lv = (ListView) findViewById(R.id.listViewPresencas);
		AdapterPresencas adapter = new AdapterPresencas(this, this.retListPresAlunoTurma);
		lv.setAdapter(adapter);
	}
	@Override
	public void onBackPressed() {
		finish();
	}
	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
