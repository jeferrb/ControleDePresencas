package com.example.controledepresencas;

import java.util.ArrayList;

import android.content.Context;
import android.content.ClipData.Item;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterPresencas extends BaseAdapter {

	// precisamos implementar o contrutor aqui
	// e passar o Array (String) com todos as presencas do aluno
	// se necessario pdoe passar mais de um array nao tem problema
	// no metodo getView vamos preencher com todas as presencas

	private Context context;
	private ArrayList<ItemPresencaAlunoTurma> presencasAlunoTurma;

	public AdapterPresencas(Context context, ArrayList<ItemPresencaAlunoTurma> presencasAlunoTurma) {
		this.context = context;
		this.presencasAlunoTurma = presencasAlunoTurma;
	}

	@Override
	public int getCount() {
		// aqui tem que devolver o total de entradas da lista
		// o tamanho do vetor de presencas
		return presencasAlunoTurma.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return presencasAlunoTurma.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View layout;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.linha_lista_presencas_aluno, parent);
		} else {
			layout = convertView;
		}

		TextView dataAula = (TextView) layout.findViewById(R.id.textViewDataAula);
		dataAula.setText(presencasAlunoTurma.get(position).getDataChamada());

		ImageView imagePresenca = (ImageView) layout.findViewById(R.id.imageViewAusentePresente);

		if (presencasAlunoTurma.get(position).isPresente) {
			imagePresenca.setImageResource(R.drawable.presente);
		} else {
			imagePresenca.setImageResource(R.drawable.ausente);
		}
		return layout;
	}

}
