package com.example.controledepresencas;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdapterRelatorioFinalAula extends BaseAdapter {
	private Context context;
	private ArrayList<String> presencas;
	private ArrayList<String> nomeAlunos;
	
	public AdapterRelatorioFinalAula(Context context, ArrayList<String> presencas, ArrayList<String> nomeAlunos) {
		this.context = context;
		this.presencas = presencas;
		this.nomeAlunos = nomeAlunos;
	}

	@Override
	public int getCount() {
		return nomeAlunos.size();
	}

	@Override
	public Object getItem(int position) {
		return nomeAlunos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View layout;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layout = inflater.inflate(R.layout.linha_lista_presencas_final_aula, null);
		} else {
			layout = convertView;
		}

		TextView nomeAluno = (TextView) layout.findViewById(R.id.textViewNomeAluno);
		nomeAluno.setText(nomeAlunos.get(position));

		ImageView imagePresenca = (ImageView) layout.findViewById(R.id.imageViewAusentePresente);

		if (presencas.get(position).equals("true")) {
			imagePresenca.setImageResource(R.drawable.presente);
		} else {
			imagePresenca.setImageResource(R.drawable.ausente);
		}
		return layout;
	}

}
