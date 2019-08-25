package com.example.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.R;
import com.example.organizze.model.Movimentation;

import java.util.List;

public class MovementsAdapter extends RecyclerView.Adapter<MovementsAdapter.MovementViewHolder> {

    private List<Movimentation>movimentationList;
    Context context;

    public MovementsAdapter(List<Movimentation>movimentationList, Context context){
        this.movimentationList = movimentationList;
        this.context = context;
    }


    @NonNull
    @Override
    public MovementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movements, parent, false);
        return new MovementViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MovementViewHolder holder, int position) {

        Movimentation movimentation = movimentationList.get(position);
        holder.textSalary.setText(movimentation.getValue().toString());
        holder.textSalaryInformation.setText(movimentation.getDescription());
        holder.textCategory.setText(movimentation.getCategory());
        holder.textSalary.setTextColor(context.getResources().getColor(R.color.colorAccentIncome));


        if(movimentation.getType()=="d" || movimentation.getType().equals("d")){
            holder.textSalary.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.textSalary.setText(" -" + movimentation.getValue());
        }

    }

    @Override
    public int getItemCount() {
        return movimentationList.size();
    }

    public class MovementViewHolder extends RecyclerView.ViewHolder{

        TextView textSalaryInformation, textSalary, textCategory;

        public MovementViewHolder(View itemView){
            super(itemView);

            textSalary = itemView.findViewById(R.id.textSalary);
            textSalaryInformation = itemView.findViewById(R.id.textSalaryInformation);
            textCategory = itemView.findViewById(R.id.textCategory);

        }

    }


}
