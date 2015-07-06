package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ActivityRelatorioFinalAula extends Activity {
	private ArrayList<String> nomesAlunos;
	private ArrayList<String> presencaAlunos;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatorio_final_aula);
		Intent intent = getIntent();
		if (intent != null) {
			Bundle params = intent.getExtras();
			if (params != null) {
				this.nomesAlunos = params.getStringArrayList("nomesAlunos");
				this.presencaAlunos = params.getStringArrayList("presencaAlunos");
				((TextView)findViewById(R.id.textViewNomeTurma)).setText(params.getString("nomeDisciplina"));
			}
		}
		listarPresencaAlunos();
		
	}
	
	public void listarPresencaAlunos() {

		ListView lv = (ListView) findViewById(R.id.listViewPresencas);
		lv.setAdapter(new AdapterRelatorioFinalAula(this, presencaAlunos, nomesAlunos));

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				confirmaAlterarPresenca(position);
				return true;
			}
		});
		lv.setOnItemClickListener(new OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		    	showToastMessage("Longo click para alterar a presença");
		    }

		});
	}
	
	private void confirmaAlterarPresenca(int position){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Tem certeza que desaja alterar a presença de "+ nomesAlunos.get(position)+"?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				enviarSolicitacaoAlterarPresenca();
			}
		}).setNegativeButton("Não", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				// Stay here!
			}
		});
		builder.show();
	}
	private void enviarSolicitacaoAlterarPresenca() {
		//TODO
		//int aluno = idAlunos[currentPosition];
		
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	public void showToastMessage(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
	}
}
