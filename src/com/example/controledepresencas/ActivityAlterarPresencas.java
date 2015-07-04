package com.example.controledepresencas;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

public class ActivityAlterarPresencas extends Activity {
	private Boolean[] presencas;
	private String[] datas;
	private String[] alunos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alterar_presencas);
		
		listarAlunos();
		
		listarPresencas();
	}
	
	public void listarAlunos(){
		
		//aqui vai a chamada rest para listar todos os alunos de uma determinada turma...
		
		alunos = new String[]{"Alan","Pedro", "Jefferson", "João", "Fulano1", "Fulano2"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, alunos);
		Spinner listaAlunos = (Spinner) findViewById(R.id.spinnerAlunos);
		
		listaAlunos.setAdapter(adapter);
		
		//Aqui vai o evento de click que vai atualizar o listview
		//com as datas e presencas dos alunos
		//Aqui dentro precisamos chamar o listarpresencas() toda vez que o usuario selecionar um aluno diferente
		/*listaTurmas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Button butaoIniciarAula = (Button) findViewById(R.id.buttonIniciarAula);
				Button butaoConsultarTurma = (Button) findViewById(R.id.ButtonConsultarTurma);
				try {//may occur a fail or there are no classes registered
					if(userType.equals("Aluno")){
						butaoIniciarAula.setEnabled(isOpenTurmas[position]);
						butaoConsultarTurma.setEnabled(isOpenTurmas[position]);
					}else if(userType.equals("Professor")){
						butaoIniciarAula.setText("Abrir Aula");
						butaoIniciarAula.setEnabled(!isOpenTurmas[position]);
						butaoConsultarTurma.setEnabled(isOpenTurmas[position]);
					}
					
				} catch (Exception e) {
					butaoIniciarAula.setEnabled(false);
					butaoConsultarTurma.setEnabled(false);
				}
				
			}
		});*/
	}
	
	public void listarPresencas(){
		//Podemos fazer a chamada rest aqui	
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
		
		ListView lv = (ListView) findViewById(R.id.listViewPresencas);
		lv.setAdapter(new AdapterPresencas(this, presencas, datas));
		
		//aqui vai o metodo que vai alterar a presenca do aluno
		//Vai ser um onClickListener para o listview...
		
	}
	
}
