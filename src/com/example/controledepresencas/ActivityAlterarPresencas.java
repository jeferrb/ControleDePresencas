package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityAlterarPresencas extends Activity {
	private Boolean[] presencas;
	private String[] datas;
	private String[] nomesAlunos;
	private int[] idAlunos;
	private int currentPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alterar_presencas);
		listarAlunos();
	}

	public void listarAlunos() {

		// aqui vai a chamada rest para listar todos os nomesAlunos de uma
		// determinada turma...

		nomesAlunos = new String[] { "Alan", "Pedro", "Jeferson", "Joao", "Fulano1", "Fulano2" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, nomesAlunos);
		Spinner listaAlunos = (Spinner) findViewById(R.id.spinnerAlunos);

		listaAlunos.setAdapter(adapter);

		// Aqui vai o evento de click que vai atualizar o listview
		// com as datas e presencas dos nomesAlunos
		// Aqui dentro precisamos chamar o listarpresencas() toda vez que o
		// usuario selecionar um aluno diferente

		listaAlunos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				listarPresencas(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void listarPresencas(int pos) {
		// TODO fazer a chamada rest aqui
		ArrayList<Boolean> presencas = new ArrayList<Boolean>();
		presencas.add(true);
		presencas.add(false);
		presencas.add(true);
		presencas.add(true);
		presencas.add(false);
		ArrayList<String> datas = new ArrayList<String>();
		datas.add("03/07/2014");
		datas.add("04/07/2014");
		datas.add("05/07/2014");
		datas.add("06/07/2014");
		datas.add("09/07/2014");

		ListView lv = (ListView) findViewById(R.id.listViewPresencas);
		lv.setAdapter(new AdapterPresencas(this, presencas, datas));

		// aqui vai o metodo que vai alterar a presenca do aluno
		// Vai ser um onClickListener para o listview...

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
		currentPosition = position;
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
