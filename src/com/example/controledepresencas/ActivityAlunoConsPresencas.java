package com.example.controledepresencas;

import java.util.ArrayList;

import com.example.controledepresencas.model.ItemPresencaAlunoTurma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAlunoConsPresencas extends Activity {
	private String nomeDisciplina;
	private String turmaId;
	private String usuarioAluno;
	
	private ArrayList<ItemPresencaAlunoTurma> retListPresAlunoTurma;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aluno_cons_presencas);

		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				this.turmaId = params.getString("turmaID");
				this.nomeDisciplina = params.getString("nomeDisciplina");
				this.usuarioAluno = params.getString("usuarioAluno");
				showToastMessage("TurmaID: " + this.turmaId + "\n" + "Disciplina: " + this.nomeDisciplina + "Usuario:"+ this.usuarioAluno);
			}
		}
		 ajustaTelaActivity();
		 listarPresencas();
		 
	}
	
	public void ajustaTelaActivity(){
		TextView textNomeDisciplina =  (TextView) findViewById(R.id.textViewNomeDisciplina);
		textNomeDisciplina.setText(this.nomeDisciplina);
		TextView textNomeTurma = (TextView) findViewById(R.id.textViewTurma);
		textNomeTurma.append(" "+this.turmaId);
		//TODO Colocar o nome do usuario depois...
		
	}
	
	public void listarPresencas() {
		//String nomeUsuario;
		//Exemplo Requisicao Rest
		//String retorno = RestClient.doRequisition("presenca/usuario/Joao/turmaId/1");
		
		String retorno = RestClient.doRequisition("presenca/usuario/"+this.usuarioAluno+"/turmaId/"+this.turmaId);
		
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
