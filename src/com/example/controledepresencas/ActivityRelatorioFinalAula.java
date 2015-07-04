package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ActivityRelatorioFinalAula extends Activity {
	private ArrayList<Boolean> nomeAlunos;
	private ArrayList<Boolean> presencas;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_relatorio_final_aula);
		//TODO exibir relatorio recebido da ActivityAula
		
		listarPresencaAlunos();
		
	}
	
	public void listarPresencaAlunos() {
		// TODO fazer a chamada rest  de final de aula aqui ou antes
		//confome a logica do sistema
		ArrayList<Boolean> presencas = new ArrayList<Boolean>();
		presencas.add(true);
		presencas.add(false);
		presencas.add(true);
		presencas.add(true);
		presencas.add(false);
		ArrayList<String> nomeAlunos = new ArrayList<String>();
		nomeAlunos.add("Pedro");
		nomeAlunos.add("Jefferson");
		nomeAlunos.add("Hernani");
		nomeAlunos.add("Daniela");
		nomeAlunos.add("Priscila");

		ListView lv = (ListView) findViewById(R.id.listViewPresencas);
		//VAi ser melhor passar um unico objeto com todos os dados do listView
		//Esse objeto pode ser criado de acordo com o xml de resposta
		//POr enquanto deixamos assim...
		lv.setAdapter(new AdapterPresencas(this, presencas, nomeAlunos));

		// aqui vai o metodo que vai alterar a presenca do aluno
		// Vai ser um onLongClickListener para o listview...
		
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
		//TODO rest alterar
		//TODO Pedrir confirmação e enviar alteração
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//currentPosition = position;
		builder.setMessage("Tem certeza que desaja alterar a presença de "/* TODO + nomesAlunos[position]*/).setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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
