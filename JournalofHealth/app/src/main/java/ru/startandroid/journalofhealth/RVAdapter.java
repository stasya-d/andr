package ru.startandroid.journalofhealth;

import android.view.View;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder> {

    public static class PersonViewHolder extends RecyclerView.ViewHolder {

        TextView resultData;
        TextView resultInformation;
        ImageView resultImage;

        PersonViewHolder(View itemView) {
            super(itemView);
            resultData = (TextView) itemView.findViewById(R.id.card_date);
            resultInformation = (TextView) itemView.findViewById(R.id.card_information);
            resultImage = (ImageView) itemView.findViewById(R.id.card_image);
        }
    }

    List<Result> listResults;

    RVAdapter(List<Result> persons) {
        this.listResults = persons;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_result, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.resultData.setText(listResults.get(i).data);
        personViewHolder.resultInformation.setText(listResults.get(i).all);
        personViewHolder.resultImage.setImageResource(listResults.get(i).quality);
    }

    @Override
    public int getItemCount() {
        return listResults.size();
    }
}